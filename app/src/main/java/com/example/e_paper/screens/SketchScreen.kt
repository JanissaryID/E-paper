package com.example.e_paper.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.e_paper.classes.ImageProcessing
import com.example.e_paper.classes.SketchImage
import com.example.e_paper.classes.Tools
import com.example.e_paper.page.SketchPage

@Composable
fun SketchScreen(sketchImage: SketchImage, imageProcessing: ImageProcessing, tools: Tools, navController: NavController) {
    SketchPage(sketchImage = sketchImage, imageProcessing = imageProcessing, tools = tools, navController = navController)
}