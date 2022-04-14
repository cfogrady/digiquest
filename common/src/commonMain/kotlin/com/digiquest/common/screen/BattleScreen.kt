package com.digiquest.common.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digiquest.common.util.SpriteLoader
import com.digiquest.core.battle.BattleHandler
import com.digiquest.core.battle.DeviceROMWrapper
import com.digiquest.core.dcom.DComManager
import com.digiquest.core.digimon.Digimon
import com.digiquest.core.digimon.DigimonLibrary
import com.github.cfogrady.dcom.digimon.battle.DigimonChoice
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mu.KotlinLogging
import kotlin.time.ExperimentalTime

private val log = KotlinLogging.logger {}

private val WIN_TEXT = "WINNER!"
private val LOOSER_TEXT = "LOOSER..."

class BattleScreen(val dComManager: DComManager, val spriteLoader: SpriteLoader, val battleHandler: BattleHandler) : Screen {
    @OptIn(ExperimentalTime::class)
    @Composable
    override fun loadScreen(onScreenChange: (ScreenType, Any?) -> Unit, parameter: Any?) {
        log.info { "Redraw of battle screen" }
        if(parameter !is BattleScreenParameters) {
            throw IllegalArgumentException("Code error. Parameter should be of the correct type.")
        }
        val deviceRoundDurationMs : Long = 2000L
        val timeBeforeRoundMs : Long = 2000L
        val composeContext = rememberCoroutineScope()
        val digimon = parameter.digimon
        var deviceDigimon by remember { mutableStateOf<Digimon?>(null) }
        var winningDigimon by remember { mutableStateOf<DigimonChoice?>(null) }
        if(!dComManager.isDComInitialized) {
            onScreenChange(ScreenType.DCOM_LOADING, DComLoadingScreen.DComLoadScreenParameter(returnScreenType = ScreenType.VIEW_DIGIMON, successScreenType = ScreenType.BATTLE, successScreenParameter = parameter))
        } else if(deviceDigimon == null) {
            battleHandler.runBattle(parameter.deviceROMWrapper).thenAccept { battleResult ->
                val result = battleResult.battleResult
                log.info {"Winner: ${result.winner}"}
                log.info {"Computer Attack Pattern: ${result.computerMonAttackPattern}"}
                log.info {"Device Attack Pattern: ${result.digiviceMonAttackPattern}"}
                log.info {"Hits: ${result.hits}"}
                deviceDigimon = battleResult.deviceDigimon
                log.info { "Device Digimon: ${battleResult.deviceDigimon}" }
                log.info { "Device Index: ${result.deviceIndex}" }
                composeContext.launch {
                    delay(timeBeforeRoundMs + deviceRoundDurationMs * result.hits.size)
                    winningDigimon = result.winner
                }
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.padding(10.dp).verticalScroll(rememberScrollState()).fillMaxWidth()
        ) {
            var computerStatus : String? = null
            var computerStatusColor : Color = Color.Black
            var deviceStatus : String? = null
            var deviceStatusColor : Color = Color.Black
            if(winningDigimon != null) {
                if(winningDigimon == DigimonChoice.COMPUTER_DIGIMON) {
                    computerStatus = WIN_TEXT
                    computerStatusColor = Color.Green
                    deviceStatus = LOOSER_TEXT
                    deviceStatusColor = Color.Red
                } else {
                    computerStatus = LOOSER_TEXT
                    computerStatusColor = Color.Red
                    deviceStatus = WIN_TEXT
                    deviceStatusColor = Color.Green
                }
            }
            spriteLoader.AsyncImage(digimon.name, "${digimon.name} Art", largeText = computerStatus, largeTextColor = computerStatusColor)
            Text("VS", color = MaterialTheme.colors.onBackground, fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(10.dp)
            )
            if(deviceDigimon == null) {
                Box(modifier = Modifier.size(320.dp).border(1.dp, color = MaterialTheme.colors.onBackground)) {
                    Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Text("Connect Device", color = MaterialTheme.colors.onBackground, fontSize = 16.sp, modifier = Modifier.padding(10.dp))
                        Text("And", color = MaterialTheme.colors.onBackground, fontSize = 16.sp, modifier = Modifier.padding(10.dp))
                        Text("Press Button On Device", color = MaterialTheme.colors.onBackground, fontSize = 16.sp, modifier = Modifier.padding(10.dp))
                    }
                }
            } else if(deviceDigimon!!.name == BattleHandler.UNKNOWN_DIGIMON) {
                Box(modifier = Modifier.size(320.dp).border(1.dp, color = MaterialTheme.colors.onBackground)) {
                    Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Text("Unknown Device Digimon!", color = MaterialTheme.colors.onBackground, fontSize = 16.sp, modifier = Modifier.padding(10.dp))
                    }
                }
            } else {
                spriteLoader.AsyncImage(deviceDigimon!!.name, "${deviceDigimon!!.name} Art", largeText = deviceStatus, largeTextColor = deviceStatusColor)
            }
            Button(onClick = {
                onScreenChange(parameter.returnScreen, null)
            }) {
                Text("Back")
            }
        }
    }

    data class BattleScreenParameters(val digimon : Digimon, val deviceROMWrapper: DeviceROMWrapper, val returnScreen : ScreenType)
}