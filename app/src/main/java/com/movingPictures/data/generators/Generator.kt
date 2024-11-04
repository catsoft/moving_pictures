package com.movingPictures.data.generators

import com.movingPictures.ui.screens.canvas.DrawSettings

interface Generator {
    fun generate(settings: DrawSettings)
}