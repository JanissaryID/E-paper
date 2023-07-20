package com.example.e_paper.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

sealed class Screens(val route: String) {
    object Home : Screens(route = "home_screen")
    object Sketch : Screens(route = "sketch_screen")
    object ItemSketch : Screens(route = "item_sketch_screen")
}