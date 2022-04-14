package com.digiquest.common.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import com.digiquest.common.screen.battlesetup.DM20BattleSetupView
import com.digiquest.common.util.SpriteLoader
import com.digiquest.core.battle.DeviceROMWrapper
import com.digiquest.core.digimon.Digimon

class BattleSetupScreen(val spriteLoader: SpriteLoader, val dM20BattleSetupView: DM20BattleSetupView) : Screen {
    @Composable
    override fun loadScreen(onScreenChange: (ScreenType, Any?) -> Unit, parameter: Any?) {
        if(parameter !is BattleSetupScreenParameter) {
            throw IllegalArgumentException("Code error. Parameter should be of the correct type.")
        }
        var deviceROMWrapper: DeviceROMWrapper? by remember { mutableStateOf(null) }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().verticalScroll(
            rememberScrollState()
        )) {
            Screen.LabelText("Name: ", parameter.digimon.name, modifier = Modifier.padding(10.dp))
            spriteLoader.AsyncImage(spriteName = parameter.digimon.name, contentDescription = "${parameter.digimon.name} art.")
            Button(onClick = {
                onScreenChange(ScreenType.BATTLE, BattleScreen.BattleScreenParameters(parameter.digimon, deviceROMWrapper!!, parameter.screenType))
            }) {
                Text("Fight!")
            }
            dM20BattleSetupView.DeviceStatSelection(parameter.digimon, deviceROMWrapper) { deviceROMWrapper = it }
        }
    }

    data class BattleSetupScreenParameter(val digimon : Digimon, val screenType: ScreenType)
}