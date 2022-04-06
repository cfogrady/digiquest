package com.digiquest.common.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.digiquest.core.digimon.Digimon
import com.digiquest.core.digimon.DigimonLibrary
import kotlinx.coroutines.launch
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

class DigimonLibraryScreen(val digimonLibrary: DigimonLibrary, val capableOfAddingToLibrary: Boolean) : Screen {
    @Composable
    override fun loadScreen(onScreenChange: (ScreenType, Any?) -> Unit, parameter: Any?) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            buttons(onScreenChange)
            library(onScreenChange)
        }

    }

    @Composable
    fun buttons(onScreenChange: (ScreenType, Any?) -> Unit) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Button(onClick = {
                onScreenChange(ScreenType.MAIN_MENU, null)
            }) {
                Text("Main Menu")
            }
            if(capableOfAddingToLibrary) {
                Button(onClick = {
                    log.info { "Add Digimon Button Clicked" }
                    //onScreenChange(ScreenType.MAIN_MENU, null)
                }) {
                    Text("Add Digimon")
                }
            }
        }
    }

    @Composable
    fun library(onScreenChange: (ScreenType, Any?) -> Unit, ) {
        val composableScope = rememberCoroutineScope()
        var isLibraryLoaded by remember { mutableStateOf(digimonLibrary.isLoaded) }
        if(!isLibraryLoaded) {
            composableScope.launch {
                digimonLibrary.initializeLibrary()
                isLibraryLoaded = true
            }
            Text("Loading Library...", color = Color.White)
        } else {
            LazyColumn(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                items(items = digimonLibrary.sortedDigimon) { digimon ->
                    Button(onClick = {
                        digimonLibrary.currentDigimon = digimon
                        onScreenChange(ScreenType.VIEW_DIGIMON, null)
                    }) {
                        Text(digimon.name)
                    }
                }
            }
        }
    }

}