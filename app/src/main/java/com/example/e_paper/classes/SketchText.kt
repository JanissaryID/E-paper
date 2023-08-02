package com.example.e_paper.classes

import android.graphics.drawable.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.e_paper.R
import com.example.e_paper.dataclass.ItemText

class SketchText {
    var textList = mutableStateListOf<ItemText>()
    var showPageEdit : Boolean by mutableStateOf(false)

    val listAlign: List<Int> = listOf( R.drawable.round_format_align_left_24,  R.drawable.round_format_align_center_24,  R.drawable.round_format_align_right_24)
    val listAlignText: List<TextAlign> = listOf( TextAlign.Left, TextAlign.Center, TextAlign.Right)
    var alignSelected : Int by mutableStateOf(1)

    val listColor: List<Color> = listOf(Color.Black, Color.Red, Color.White)
    var colorSelected : Int by mutableStateOf(0)
    var color : Color by mutableStateOf(listColor[colorSelected])

    val listOrientation: List<Int> = listOf( R.drawable.round_crop_landscape_24,  R.drawable.round_crop_portrait_24)
    val listOrientationDegree: List<Float> = listOf( 90f, 0f)
    var orientationSelected : Int by mutableStateOf(0)

    var editMode : Boolean by mutableStateOf(false)
    var itemIndex : Int by mutableStateOf(0)
}