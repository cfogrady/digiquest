package com.digiquest.common.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp

@Composable
fun DropDownSelect(items : List<String>, currentValue: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart).composed { modifier }) {
        Text(currentValue, modifier = Modifier.background(color = TextFieldDefaults.textFieldColors().backgroundColor(true).value).clickable(onClick = { expanded = true }).padding(10.dp),
            color = MaterialTheme.colors.onBackground)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEach { value ->
                DropdownMenuItem(onClick = {
                    onValueChange(value)
                    expanded = false
                }) {
                    Text(text = value)
                }
            }
        }
    }
}

@Composable
fun DropDownSelect(items : List<String>, selectedIndex: Int, onValueChange: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart).background(color = TextFieldDefaults.textFieldColors().backgroundColor(true).value)
        .width(TextFieldDefaults.MinWidth)
        .height(TextFieldDefaults.MinHeight)) {
        Text(items[selectedIndex], modifier = Modifier.clickable(onClick = { expanded = true }).align(Alignment.CenterStart),
            color = MaterialTheme.colors.onBackground)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEachIndexed { index, value ->
                DropdownMenuItem(onClick = {
                    onValueChange(index)
                    expanded = false
                }) {
                    Text(text = value)
                }
            }
        }
    }
}
