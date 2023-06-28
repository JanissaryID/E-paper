package com.example.e_paper.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberImagePainter
import com.example.e_paper.image_processing.ImageProcessing

@Composable
fun HomeScreen(imageProcessing: ImageProcessing) {

    val context = LocalContext.current

//    val imageBitmap: ImageBitmap = bitmap.asImageBitmap()

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        try {
            if(!it!!.toString().isNullOrBlank()){
                imageProcessing.selectImages = it!!.toString()

//                Log.d("log_item", "Image : $imageProcessing.selectImages ${it.lastPathSegment}")
            }
        }
        catch (e: Exception){
            Log.d("log_item", "Error : $e")
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { galleryLauncher.launch("image/*") },
        ) {
            Text(text = "Select Image")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                imageProcessing.getImage(imageProcessing.selectImages.toUri())
            },
        ) {
            Text(text = "Process")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Original Image", color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = rememberImagePainter(imageProcessing.selectImages.toUri()),
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = Modifier
                .size(300.dp)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Black White Image", color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = rememberImagePainter(
//                imageProcessing.bitmap
                imageProcessing.uriToBitmap(imageProcessing.selectImages.toUri())
                    ?.let {
                        imageProcessing.colorScaleWithoutDithering(it)
//                        imageProcessing.errorDiffusionDithering2(it)
                    }
            ),
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = Modifier
                .size(300.dp)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Filter Image", color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = rememberImagePainter(
//                imageProcessing.bitmap
                imageProcessing.uriToBitmap(imageProcessing.selectImages.toUri())
                    ?.let {
                        imageProcessing.colorScaleWithoutDithering(it)
//                        imageProcessing.errorDiffusionDithering2(it)
                    }
            ),
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = Modifier
                .size(300.dp)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Dither Image", color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = rememberImagePainter(
//                imageProcessing.bitmap
                imageProcessing.uriToBitmap(imageProcessing.selectImages.toUri())
                    ?.let {
                        imageProcessing.errorDiffusionDithering2(it)
                    }
            ),
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = Modifier
                .size(300.dp)
                .padding(8.dp)
        )
    }
}