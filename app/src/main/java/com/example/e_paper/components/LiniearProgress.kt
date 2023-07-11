package com.example.e_paper.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun LinearProgressIndicatorSample() {
    var progress by remember { mutableStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Progress = ${(progress*100).roundToInt()}", color = MaterialTheme.colorScheme.primary)
        LinearProgressIndicator(
            modifier = Modifier.semantics(mergeDescendants = true) {}.padding(10.dp),
            progress = animatedProgress,
        )
        Spacer(Modifier.requiredHeight(30.dp))
        OutlinedButton(
            modifier = Modifier.semantics {
                val progressPercent = (progress * 100).toInt()
                if (progressPercent in 0..100) {
                    stateDescription = "Progress $progressPercent%"
                }
            },
            onClick = {
                if (progress < 1f) progress += 0.01f
            }
        ) {
            Text("Increase")
        }
    }
}
