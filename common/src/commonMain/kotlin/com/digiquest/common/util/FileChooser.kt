package com.digiquest.common.util

interface FileChooser {
    fun getFile(pattern: String = "", save: Boolean = false) : String?
}