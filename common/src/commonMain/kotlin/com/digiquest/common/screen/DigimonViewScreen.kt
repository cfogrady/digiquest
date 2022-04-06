package com.digiquest.common.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digiquest.common.util.SpriteLoader
import com.digiquest.core.digimon.DigimonLibrary

class DigimonViewScreen(val spriteLoader: SpriteLoader, val digimonLibrary: DigimonLibrary, val canEdit: Boolean) : Screen {
    @Composable
    override fun loadScreen(onScreenChange: (ScreenType, Any?) -> Unit, parameter: Any?) {
        val digimon = digimonLibrary.currentDigimon
        Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(10.dp).verticalScroll(rememberScrollState())) {
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(10.dp).fillMaxWidth()) {
                Button(onClick = {
                    onScreenChange(ScreenType.DIGIMON_LIBRARY, null)
                }) {
                    Text("Back")
                }
                if(canEdit) {
                    Button(enabled = false, onClick = {

                    }) {
                        Text("Edit")
                    }
                }
                Button(onClick = {
                    onScreenChange(ScreenType.BATTLE, BattleScreen.BattleScreenParameters(ScreenType.VIEW_DIGIMON))
                }) {
                    Text("Battle")
                }
            }
            val digimonName = digimon.name
            Screen.LabelText("Name: ", digimonName, modifier = Modifier.padding(10.dp))
            spriteLoader.AsyncImage(spriteName = digimon.name.toLowerCase(Locale.current), credit = digimon.artCredit, contentDescription = "${digimon.name} art.")
            Text(digimon.description, modifier = Modifier.padding(10.dp), color = MaterialTheme.colors.onBackground, fontSize = 12.sp)
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(horizontal = 10.dp, vertical = 0.dp).fillMaxWidth()) {
                Screen.LabelText("Attribute: ", digimon.attribute.name, fontSize = 12.sp)
                val stageName = digimon.stage.dubName
                Screen.LabelText("Stage: ", stageName, fontSize = 12.sp)
            }
        }
    }
}