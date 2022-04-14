package com.digiquest.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.digiquest.common.screen.*
import com.digiquest.common.screen.battlesetup.DM20BattleSetupView
import com.digiquest.common.util.FileChooser
import com.digiquest.common.util.SpriteLoader
import com.digiquest.core.PlatformProperties
import com.digiquest.core.battle.dm20.DM20BattleHandler
import com.digiquest.core.battle.dm20.DM20IndexPowerFactory
import com.digiquest.core.battle.dm20.DM20RomFactory
import com.digiquest.core.dcom.DComManager
import com.digiquest.core.digimon.DigimonLibrary
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.cfogrady.dcom.digimon.dm20.DM20RomReader
import com.github.cfogrady.dcom.digimon.dm20.DM20RomWriter
import com.github.cfogrady.dcom.digimon.dm20.battle.DM20BattleRunner
import com.github.cfogrady.dcom.digimon.dm20.battle.DM20BattleSimulator
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

class App(val screensByType : Map<ScreenType, Screen>) {

    companion object {
        fun createApp(dcomManager : DComManager, platformProperties: PlatformProperties, spriteLoader: SpriteLoader, fileChooser: FileChooser) : App {
            val objectMapper = ObjectMapper()
            val dm20IndexPowerFactory = DM20IndexPowerFactory(objectMapper)
            val dm20Indexes = dm20IndexPowerFactory.powerByIndex
            val digimonLibrary = DigimonLibrary(objectMapper, platformProperties.relativeLocation)
            val dM20RomFactory = DM20RomFactory(dm20Indexes)
            val dm20BattleSimulator = DM20BattleSimulator(dm20Indexes)
            val dm20BattleRunner = DM20BattleRunner(DM20RomWriter(), DM20RomReader(), dm20BattleSimulator)
            val dm20BattleHandler = DM20BattleHandler(dcomManager, dm20BattleRunner, dM20RomFactory, dm20Indexes, digimonLibrary)
            val mainMenuScreen = MainMenuScreen(dcomManager)
            val digimonLibraryScreen = DigimonLibraryScreen(digimonLibrary, platformProperties.isCapableOfAddingToLibrary, fileChooser, spriteLoader)
            val digimonViewScreen = DigimonViewScreen(spriteLoader = spriteLoader, digimonLibrary = digimonLibrary, canEdit = platformProperties.isCapableOfAddingToLibrary)
            val dComLoadingScreen = DComLoadingScreen(dcomManager)
            val battleScreen = BattleScreen(dcomManager, spriteLoader, dm20BattleHandler)
            val editDigimonScreen = EditDigimonScreen(digimonLibrary, spriteLoader, fileChooser)
            val dM20BattleSetupView = DM20BattleSetupView(dM20RomFactory)
            val battleSetupScreen = BattleSetupScreen(spriteLoader, dM20BattleSetupView)
            return App(Screen.fetchScreenMap(mainMenuScreen = mainMenuScreen,
                digimonLibraryScreen = digimonLibraryScreen,
                dComLoadingScreen = dComLoadingScreen,
                digimonViewScreen = digimonViewScreen,
                battleScreen = battleScreen,
                editDigimonScreen = editDigimonScreen,
                battleSetupScreen = battleSetupScreen
            ))
        }
    }

    @Composable
    fun runAppUI() {
        var screenType by remember { mutableStateOf(ScreenType.MAIN_MENU) }
        var screenParameter : Any? by remember { mutableStateOf(null) }
        MaterialTheme(colors = appColors()) {
            Box(Modifier.fillMaxSize().background(MaterialTheme.colors.background),
                contentAlignment = Alignment.TopCenter
            ) {
                log.info { "Screen Refresh" }
                screensByType.get(screenType)?.loadScreen(
                    onScreenChange = {type, parameter ->
                        log.info { "Screen Change to $type" }
                        screenType = type
                        screenParameter = parameter
                    },
                    parameter = screenParameter)
            }
        }
    }

    fun appColors() : Colors {
        return darkColors(
        )
    }
}
