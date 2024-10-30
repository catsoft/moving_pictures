package com.movingPictures.ui.theme.colors

import androidx.compose.ui.graphics.Color
import com.example.movingPictures.ui.theme.colors.Black
import com.example.movingPictures.ui.theme.colors.Blue
import com.example.movingPictures.ui.theme.colors.Gray
import com.example.movingPictures.ui.theme.colors.Green
import com.example.movingPictures.ui.theme.colors.White
import com.example.movingPictures.ui.theme.colors.colorPaletteSet

internal object DarkPalette: Palette {
    override val primary = Blue
    override val secondary = Green
    override val primaryContainer = Black
    override val secondaryContainer = White
    override var iconsDisabled = Gray
    override val iconsIdle = White
    override val iconsActive = Green
    override val colorsSet: List<Color> = colorPaletteSet
}