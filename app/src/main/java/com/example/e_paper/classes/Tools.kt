package com.example.e_paper.classes

import androidx.navigation.NavController
import com.example.e_paper.R
import com.example.e_paper.navigation.Screens
import dev.shreyaspatil.capturable.controller.CaptureController

class Tools() {
    var listTools: List<String> = listOf("Line", "Box", "Circle", "Text", "Image", "Save")
    var listIcon: List<Int> = listOf(
        R.drawable.baseline_horizontal_rule_24,
        R.drawable.baseline_check_box_outline_blank_24,
        R.drawable.baseline_panorama_fish_eye_24,
        R.drawable.baseline_text_fields_24,
        R.drawable.baseline_image_24,
        R.drawable.baseline_download_24
    )

    var listTools2: List<String> = listOf("Undo", "Redo", "Brush", "Save")
    var listIcon2: List<Int> = listOf(
        R.drawable.baseline_undo_24,
        R.drawable.baseline_redo_24,
        R.drawable.baseline_brush_24,
        R.drawable.baseline_download_24
    )

    fun addItemCanvas(position: Int, sketchImage: SketchImage, captureController: CaptureController, navController: NavController){
        when(position){
            0 -> {
                sketchImage.itemType = 0
                navController.navigate(route = Screens.ItemSketch.route) {
                    popUpTo(Screens.ItemSketch.route) {
                        inclusive = true
                    }
                }
//                sketchImage.itemCanvas.add(ItemCanvas(
//                    itemType = ,
//                    color = ,
//                    xPos = ,
//                    yPos = ,
//                    width = ,
//                    height = ,
//                    rotate = ,
//                    stroke = ,
//                    startA = ,
//                    startB = ,
//                    endA = ,
//                    endB = ,
//                    fill = ,
//                ))
            }
            1 -> {
                sketchImage.itemType = 1
                navController.navigate(route = Screens.ItemSketch.route) {
                    popUpTo(Screens.ItemSketch.route) {
                        inclusive = true
                    }
                }
            }
            2 -> {
                sketchImage.itemType = 2
                navController.navigate(route = Screens.ItemSketch.route) {
                    popUpTo(Screens.ItemSketch.route) {
                        inclusive = true
                    }
                }
            }
            3 -> {
                sketchImage.itemType = 3
                navController.navigate(route = Screens.ItemSketch.route) {
                    popUpTo(Screens.ItemSketch.route) {
                        inclusive = true
                    }
                }
            }
            4 -> {
                sketchImage.itemType = 4
                navController.navigate(route = Screens.ItemSketch.route) {
                    popUpTo(Screens.ItemSketch.route) {
                        inclusive = true
                    }
                }
            }
            5 -> {
                captureController.capture()
            }
            else -> "Tools Wrong"
        }
    }

    fun addItemCanvas2(position: Int, sketchImage: SketchDrawing, captureController: CaptureController, navController: NavController){
//        when(position){
//            0 -> {
//                sketchImage.itemType = 0
//                navController.navigate(route = Screens.ItemSketch.route) {
//                    popUpTo(Screens.ItemSketch.route) {
//                        inclusive = true
//                    }
//                }
//            }
//            1 -> {
//                sketchImage.itemType = 1
//                navController.navigate(route = Screens.ItemSketch.route) {
//                    popUpTo(Screens.ItemSketch.route) {
//                        inclusive = true
//                    }
//                }
//            }
//            2 -> {
//                sketchImage.itemType = 2
//                navController.navigate(route = Screens.ItemSketch.route) {
//                    popUpTo(Screens.ItemSketch.route) {
//                        inclusive = true
//                    }
//                }
//            }
//            3 -> {
//                captureController.capture()
//            }
//            else -> "Tools Wrong"
//        }
    }
}