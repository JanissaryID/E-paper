package com.example.e_paper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.e_paper.classes.ImageProcessing
import com.example.e_paper.classes.SketchDrawing
import com.example.e_paper.classes.Tools
import com.example.e_paper.navigation.NavGraphSetup
import com.example.e_paper.ui.theme.EpaperTheme

class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ImageProcessing = ImageProcessing(context = this)
        val sketchImage = SketchDrawing(context = this)
        val sketchTools = Tools()

        setContent {
            EpaperTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    navController = rememberNavController()
                    NavGraphSetup(
                        navController = navController,
                        imageProcessing = ImageProcessing,
                        sketchImage = sketchImage,
                        tools = sketchTools
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EpaperTheme {
        Greeting("Android")
    }
}