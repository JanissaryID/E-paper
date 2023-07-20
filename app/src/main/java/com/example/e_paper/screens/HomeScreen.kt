package com.example.e_paper.screens


import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.e_paper.components.ModalNavigationDrawerSample
import com.example.e_paper.classes.ImageProcessing

@Composable
fun HomeScreen(imageProcessing: ImageProcessing, navController: NavController) {
    ModalNavigationDrawerSample(imageProcessing = imageProcessing, navController = navController)
}