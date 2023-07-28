package com.example.e_paper.page

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.e_paper.classes.ImageProcessing
import com.example.e_paper.classes.SketchDrawing
import com.example.e_paper.classes.Tools
import dev.shreyaspatil.capturable.controller.rememberCaptureController


@Composable
fun SketchPage(imageProcessing: ImageProcessing, sketchImage: SketchDrawing, tools: Tools, navController: NavController) {

    val captureController = rememberCaptureController()

    var sliderValue by remember {
        mutableStateOf(0f) // pass the initial value
    }

    var selectedItem by remember { mutableStateOf(0) }

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
            itemsIndexed(items = tools.listTools2) { index, device ->
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
                    Icon(painter = painterResource(id = tools.listIcon2[index]), contentDescription = "Localized description")
                }
            }
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(610.dp)
            .background(Color.White)) {
            CanvasPage(sketchImage = sketchImage, imageProcessing = imageProcessing, captureController = captureController)

        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
                .height(64.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            itemsIndexed(items = tools.listColor) { index, color ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(64.dp)
                        .clickable {
                            selectedItem = index
                            sketchImage.color = sketchImage.listColor[selectedItem]
                        }
                ) {
                    Surface(modifier = Modifier.size(46.dp), shape = CircleShape, color = color, border = BorderStroke(5.dp, if(selectedItem == index) Color.Green else Color.Transparent)) {

                    }
                }
            }
        }
        Slider(
            value = sketchImage.strokeWidth,
            onValueChange = { sliderValue_ ->
                sliderValue = sliderValue_
                sketchImage.strokeWidth = sliderValue
            },
            onValueChangeFinished = {

                // this is called when the user completed selecting the value
//                Log.d("MainActivity", "sliderValue = $sliderValue")
            },
            valueRange = 1f..100f,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )
    }
}