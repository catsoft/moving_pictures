package com.example.movingPictures.ui.theme.colors

object ColorPalette {
    lateinit var currentPalette: Palette
        private set

    fun setupPalette(palette: Palette) {
        currentPalette = palette
    }
}