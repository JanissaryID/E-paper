package com.example.e_paper.page

import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.withSave
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import com.example.e_paper.classes.ImageProcessing
import com.example.e_paper.classes.SketchImage
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.CaptureController
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.example.e_paper.classes.SketchDrawing
import com.example.e_paper.dataclass.Line

@OptIn(ExperimentalTextApi::class)
@Composable
fun CanvasPage(sketchImage: SketchDrawing, imageProcessing: ImageProcessing, captureController: CaptureController) {

    val lines = remember {
        mutableStateListOf<Line>()
    }

    val textMeasurer = rememberTextMeasurer()

    Capturable(
        controller = captureController,
        onCaptured = { bitmap, error ->
            // This is captured bitmap of a content inside Capturable Composable.
            if (bitmap != null) {
                // Bitmap is captured successfully. Do something with it!
                imageProcessing.myBitmapCanvas = imageProcessing.rotateBitmap(bitmap.asAndroidBitmap(), 270f)
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
                detectDragGestures { change, dragAmount ->
                    change.consume()

                    val line = Line(
                        start = change.position - dragAmount,
                        end = change.position
                    )

                    lines.add(line)
                }
            }
        ) {
            sketchImage.drawCanvas(drawScope = this)
        }
    }
}