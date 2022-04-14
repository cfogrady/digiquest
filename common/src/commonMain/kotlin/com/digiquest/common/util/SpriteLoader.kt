package com.digiquest.common.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.layout.ContentScale
import com.digiquest.core.digimon.SpritePackager
import java.awt.image.BufferedImage
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import javax.imageio.ImageIO

abstract class SpriteLoader : SpritePackager {
    companion object {
        const val SPRITES_DIR = "sprites"
        const val SPRITE_FILE_EXTENSION = ".png"
        const val LOADING_TEXT = "Loading Image..."
    }

    @Composable
    protected abstract fun InternalAsyncImage(spriteName: String,
                           contentDescription : String,
                           credit : String?,
                           contentScale: ContentScale,
                           modifier: Modifier,
                           largeText: String?,
                           largeTextColor: Color,
    )

    fun getImage(fileLocation: String) : ImageBitmap {
        val file = File(fileLocation)
        return getImage(file)
    }

    abstract fun getImage(file: File) : ImageBitmap

    protected abstract fun getSpriteSaveLocation(spriteName: String) : File

    fun saveSprite(imageBitmap: ImageBitmap, spriteName: String) {
        ImageIO.write(imageBitmap.toAwtImage(), "png", getSpriteSaveLocation(spriteName.lowercase()))
    }

    override fun addSpriteToStream(spriteName: String?, outputStream: OutputStream?) {
        val bitmap = getImage(getSpriteSaveLocation(spriteName!!.lowercase()))
        ImageIO.write(bitmap.toAwtImage(), "png", outputStream)
    }

    override fun importSpriteFromStream(spriteName: String?, inputStream: InputStream?) {
        val image = ImageIO.read(inputStream)
        ImageIO.write(image, "png", getSpriteSaveLocation(spriteName!!.lowercase()))
    }

    @Composable
    fun AsyncImage(spriteName: String,
                   contentDescription: String,
                   credit: String? = null,
                   contentScale: ContentScale = ContentScale.Fit,
                   modifier: Modifier = Modifier,
                   largeText: String? = null,
                   largeTextColor: Color = Color.Black
    ) {
        InternalAsyncImage(spriteName.lowercase(), contentDescription,
            credit = credit,
            contentScale = contentScale,
            modifier = modifier,
            largeText = largeText,
            largeTextColor = largeTextColor
        )
    }
}