package com.example.e_paper.classes

import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.e_paper.R
import com.example.e_paper.navigation.Screens
import dev.shreyaspatil.capturable.controller.CaptureController

class Tools() {
    var listTools2: List<String> = listOf("Undo", "Redo", "Brush", "Save")
    var listIcon2: List<Int> = listOf(
        R.drawable.baseline_undo_24,
        R.drawable.baseline_redo_24,
        R.drawable.baseline_brush_24,
        R.drawable.baseline_download_24
    )

    val listColor: List<Color> = listOf(Color.Black, Color.Red, Color.White)

    fun addItemCanvas2(position: Int, sketchImage: SketchDrawing, captureController: CaptureController, navController: NavController){
        when(position){
            0 -> {
                sketchImage.Undo()
            }
            1 -> {
                sketchImage.Redo()
            }
            2 -> {
//                sketchImage.itemCanvasDraw.
//                navController.navigate(route = Screens.ItemSketch.route) {
//                    popUpTo(Screens.ItemSketch.route) {
//                        inclusive = true
//                    }
//                }
            }
            3 -> {
                captureController.capture()
            }
            else -> "Tools Wrong"
        }
    }
}