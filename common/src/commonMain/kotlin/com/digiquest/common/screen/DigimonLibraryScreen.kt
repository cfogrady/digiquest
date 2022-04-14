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
import com.digiquest.common.util.FileChooser
import com.digiquest.core.digimon.Digimon
import com.digiquest.core.digimon.DigimonLibrary
import com.digiquest.core.digimon.SpritePackager
import kotlinx.coroutines.launch
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

class DigimonLibraryScreen(val digimonLibrary: DigimonLibrary, val capableOfAddingToLibrary: Boolean, val fileChooser: FileChooser, val spritePackager: SpritePackager) : Screen {
    @Composable
    override fun loadScreen(onScreenChange: (ScreenType, Any?) -> Unit, parameter: Any?) {
        var loadingStatus by remember { mutableStateOf( if(digimonLibrary.isLoaded) LoadingStatus.INITIALIZED else LoadingStatus.UNINITIALIZED) }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            buttons(onScreenChange, {loadingStatus = it})
            library(onScreenChange, loadingStatus, {loadingStatus = it})
        }

    }

    @Composable
    fun buttons(onScreenChange: (ScreenType, Any?) -> Unit, onLoadingStatusChange: (LoadingStatus) -> Unit) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Button(onClick = {
                onScreenChange(ScreenType.MAIN_MENU, null)
            }) {
                Text("Main Menu")
            }
            Button(onClick = {
                val file = fileChooser.getFile("*.dql", true);
                if(file != null) {
                    log.info { "File selected: $file" }
                    digimonLibrary.importLibrary(file, spritePackager).thenRun {
                        onLoadingStatusChange(LoadingStatus.INITIALIZED)
                    }
                    onLoadingStatusChange(LoadingStatus.RELOADING)
                }
            }) {
                Text("Import Library")
            }
            Button(onClick = {
                val file = fileChooser.getFile("*.dql", false);
                if(file != null) {
                    log.info { "File selected: $file" }
                    digimonLibrary.exportLibrary(file, spritePackager)
                }
            }) {
                Text("Export Library")
            }
            if(capableOfAddingToLibrary) {
                Button(onClick = {
                    log.info { "Add Digimon Button Clicked" }
                    onScreenChange(ScreenType.EDIT_DIGIMON, EditDigimonScreen.EditDigimonScreenParameter(digimon = null))
                }) {
                    Text("Add Digimon")
                }
            }
        }
    }

    @Composable
    fun library(onScreenChange: (ScreenType, Any?) -> Unit, loadingStatus: LoadingStatus, onLoadingStatusChange: (LoadingStatus) -> Unit) {
        val composableScope = rememberCoroutineScope()
        if(loadingStatus == LoadingStatus.UNINITIALIZED) {
            composableScope.launch {
                digimonLibrary.initializeLibrary()
                onLoadingStatusChange(LoadingStatus.INITIALIZED)
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

    enum class LoadingStatus {
        UNINITIALIZED,
        INITIALIZED,
        RELOADING
    }
}