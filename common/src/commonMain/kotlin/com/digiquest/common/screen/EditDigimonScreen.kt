package com.digiquest.common.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import com.digiquest.common.util.DropDownSelect
import com.digiquest.common.util.FileChooser
import com.digiquest.common.util.SpriteLoader
import com.digiquest.common.util.TextFieldWithAutoFill
import com.digiquest.core.digimon.Digimon
import com.digiquest.core.digimon.DigimonLibrary
import com.github.cfogrady.dcom.digimon.Attribute
import com.github.cfogrady.dcom.digimon.Stage
import com.github.cfogrady.dcom.digimon.dm20.DM20Attack
import kotlinx.coroutines.launch
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

class EditDigimonScreen(val digimonLibrary: DigimonLibrary, val spriteLoader: SpriteLoader, val fileChooser: FileChooser) : Screen {
    @Composable
    override fun loadScreen(onScreenChange: (ScreenType, Any?) -> Unit, parameter: Any?) {
        if(parameter !is EditDigimonScreenParameter) {
            throw IllegalArgumentException("Code error. Parameter should be of the correct type.")
        }
        val contextScope = rememberCoroutineScope()
        val lowerCaseDigimonSpriteName = if(parameter.digimon != null) parameter.digimon.name.toLowerCase(Locale.current) else ""
        var image: ImageBitmap? by remember { mutableStateOf(null) }
        contextScope.launch {
            if(parameter.digimon != null) {
                val spriteName = lowerCaseDigimonSpriteName
                image = spriteLoader.getImage(spriteName)
            }
        }

        val textFieldColors = TextFieldDefaults.textFieldColors(textColor = Color.White)


        var digimon by remember { mutableStateOf(if(parameter.digimon != null) parameter.digimon else Digimon.builder()
            .name("")
            .dubName("")
            .artCredit("")
            .description("")
            .attribute(Attribute.FREE)
            .stage(Stage.CHILD)
            .strongAttackDM20(DM20Attack.DART)
            .weakAttackDM20(DM20Attack.SMILE)
            .build()) }
        var credits by remember { mutableStateOf(if(parameter.digimon != null) parameter.digimon.digimonEntryCredits.toList() else ArrayList())}
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                Button(onClick = {
                    onScreenChange(ScreenType.DIGIMON_LIBRARY, null)
                }) {
                    Text("Back")
                }
                val canBeSaved = digimon.name.isNotBlank()
                        && digimon.artCredit.isNotBlank()
                        && digimon.description.isNotBlank()
                        && image != null
                Button(enabled = canBeSaved, onClick = {
                    var digimonToSaveBuilder = digimon.toBuilder().digimonEntryCredits(credits.toMutableSet())
                    if(digimon.dubName.isBlank()) {
                        digimonToSaveBuilder = digimonToSaveBuilder.dubName(digimon.name)
                    }
                    contextScope.launch {
                        digimonLibrary.addDigimon(digimonToSaveBuilder.build())
                        digimonLibrary.saveLibrary()
                        spriteLoader.saveSprite(image!!, digimon.name)
                        onScreenChange(ScreenType.DIGIMON_LIBRARY, null)
                    }
                }) {
                    Text("Save")
                }
            }
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                Column(horizontalAlignment = Alignment.Start) {
                    Screen.LabelText("Name:", "", modifier = Modifier.padding(10.dp))
                    SelectionContainer {
                        TextField(value = digimon.name, onValueChange = {digimon = digimon.toBuilder().name(it).build()},
                        singleLine = true,
                        colors = textFieldColors,)
                    }
                }
                Column(horizontalAlignment = Alignment.Start) {
                    Screen.LabelText("Dub Name:", "", modifier = Modifier.padding(10.dp))
                    TextField(value = digimon.dubName, onValueChange = {digimon = digimon.toBuilder().dubName(it).build()},
                    singleLine = true,
                    colors = textFieldColors)
                }
            }
            if(image != null) {
                Image(painter = BitmapPainter(image!!), contentDescription = "", contentScale = ContentScale.Fit, modifier = Modifier.padding(10.dp))
            } else {
                Box(modifier = Modifier.size(320.dp).border(1.dp, color = MaterialTheme.colors.onBackground).padding(10.dp)) {
                    Button(onClick = {
                        val file = fileChooser.getFile();
                        if(file != null) {
                            log.info { "File selected: $file" }
                            image = spriteLoader.getImage(file)
                        }
                    }, modifier = Modifier.align(Alignment.Center)) {
                        Text("Select Image")
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top ,horizontalArrangement = Arrangement.Start) {
                Screen.LabelText("Art Credit:", modifier = Modifier.padding(10.dp))
                TextFieldWithAutoFill(value = digimon.artCredit, list = digimonLibrary.existingCredits.values.toList(), onChangeValue = {
                    digimon = digimon.toBuilder().artCredit(it).build()
                }, colors = textFieldColors, padding = 10.dp)
            }
            Screen.LabelText("Profile:", modifier = Modifier.align(Alignment.Start).padding(10.dp))
            SelectionContainer {
                TextField(value = digimon.description, onValueChange = {digimon = digimon.toBuilder().description(it).build()},
                    modifier = Modifier.padding(10.dp).height(200.dp).fillMaxWidth(),
                    colors = textFieldColors,
                )
            }
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column {
                    Screen.LabelText("Attribute:", modifier = Modifier.padding(10.dp))
                    DropDownSelect(items = Attribute.values().map { it.name!! }.toList(),
                        currentValue = digimon.attribute.name,
                        onValueChange = {digimon = digimon.toBuilder().attribute(Attribute.valueOf(it)).build()},
                        modifier = Modifier.padding(10.dp))
                }
                Column {
                    Screen.LabelText("Stage:", modifier = Modifier.padding(10.dp))
                    DropDownSelect(items = Stage.values().map { it.name!! }.toList(),
                        currentValue = digimon.stage.name,
                        onValueChange = {digimon = digimon.toBuilder().stage(Stage.valueOf(it)).build()},
                        modifier = Modifier.padding(10.dp))
                }
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column {
                    Screen.LabelText("DM20 Strong Attack:", modifier = Modifier.padding(10.dp))
                    DropDownSelect(items = DM20Attack.values().map { it.name!! }.toList(),
                        currentValue = digimon.strongAttackDM20.name,
                        onValueChange = {digimon = digimon.toBuilder().strongAttackDM20(DM20Attack.valueOf(it)).build()},
                        modifier = Modifier.padding(10.dp))
                }
                Column {
                    Screen.LabelText("DM20 Weak Attack:", modifier = Modifier.padding(10.dp))
                    DropDownSelect(items = DM20Attack.values().map { it.name!! }.toList(),
                        currentValue = digimon.weakAttackDM20.name,
                        onValueChange = {digimon = digimon.toBuilder().weakAttackDM20(DM20Attack.valueOf(it)).build()},
                        modifier = Modifier.padding(10.dp))
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top ,horizontalArrangement = Arrangement.Start) {
                Screen.LabelText("Relative Strength:", modifier = Modifier.padding(10.dp))
                TextField(value = digimon.defaultStageStrength.toString(), onValueChange = {value ->
                    try {
                        digimon = digimon.toBuilder().defaultStageStrength(value.toDouble()).build()
                    } catch (nfe: NumberFormatException) {
                        log.info { "Invalid decimal: $value" }
                    }
                }, colors = textFieldColors)
            }
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Screen.LabelText("Data Entry Credits:", modifier = Modifier.padding(10.dp))
                credits.forEachIndexed(action = {index, value ->
                    TextFieldWithAutoFill(value = value, list = digimonLibrary.existingCredits.values.toList(), onChangeValue = {
                        credits = credits.toMutableList().apply { set(index, it) }
                    }, colors = textFieldColors, padding = 10.dp)
                })
                Button(onClick = {
                    credits = credits.toMutableList().apply { add("") }
                }, modifier = Modifier.padding(10.dp)) {
                    Text("Add Data Entry Credit", color = MaterialTheme.colors.onBackground)
                }
            }

        }
    }

    data class EditDigimonScreenParameter(val digimon : Digimon?)
}
