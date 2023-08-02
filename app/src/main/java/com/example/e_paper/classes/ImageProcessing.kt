package com.example.e_paper.classes

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okio.ByteString
import okio.ByteString.Companion.toByteString
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.Socket
import kotlin.experimental.or

class ImageProcessing(context: Context) {

    var selectImages : String by mutableStateOf("")
    var imageShow : Boolean by mutableStateOf(false)
    var selectProcessing : Int by mutableStateOf(0)
    var display : Int by mutableStateOf(0)
    val context: Context
    var imgHeight = 680
    var imgWidth = 960

    var sendding : Boolean by mutableStateOf(false)
    var progressDone : Float by mutableStateOf(1f)
    var progress : Float by mutableStateOf(0f)

    var rotateImage90 : Float by mutableStateOf(0f)

    var delaySendTCP = 10L

    var myIpAddress = ""
    var myPort = 0

    var myBitmap: MutableState<Bitmap?> = mutableStateOf(null)

    var myBitmapCanvas: MutableState<Bitmap?> = mutableStateOf(null)

    var allDataArrayReady : Boolean by mutableStateOf(false)

    var myListBlack : MutableList<ByteString> = mutableListOf()
    var myListRed : MutableList<ByteString> = mutableListOf()

    var colorSection : Boolean by mutableStateOf(false)

    val timer: Timer? = null

    init {
        this@ImageProcessing.context = context
    }

    fun UriToBitmap(uri: Uri): Bitmap? {
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
        myBitmapCanvas.value = bitmap
        Log.i("Bitmap", "Bitmap Canvas = ${myBitmapCanvas}")
        return myBitmapCanvas.value
    }

