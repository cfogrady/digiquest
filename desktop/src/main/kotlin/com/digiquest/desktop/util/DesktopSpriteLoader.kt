package com.digiquest.desktop.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digiquest.common.util.SpriteLoader
import com.digiquest.common.util.SpriteLoader.Companion.SPRITES_DIR
import com.digiquest.common.util.SpriteLoader.Companion.SPRITE_FILE_EXTENSION
import com.digiquest.core.util.FileManager
import mu.KotlinLogging
import java.io.File

private val log = KotlinLogging.logger {}

class DesktopSpriteLoader(val fileManager: FileManager) : SpriteLoader {

    fun loadImageBitmap(file: File): ImageBitmap =
        file.inputStream().buffered().use(::loadImageBitmap)

    @Composable
    override fun InternalAsyncImage(
        spriteName: String,
        contentDescription: String,
        credit: String?,
        contentScale: ContentScale,
        modifier: Modifier,
        largeText: String?,
        largeTextColor: Color
    ) {
        val spriteFile = fileManager.getFileRelativeToAppPath("$SPRITES_DIR/$spriteName$SPRITE_FILE_EXTENSION")
        if(!spriteFile.exists()) {
            log.error { "Could not find sprite for $spriteName" }
        }
        val imageBitmap: ImageBitmap? by produceState<ImageBitmap?>(null) {
            value = loadImageBitmap(spriteFile)
        }

        if(imageBitmap == null) {
            Text(contentDescription)
        } else {
            Box{
                Image(painter = BitmapPainter(imageBitmap!!),
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    modifier = modifier
                )
                if(credit != null) {
                    Text(credit, color = Color.Black, modifier = Modifier.align(Alignment.BottomStart).padding(5.dp), fontSize = 10.sp)
                }
                if(largeText != null) {
                    Text(largeText, color = largeTextColor, modifier = Modifier.align(Alignment.Center), fontSize = 36.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

    }
}