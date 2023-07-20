package com.example.e_paper.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.e_paper.classes.ImageProcessing
import com.example.e_paper.classes.SketchImage
import com.example.e_paper.classes.Tools
import com.example.e_paper.screens.HomeScreen
import com.example.e_paper.screens.ItemSketchScreen
import com.example.e_paper.screens.SketchScreen

@Composable
fun NavGraphSetup(
    navController: NavHostController,
    imageProcessing: ImageProcessing,
    sketchImage: SketchImage,
    tools: Tools
) {
    NavHost(navController = navController, startDestination = Screens.Home.route){

        composable(
            route = Screens.Home.route,
        ){
            HomeScreen(imageProcessing = imageProcessing, navController = navController)
        }

        composable(
            route = Screens.Sketch.route,
        ){
            SketchScreen(sketchImage = sketchImage, imageProcessing = imageProcessing, tools = tools, navController = navController)
        }

        composable(
            route = Screens.ItemSketch.route,
        ){
            ItemSketchScreen(sketchImage = sketchImage, itemIndex = sketchImage.itemType, navController = navController)
        }
    }
}