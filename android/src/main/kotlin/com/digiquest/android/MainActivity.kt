package com.digiquest.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.digiquest.android.dcom.AndroidDComPortFactory
import com.digiquest.common.App
import com.digiquest.core.dcom.DComManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dcomManager = DComManager(AndroidDComPortFactory())
        val app = App.createApp(dcomManager)

        setContent {
            app.runAppUI()
        }
    }
}