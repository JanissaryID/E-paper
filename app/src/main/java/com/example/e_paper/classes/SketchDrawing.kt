package com.example.e_paper.classes

import android.content.Context
import android.graphics.Bitmap
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.e_paper.dataclass.TrackLine

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

    var undoList = mutableStateListOf<TrackLine>()
    var redoList = mutableStateListOf<TrackLine>()
    var historyTrack: SnapshotStateList<TrackLine> = undoList

    var savedBitmap : Bitmap? = null

    val listColor: List<Color> = listOf(Color.Black, Color.Red, Color.White)

    init {
        this@SketchDrawing.context = context
    }

    fun updateLatestPath(newPoint: Offset) {
        val index = undoList.lastIndex
        undoList[index].points.add(newPoint)
    }

    fun insertNewPath(newPoint: Offset){
        val pathTrackLine = TrackLine(
            points = mutableStateListOf(newPoint),
            strokeColor = color,
            alpha = opacity,
            strokeWidth = strokeWidth,
        )
        undoList.add(pathTrackLine)
        redoList.clear()
    }

    fun Undo(){
        if(undoList.isNotEmpty()){
            val last = undoList.last()
            redoList.add(last)
            undoList.remove(last)
        }
    }

    fun Redo(){
        if(redoList.isNotEmpty()){
            val last = redoList.last()
            undoList.add(last)
            redoList.remove(last)
        }
    }

    var strokeWidth by mutableStateOf(10f)

    var color by mutableStateOf(Color.Black)

    var opacity by mutableStateOf(1f)
        private set

    fun drawCanvas(drawScope: DrawScope) {
        drawScope.apply {
            canvasWidth = size.width
            canvasHeight = size.height

            ratioWidth = (canvasWidth / epdWidth)
            ratioHeight = (canvasHeight / epdHeight)

            historyTrack.forEach { track ->
                drawPath(
                    path = createPath(track.points),
                    color = track.strokeColor,
                    alpha = track.alpha,
                    style = Stroke(
                        width = track.strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }
    }

    fun createPath(points: List<Offset>) = Path().apply {
        if (points.size > 1) {
            var oldPoint: Offset? = null
            this.moveTo(points[0].x, points[0].y)
            for (i in 1 until points.size) {
                val point: Offset = points[i]
                oldPoint?.let {
                    val midPoint = calculateMidpoint(it, point)
                    if (i == 1) {
                        this.lineTo(midPoint.x, midPoint.y)
                    } else {
                        this.quadraticBezierTo(it.x, it.y, midPoint.x, midPoint.y)
                    }
                }
                oldPoint = point
            }
            oldPoint?.let { this.lineTo(it.x, oldPoint.y) }
        }
    }

    private fun calculateMidpoint(start: Offset, end: Offset) =
        Offset((start.x + end.x) / 2, (start.y + end.y) / 2)

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