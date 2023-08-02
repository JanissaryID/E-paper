package com.example.e_paper.screens


import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.e_paper.components.ModalNavigationDrawerSample
import com.example.e_paper.classes.ImageProcessing
import com.example.e_paper.classes.Timer
import com.example.e_paper.classes.WifiConnector

@Composable
fun HomeScreen(imageProcessing: ImageProcessing, navController: NavController, wifiConnector: WifiConnector, timer: Timer) {
    ModalNavigationDrawerSample(imageProcessing = imageProcessing, navController = navController, wifiConnector = wifiConnector, timer = timer)
}