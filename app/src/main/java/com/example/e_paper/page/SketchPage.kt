package com.example.e_paper.page

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.e_paper.R
import com.example.e_paper.classes.ImageProcessing
import com.example.e_paper.classes.SketchDrawing
import com.example.e_paper.classes.SketchImage
import com.example.e_paper.classes.Tools
import com.example.e_paper.components.ItemTool
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import io.ak1.drawbox.DrawBox
import io.ak1.drawbox.rememberDrawController

@Composable
fun SketchPage(imageProcessing: ImageProcessing, sketchImage: SketchDrawing, tools: Tools, navController: NavController) {

    val captureController = rememberCaptureController()

    sketchImage.GetScreenDevice()

//    w = 1080
//    h = 2138
    Column(modifier = Modifier.fillMaxSize()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
                .height(64.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            itemsIndexed(items = tools.listTools) { index, device ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(64.dp)
                        .clickable {
                            tools.addItemCanvas2(
                                sketchImage = sketchImage,
                                position = index,
                                captureController = captureController,
                                navController = navController
                            )
                        }
                ) {
                    Icon(painter = painterResource(id = tools.listIcon[index]), contentDescription = "Localized description")
                }
            }
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(610.dp)
            .background(Color.White)) {
            CanvasPage(sketchImage = sketchImage, imageProcessing = imageProcessing, captureController = captureController)
        }
//        LazyRow(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.Gray)
//                .height(64.dp),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ){
//            itemsIndexed(items = tools.listTools2) { index, device ->
//                Box(
//                    contentAlignment = Alignment.Center,
//                    modifier = Modifier
//                        .size(64.dp)
//                        .clickable {
//                            tools.addItemCanvas(
//                                sketchImage = sketchImage,
//                                position = index,
//                                captureController = captureController,
//                                navController = navController
//                            )
//                        }
//                ) {
//                    Icon(painter = painterResource(id = tools.listIcon2[index]), contentDescription = "Localized description")
//                }
//            }
//        }
    }
}