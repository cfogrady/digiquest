package com.digiquest.common.screen

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit

interface Screen {
    @Composable
    fun loadScreen(onScreenChange: (ScreenType, Any?) -> Unit, parameter: Any?)

    companion object {
        fun fetchScreenMap(mainMenuScreen: MainMenuScreen,
                           digimonLibraryScreen: DigimonLibraryScreen,
                           dComLoadingScreen: DComLoadingScreen,
                           digimonViewScreen: DigimonViewScreen,
                           battleScreen: BattleScreen,
                           editDigimonScreen: EditDigimonScreen,
                           battleSetupScreen : BattleSetupScreen
        ) : Map<ScreenType, Screen> {
            val screenMap = HashMap<ScreenType, Screen>()
            screenMap.put(ScreenType.MAIN_MENU, mainMenuScreen)
            screenMap.put(ScreenType.DIGIMON_LIBRARY, digimonLibraryScreen)
            screenMap.put(ScreenType.DCOM_LOADING, dComLoadingScreen)
            screenMap.put(ScreenType.VIEW_DIGIMON, digimonViewScreen)
            screenMap.put(ScreenType.BATTLE, battleScreen)
            screenMap.put(ScreenType.EDIT_DIGIMON, editDigimonScreen)
            screenMap.put(ScreenType.BATTLE_SETUP, battleSetupScreen)
            return screenMap
        }

        @Composable
        fun LabelText(label: String, value: String = "", color: Color = MaterialTheme.colors.onBackground, modifier: Modifier = Modifier, fontSize : TextUnit? = null) {
            val spanStyles = listOf(
                AnnotatedString.Range(
                    SpanStyle(fontWeight = FontWeight.Bold),
                    start = 0,
                    end = label.length
                )
            )
            if(fontSize != null) {
                Text(text = AnnotatedString(text = label + value, spanStyles = spanStyles),
                    color = color, modifier = modifier, fontSize = fontSize)
            } else {
                Text(text = AnnotatedString(text = label + value, spanStyles = spanStyles),
                    color = color, modifier = modifier)
            }
        }
    }
}
