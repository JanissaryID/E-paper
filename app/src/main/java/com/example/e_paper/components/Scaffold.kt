package com.example.e_paper.components

import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.e_paper.R
import com.example.e_paper.classes.ImageProcessing
import com.example.e_paper.classes.Timer
import com.example.e_paper.classes.WifiConnector
import com.example.e_paper.page.HomePage

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MyScaffold(
    imageProcessing: ImageProcessing,
    wifiConnector: WifiConnector,
    timer: Timer,
    onClick: (Boolean) -> Unit
) {
    val context = LocalContext.current

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

                            if(wifiConnector.CheckWifiState()){
                                if(wifiConnector.GetInfoWifi()){
                                    imageProcessing.SendImage(
                                        host = ipAddress,
                                        port = port,
                                        timer = timer
                                    )
                                }
                                else{
                                    Toast.makeText(context, "Please Connect to EPD Access Point", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else{
                                Toast.makeText(context, "Please Turn On Wifi", Toast.LENGTH_SHORT).show()
                            }
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
                        timer.currentTimeInSecond = 0L
//                        imageProcessing.SizeDisplay(imageProcessing.display)

                        imageProcessing.SelectProcessing()
                    }
                }
            ) {
                Text("Process Image")
            }
        },
        content = {
            HomePage(imageProcessing = imageProcessing, paddingValues = it, timer = timer)
//            SketchPage()
        }
    )
}
