package com.example.e_paper.image_processing

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.toHexString
import okio.ByteString
import okio.ByteString.Companion.toByteString
import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.Socket
import java.nio.ByteBuffer
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.math.roundToInt

class ImageProcessing(context: Context) {

    var selectImages : String by mutableStateOf("")
    var imageShow : Boolean by mutableStateOf(false)
    var selectProcessing : Int by mutableStateOf(0)
    private var display : Int by mutableStateOf(0)
    val context: Context
    var imgHeight = 0
    var imgWidth = 0

    var myIpAddress = ""
    var myPort = 0

    var myBitmap: Bitmap? = null

    var myBitmapTop: Bitmap? = null
    var myBitmapBottom: Bitmap? = null

    var myListBlackTop : MutableList<ByteString> = mutableListOf()
    var myListRedTop : MutableList<ByteString> = mutableListOf()
    var myListBlackBottom : MutableList<ByteString> = mutableListOf()
    var myListRedBottom : MutableList<ByteString> = mutableListOf()

    var allDataArrayReady : Boolean by mutableStateOf(false)
    var allDataArrayReadyTop : Boolean by mutableStateOf(false)
    var allDataArrayReadyBottom : Boolean by mutableStateOf(false)

    var myListBlack : MutableList<ByteString> = mutableListOf()
    var myListRed : MutableList<ByteString> = mutableListOf()

    var colorSection : Boolean by mutableStateOf(false)

    init {
        this@ImageProcessing.context = context
    }

    fun uriToBitmap(uri: Uri): Bitmap? {
        val contentResolver: ContentResolver = context.contentResolver
        var inputStream: InputStream? = null
        var bitmap: Bitmap? = null

        try {
            inputStream = contentResolver.openInputStream(uri)
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }

        return bitmap
    }

    fun SizeDisplay(choice: Int){

        display = choice

        when(choice){
            0 -> {
                imgHeight = 680
                imgWidth = 960
            }
            1 -> {
                imgHeight = 300
                imgWidth = 400
            }
            else -> println("You can chose another choice")
        }
    }

    fun SelectProcessing(){
        Log.i("Bitmap", "Select processing = ${selectProcessing}")
        when(selectProcessing) {
            0 -> {
                if(myBitmap != null){
                    myBitmap!!.recycle()
                    myBitmap = null
                }
                myBitmap = uriToBitmap(selectImages.toUri())?.let { colorScaleWithoutDithering(image = it) }?.let {
                    resizeImage(
                        image = it,
                        newWidth = imgWidth,
                        newHeight = imgHeight
                    )
                }
                if(display == 0){
                    processHalfImage()
                }
                else if(display == 1){
                    processFullImage()
                }
            }
            1 -> {
                if(myBitmap != null){
                    myBitmap!!.recycle()
                    myBitmap = null
                }
                myBitmap = uriToBitmap(selectImages.toUri())?.let { convertToBinary(image = it) }?.let {
                    resizeImage(
                        image = it,
                        newWidth = imgWidth,
                        newHeight = imgHeight
                    )
                }
                if(display == 0){
                    processHalfImage()
                }
                else if(display == 1){
                    processFullImage()
                }
            }
            2 -> {
                if(myBitmap != null){
                    myBitmap!!.recycle()
                    myBitmap = null
                }
                myBitmap = uriToBitmap(selectImages.toUri())?.let { errorDiffusionDithering(image = convertToGrayscale(image = it))
                }?.let {
                    resizeImage(
                        image = it,
                        newWidth = imgWidth,
                        newHeight = imgHeight
                    )
                }
                if(display == 0){
                    processHalfImage()
                }
                else if(display == 1){
                    processFullImage()
                }
            }
            3 -> {
                if(myBitmap != null){
                    myBitmap!!.recycle()
                    myBitmap = null
                }
                myBitmap = uriToBitmap(selectImages.toUri())?.let { errorDiffusionDithering(image = it) }?.let {
                    resizeImage(
                        image = it,
                        newWidth = imgWidth,
                        newHeight = imgHeight
                    )
                }
                if(display == 0){
                    processHalfImage()
                }
                else if(display == 1){
                    processFullImage()
                }
            }
            else -> println("I don't know anything about it")
        }
        imageShow = true
        colorSection = false
    }

