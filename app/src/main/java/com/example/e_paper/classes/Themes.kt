package com.example.e_paper.classes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class Themes {
    var listThemes: List<String> = listOf("Weekly Planner", "Week's Menu")
    var themesIndex : Int by mutableStateOf(0)
    var selectedFilter : String by mutableStateOf(listThemes[themesIndex])


}