package com.example.e_paper.classes

import android.content.Context
import android.graphics.Bitmap
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import com.example.e_paper.dataclass.Line

class SketchDrawing(context: Context) {

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

    var itemCanvasDraw : MutableList<SnapshotStateList<Line>?> = mutableListOf()

    var savedBitmap : Bitmap? = null

    val listColor: List<Color> = listOf(Color.Black, Color.Red, Color.White)

    init {
        this@SketchDrawing.context = context
    }

    fun drawCanvas(drawScope: DrawScope) {
        drawScope.apply {
            canvasWidth = size.width
            canvasHeight = size.height

            ratioWidth = (canvasWidth / epdWidth)
            ratioHeight = (canvasHeight / epdHeight)

            if(!itemCanvasDraw.isNullOrEmpty()) {
                itemCanvasDraw.forEachIndexed { index, itemUnit ->
                    itemUnit?.forEach { line ->
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
}