    private fun processFullImage(){
        myListBlack.clear()
        myListRed.clear()
        createList(
            myBytesBlack = convertBitmapToEPDBytesBlack(myBitmap!!),
            myBytesRed = convertBitmapToEPDBytesRed(myBitmap!!)
        )
    }

    private fun processHalfImage(){
        myListBlackTop.clear()
        myListBlackBottom.clear()
        myListRedTop.clear()
        myListRedBottom.clear()
        imageTop(myBitmap!!)
        imageBottom(myBitmap!!)
    }

    fun resizeImage(image: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        var result = Bitmap.createScaledBitmap(image, newWidth, newHeight, false)
//        Log.i("Bitmap", "EPD = ${bitmapToPixelByteArray().toByteString()}")
        Log.i("Bitmap", "Height 1 = ${result.height}")
        Log.i("Bitmap", "Width 1 = ${result.width}")
        return result
    }

    fun imageTop(image: Bitmap){
        val width = image.width
        val height = image.height
        val halfHeight = height / 2

        val topBitmap = Bitmap.createBitmap(image, 0, 0, width, halfHeight)
        myBitmapTop = topBitmap

        createListTop(
            myBytesBlack = convertBitmapToEPDBytesBlack(topBitmap!!),
            myBytesRed = convertBitmapToEPDBytesRed(topBitmap!!)
        )
    }

    fun imageBottom(image: Bitmap){
        val width = image.width
        val height = image.height
        val halfHeight = height / 2

        val bottomBitmap = Bitmap.createBitmap(image, 0, halfHeight, width, halfHeight)
        myBitmapBottom = bottomBitmap

        createListBottom(
            myBytesBlack = convertBitmapToEPDBytesBlack(bottomBitmap!!),
            myBytesRed = convertBitmapToEPDBytesRed(bottomBitmap!!)
        )
//        Log.d("TCP", "Bottom ----------------- = ${convertBitmapToEPDBytesBlack(bottomBitmap!!)}")
    }

    fun SendImage(host: String, port: Int){
        if(display == 0){
            sendDataThroughTcpHalf(host, port)
        }
        else if(display == 1){
            sendDataThroughTcp(host, port)
        }
    }
    fun sendDataThroughTcp(host: String, port: Int) {

        myIpAddress = host
        myPort = port

        GlobalScope.launch(Dispatchers.IO) {
            try {
                var dataList = myListBlack
                Log.d("TCP", "Data Black = $dataList")
                val socket = Socket(myIpAddress, myPort)
                val outputStream: OutputStream = socket.getOutputStream()

                // Send the data
                if(!dataList.isNullOrEmpty()){
                    dataList.forEachIndexed {index, dataByte ->
                        outputStream.write(dataByte.toByteArray())
                        Log.d("TCP", "$index Data Black Sent ${dataByte[dataByte.size - 1]}")
                        delay(300)
                    }
                }
                dataList.clear()
                outputStream.flush()
                dataList = myListRed

                Log.d("TCP", "Data Red = $dataList")

                val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                val response = reader.read()
//                Log.d("TCP", "Receive Data = $response")
                if(!dataList.isNullOrEmpty()){
                    if(response == 82){
                        Log.d("TCP", "Prepare Send Red Data")
                        dataList.forEachIndexed {index ,dataByte ->
                            outputStream.write(dataByte.toByteArray())
                            Log.d("TCP", "$index Data Red Sent ${dataByte[dataByte.size - 1]}")
                            delay(300)
                        }
                        outputStream.flush()
                    }
                }
                socket.close()

                Log.d("TCP", "Data sent successfully")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("TCP", "Failed to send data: ${e.message}")
            }
        }
    }

