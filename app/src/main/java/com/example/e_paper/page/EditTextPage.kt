package com.example.e_paper.page

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_paper.ImeState
import com.example.e_paper.classes.SketchDrawing
import com.example.e_paper.classes.SketchText
import com.example.e_paper.dataclass.ItemText
import com.example.e_paper.navigation.Screens
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTextPage(sketchText: SketchText) {

    var sliderValue by remember {
        mutableStateOf(if(sketchText.editMode) sketchText.textList[sketchText.itemIndex].sizeText.toFloat() else 20f) // pass the initial value
    }

    var sizeText by remember {
        mutableStateOf(if(sketchText.editMode) sketchText.textList[sketchText.itemIndex].sizeText else 20)
    }

    var text by remember { mutableStateOf(TextFieldValue(if(sketchText.editMode) sketchText.textList[sketchText.itemIndex].Text else "Hello World"))}

    val imeState = ImeState()
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value){
            scrollState.animateScrollTo(scrollState.maxValue, tween(300))
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.DarkGray.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ){

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)) {
            Slider(
                value = sizeText.toFloat(),
                onValueChange = { sliderValue_ ->
                    sliderValue = sliderValue_
                    sizeText = sliderValue.roundToInt()
                    Log.i("Slider", "Slider = $sizeText")
                },
                onValueChangeFinished = {

                    // this is called when the user completed selecting the value
//                Log.d("MainActivity", "sliderValue = $sliderValue")
                },
                valueRange = 20f..70f,
                steps = 50,
                thumb = {
                    Surface(modifier = Modifier.size(26.dp), shape = CircleShape) {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center){
                            Text(text = "${sizeText}", fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Row(modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(modifier = Modifier
                    .size(36.dp), shape = RoundedCornerShape(8.dp)) {
                    Box(modifier = Modifier.fillMaxSize().background(Color.White).clickable {
                        if(sketchText.alignSelected == 2){
                            sketchText.alignSelected = 0
                        }
                        else if(sketchText.alignSelected <= 2 && sketchText.alignSelected >= 0){
                            sketchText.alignSelected++
                        }
                    }, contentAlignment = Alignment.Center){
                        Icon(tint = Color.DarkGray,modifier = Modifier.padding(8.dp),painter = painterResource(id = sketchText.listAlign[sketchText.alignSelected]), contentDescription = "Icon Align Text")
                    }
                }
                Surface(modifier = Modifier
                    .size(36.dp), shape = RoundedCornerShape(8.dp)) {
                    Box(modifier = Modifier.fillMaxSize().background(Color.White).clickable {
                        if(sketchText.orientationSelected == 1){
                            sketchText.orientationSelected = 0
                        }
                        else if(sketchText.orientationSelected == 0){
                            sketchText.orientationSelected++
                        }
                    }, contentAlignment = Alignment.Center){
                        Icon(tint = Color.DarkGray,modifier = Modifier.padding(8.dp),painter = painterResource(id = sketchText.listOrientation[sketchText.orientationSelected]), contentDescription = "Icon Orientation")
                    }
                }
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    itemsIndexed(items = sketchText.listColor) { index, color ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(36.dp)
                                .clickable {
                                    sketchText.colorSelected = index
                                    sketchText.color = sketchText.listColor[sketchText.colorSelected]
                                }
                        ) {
                            Surface(modifier = Modifier.size(36.dp), shape = CircleShape, color = color, border = BorderStroke(3.dp, if(sketchText.colorSelected == index) MaterialTheme.colorScheme.primary else Color.Transparent)) {

                            }
                        }
                    }
                }
            }
            Box(modifier = Modifier
                .fillMaxSize()
                .weight(10f), contentAlignment = Alignment.Center){
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    textStyle = TextStyle(fontSize = sizeText.sp, textAlign = sketchText.listAlignText[sketchText.alignSelected], color = sketchText.color),
                    isError = false,
                    modifier = Modifier.wrapContentSize(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        containerColor = Color.Transparent,
                        disabledTextColor = Color.Transparent,
                        cursorColor = Color.Transparent,
                        disabledPlaceholderColor = Color.Transparent,
                    ),
                )
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    modifier = Modifier
                        .wrapContentSize()
                        .height(52.dp)
                        .padding(horizontal = 16.dp),
                    onClick = {
                        if(sketchText.editMode){
                            sketchText.textList[sketchText.itemIndex] = ItemText(
                                Text = text.text,
                                sizeText = sizeText,
                                Rotation = sketchText.listOrientationDegree[sketchText.orientationSelected],
                                color = sketchText.color,
                                align = sketchText.listAlignText[sketchText.alignSelected],
                                indexAlign = sketchText.alignSelected,
                                indexOrientation = sketchText.orientationSelected,
                                indexColor = sketchText.colorSelected
                            )
                        }
                        else{
                            sketchText.textList.add(ItemText(
                                Text = text.text,
                                sizeText = sizeText,
                                Rotation = sketchText.listOrientationDegree[sketchText.orientationSelected],
                                color = sketchText.color,
                                align = sketchText.listAlignText[sketchText.alignSelected],
                                indexAlign = sketchText.alignSelected,
                                indexOrientation = sketchText.orientationSelected,
                                indexColor = sketchText.colorSelected
                            ))
                        }
                        sketchText.showPageEdit = false
                    },
                ) {
                    Text(text = "Done")
                }
                Button(
                    modifier = Modifier
                        .wrapContentSize()
                        .height(52.dp)
                        .padding(horizontal = 16.dp),
                    onClick = {

                    },
                ) {
                    Text(text = "Delete")
                }
            }
        }
    }
}