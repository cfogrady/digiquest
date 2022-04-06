package com.digiquest.android.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter
import com.digiquest.common.SpriteLoader

class AndroidSpriteLoader : SpriteLoader {
    @Composable
    override fun AsyncImage(spriteName: String,
                            contentDescription : String,
                            credit: String?,
                            contentScale: ContentScale,
                            modifier: Modifier
    ) {
        //val imageValue = rememberImagePainter()
    }
}