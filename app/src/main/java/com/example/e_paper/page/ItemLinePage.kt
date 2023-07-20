package com.example.e_paper.page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.e_paper.classes.SketchImage
import com.example.e_paper.navigation.Screens
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemLinePage(sketchImage: SketchImage, navController: NavController) {
    val listItem: List<String> = listOf("Line","Square", "Circle", "Text", "Image")
    val listColor: List<Color> = listOf(Color.Black, Color.Red, Color.White)

    var stroke by remember { mutableStateOf(TextFieldValue(sketchImage.itemStroke.toString())) }

    var startA by remember { mutableStateOf(TextFieldValue(sketchImage.itemStartA.roundToInt().toString())) }
    var startB by remember { mutableStateOf(TextFieldValue(sketchImage.itemStartB.roundToInt().toString())) }

    var endA by remember { mutableStateOf(TextFieldValue(sketchImage.itemEndA.roundToInt().toString())) }
    var endB by remember { mutableStateOf(TextFieldValue(sketchImage.itemEndB.roundToInt().toString())) }

    var selectedItem by remember { mutableStateOf(0) }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (button, body) = createRefs()

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .constrainAs(body){
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            }
        ) {
            Text(text = "${listItem[sketchImage.itemType]}", fontSize = MaterialTheme.typography.titleLarge.fontSize)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Color", fontSize = MaterialTheme.typography.titleLarge.fontSize)
            Row(modifier = Modifier.fillMaxWidth()) {
                listColor.forEachIndexed { index, color ->
                    if(selectedItem == index){
                        Box(modifier = Modifier
                            .size(36.dp)
                            .background(color)
                            .border(5.dp, Color.White)
                            .clickable {
                                selectedItem = index
                            }
                        )
                    }
                    else{
                        Box(modifier = Modifier
                            .size(36.dp)
                            .background(color)
                            .clickable {
                                selectedItem = index
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Stroke", fontSize = MaterialTheme.typography.titleLarge.fontSize)
            OutlinedTextField(
                value = stroke,
                label = { Text(text = "Width") },
                onValueChange = {
                    stroke = it
                    if(!stroke.text.isNullOrEmpty()){
                        if(stroke.text.toInt() < 0){
                            stroke = TextFieldValue("0")
                        }
//                        else if(xPos.text.toInt() > screenWidth.value){
//                            xPos = TextFieldValue(screenWidth.toString())
//                        }
                    }
                    else{
                        stroke = TextFieldValue("0")
                    }
                },
                isError = false,
                modifier = Modifier.width(100.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    containerColor = MaterialTheme.colorScheme.surface,
//                    textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTextColor = MaterialTheme.colorScheme.surfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                    placeholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Start", fontSize = MaterialTheme.typography.titleLarge.fontSize)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                OutlinedTextField(
                    value = startA,
                    label = { Text(text = "StartA") },
                    onValueChange = {
                        startA = it
                        if(!startA.text.isNullOrEmpty()){
                            if(startA.text.toInt() < 0){
                                startA = TextFieldValue("0")
                            }
//                        else if(width.text.toInt() > screenWidth.value){
//                            width = TextFieldValue(screenWidth.toString())
//                        }
                        }
                        else{
                            startA = TextFieldValue("0")
                        }
                    },
                    isError = false,
                    modifier = Modifier.width(100.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        containerColor = MaterialTheme.colorScheme.surface,
//                    textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTextColor = MaterialTheme.colorScheme.surfaceVariant,
                        cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                    placeholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = startB,
                    label = { Text(text = "startB") },
                    onValueChange = {
                        startB = it
                        if(!startB.text.isNullOrEmpty()){
                            if(startB.text.toInt() < 0){
                                startB = TextFieldValue("0")
                            }
//                        else if(height.text.toInt() > screenHeight.value){
//                            height = TextFieldValue(screenHeight.toString())
//                        }
                        }
                        else{
                            startB = TextFieldValue("0")
                        }
                    },
                    isError = false,
                    modifier = Modifier.width(100.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        containerColor = MaterialTheme.colorScheme.surface,
//                    textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTextColor = MaterialTheme.colorScheme.surfaceVariant,
                        cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                    placeholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "End", fontSize = MaterialTheme.typography.titleLarge.fontSize)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                OutlinedTextField(
                    value = endA,
                    label = { Text(text = "endA") },
                    onValueChange = {
                        endA = it
                        if(!endA.text.isNullOrEmpty()){
                            if(endA.text.toInt() < 0){
                                endA = TextFieldValue("0")
                            }
//                        else if(width.text.toInt() > screenWidth.value){
//                            width = TextFieldValue(screenWidth.toString())
//                        }
                        }
                        else{
                            endA = TextFieldValue("0")
                        }
                    },
                    isError = false,
                    modifier = Modifier.width(100.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        containerColor = MaterialTheme.colorScheme.surface,
//                    textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTextColor = MaterialTheme.colorScheme.surfaceVariant,
                        cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                    placeholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = endB,
                    label = { Text(text = "endB") },
                    onValueChange = {
                        endB = it
                        if(!endB.text.isNullOrEmpty()){
                            if(endB.text.toInt() < 0){
                                endB = TextFieldValue("0")
                            }
//                        else if(height.text.toInt() > screenHeight.value){
//                            height = TextFieldValue(screenHeight.toString())
//                        }
                        }
                        else{
                            endB = TextFieldValue("0")
                        }
                    },
                    isError = false,
                    modifier = Modifier.width(100.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        containerColor = MaterialTheme.colorScheme.surface,
//                    textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTextColor = MaterialTheme.colorScheme.surfaceVariant,
                        cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                    placeholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                )
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(horizontal = 16.dp)
                .constrainAs(button){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, 16.dp)
                },
            onClick = {
//                sketchImage.itemCanvas.add(
//                    ItemCanvas(
//                        itemType = 0,
//                        color = listColor[selectedItem],
//                        stroke = stroke.text.toInt(),
//                        startA = startA.text.toFloat(),
//                        startB = startB.text.toFloat(),
//                        endA = endA.text.toFloat(),
//                        endB = endB.text.toFloat(),
//                        fill = false,
//                    )
//                )
                navController.navigate(route = Screens.Sketch.route) {
                    popUpTo(Screens.Sketch.route) {
                        inclusive = true
                    }
                }
            },
        ) {
            Text(text = "Create Line")
        }
    }
}