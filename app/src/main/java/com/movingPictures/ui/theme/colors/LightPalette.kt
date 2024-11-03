package com.movingPictures.ui.theme.colors

import androidx.compose.ui.graphics.Color

internal object LightPalette : Palette {
    override val primary = Blue
    override val secondary = Green
    override val primaryContainer = White
    override val secondaryContainer = Black
    override var iconsDisabled = Gray
    override val iconsIdle = Black
    override val iconsActive = Green

    override val shortColorsSet: List<Color> = shortColorSet
    override val colorsSet: List<Color> = colorPaletteSet
    override val greenGradient: List<Color> = listOf(GreenGradientStart, GreenGradientEnd)
}