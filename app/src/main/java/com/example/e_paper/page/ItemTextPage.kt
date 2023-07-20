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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
fun ItemTextPage(sketchImage: SketchImage, navController: NavController) {
    val listItem: List<String> = listOf("Line","Square", "Circle", "Text", "Image")
    val listColor: List<Color> = listOf(Color.Black, Color.Red, Color.White)

    var selectedItem by remember { mutableStateOf(0) }
    var text by remember { mutableStateOf(TextFieldValue(sketchImage.itemText)) }
    var rotate by remember { mutableStateOf(TextFieldValue(sketchImage.itemRotate.roundToInt().toString())) }
    var fontSize by remember { mutableStateOf(TextFieldValue(sketchImage.itemFontSize.toString())) }
    var fill by remember { mutableStateOf(sketchImage.itemFill) }
    var xPos by remember { mutableStateOf(TextFieldValue(sketchImage.itemXPos.roundToInt().toString())) }
    var yPos by remember { mutableStateOf(TextFieldValue(sketchImage.itemYPos.roundToInt().toString())) }

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
            Text(text = "Text", fontSize = MaterialTheme.typography.titleLarge.fontSize)
            OutlinedTextField(
                value = text,
                label = { Text(text = "Text") },
                onValueChange = {
                    text = it
                },
                isError = false,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
            Text(text = "Position", fontSize = MaterialTheme.typography.titleLarge.fontSize)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                OutlinedTextField(
                    value = xPos,
                    label = { Text(text = "X") },
                    onValueChange = {
                        xPos = it
                        if(!xPos.text.isNullOrEmpty()){
                            if(xPos.text.toInt() < 0){
                                xPos = TextFieldValue("0")
                            }
//                        else if(xPos.text.toInt() > screenWidth.value){
//                            xPos = TextFieldValue(screenWidth.toString())
//                        }
                        }
                        else{
                            xPos = TextFieldValue("0")
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
                    value = yPos,
                    label = { Text(text = "Y") },
                    onValueChange = {
                        yPos = it
                        if(!yPos.text.isNullOrEmpty()){
                            if(yPos.text.toInt() < 0){
                                yPos = TextFieldValue("0")
                            }
//                        else if(yPos.text.toInt() > screenHeight.value){
//                            yPos = TextFieldValue(screenHeight.toString())
//                        }
                        }
                        else{
                            yPos = TextFieldValue("0")
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
            Text(text = "Font Size", fontSize = MaterialTheme.typography.titleLarge.fontSize)
            OutlinedTextField(
                value = fontSize,
                label = { Text(text = "Size") },
                onValueChange = {
                    fontSize = it
                    if(!fontSize.text.isNullOrEmpty()){
                        if(fontSize.text.toInt() < 0){
                            fontSize = TextFieldValue("0")
                        }
//                        else if(width.text.toInt() > screenWidth.value){
//                            width = TextFieldValue(screenWidth.toString())
//                        }
                    }
                    else{
                        fontSize = TextFieldValue("0")
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
            Text(text = "Rotate", fontSize = MaterialTheme.typography.titleLarge.fontSize)
            OutlinedTextField(
                value = rotate,
                label = { Text(text = "Degree") },
                onValueChange = {
                    rotate = it
                    if(!rotate.text.isNullOrEmpty()){
                        if(rotate.text.toInt() < 0){
                            rotate = TextFieldValue("0")
                        }
//                        else if(xPos.text.toInt() > screenWidth.value){
//                            xPos = TextFieldValue(screenWidth.toString())
//                        }
                    }
                    else{
                        rotate = TextFieldValue("0")
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
            Text(text = "Bold", fontSize = MaterialTheme.typography.titleLarge.fontSize)
            Box(
                modifier = Modifier
                    .size(25.dp)
                    .background(if (fill) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurfaceVariant)
                    .clickable {
                        fill = !fill
                    },
                contentAlignment = Alignment.Center
            ) {
                if(fill)
                    Icon(Icons.Default.Check, contentDescription = "Check Box", tint = MaterialTheme.colorScheme.surface)
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
                sketchImage.itemCanvas.add(
                    ItemCanvas(
                        itemType = 3,
                        color = listColor[selectedItem],
                        Text = text.text,
                        rotate = rotate.text.toFloat(),
                        FontSize = fontSize.text.toInt(),
                        fill = fill,
                        FontWeight = if(fill) FontWeight.Bold else FontWeight.Normal,
                        xPos = xPos.text.toFloat(),
                        yPos = yPos.text.toFloat()
                    )
                )
                navController.navigate(route = Screens.Sketch.route) {
                    popUpTo(Screens.Sketch.route) {
                        inclusive = true
                    }
                }
            },
        ) {
            Text(text = "Create Text")
        }
    }
}