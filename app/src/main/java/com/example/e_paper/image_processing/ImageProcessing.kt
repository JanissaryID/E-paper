package com.example.e_paper.image_processing

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import kotlin.math.roundToInt

class ImageProcessing(context: Context) {

    var selectImages : String by mutableStateOf("")
    val context: Context

    var bitmap: Bitmap? = null

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

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

    fun getImage(uri: Uri): Bitmap{
        try {
            coroutineScope.launch {
                bitmap = uriToBitmap(uri = uri)?.let { ditherImage(it) }!!
//                floydSteinbergDithering
            }
        }
        catch (e: Exception){

        }
        return bitmap!!
    }

    fun ditherImage(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val ditheredBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // Convert the image to grayscale
        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = bitmap.getPixel(x, y)
                val grayscale = (Color.red(pixel) + Color.green(pixel) + Color.blue(pixel)) / 3
                ditheredBitmap.setPixel(x, y, Color.rgb(grayscale, grayscale, grayscale))
            }
        }

        // Perform Floyd-Steinberg dithering
        for (y in 0 until height) {
            for (x in 0 until width) {
                val oldPixel = ditheredBitmap.getPixel(x, y)
                val newPixel = if (Color.red(oldPixel) < 128) Color.BLACK else Color.WHITE
                ditheredBitmap.setPixel(x, y, newPixel)

                val quantError = Color.red(oldPixel) - Color.red(newPixel)
                if (x + 1 < width) {
                    val neighborPixel = ditheredBitmap.getPixel(x + 1, y)
                    val correctedNeighborPixel = Color.rgb(
                        clamp(Color.red(neighborPixel) + quantError * (7.0 / 16.0).roundToInt()),
                        clamp(Color.green(neighborPixel) + quantError * (7.0 / 16.0).roundToInt()),
                        clamp(Color.blue(neighborPixel) + quantError * (7.0 / 16.0).roundToInt())
                    )
                    ditheredBitmap.setPixel(x + 1, y, correctedNeighborPixel)
                }
                if (x - 1 >= 0 && y + 1 < height) {
                    val neighborPixel = ditheredBitmap.getPixel(x - 1, y + 1)
                    val correctedNeighborPixel = Color.rgb(
                        clamp(Color.red(neighborPixel) + quantError * (3.0 / 16.0).roundToInt()),
                        clamp(Color.green(neighborPixel) + quantError * (3.0 / 16.0).roundToInt()),
                        clamp(Color.blue(neighborPixel) + quantError * (3.0 / 16.0).roundToInt())
                    )
                    ditheredBitmap.setPixel(x - 1, y + 1, correctedNeighborPixel)
                }
                if (y + 1 < height) {
                    val neighborPixel = ditheredBitmap.getPixel(x, y + 1)
                    val correctedNeighborPixel = Color.rgb(
                        clamp(Color.red(neighborPixel) + quantError * (5.0 / 16.0).roundToInt()),
                        clamp(Color.green(neighborPixel) + quantError * (5.0 / 16.0).roundToInt()),
                        clamp(Color.blue(neighborPixel) + quantError * (5.0 / 16.0).roundToInt())
                    )
                    ditheredBitmap.setPixel(x, y + 1, correctedNeighborPixel)
                }
                if (x + 1 < width && y + 1 < height) {
                    val neighborPixel = ditheredBitmap.getPixel(x + 1, y + 1)
                    val correctedNeighborPixel = Color.rgb(
                        clamp(Color.red(neighborPixel) + quantError * (1.0 / 16.0).roundToInt()),
                        clamp(Color.green(neighborPixel) + quantError * (1.0 / 16.0).roundToInt()),
                        clamp(Color.blue(neighborPixel) + quantError * (1.0 / 16.0).roundToInt())
                    )
                    ditheredBitmap.setPixel(x + 1, y + 1, correctedNeighborPixel)
                }
            }
        }

        return ditheredBitmap
    }

    fun clamp(value: Int): Int {
        return when {
            value < 0 -> 0
            value > 255 -> 255
            else -> value
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    fun floydSteinbergDithering(image: Bitmap): Bitmap {
        val result = image.copy(Bitmap.Config.ARGB_8888, true)

        val width = result.width
        val height = result.height

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = image.getPixel(x, y)
                val grayscale = (Color.red(pixel) + Color.green(pixel) + Color.blue(pixel)) / 3
                result.setPixel(x, y, Color.rgb(grayscale, grayscale, grayscale))
            }
        }

        for (y in 0 until height) {
            for (x in 0 until width) {
                val oldPixel = result.getPixel(x, y)
                val oldRed = Color.red(oldPixel)
                val oldGreen = Color.green(oldPixel)
                val oldBlue = Color.blue(oldPixel)

                val newRed = if (oldRed < 128) 0 else 255
                val newGreen = if (oldGreen < 128) 0 else 255
                val newBlue = if (oldBlue < 128) 0 else 255

                result.setPixel(x, y, Color.rgb(newRed, newGreen, newBlue))

                val errorRed = oldRed - newRed
                val errorGreen = oldGreen - newGreen
                val errorBlue = oldBlue - newBlue

                if (x < width - 1) {
                    distributeError(result, x + 1, y, errorRed, errorGreen, errorBlue, 7f / 16f)
                }
                if (x > 0 && y < height - 1) {
                    distributeError(result, x - 1, y + 1, errorRed, errorGreen, errorBlue, 3f / 16f)
                }
                if (y < height - 1) {
                    distributeError(result, x, y + 1, errorRed, errorGreen, errorBlue, 5f / 16f)
                }
                if (x < width - 1 && y < height - 1) {
                    distributeError(result, x + 1, y + 1, errorRed, errorGreen, errorBlue, 1f / 16f)
                }
            }
        }

        return result
    }

    private fun distributeError(bitmap: Bitmap, x: Int, y: Int, errorRed: Int, errorGreen: Int, errorBlue: Int, weight: Float) {
        val pixel = bitmap.getPixel(x, y)
        val red = (Color.red(pixel) + errorRed * weight).coerceIn(0F, 255F)
        val green = (Color.green(pixel) + errorGreen * weight).coerceIn(0F, 255F)
        val blue = (Color.blue(pixel) + errorBlue * weight).coerceIn(0F, 255F)
        bitmap.setPixel(x, y, Color.rgb(red.toInt(), green.toInt(), blue.toInt()))
    }

    ///////////////////////////////////////////////////////////////////////

    fun errorDiffusionDithering(image: Bitmap): Bitmap {
        val result = image.copy(Bitmap.Config.ARGB_8888, true)
        val width = result.width
        val height = result.height

        for (y in 0 until height) {
            for (x in 0 until width) {
                val oldPixel = result.getPixel(x, y)
                val oldRed = Color.red(oldPixel)
                val oldGreen = Color.green(oldPixel)
                val oldBlue = Color.blue(oldPixel)

                val newPixel = quantizePixel(oldPixel)
                result.setPixel(x, y, newPixel)

                val errorRed = oldRed - Color.red(newPixel)
                val errorGreen = oldGreen - Color.green(newPixel)
                val errorBlue = oldBlue - Color.blue(newPixel)

                distributeError2(result, x + 1, y, errorRed, errorGreen, errorBlue, 7 / 16f)
                distributeError2(result, x - 1, y + 1, errorRed, errorGreen, errorBlue, 3 / 16f)
                distributeError2(result, x, y + 1, errorRed, errorGreen, errorBlue, 5 / 16f)
                distributeError2(result, x + 1, y + 1, errorRed, errorGreen, errorBlue, 1 / 16f)
            }
        }

        return result
    }

    private fun quantizePixel(pixel: Int): Int {
        val alpha = Color.alpha(pixel)
        val red = Color.red(pixel)
        val green = Color.green(pixel)
        val blue = Color.blue(pixel)

        val newRed = if (red < 128) 0 else 255
        val newGreen = if (green < 128) 0 else 255
        val newBlue = if (blue < 128) 0 else 255

        return Color.argb(alpha, newRed, newGreen, newBlue)
    }

    private fun distributeError2(bitmap: Bitmap, x: Int, y: Int, errorRed: Int, errorGreen: Int, errorBlue: Int, weight: Float) {
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

    //////////////////////////////////////////////////////////////////////////////////////////////////

    fun errorDiffusionDithering2(image: Bitmap): Bitmap {
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

                distributeError3(result, x + 1, y, errorRed, errorGreen, errorBlue, 7 / 16f)
                distributeError3(result, x - 1, y + 1, errorRed, errorGreen, errorBlue, 3 / 16f)
                distributeError3(result, x, y + 1, errorRed, errorGreen, errorBlue, 5 / 16f)
                distributeError3(result, x + 1, y + 1, errorRed, errorGreen, errorBlue, 1 / 16f)
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

    private fun distributeError3(bitmap: Bitmap, x: Int, y: Int, errorRed: Int, errorGreen: Int, errorBlue: Int, weight: Float) {
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

                val closestColorIndex = findClosestColor2(oldRed, oldGreen, oldBlue, curPal)
                val newPixel = getColorFromPalette2(closestColorIndex, curPal)
                result.setPixel(x, y, newPixel)
            }
        }

        return result
    }

    private fun findClosestColor2(red: Int, green: Int, blue: Int, palette: Array<IntArray>): Int {
        var minDist = Int.MAX_VALUE
        var closestColorIndex = 0

        for (i in palette.indices) {
            val paletteColor = palette[i]
            val colorDist = (red - paletteColor[0]).squared2() +
                    (green - paletteColor[1]).squared2() +
                    (blue - paletteColor[2]).squared2()

            if (colorDist < minDist) {
                minDist = colorDist
                closestColorIndex = i
            }
        }

        return closestColorIndex
    }

    private fun getColorFromPalette2(colorIndex: Int, palette: Array<IntArray>): Int {
        val color = palette[colorIndex]
        return Color.rgb(color[0], color[1], color[2])
    }

    private fun Int.squared2(): Int = this * this

}