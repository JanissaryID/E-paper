package com.example.e_paper.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberImagePainter
import com.example.e_paper.components.LinearProgressIndicatorSample
import com.example.e_paper.classes.ImageProcessing
import com.example.e_paper.classes.Timer

@Composable
fun HomePage(imageProcessing: ImageProcessing, paddingValues: PaddingValues, timer: Timer) {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(
            top = paddingValues.calculateTopPadding(),
            start = 16.dp,
            end = 16.dp,
            bottom = paddingValues.calculateBottomPadding()
        )
        .verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {

        LinearProgressIndicatorSample(imageProcessing = imageProcessing, timer = timer)

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Original Image", color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = rememberImagePainter(imageProcessing.myBitmapCanvas.value),
//            painter = rememberImagePainter(imageProcessing.selectImages.toUri()),
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = Modifier
                .width(400.dp)
                .height(300.dp)
                .padding(8.dp)
        )

        if(imageProcessing.imageShow){
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Result Image", color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberImagePainter(
                    imageProcessing.myBitmap.value
                ),
                contentScale = ContentScale.Fit,
                contentDescription = null,
                modifier = Modifier
                    .width(400.dp)
                    .height(300.dp)
                    .padding(8.dp)
            )
        }
    }
}