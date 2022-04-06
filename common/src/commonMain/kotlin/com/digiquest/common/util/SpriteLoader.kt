package com.digiquest.common.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale

interface SpriteLoader {
    companion object {
        const val SPRITES_DIR = "sprites"
        const val SPRITE_FILE_EXTENSION = ".png"
        const val LOADING_TEXT = "Loading Image..."
    }

    @Composable
    fun InternalAsyncImage(spriteName: String,
                           contentDescription : String,
                           credit : String?,
                           contentScale: ContentScale,
                           modifier: Modifier,
                           largeText: String?,
                           largeTextColor: Color,
    )

    @Composable
    fun AsyncImage(spriteName: String,
                   contentDescription: String,
                   credit: String? = null,
                   contentScale: ContentScale = ContentScale.Fit,
                   modifier: Modifier = Modifier,
                   largeText: String? = null,
                   largeTextColor: Color = Color.Black
    ) {
        InternalAsyncImage(spriteName, contentDescription,
            credit = credit,
            contentScale = contentScale,
            modifier = modifier,
            largeText = largeText,
            largeTextColor = largeTextColor
        )
    }
}