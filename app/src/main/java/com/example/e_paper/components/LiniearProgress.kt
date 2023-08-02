package com.example.e_paper.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.e_paper.classes.ImageProcessing
import com.example.e_paper.classes.Timer
import kotlin.math.roundToInt

@Composable
fun LinearProgressIndicatorSample(imageProcessing: ImageProcessing, timer: Timer) {
    val animatedProgress by animateFloatAsState(
        targetValue = imageProcessing.progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            Text(text = "Timer = ${timer.currentTimeInSecond} Second", color = MaterialTheme.colorScheme.primary)
            Text(text = "Progress = ${(imageProcessing.progress*100).roundToInt()}", color = MaterialTheme.colorScheme.primary)
            LinearProgressIndicator(
                modifier = Modifier
                    .semantics(mergeDescendants = true) {}
                    .padding(10.dp),
                progress = animatedProgress,
            )
        }
    }
}
