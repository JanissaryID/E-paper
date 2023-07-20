package com.example.e_paper.dataclass

import androidx.compose.ui.graphics.Color

object ItemCanvasObject {
    private val itemType = arrayOf(
        1,
        1,
        1,
        1,
        0
    )

    private val color = arrayOf(
        Color.Black,
        Color.Red,
        Color.Black,
        Color.Red,
        Color.Red
    )

    private val xPos = arrayOf(
        0f,
        100f,
        200f,
        100f,
        0f
    )

    private val yPos = arrayOf(
        0f,
        100f,
        200f,
        300f,
        0f
    )

    private val width = arrayOf(
        100f,
        100f,
        100f,
        100f,
        0f
    )

    private val height = arrayOf(
        100f,
        100f,
        100f,
        100f,
        0f
    )

    private val rotate = arrayOf(
        0f,
        0f,
        0f,
        0f,
        0f
    )

    private val stroke = arrayOf(
        3,
        3,
        3,
        3,
        3
    )

    private val startA = arrayOf(
        100f,
        100f,
        100f,
        100f,
        0f
    )

    private val startB = arrayOf(
        100f,
        100f,
        100f,
        100f,
        0f
    )

    private val endA = arrayOf(
        100f,
        100f,
        100f,
        100f,
        300f
    )

    private val endB = arrayOf(
        100f,
        100f,
        100f,
        100f,
        300f
    )

    private val fill = arrayOf(
        false,
        false,
        false,
        false,
        false
    )

//    val listData: ArrayList<ItemCanvas>
//
//        get() {
//            val list = arrayListOf<ItemCanvas>()
//            for (position in itemType.indices) {
//                val itemDevice = ItemCanvas()
//                itemDevice.itemType = itemType[position]
//                itemDevice.color = color[position]
//                itemDevice.xPos = xPos[position]
//                itemDevice.yPos = yPos[position]
//                itemDevice.width = width[position]
//                itemDevice.height = height[position]
//                itemDevice.rotate = rotate[position]
//                itemDevice.stroke = stroke[position]
//                itemDevice.startA = startA[position]
//                itemDevice.startB = startB[position]
//                itemDevice.endA = endA[position]
//                itemDevice.endB = endB[position]
//                itemDevice.fill = fill[position]
//                list.add(itemDevice)
//            }
//            return list
//        }
}