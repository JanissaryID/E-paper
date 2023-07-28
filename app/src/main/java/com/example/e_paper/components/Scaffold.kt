package com.example.e_paper.components

import android.util.Log
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.e_paper.R
import com.example.e_paper.classes.ImageProcessing
import com.example.e_paper.page.HomePage

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MyScaffold(
    imageProcessing: ImageProcessing,
    onClick: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("E-Paper Display") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onClick.invoke(true)
                        }
                    ) {
                        Icon(Icons.Filled.Menu, contentDescription = "Localized description")
                    }
                }, actions = {
                    IconButton(
                        onClick = {
                            imageProcessing.rotateBitmap90()
                        }
                    ) {
                        Icon(painter = painterResource(id = R.drawable.baseline_rotate_90_degrees_cw_24), contentDescription = "Localized description")
                    }
                    IconButton(
                        onClick = {

                            val ipAddress = "192.168.4.1"
                            val port = 80

                            imageProcessing.SendImage(
                                host = ipAddress,
                                port = port,
                            )
                        }
                    ) {
                        Icon(painter = painterResource(id = R.drawable.twotone_upload_file_24), contentDescription = "Localized description")
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if(!imageProcessing.myBitmapCanvas.toString().isNullOrBlank()){
                        imageProcessing.progress = 0f
                        imageProcessing.myListBlack.clear()
                        imageProcessing.myListRed.clear()
                        imageProcessing.SizeDisplay(imageProcessing.display)

                        imageProcessing.SelectProcessing()
                    }
                }
            ) {
                Text("Process Image")
            }
        },
        content = {
            HomePage(imageProcessing = imageProcessing, paddingValues = it)
//            SketchPage()
        }
    )
}
