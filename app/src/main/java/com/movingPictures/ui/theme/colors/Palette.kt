package com.movingPictures.ui.theme.colors

import androidx.compose.ui.graphics.Color

interface Palette {
    val primary: Color
    val secondary: Color
    val primaryContainer: Color
    val secondaryContainer: Color
    var iconsDisabled: Color
    val iconsIdle: Color
    val iconsActive: Color
    val colorsSet: List<Color>
}