package com.digiquest.common.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextFieldWithAutoFill(
    modifier: Modifier = Modifier,
    padding: Dp = 0.dp,
    value: String,
    onChangeValue: (String) -> Unit,
    list: List<String>,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors()
) {
    var showDropDown by remember { mutableStateOf(false) }
    Column(modifier = Modifier.width(IntrinsicSize.Min).padding(padding)) {
        SelectionContainer {
            TextField(
                value = value,
                onValueChange = {
                    onChangeValue(it)
                    showDropDown = true
                },
                modifier = modifier.onFocusChanged { focusState ->
                    showDropDown = focusState.isFocused
                }.onKeyEvent {
                    if(it.key == Key.Escape) {
                        showDropDown = false;
                        return@onKeyEvent true
                    }
                    return@onKeyEvent false
                },
                maxLines = 1,
                colors = colors
            )
        }
        if(showDropDown) {
            var colModifier = Modifier
                .background(color = Color.DarkGray)
                .heightIn(0.dp, 60.dp)
                .fillMaxWidth()
            Column(modifier = colModifier.verticalScroll(rememberScrollState())) {
                list.filter { text -> text.toLowerCase(Locale.current).contains(value.toLowerCase(Locale.current))}.forEach {
                    text ->
                    Box(modifier = Modifier.clickable {
                        onChangeValue(text)
                        showDropDown = false
                    }.fillMaxWidth()) {
                        Text(text, color = colors.textColor(true).value)
                    }
                }
            }
        }
    }
}