    fun sendDataThroughTcpHalf(host: String, port: Int) {

        myIpAddress = host
        myPort = port

        GlobalScope.launch(Dispatchers.IO) {
            try {
//                var halfScreen = false
                var section: Int by mutableStateOf(0)
                val socket = Socket(myIpAddress, myPort)
                val outputStream: OutputStream = socket.getOutputStream()

                outputStream.write('S'.toInt())
                outputStream.flush()

                var reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                var response = reader.read()
                Log.d("TCP", "Response = $response")

                if(response == 78){
                    var dataList = myListBlackTop
                    Log.d("TCP", "Data Black = $dataList")
                    // Send the data
                    if(!dataList.isNullOrEmpty()){
                        dataList.forEachIndexed {index, dataByte ->
                            outputStream.write(dataByte.toByteArray())
                            Log.d("TCP", "$index -- 0 Data Black Sent ${dataByte[dataByte.size - 1]}")
                            delay(300)
                        }
                    }
                    dataList.clear()
                    outputStream.flush()
                    dataList = myListRedTop

                    Log.d("TCP", "Data Red = $dataList")

                    val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                    val response = reader.read()
//                Log.d("TCP", "Receive Data = $response")
                    if(!dataList.isNullOrEmpty()){
                        if(response == 82){
                            Log.d("TCP", "Prepare Send Red Data")
                            dataList.forEachIndexed {index ,dataByte ->
                                outputStream.write(dataByte.toByteArray())
                                Log.d("TCP", "$index -- 0 Data Red Sent ${dataByte[dataByte.size - 1]}")
                                delay(300)
                            }
                            outputStream.flush()
                        }
                    }
                    Log.d("TCP", "Data sent successfully")
                }
                outputStream.write('S'.toInt())
                outputStream.flush()

                reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                response = reader.read()
                Log.d("TCP", "Response = $response")

                if(response == 78){
                    var dataList = myListBlackBottom
                    Log.d("TCP", "Data Black = $dataList")
                    // Send the data
                    if(!dataList.isNullOrEmpty()){
                        dataList.forEachIndexed {index, dataByte ->
                            outputStream.write(dataByte.toByteArray())
                            Log.d("TCP", "$index -- 1 Data Black Sent ${dataByte[dataByte.size - 1]}")
                            delay(300)
                        }
                    }
                    dataList.clear()
                    outputStream.flush()
                    dataList = myListRedBottom

                    Log.d("TCP", "Data Red = $dataList")

                    val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                    val response = reader.read()
//                Log.d("TCP", "Receive Data = $response")
                    if(!dataList.isNullOrEmpty()){
                        if(response == 82){
                            Log.d("TCP", "Prepare Send Red Data")
                            dataList.forEachIndexed {index ,dataByte ->
                                outputStream.write(dataByte.toByteArray())
                                Log.d("TCP", "$index -- 1 Data Red Sent ${dataByte[dataByte.size - 1]}")
                                delay(300)
                            }
                            outputStream.flush()
                        }
                    }
                    Log.d("TCP", "Data sent successfully")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("TCP", "Failed to send data: ${e.message}")
            }
        }
    }

    fun createList(myBytesBlack: ByteArray, myBytesRed: ByteArray){
        val bytesBlack = myBytesBlack
        val bytesRed = myBytesRed
        val bytesSizeBlack = myBytesBlack.size
        var column = 50
        var tempByte = ArrayList<Byte>()
        var count = 0

        for(i in bytesBlack.indices){
            count++
            tempByte.add(bytesBlack[i])

            if(count == (bytesSizeBlack / 50)){
                if(column > 1){
                    tempByte.add('6'.toByte())
                    myListBlack.add(tempByte.toByteArray().toByteString())
                    tempByte.clear()
                    column--
                }
                else{
                    tempByte.add('7'.toByte())
                    myListBlack.add(tempByte.toByteArray().toByteString())
                    tempByte.clear()
                }
                count = 0
            }
        }

        column = 50

        for(i in bytesRed.indices){
            count++
            tempByte.add(bytesRed[i])

            if(count == (bytesSizeBlack / 50)){
                if(column > 1){
                    tempByte.add('8'.toByte())
                    myListRed.add(tempByte.toByteArray().toByteString())
                    tempByte.clear()
                    column--
                }
                else{
                    tempByte.add('9'.toByte())
                    myListRed.add(tempByte.toByteArray().toByteString())
                    tempByte.clear()
                }
                count = 0
            }
        }
        if(myBytesBlack.isNotEmpty() && myBytesRed.isNotEmpty()){
            allDataArrayReady = true
            Log.d("TCP", "Create All List Done")
        }

//        myListBlack.forEachIndexed { index, byteString ->
//            Log.d("TCP", "Column = $index - ${byteString} -- ${byteString[byteString.size - 1]}")
//        }

//        myListRed.forEachIndexed { index, byteString ->
//            Log.d("TCP", "Column = $index - ${byteString} -- ${byteString[byteString.size - 1]}")
//        }
    }

    fun createListTop(myBytesBlack: ByteArray, myBytesRed: ByteArray){
        val bytesBlack = myBytesBlack
        val bytesRed = myBytesRed
        val bytesSizeBlack = myBytesBlack.size
        var column = 50
        var tempByte = ArrayList<Byte>()
        var count = 0

        Log.d("TCP", "Data Top Black ${myBytesBlack.toByteString()}")

        for(i in bytesBlack.indices){
            count++
            tempByte.add(bytesBlack[i])

            if(count == (bytesSizeBlack / 50)){
                if(column > 1){
                    tempByte.add('6'.toByte())
                    myListBlackTop.add(tempByte.toByteArray().toByteString())
                    tempByte.clear()
                    column--
                }
                else{
                    tempByte.add('7'.toByte())
                    myListBlackTop.add(tempByte.toByteArray().toByteString())
                    tempByte.clear()
                }
                count = 0
            }
        }

        column = 50

        for(i in bytesRed.indices){
            count++
            tempByte.add(bytesRed[i])

            if(count == (bytesSizeBlack / 50)){
                if(column > 1){
                    tempByte.add('8'.toByte())
                    myListRedTop.add(tempByte.toByteArray().toByteString())
                    tempByte.clear()
                    column--
                }
                else{
                    tempByte.add('9'.toByte())
                    myListRedTop.add(tempByte.toByteArray().toByteString())
                    tempByte.clear()
                }
                count = 0
            }
        }
        if(myListBlackTop.isNotEmpty() && myListRedTop.isNotEmpty()){
            allDataArrayReadyTop = true
            Log.d("TCP", "Create All List Done Top")
        }

    }

    fun createListBottom(myBytesBlack: ByteArray, myBytesRed: ByteArray){
        val bytesBlack = myBytesBlack
        val bytesRed = myBytesRed
        val bytesSizeBlack = myBytesBlack.size
        var column = 50
        var tempByte = ArrayList<Byte>()
        var count = 0

        Log.d("TCP", "Data Bottom Black ${myBytesBlack.toByteString()}")

        for(i in bytesBlack.indices){
            count++
            tempByte.add(bytesBlack[i])

            if(count == (bytesSizeBlack / 50)){
                if(column > 1){
                    tempByte.add('6'.toByte())
                    myListBlackBottom.add(tempByte.toByteArray().toByteString())
                    tempByte.clear()
                    column--
                }
                else{
                    tempByte.add('7'.toByte())
                    myListBlackBottom.add(tempByte.toByteArray().toByteString())
                    tempByte.clear()
                }
                count = 0
            }
        }

        column = 50

        for(i in bytesRed.indices){
            count++
            tempByte.add(bytesRed[i])

            if(count == (bytesSizeBlack / 50)){
                if(column > 1){
                    tempByte.add('8'.toByte())
                    myListRedBottom.add(tempByte.toByteArray().toByteString())
                    tempByte.clear()
                    column--
                }
                else{
                    tempByte.add('9'.toByte())
                    myListRedBottom.add(tempByte.toByteArray().toByteString())
                    tempByte.clear()
                }
                count = 0
            }
        }
        if(myListBlackBottom.isNotEmpty() && myListRedBottom.isNotEmpty()){
            allDataArrayReadyBottom = true
            Log.d("TCP", "Create All List Done")
        }
    }

    fun convertBitmapToEPDBytesRed(image: Bitmap): ByteArray {
        val width = image.width
        val height = image.height

        val bytesPerRow = (width + 7) / 8
        val dataSize = bytesPerRow * height

        val bytes = ByteArray(dataSize)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = image!!.getPixel(x,y)
                val byteIndex = x / 8
                val bitIndex = 7 - (x % 8)
                var bitValue = 0

                if ((Color.red(pixel) == 255) and (Color.green(pixel) == 0) and (Color.blue(pixel) == 0)){
                    bitValue = 1 //Waveshare Red is 0
                }
                else{
                    bitValue = 0
                }
                bytes[y * bytesPerRow + byteIndex] = bytes[y * bytesPerRow + byteIndex] or (bitValue shl bitIndex).toByte()
            }
        }

