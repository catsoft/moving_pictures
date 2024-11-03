package com.movingPictures.ui.screens.canvas

import androidx.compose.ui.graphics.Color
import com.movingPictures.ui.theme.colors.ColorPalette

data class DrawSettings(
    val color: Color = ColorPalette.currentPalette.primary,
    val penSize: Float = 5F,
    val eraseSize: Float = 15F,
    val eraseColor: Color = Color.Transparent,

    val shapeWidth: Float = 15F,
    val shapeSize: Float = 800F,

    val centerX: Float = 500F,
    val centerY: Float = 500F,
)