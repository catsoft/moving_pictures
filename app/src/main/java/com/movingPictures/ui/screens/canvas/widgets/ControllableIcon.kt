package com.movingPictures.ui.screens.canvas.widgets

import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.movingPictures.ui.theme.colors.ColorPalette

val smallIconModifier = Modifier.size(24.dp)

val mediumIconModifier = Modifier.size(32.dp)

enum class ControllableState {
    DISABLED,
    IDLE,
    ACTIVE
}

@Composable
fun ControllableIcon(state: ControllableState, icon: @Composable () -> Unit) {
    val color = when (state) {
        ControllableState.DISABLED -> ColorPalette.currentPalette.iconsDisabled
        ControllableState.IDLE -> ColorPalette.currentPalette.iconsIdle
        ControllableState.ACTIVE -> ColorPalette.currentPalette.iconsActive
    }

    CompositionLocalProvider(LocalContentColor.provides(color)) {
        icon()
    }
}