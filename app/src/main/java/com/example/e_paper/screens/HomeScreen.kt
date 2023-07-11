package com.example.e_paper.screens


import androidx.compose.runtime.Composable
import com.example.e_paper.components.ModalNavigationDrawerSample
import com.example.e_paper.components.MyScaffild
import com.example.e_paper.image_processing.ImageProcessing
import com.example.e_paper.page.HomePage

@Composable
fun HomeScreen(imageProcessing: ImageProcessing) {
    ModalNavigationDrawerSample(imageProcessing = imageProcessing)
}