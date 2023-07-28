package com.example.e_paper.page

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.input.pointer.pointerInput
import com.example.e_paper.classes.ImageProcessing
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.CaptureController
import androidx.compose.ui.text.ExperimentalTextApi
import com.example.e_paper.classes.SketchDrawing

@OptIn(ExperimentalTextApi::class)
@Composable
fun CanvasPage(sketchImage: SketchDrawing, imageProcessing: ImageProcessing, captureController: CaptureController) {

    Capturable(
        controller = captureController,
        onCaptured = { bitmap, error ->
            // This is captured bitmap of a content inside Capturable Composable.
            if (bitmap != null) {
                // Bitmap is captured successfully. Do something with it!
                imageProcessing.myBitmapCanvas.value = imageProcessing.rotateBitmap(bitmap.asAndroidBitmap(), 270f)
            }

            if (error != null) {
                // Error occurred. Handle it!
                Log.i("Sketch", "Error Get Image")
            }
        }
    ) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = {
                        sketchImage.insertNewPath(it)
                    }
                ){change, _ ->
                    val newPoint = change.position
                    sketchImage.updateLatestPath(newPoint)
                }
            }
            .pointerInput(true) {
                detectTapGestures(
                    onTap = {
                        sketchImage.insertNewPath(it)
                        sketchImage.updateLatestPath(it)
                        sketchImage.historyTrack
                    }
                )
            }
        ) {
            sketchImage.drawCanvas(drawScope = this)
        }
    }
}