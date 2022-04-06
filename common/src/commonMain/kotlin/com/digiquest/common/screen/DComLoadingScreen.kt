package com.digiquest.common.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.digiquest.core.dcom.DComManager
import com.digiquest.core.runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

class DComLoadingScreen(val dcomManager : DComManager) : Screen {
    val NOT_DETECTED = "No D-COM/A-COM Detected..."
    val INITIALIZING = "Initializing D-COM/A-COM..."
    val CONNECTED = "Digiport Opened!"

    @Composable
    override fun loadScreen(onScreenChange: (ScreenType, Any?) -> Unit, parameter: Any?) {
        if(parameter !is DComLoadScreenParameter) {
            throw IllegalArgumentException("Code error. Parameter should be of the correct type.")
        }
        val composableScope = rememberCoroutineScope()
        var loadingText by remember { mutableStateOf(getDefaultText()) }
        if(dcomManager.noDComLikeDeviceConnected()) {
            log.info { "Looking for dcom" }
            dcomManager.onDeviceConnected(runnable {
                loadingText = INITIALIZING
            })
        } else if(!dcomManager.isDComInitialized()) {
            log.info { "DCOM Connected. Initializing..." }
            dcomManager.getDComPort().thenRun(runnable { loadingText = CONNECTED })
        } else {
            composableScope.launch {
                delay(500)
                onScreenChange(parameter.successScreenType, parameter.successScreenParameter)
            }
        }
        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(loadingText, color = Color.White)
            Button(onClick = {
                onScreenChange(parameter.returnScreenType, parameter.returnScreenParameter)
            }) {
                Text("Back")
            }
        }
    }

    fun getDefaultText() : String {
        if(dcomManager.noDComLikeDeviceConnected()) {
            return NOT_DETECTED
        } else if(!dcomManager.isDComInitialized()) {
            return INITIALIZING
        } else {
            return CONNECTED
        }
    }

    class DComLoadScreenParameter(val returnScreenType : ScreenType, val returnScreenParameter : Any? = null, val successScreenType : ScreenType, val successScreenParameter : Any? = null)
}