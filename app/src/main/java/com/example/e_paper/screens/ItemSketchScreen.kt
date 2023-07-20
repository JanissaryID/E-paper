package com.example.e_paper.screens

import androidx.compose.foundation.background
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
import androidx.navigation.NavController
import com.example.e_paper.classes.SketchImage
import com.example.e_paper.page.ItemCirclePage
import com.example.e_paper.page.ItemLinePage
import com.example.e_paper.page.ItemShapePage
import com.example.e_paper.page.ItemTextPage
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemSketchScreen(sketchImage: SketchImage, itemIndex: Int, navController: NavController) {
    when(itemIndex){
        0 -> {
            ItemLinePage(sketchImage = sketchImage, navController = navController)
        }
        1 -> {
            ItemShapePage(sketchImage = sketchImage, navController = navController)
        }
        2 -> {
            ItemCirclePage(sketchImage = sketchImage, navController = navController)
        }
        3 -> {
            ItemTextPage(sketchImage = sketchImage, navController = navController)
        }
        else -> ""
    }
}