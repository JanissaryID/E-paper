package com.example.e_paper.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.e_paper.classes.ImageProcessing
import com.example.e_paper.classes.SketchDrawing
import com.example.e_paper.classes.SketchText
import com.example.e_paper.classes.Timer
import com.example.e_paper.classes.Tools
import com.example.e_paper.classes.WifiConnector
import com.example.e_paper.screens.HomeScreen
import com.example.e_paper.screens.SketchScreen

@Composable
fun NavGraphSetup(
    navController: NavHostController,
    imageProcessing: ImageProcessing,
    sketchImage: SketchDrawing,
    wifiConnector: WifiConnector,
    sketchText: SketchText,
    timer: Timer,
    tools: Tools
) {
    NavHost(navController = navController, startDestination = Screens.Home.route){

        composable(
            route = Screens.Home.route,
        ){
            HomeScreen(imageProcessing = imageProcessing, navController = navController, wifiConnector = wifiConnector, timer = timer)
        }

        composable(
            route = Screens.Sketch.route,
        ){
            SketchScreen(sketchImage = sketchImage, imageProcessing = imageProcessing, tools = tools, navController = navController, sketchText = sketchText)
        }

    }
}