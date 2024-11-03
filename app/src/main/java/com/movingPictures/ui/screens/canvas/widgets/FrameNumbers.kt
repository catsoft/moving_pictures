package com.movingPictures.ui.screens.canvas.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.movingPictures.data.FrameComposer
import com.movingPictures.ui.theme.colors.ColorPalette

@Composable
fun FrameNumbers(modifier: Modifier, currentFrame: FrameComposer?, previousFrame: FrameComposer?) {
    Column(modifier = modifier) {
        previousFrame?.let {
            Text(previousFrame.number.toString(), color = ColorPalette.currentPalette.primary.copy(alpha = 0.5f))
        }

        currentFrame?.let {
            Text(currentFrame.number.toString(), color = ColorPalette.currentPalette.primary)
        }
    }
}