        Log.i("TCP", "EPD Filter Red = ${bytes.toByteString()}")

        return bytes
    }

    fun convertBitmapToEPDBytesBlack(image: Bitmap): ByteArray {
        val width = image.width
        val height = image.height

        val bytesPerRow = (width + 7) / 8
        val dataSize = bytesPerRow * height

        val bytes = ByteArray(dataSize)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = image!!.getPixel(x,y)
                val byteIndex = x / 8
                val bitIndex = 7 - (x % 8)
                var bitValue = 0

                if ((Color.red(pixel) == 255) and (Color.green(pixel) == 0) and (Color.blue(pixel) == 0)){
                    bitValue = 0
                }
                else if ((Color.red(pixel) == 255) and (Color.green(pixel) == 255) and (Color.blue(pixel) == 255)){
                    bitValue = 1
                }
                else{
                    bitValue = 0
                }
                bytes[y * bytesPerRow + byteIndex] = bytes[y * bytesPerRow + byteIndex] or (bitValue shl bitIndex).toByte()
            }
        }

        Log.i("TCP", "EPD Filter Black = ${bytes.toByteString()}")

        return bytes
    }
    fun convertToBinary(image: Bitmap): Bitmap {
        val result = image.copy(Bitmap.Config.ARGB_8888, true)
        val width = result.width
        val height = result.height

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = result.getPixel(x, y)
                val gray = Color.red(pixel) * 0.2989 + Color.green(pixel) * 0.587 + Color.blue(pixel) * 0.114
                val binaryPixel = if (gray > 128) Color.WHITE else Color.BLACK
                result.setPixel(x, y, binaryPixel)
            }
        }
        return result
    }

    fun convertToGrayscale(image: Bitmap): Bitmap {
        val width = image.width
        val height = image.height
        val grayscaleImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = image.getPixel(x, y)
                val red = Color.red(pixel)
                val green = Color.green(pixel)
                val blue = Color.blue(pixel)
                val grayscaleValue = (red * 0.299 + green * 0.587 + blue * 0.114).toInt()
                val grayscalePixel = Color.rgb(grayscaleValue, grayscaleValue, grayscaleValue)
                grayscaleImage.setPixel(x, y, grayscalePixel)
            }
        }

        return grayscaleImage
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////

    fun errorDiffusionDithering(image: Bitmap): Bitmap {
        val result = image.copy(Bitmap.Config.ARGB_8888, true)
        val width = result.width
        val height = result.height

        // Define the color palette
        val curPal = arrayOf(
            intArrayOf(0, 0, 0),     // Black
            intArrayOf(255, 255, 255), // White
            intArrayOf(255, 0, 0) // White
            // Add more colors to the palette if needed
        )

        for (y in 0 until height) {
            for (x in 0 until width) {
                val oldPixel = result.getPixel(x, y)
                val oldRed = Color.red(oldPixel)
                val oldGreen = Color.green(oldPixel)
                val oldBlue = Color.blue(oldPixel)

                val closestColorIndex = findClosestColor(oldRed, oldGreen, oldBlue, curPal)
                val newPixel = getColorFromPalette(closestColorIndex, curPal)
                result.setPixel(x, y, newPixel)

                val errorRed = oldRed - Color.red(newPixel)
                val errorGreen = oldGreen - Color.green(newPixel)
                val errorBlue = oldBlue - Color.blue(newPixel)

                distributeError(result, x + 1, y, errorRed, errorGreen, errorBlue, 7f / 16f)
                distributeError(result, x - 1, y + 1, errorRed, errorGreen, errorBlue, 3f / 16f)
                distributeError(result, x, y + 1, errorRed, errorGreen, errorBlue, 5f / 16f)
                distributeError(result, x + 1, y + 1, errorRed, errorGreen, errorBlue, 1f / 16f)
            }
        }

        return result
    }

    private fun findClosestColor(red: Int, green: Int, blue: Int, palette: Array<IntArray>): Int {
        var minDist = Int.MAX_VALUE
        var closestColorIndex = 0

        for (i in palette.indices) {
            val paletteColor = palette[i]
            val colorDist = (red - paletteColor[0]).squared() +
                    (green - paletteColor[1]).squared() +
                    (blue - paletteColor[2]).squared()

            if (colorDist < minDist) {
                minDist = colorDist
                closestColorIndex = i
            }
        }

        return closestColorIndex
    }

    private fun getColorFromPalette(colorIndex: Int, palette: Array<IntArray>): Int {
        val color = palette[colorIndex]
        return Color.rgb(color[0], color[1], color[2])
    }

    private fun Int.squared(): Int = this * this

    private fun distributeError(bitmap: Bitmap, x: Int, y: Int, errorRed: Int, errorGreen: Int, errorBlue: Int, weight: Float) {
        if (x < 0 || x >= bitmap.width || y < 0 || y >= bitmap.height) {
            return
        }

        val pixel = bitmap.getPixel(x, y)
        val red = Color.red(pixel) + (errorRed * weight).toInt()
        val green = Color.green(pixel) + (errorGreen * weight).toInt()
        val blue = Color.blue(pixel) + (errorBlue * weight).toInt()

        val newPixel = Color.rgb(red.coerceIn(0, 255), green.coerceIn(0, 255), blue.coerceIn(0, 255))
        bitmap.setPixel(x, y, newPixel)
    }

    /////////////////////////////////////////////////////////////////////////////////

    fun colorScaleWithoutDithering(image: Bitmap): Bitmap {
        val result = image.copy(Bitmap.Config.ARGB_8888, true)
        val width = result.width
        val height = result.height

        // Define the color palette
        val curPal = arrayOf(
            intArrayOf(255, 0, 0),     // Red
            intArrayOf(255, 255, 255), // White
            intArrayOf(0, 0, 0)        // Black
            // Add more colors to the palette if needed
        )

        for (y in 0 until height) {
            for (x in 0 until width) {
                val oldPixel = result.getPixel(x, y)
                val oldRed = Color.red(oldPixel)
                val oldGreen = Color.green(oldPixel)
                val oldBlue = Color.blue(oldPixel)

                val closestColorIndex = findClosestColor(oldRed, oldGreen, oldBlue, curPal)
                val newPixel = getColorFromPalette(closestColorIndex, curPal)
                result.setPixel(x, y, newPixel)
            }
        }
        return result
    }
}