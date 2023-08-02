package com.example.e_paper.page

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.input.pointer.pointerInput
import com.example.e_paper.classes.ImageProcessing
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.CaptureController
import androidx.compose.ui.text.ExperimentalTextApi
import com.example.e_paper.classes.SketchDrawing
import com.example.e_paper.classes.SketchText
import com.example.e_paper.components.ItemTextLandscape
import com.example.e_paper.components.ItemTextPotrait

@OptIn(ExperimentalTextApi::class)
@Composable
fun CanvasPage(sketchImage: SketchDrawing, imageProcessing: ImageProcessing, captureController: CaptureController, sketchText: SketchText) {


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
                ) { change, _ ->
                    if (sketchImage.enablePen) {
                        var x = change.position.x
                        var y =
                            if (change.position.y <= 0) 0f else if (change.position.y >= size.height) size.height.toFloat() else change.position.y
                        val newPoint = Offset(x = x, y = y)
//                        Log.i("Sketch", "Height ${size.width}")
                        sketchImage.updateLatestPath(newPoint)
                    }
                }
            }
            .pointerInput(true) {
                detectTapGestures(
                    onTap = {
                        if (sketchImage.enablePen) {
                            sketchImage.insertNewPath(it)
                            sketchImage.updateLatestPath(it)
                            sketchImage.historyTrack
                        }
                    }
                )
            }
        ) {
            sketchImage.drawCanvas(drawScope = this)
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            if(sketchText.textList.isNotEmpty()){
                sketchText.textList.forEachIndexed { index, itemText ->

                    if(itemText.indexOrientation == 1){
                        ItemTextPotrait(sketchText = sketchText, itemText = itemText, index = index)
                    }
                    else{
                        ItemTextLandscape(sketchText = sketchText, itemText = itemText, index = index)
                    }

                }
            }
        }
    }
}