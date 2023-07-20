package com.example.e_paper.classes

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.e_paper.dataclass.ItemCanvasObject
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.example.e_paper.dataclass.Line

class SketchImage(context: Context) {
    val context: Context

    var deviceWidth : Int by mutableStateOf(0)
    var deviceHeight : Int by mutableStateOf(0)
    var deviceDpi : Int by mutableStateOf(0)

    var canvasWidth : Float by mutableStateOf(0f)
    var canvasHeight : Float by mutableStateOf(0f)

    var epdWidth : Float by mutableStateOf(300f)
    var epdHeight : Float by mutableStateOf(400f)

    var ratioWidth : Float by mutableStateOf(0f)
    var ratioHeight : Float by mutableStateOf(0f)

//    var itemCanvas : MutableList<ItemCanvas?> = mutableListOf()
    var itemCanvasDraw : MutableList<SnapshotStateList<Line>?> = mutableListOf()

    var savedBitmap :Bitmap? = null

    val listColor: List<Color> = listOf(Color.Black, Color.Red, Color.White)

    //////////////////////////////////////////////////////////////////////
    var itemType : Int by mutableStateOf(0)
    var itemColor : Int by mutableStateOf(0)
    var itemXPos : Float by mutableStateOf(0f)
    var itemYPos : Float by mutableStateOf(0f)
    var itemWidth : Float by mutableStateOf(0f)
    var itemHeight : Float by mutableStateOf(0f)
    var itemRotate : Float by mutableStateOf(0f)
    var itemStroke : Int by mutableStateOf(0)
    var itemStartA : Float by mutableStateOf(0f)
    var itemStartB : Float by mutableStateOf(0f)
    var itemEndA : Float by mutableStateOf(0f)
    var itemEndB : Float by mutableStateOf(0f)
    var itemFill : Boolean by mutableStateOf(false)
    var itemText : String by mutableStateOf("")
    var itemFontSize : Int by mutableStateOf(0)
    var itemFontWeight : FontWeight by mutableStateOf(FontWeight.Normal)
    /////////////////////////////////////////////////////////////////////

//    val textMeasurer = rememberTextMeasurer()

    init {
        this@SketchImage.context = context
//        itemCanvas.addAll(ItemCanvasObject.listData)
    }

    fun getBitmapFromCanvas(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        view.draw(canvas)
        return bitmap
    }

