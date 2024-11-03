package com.movingPictures.ui.theme.colors

import androidx.compose.ui.graphics.Color

internal object DarkPalette: Palette {
    override val primary = Blue
    override val secondary = Green
    override val primaryContainer = Black
    override val secondaryContainer = White
    override var iconsDisabled = Gray
    override val iconsIdle = White
    override val iconsActive = Green

    override val shortColorsSet: List<Color> = shortColorSet
    override val colorsSet: List<Color> = colorPaletteSet
    override val greenGradient: List<Color> = listOf(GreenGradientStart, GreenGradientEnd)
}