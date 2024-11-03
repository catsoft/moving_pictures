package com.movingPictures.ui.screens.canvas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.movingPictures.compose.MPIcons
import com.movingPictures.ui.screens.canvas.canvas.CanvasView
import com.movingPictures.ui.screens.canvas.widgets.ColorFullPalettePickerPopup
import com.movingPictures.ui.screens.canvas.widgets.ColorPicker
import com.movingPictures.ui.screens.canvas.widgets.ColorPickerPopup
import com.movingPictures.ui.screens.canvas.widgets.ControllableIcon
import com.movingPictures.ui.screens.canvas.widgets.FrameNumbers
import com.movingPictures.ui.screens.canvas.widgets.ThicknessSlider
import com.movingPictures.ui.screens.canvas.widgets.mediumIconModifier
import com.movingPictures.ui.screens.canvas.widgets.smallIconModifier
import com.movingPictures.ui.theme.themes.MPTheme

@Composable
fun CanvasScreen(modifier: Modifier = Modifier, viewModel: CanvasViewModel) {
    Box(modifier.fillMaxSize()) {
        Column(modifier.fillMaxSize()) {
            TopBar(modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 0.dp), viewModel)
            CanvasView(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                viewModel
            )
            BottomBar(modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp), viewModel)
        }

        val tools = viewModel.currentTool.collectAsState()
        ColorPickerPopup(tools.value == ControlTool.COLOR_PICKER, viewModel)

        val fullPalette = viewModel.fullPalette.collectAsState()
        ColorFullPalettePickerPopup(fullPalette.value, viewModel)

        val settings = viewModel.drawSettings.collectAsState()
        val penThickness = remember { mutableFloatStateOf(settings.value.penSize) }
        LaunchedEffect(penThickness.floatValue) {
            viewModel.setPenSize(penThickness.floatValue)
        }
        ThicknessSlider(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 82.dp),
            show = tools.value == ControlTool.PEN,
            viewModel = viewModel,
            thickness = penThickness
        )

        val eraserThickness = remember { mutableFloatStateOf(settings.value.eraseSize) }
        LaunchedEffect(eraserThickness.floatValue) {
            viewModel.setEraseSize(eraserThickness.floatValue)
        }
        ThicknessSlider(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 82.dp),
            show = tools.value == ControlTool.ERASER,
            viewModel = viewModel,
            thickness = eraserThickness
        )

        val currentFrame = viewModel.currentFrame.collectAsState()
        val previousFrame = viewModel.previousFrame.collectAsState()
        FrameNumbers(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 82.dp),
            currentFrame.value,
            previousFrame.value
        )
    }
}

@Composable
fun TopBar(modifier: Modifier = Modifier, viewModel: CanvasViewModel) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val undoButtonState = viewModel.undoButtonState.collectAsState()
        ControllableIcon(undoButtonState.value) {
            MPIcons.IcToLeft(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .then(smallIconModifier)
                    .clickable { viewModel.undo() }
            )
        }

        val redoButtonState = viewModel.redoButtonState.collectAsState()
        ControllableIcon(redoButtonState.value) {
            MPIcons.IcToRight(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .then(smallIconModifier)
                    .clickable { viewModel.redo() }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        val deleteButtonState = viewModel.deleteButtonState.collectAsState()
        ControllableIcon(deleteButtonState.value) {
            MPIcons.IcBin(modifier = mediumIconModifier.clickable { viewModel.deleteFrame() })
        }

        val addButtonState = viewModel.addButtonState.collectAsState()
        ControllableIcon(addButtonState.value) {
            MPIcons.IcAddFile(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .then(mediumIconModifier.clickable { viewModel.addNewFrame() })
            )
        }

        val layersButtonState = viewModel.layersButtonState.collectAsState()
        ControllableIcon(layersButtonState.value) {
            MPIcons.IcLayers(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .then(mediumIconModifier)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        val pauseButtonState = viewModel.pauseButtonState.collectAsState()
        ControllableIcon(pauseButtonState.value) {
            MPIcons.IcPause(
                modifier = mediumIconModifier
            )
        }

        val playButtonState = viewModel.playButtonState.collectAsState()
        ControllableIcon(playButtonState.value) {
            MPIcons.IcPlay(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .then(mediumIconModifier)
            )
        }
    }
}

@Composable
fun BottomBar(modifier: Modifier = Modifier, viewModel: CanvasViewModel) {
    Box(
        modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val penButtonState = viewModel.penButtonState.collectAsState()
            ControllableIcon(penButtonState.value) {
                MPIcons.IcPencil(modifier = mediumIconModifier.clickable { viewModel.selectTool(ControlTool.PEN) })
            }

            val brushState = viewModel.brushButtonState.collectAsState()
            ControllableIcon(brushState.value) {
                MPIcons.IcBrush(modifier = mediumIconModifier.clickable { viewModel.selectTool(ControlTool.BRUSH) })
            }

            val eraseState = viewModel.eraserButtonState.collectAsState()
            ControllableIcon(eraseState.value) {
                MPIcons.IcErase(modifier = mediumIconModifier.clickable { viewModel.selectTool(ControlTool.ERASER) })
            }

            val editState = viewModel.editButtonState.collectAsState()
            ControllableIcon(editState.value) {
                MPIcons.IcEdit(modifier = mediumIconModifier)
            }

            ColorPicker(Modifier, viewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MPTheme {
        CanvasScreen(Modifier, CanvasViewModel())
    }
}