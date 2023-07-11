package com.example.e_paper.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.e_paper.R
import com.example.e_paper.image_processing.ImageProcessing
import com.example.e_paper.page.HomePage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MyScaffild(
    imageProcessing: ImageProcessing,
    onClick: (Boolean) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
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
                    if(!imageProcessing.selectImages.isNullOrBlank()){
                        imageProcessing.myListBlack.clear()
                        imageProcessing.SelectProcessing()
                    }
                }
            ) {
                Text("Process Image")
            }
        },
        content = {
            HomePage(imageProcessing = imageProcessing, paddingValues = it)
        }
    )
}
