package com.movingPictures.ui.theme.colors

import androidx.compose.ui.graphics.Color
import com.example.movingPictures.ui.theme.colors.Black
import com.example.movingPictures.ui.theme.colors.Blue
import com.example.movingPictures.ui.theme.colors.Gray
import com.example.movingPictures.ui.theme.colors.Green
import com.example.movingPictures.ui.theme.colors.White
import com.example.movingPictures.ui.theme.colors.colorPaletteSet

internal object LightPalette: Palette {
    override val primary = Blue
    override val secondary = Green
    override val primaryContainer = White
    override val secondaryContainer = Black
    override var iconsDisabled = Gray
    override val iconsIdle = Black
    override val iconsActive = Green
    override val colorsSet: List<Color> = colorPaletteSet
}