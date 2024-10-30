package com.example.movingPictures.ui.theme.colors

import com.movingPictures.ui.theme.colors.Palette

object ColorPalette {
    lateinit var currentPalette: Palette
        private set

    fun setupPalette(palette: Palette) {
        currentPalette = palette
    }
}