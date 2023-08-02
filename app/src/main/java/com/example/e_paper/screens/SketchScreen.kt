package com.example.e_paper.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.e_paper.classes.ImageProcessing
import com.example.e_paper.classes.SketchDrawing
import com.example.e_paper.classes.SketchText
import com.example.e_paper.classes.Tools
import com.example.e_paper.page.EditTextPage
import com.example.e_paper.page.SketchPage

@Composable
fun SketchScreen(sketchImage: SketchDrawing, imageProcessing: ImageProcessing, tools: Tools, navController: NavController, sketchText: SketchText) {
    SketchPage(sketchImage = sketchImage, imageProcessing = imageProcessing, tools = tools, navController = navController, sketchText = sketchText)
    if(sketchText.showPageEdit){
        EditTextPage(sketchText = sketchText)
    }
}