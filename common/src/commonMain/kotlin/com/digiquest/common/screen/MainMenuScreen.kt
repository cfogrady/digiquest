package com.digiquest.common.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.digiquest.core.dcom.DComManager

class MainMenuScreen(val dComManager: DComManager) : Screen {
    @Composable
    override fun loadScreen(onScreenChange: (ScreenType, Any?) -> Unit, parameter: Any?) {
        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            val dcomStatusText = if(dComManager.isDComInitialized) "D-COM/A-COM loaded " else "D-COM/A-COM is not yet loaded"
            Text(dcomStatusText, color = MaterialTheme.colors.onBackground)
            Button(onClick = {
                onScreenChange(ScreenType.DCOM_LOADING,
                    DComLoadingScreen.DComLoadScreenParameter(returnScreenType = ScreenType.MAIN_MENU, successScreenType = ScreenType.MAIN_MENU ))

            }, enabled = !dComManager.isDComInitialized) {
                Text("Load D-COM/A-COM")
            }
            Button(onClick = {
                onScreenChange(ScreenType.DIGIMON_LIBRARY, null)
            }) {
                Text("Digimon Library")
            }
        }
    }

}