    @OptIn(ExperimentalTextApi::class)
    fun drawCanvas(drawScope: DrawScope, line: SnapshotStateList<Line>, textMeasurer: TextMeasurer) {
        drawScope.apply {
            canvasWidth = size.width
            canvasHeight = size.height

            ratioWidth = (canvasWidth / epdWidth)
            ratioHeight = (canvasHeight / epdHeight)

            Log.i("Sketch", "Canvas = $canvasWidth -- $canvasHeight -- $ratioWidth -- $ratioHeight")

//            Log.i("Sketch", "Item = $itemCanvas")

//            if(!itemCanvas.isNullOrEmpty()){
//                itemCanvas.forEachIndexed{ index, itemUnit ->
//                    when(itemUnit!!.itemType){
//                        0 -> {
//                            drawLine(
//                                color = itemUnit.color,
//                                start = Offset((itemUnit.startA*ratioWidth), (itemUnit.startB*ratioHeight)),
//                                end = Offset((itemUnit.endA*ratioWidth), (itemUnit.endB*ratioHeight)),
//                                strokeWidth = itemUnit.stroke.dp.toPx()
//                            )
//                        }
//                        1 -> {
//                            rotate(itemUnit.rotate, pivot = Offset(((itemUnit.width*ratioWidth)+(itemUnit.width*ratioWidth)/2), ((itemUnit.height*ratioHeight)+(itemUnit.width*ratioWidth)/2))){
//                                if(itemUnit!!.fill){
//                                    drawRect(
//                                        color = itemUnit.color,
//                                        topLeft = Offset((itemUnit.xPos*ratioWidth), (itemUnit.yPos*ratioHeight)),
//                                        size = Size((itemUnit.width*ratioWidth), (itemUnit.height*ratioHeight))
//                                    )
//                                }
//                                else{
//                                    drawRect(
//                                        color = itemUnit.color,
//                                        topLeft = Offset((itemUnit.xPos*ratioWidth), (itemUnit.yPos*ratioHeight)),
//                                        size = Size((itemUnit.width*ratioWidth), (itemUnit.height*ratioHeight)),
//                                        style = Stroke(
//                                            width = itemUnit.stroke.dp.toPx()
//                                        )
//                                    )
//                                }
//                            }
//                        }
//                        2 -> {
//                            if(itemUnit!!.fill){
//                                drawCircle(
//                                    color = itemUnit.color,
//                                    radius = (itemUnit.width*ratioWidth) / 2.0f,
//                                    center = Offset((itemUnit.xPos*ratioWidth), (itemUnit.yPos*ratioHeight))
//                                )
//                            }
//                            else{
//                                drawCircle(
//                                    color = itemUnit.color,
//                                    radius = (itemUnit.width*ratioWidth) / 2.0f,
//                                    center = Offset((itemUnit.xPos*ratioWidth), (itemUnit.yPos*ratioHeight)),
//                                    style = Stroke(
//                                        width = itemUnit.stroke.dp.toPx()
//                                    )
//                                )
//                            }
//                        }
//                        3 -> {
////                            val myText = textMeasurer.measure(style = TextStyle(fontSize = itemUnit.FontSize.sp, fontWeight = itemUnit.FontWeight, background = Color.Cyan), text = itemUnit.Text)
//                            rotate(itemUnit.rotate){
//                                val paint = Paint()
//                                paint.textAlign = Paint.Align.CENTER
//                                paint.textSize = 64f
//                                paint.color = 0xffb0b3ff.toInt()
//
//                                drawContext.canvas.nativeCanvas.drawText(
//                                    "-----",
//                                    (itemUnit.xPos*ratioWidth), (itemUnit.yPos*ratioHeight), paint
//                                )
////                                drawText(
////                                    textLayoutResult = myText,
////                                    color = itemUnit.color,
////                                    topLeft = Offset((itemUnit.xPos*ratioWidth), (itemUnit.yPos*ratioHeight)),
////                                )
//                            }
//                        }
//                        4 -> {
//
//                        }
//                        else -> "Tools Wrong"
//                    }
//                }
//            }

            line.forEach { line ->
//                Log.i("Sketch", "Canvas = ${line.start} -- ${line.end}")
                var yOffsetStart = if(line.start.y < 0) 0f else if (line.start.y > canvasHeight) canvasHeight else line.start.y
                var yOffsetEnd = if(line.end.y < 0) 0f else if (line.end.y > canvasHeight) canvasHeight else line.end.y
                drawLine(
                    color = line.color,
                    start = Offset(line.start.x, yOffsetStart),
                    end = Offset(line.end.x, yOffsetEnd),
                    strokeWidth = line.strokeWidth.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
    }

    fun GetScreenDevice(){
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        deviceWidth = displayMetrics.widthPixels
        deviceHeight = displayMetrics.heightPixels
        deviceDpi = displayMetrics.densityDpi

        Log.i("Sketch", "Window = $deviceWidth -- $deviceHeight -- $deviceDpi")
    }

    fun GetScreenOrientation(): Boolean{
        var status = false
        if (isLandscape()) {
            Log.i("Sketch", "Orientation = Landscape")
            status = false
        } else {
            Log.i("Sketch", "Orientation = Potrait")
            status = true
        }

        return status
    }

    fun getCurrentOrientation(): Int {
        val configuration = context.resources.configuration
        return configuration.orientation
    }

    private fun isLandscape(): Boolean {
        return getCurrentOrientation() == Configuration.ORIENTATION_LANDSCAPE
    }

//    fun isPortrait(): Boolean {
//        return getCurrentOrientation() == Configuration.ORIENTATION_PORTRAIT
//    }
}