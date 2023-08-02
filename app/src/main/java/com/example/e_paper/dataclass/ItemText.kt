package com.example.e_paper.dataclass

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

data class ItemText(
    var color: Color = Color.Black,
    val sizeText: Int = 24,
    val Rotation: Float = 0f,
    val Text: String = "",
    val align: TextAlign = TextAlign.Center,
    val indexAlign: Int = 0,
    val indexOrientation: Int = 0,
    val indexColor: Int = 0,
)
