package com.example.e_paper.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.ak1.drawbox.DrawBox
import io.ak1.drawbox.rememberDrawController

@Composable
fun CanvasPage2() {
    val controller = rememberDrawController()

//    Column() {
//        DrawBox(
//            drawController = controller,
//            modifier = Modifier.fillMaxSize()
//                .weight(1f, true),
//            bitmapCallback = { imageBitmap, throwable ->
//                imageBitmap?.let {
//                    save
//                }
//            }
//        )
//    }
}