    fun SelectProcessing(){
        Log.i("Bitmap", "Select processing = ${selectProcessing}")
        when(selectProcessing) {
            0 -> {
                if(myBitmap.value != null){
                    myBitmap.value!!.recycle()
                    myBitmap.value = null
                }
                myBitmap.value = myBitmapCanvas?.let {
                    convertToBinary(image = it.value!!)?.let {
                        resizeImage(
                            image = it,
                            newWidth = imgWidth,
                            newHeight = imgHeight
                        )
                    }
                }
                processFullImage()
            }
            1 -> {
                if(myBitmap.value != null){
                    myBitmap.value!!.recycle()
                    myBitmap.value = null
                }
                myBitmap.value = myBitmapCanvas?.let {
                    colorScaleWithoutDithering(image = it.value!!)?.let {
                        resizeImage(
                            image = it,
                            newWidth = imgWidth,
                            newHeight = imgHeight
                        )
                    }
                }
                processFullImage()
            }
            2 -> {
                if(myBitmap.value != null){
                    myBitmap.value!!.recycle()
                    myBitmap.value = null
                }
                myBitmap.value = myBitmapCanvas?.let { convertToGrayscale(image = it.value!!) }?.let {
                    errorDiffusionDithering(image = it)?.let {
                        resizeImage(
                            image = it,
                            newWidth = imgWidth,
                            newHeight = imgHeight
                        )
                    }
                }
                processFullImage()
            }
            3 -> {
                if(myBitmap.value != null){
                    myBitmap.value!!.recycle()
                    myBitmap.value = null
                }
                myBitmap.value = myBitmapCanvas?.let {
                    errorDiffusionDithering(image = it.value!!)?.let {
                        resizeImage(
                            image = it,
                            newWidth = imgWidth,
                            newHeight = imgHeight
                        )
                    }
                }
                processFullImage()
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
            myBytesBlack = convertBitmapToEPDBytesBlack(myBitmap.value!!),
            myBytesRed = convertBitmapToEPDBytesRed(myBitmap.value!!)
        )
    }

    fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun rotateBitmap90(){
        rotateImage90 += 90f
        val matrix = Matrix()
        matrix.postRotate(rotateImage90)
        myBitmapCanvas.value = Bitmap.createBitmap(myBitmapCanvas.value!!, 0, 0, myBitmapCanvas.value!!.width, myBitmapCanvas.value!!.height, matrix, true)
    }

    private fun resizeImage(image: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        var result = Bitmap.createScaledBitmap(image, newWidth, newHeight, false)
        Log.i("Bitmap", "Height 1 = ${result.height}")
        Log.i("Bitmap", "Width 1 = ${result.width}")
        return result
    }

    fun SendImage(host: String, port: Int, timer: Timer){
        sendding = true
        sendDataThroughTcp(host, port, timer)
    }

    private fun progressSending(addProgress: Float){
        if (progress < progressDone) progress += addProgress
    }
    private fun sendDataThroughTcp(host: String, port: Int, timer: Timer) {

        myIpAddress = host
        myPort = port

        GlobalScope.launch(Dispatchers.IO) {
            try {
                timer.start()
                Log.i("Sketch", "Device = ${display}")
                var dataList = myListBlack
                Log.d("TCP", "Data Black = $dataList")
                val socket = Socket(myIpAddress, myPort)
                val outputStream: OutputStream = socket.getOutputStream()

                outputStream.write(2)

                var reader1 = BufferedReader(InputStreamReader(socket.getInputStream()))
                var response1 = reader1.read()
                if(response1 == 68){
                    // Send the data
                    if(!dataList.isNullOrEmpty()){
                        dataList.forEachIndexed {index, dataByte ->
                            outputStream.write(dataList[index].toByteArray())
                            Log.d("TCP", "$index -- 0 Data Black Sent ${dataList[index][dataList[index].size - 1]} -- $progress")

                            var reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                            var response = reader.read()
                            if(response == 65){
                                outputStream.write(dataList[index].toByteArray())
                                Log.d("TCP", "$index -- 0 Data Black Sent ${dataList[index][dataList[index].size - 1]} -- $progress")
                            }
                            progressSending(addProgress = 0.0045f)
                            delay(delaySendTCP)
                        }
                        outputStream.flush()
                    }
                    dataList.clear()

                    dataList = myListRed

                    Log.d("TCP", "Data Red = $dataList")

                    val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                    val response = reader.read()
//                Log.d("TCP", "Receive Data = $response")
                    if(!dataList.isNullOrEmpty()){
                        if(response == 82){
                            Log.d("TCP", "Prepare Send Red Data")
                            dataList.forEachIndexed {index ,dataByte ->
                                outputStream.write(dataList[index].toByteArray())
                                Log.d("TCP", "$index -- 0 Data Red Sent ${dataList[index][dataList[index].size - 1]} -- $progress")

                                var reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                                var response = reader.read()
                                if(response == 65){
                                    outputStream.write(dataList[index].toByteArray())
                                    Log.d("TCP", "$index -- 0 Data Black Sent ${dataList[index][dataList[index].size - 1]} -- $progress")
                                }
                                progressSending(addProgress = 0.0045f)
                                delay(delaySendTCP)
                            }
                            outputStream.flush()
                        }
                    }

                    val reader2 = BufferedReader(InputStreamReader(socket.getInputStream()))
                    val response2 = reader2.read()
                    Log.d("TCP", "Receive Data = $response2")
                    if(response2 == 68){
                        timer.stop()
                        progressSending(addProgress = 0.1f)
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

    private fun createList(myBytesBlack: ByteArray, myBytesRed: ByteArray){
        val bytesBlack = myBytesBlack
        val bytesRed = myBytesRed
        val bytesSizeBlack = myBytesBlack.size
        var column = 100
        var columnDiv = 100
        var tempByte = ArrayList<Byte>()
        var count = 0

        for(i in bytesBlack.indices){
            count++
            tempByte.add(bytesBlack[i])

            if(count == (bytesSizeBlack / columnDiv)){
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

        column = columnDiv

        for(i in bytesRed.indices){
            count++
            tempByte.add(bytesRed[i])

            if(count == (bytesSizeBlack / columnDiv)){
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
    }

    private fun convertBitmapToEPDBytesRed(image: Bitmap): ByteArray {
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
                    bitValue = 1

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

    private fun convertBitmapToEPDBytesBlack(image: Bitmap): ByteArray {
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
    private fun convertToBinary(image: Bitmap): Bitmap {
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

    private fun convertToGrayscale(image: Bitmap): Bitmap {
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

    private fun errorDiffusionDithering(image: Bitmap): Bitmap {
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

    private fun colorScaleWithoutDithering(image: Bitmap): Bitmap {
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