package com.example.e_paper.components

import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_paper.classes.SketchText
import com.example.e_paper.dataclass.ItemText
import kotlin.math.roundToInt

@Composable
fun ItemTextLandscape(sketchText: SketchText, itemText: ItemText, index: Int) {
    //    Height = 1601

    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
//        rotation += rotationChange
    }

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Text(
        text = "${itemText.Text}",
        fontSize = itemText.sizeText.sp,
        textAlign = itemText.align,
        modifier = Modifier
            .padding(8.dp)
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .transformable(state = state)
            .padding(40.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        sketchText.editMode = true
                        sketchText.itemIndex = index
                        sketchText.alignSelected = sketchText.textList[sketchText.itemIndex].indexAlign
                        sketchText.orientationSelected = sketchText.textList[sketchText.itemIndex].indexOrientation
                        sketchText.colorSelected = sketchText.textList[sketchText.itemIndex].indexColor
                        sketchText.showPageEdit = true
                    }
                )
            }
            .graphicsLayer(
                rotationZ = itemText.Rotation,
            )
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val maxHeight = 1601
                    val maxWidth = 1080

                    offsetX -= dragAmount.y
                    offsetY += dragAmount.x

                    if (offsetY < -((maxHeight / 2) - (size.width / 2).toFloat())) {
                        offsetY = -((maxHeight / 2) - (size.width / 2).toFloat())
                    }
                    if (offsetY > ((maxHeight / 2) - (size.width / 2).toFloat())) {
                        offsetY = ((maxHeight / 2) - (size.width / 2).toFloat())
                    }

                    if (offsetX < -((maxWidth / 2) - (size.height / 2).toFloat())) {
                        offsetX = -((maxWidth / 2) - (size.height / 2).toFloat())
                    }
                    if (offsetX > ((maxWidth / 2) - (size.height / 2).toFloat())) {
                        offsetX = ((maxWidth / 2) - (size.height / 2).toFloat())
                    }

                    Log.i("Sketch", "Landscape ${offsetX} -- $offsetY -- ${size}")
                }
            },
        color = itemText.color
    )
}