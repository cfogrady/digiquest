package com.digiquest.desktop.util

import androidx.compose.ui.awt.ComposeWindow
import com.digiquest.common.util.FileChooser
import java.awt.FileDialog

class DesktopFileChooser : FileChooser {

    override fun getFile(pattern: String, save: Boolean): String? {
        val fileDialog = FileDialog(ComposeWindow(), "Choose an image", FileDialog.LOAD)
        if(pattern != "") {
            fileDialog.file = pattern //example pattern "*.jpg; *.png"
        }
        fileDialog.isVisible = true
        if(fileDialog.file == null) {
            return null
        }
        return "${fileDialog.directory}/${fileDialog.file}"
    }
}