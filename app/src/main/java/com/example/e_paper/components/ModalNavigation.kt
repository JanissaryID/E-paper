package com.example.e_paper.components

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.example.e_paper.R
import com.example.e_paper.classes.ImageProcessing
import com.example.e_paper.classes.Timer
import com.example.e_paper.classes.WifiConnector
import com.example.e_paper.navigation.Screens
import kotlinx.coroutines.launch

@Composable
fun ModalNavigationDrawerSample(imageProcessing: ImageProcessing, navController: NavController, wifiConnector: WifiConnector, timer: Timer) {
    val context = LocalContext.current

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scopeSelectImage = rememberCoroutineScope()
    // icons to mimic drawer destinations
//    val items = listOf("4.2 Inch WS", "4.2 Inch GD", "13.3 Inch HINK")
    val items = listOf("13.3 Inch HINK")
    val filter = listOf("Binary", "Filter", "Dither Gray", "Dither")
    val selectedItem = remember { mutableStateOf(items[0]) }
    val selectedFilter = remember { mutableStateOf(filter[0]) }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        try {
            if(!it!!.toString().isNullOrBlank()){
                imageProcessing.selectImages = it!!.toString()
                imageProcessing.UriToBitmap(it)
            }
        }
        catch (e: Exception){
            Log.d("log_item", "Error : $e")
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(16.dp))
                Text(text = "Display", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                Spacer(Modifier.height(4.dp))
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        icon = {
                            Icon(painter = painterResource(id = R.drawable.twotone_aspect_ratio_24),
                                contentDescription = "Ratio")
                               },
                        label = { Text(item) },
                        selected = item == selectedItem.value,
                        onClick = {
//                            imageProcessing.SizeDisplay(choice = index)
                            selectedItem.value = item

                            imageProcessing.imageShow = false
                            imageProcessing.myListBlack.clear()
                            imageProcessing.myListRed.clear()
                            timer.currentTimeInSecond = 0L
                            imageProcessing.allDataArrayReady = false
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
                Spacer(Modifier.height(16.dp))
                Text(text = "Filter", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                Spacer(Modifier.height(4.dp))
                filter.forEachIndexed { index, filter ->
                    NavigationDrawerItem(
                        icon = {
                            Icon(painter = painterResource(id = R.drawable.twotone_photo_filter_24),
                                contentDescription = "Ratio")
                        },
                        label = { Text(filter) },
                        selected = filter == selectedFilter.value,
                        onClick = {
                            imageProcessing.selectProcessing = index
                            imageProcessing.imageShow = false
                            imageProcessing.myListBlack.clear()
                            imageProcessing.myListRed.clear()
                            imageProcessing.allDataArrayReady = false
                            selectedFilter.value = filter
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
                Spacer(Modifier.height(32.dp))
                Button(
                    modifier = Modifier.fillMaxWidth().height(52.dp).padding(horizontal = 16.dp),
                    onClick = {
                        scopeSelectImage.launch { galleryLauncher.launch("image/*") }
                        scope.launch { drawerState.close() } },
                ) {
                    Text(text = "Select Image")
                }
                Spacer(Modifier.height(32.dp))
                Button(
                    modifier = Modifier.fillMaxWidth().height(52.dp).padding(horizontal = 16.dp),
                    onClick = {
                        navController.navigate(route = Screens.Sketch.route) {
                            popUpTo(Screens.Sketch.route) {
                                inclusive = true
                            }
                        }
                    },
                ) {
                    Text(text = "Create Sketch")
                }
            }
        },
        content = {
            MyScaffold(imageProcessing = imageProcessing, wifiConnector = wifiConnector, timer = timer){
                scope.launch { if(it) drawerState.open() else drawerState.close() }
            }
//            SimpleBottomSheetScaffoldSample()
        }
    )
}
