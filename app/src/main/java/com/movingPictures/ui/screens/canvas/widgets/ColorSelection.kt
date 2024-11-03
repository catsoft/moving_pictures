package com.movingPictures.ui.screens.canvas.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.movingPictures.compose.MPIcons
import com.movingPictures.ui.theme.colors.ColorPalette
import com.movingPictures.ui.screens.canvas.CanvasViewModel
import com.movingPictures.ui.screens.canvas.ControlTool


@Composable
fun ColorPicker(modifier: Modifier = Modifier, viewModel: CanvasViewModel) {
    val colorState = viewModel.colorButtonState.collectAsState()
    ColorItem(viewModel, modifier.clickable { viewModel.selectTool(ControlTool.COLOR_PICKER) }, colorState.value)
}

@Composable
fun ColorItem(viewModel: CanvasViewModel, modifier: Modifier, state: ControllableState) {
    val settings = viewModel.drawSettings.collectAsState()
    val color = settings.value.color

    Box(
        modifier = modifier
            .then(mediumIconModifier)
            .background(color, shape = RoundedCornerShape(16.dp))
            .then(
                if (state == ControllableState.ACTIVE) Modifier.border(
                    width = 2.dp,
                    color = ColorPalette.currentPalette.iconsActive,
                    shape = RoundedCornerShape(16.dp)
                )
                else Modifier
            )
            .clip(RoundedCornerShape(16.dp))
    ) {

    }
}

@Composable
fun ColorOption(color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(color, shape = RoundedCornerShape(14.dp))
        )
    }
}

@Composable
fun BoxScope.ColorPickerPopup(
    isShown: Boolean,
    viewModel: CanvasViewModel,
) {
    val width = 5 * 32.dp + 6 * 16.dp + 2.dp
    Popup(
        Modifier
            .padding(bottom = 82.dp)
            .width(width),
        isShown
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val isShownFullPalette = viewModel.fullPalette.collectAsState()
            val state = if (isShownFullPalette.value) ControllableState.ACTIVE else ControllableState.IDLE
            ControllableIcon(state) {
                MPIcons.IcColorPalette(modifier = mediumIconModifier.clickable { viewModel.fullPalette.value = !isShownFullPalette.value })
            }

            ColorPalette.currentPalette.colorsSet.take(4).forEach { color ->
                ColorOption(color = color, onClick = {
                    selectColor(viewModel, color)
                })
            }
        }
    }
}

@Composable
fun BoxScope.ColorFullPalettePickerPopup(
    isShown: Boolean,
    viewModel: CanvasViewModel,
) {
    val width = 5 * 32.dp + 6 * 16.dp + 2.dp
    Popup(
        Modifier
            .padding(bottom = 158.dp)
            .width(width),
        isShown
    ) {
        LazyVerticalGrid(
            modifier = Modifier.wrapContentSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            columns = GridCells.Adaptive(32.dp),
            content = {
                items(ColorPalette.currentPalette.colorsSet.size) { index ->
                    val color = ColorPalette.currentPalette.colorsSet[index]
                    ColorOption(color = color, onClick = {
                        selectColor(viewModel, color)
                    })
                }
            }
        )
    }
}

private fun selectColor(viewModel: CanvasViewModel, color: Color) {
    viewModel.selectColor(color)
    viewModel.selectTool(ControlTool.PEN)
    viewModel.fullPalette.value = false
}