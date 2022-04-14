package com.digiquest.common.screen.battlesetup

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.digiquest.common.screen.Screen
import com.digiquest.common.util.DropDownSelect
import com.digiquest.core.battle.DeviceROMWrapper
import com.digiquest.core.battle.dm20.DM20ROMWrapper
import com.digiquest.core.battle.dm20.DM20RomFactory
import com.digiquest.core.digimon.Digimon
import com.github.cfogrady.dcom.digimon.Stage

class DM20BattleSetupView(val dM20RomFactory: DM20RomFactory) {
    @Composable
    fun DeviceStatSelection(digimon: Digimon, deviceROMWrapper: DeviceROMWrapper?, onRomChange: (DeviceROMWrapper) -> Unit) {
        var basePowerIndexEntry by remember { mutableStateOf(dM20RomFactory.getIndexForStageAndRelativePower(digimon.stage, digimon.defaultStageStrength)) }
        if(deviceROMWrapper == null) {
            onRomChange(DM20ROMWrapper(dM20RomFactory.createDM20Rom(digimon.name, digimon.stage, digimon.attribute, basePowerIndexEntry, 0, 0, digimon.strongAttackDM20, digimon.weakAttackDM20)))
        }
        val actualDeviceROMWrapper = deviceROMWrapper ?: DM20ROMWrapper(dM20RomFactory.createDM20Rom(digimon, 0, 0))
        if(actualDeviceROMWrapper !is DM20ROMWrapper) {
            throw IllegalArgumentException("Code error. Parameter should be of the correct type.")
        }
        var strength by remember { mutableStateOf(0) }
        var stage by remember { mutableStateOf(digimon.stage) }
        var attack by remember { mutableStateOf(0) }
        val updateRom = {
            onRomChange(DM20ROMWrapper(dM20RomFactory.createDM20Rom(digimon.name,
                stage,
                digimon.attribute,
                basePowerIndexEntry,
                attack,
                strength,
                digimon.strongAttackDM20,
                digimon.weakAttackDM20)))
        }
        val stages = Stage.values().toList().map { it.name }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Screen.LabelText("Stage:", modifier = Modifier.padding(10.dp))
            DropDownSelect(items = stages, currentValue = stage.name, onValueChange = {
                stage = Stage.valueOf(it)
                basePowerIndexEntry = dM20RomFactory.getIndexForStageAndRelativePower(stage, digimon.defaultStageStrength)
                updateRom()
            }, modifier = Modifier.padding(10.dp))
        }
        val powerLevels = dM20RomFactory.getPowersAtStage(stage).toList().sorted().map { it.toString() }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Screen.LabelText("Power:", modifier = Modifier.padding(10.dp))
            DropDownSelect(items = powerLevels, currentValue = basePowerIndexEntry.power.toString(), onValueChange = {
                basePowerIndexEntry = dM20RomFactory.getIndexForStageAndPower(stage, it.toInt())
                updateRom()
            }, modifier = Modifier.padding(10.dp))
        }
        val strengthLevels = listOf("0", "1", "2", "3", "4")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Screen.LabelText("Strength:", modifier = Modifier.padding(10.dp))
            DropDownSelect(items = strengthLevels, currentValue = strength.toString(), onValueChange = {
                strength = it.toInt()
                updateRom()
            }, modifier = Modifier.padding(10.dp))
        }
        val attackLevels = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Screen.LabelText("Attack:", modifier = Modifier.padding(10.dp))
            DropDownSelect(items = attackLevels, currentValue = attack.toString(), onValueChange = {
                attack = it.toInt()
                updateRom()
            }, modifier = Modifier.padding(10.dp))
        }
    }
}