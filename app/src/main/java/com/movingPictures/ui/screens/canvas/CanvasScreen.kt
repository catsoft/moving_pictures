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
import com.movingPictures.ui.screens.canvas.widgets.ShapeSelector
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

        ShapeSelector(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 82.dp),
            show = tools.value == ControlTool.SHAPES,
            viewModel
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
        ControllableIcon(Modifier.padding(end = 8.dp), undoButtonState.value, clickAction = { viewModel.undo() }) {
            MPIcons.IcToLeft(smallIconModifier)
        }

        val redoButtonState = viewModel.redoButtonState.collectAsState()
        ControllableIcon(Modifier.padding(end = 8.dp), redoButtonState.value, clickAction = { viewModel.redo() }) {
            MPIcons.IcToRight(smallIconModifier)
        }

        Spacer(modifier = Modifier.weight(1f))

        val deleteButtonState = viewModel.deleteButtonState.collectAsState()
        ControllableIcon(Modifier, deleteButtonState.value, clickAction = { viewModel.deleteFrame() }) {
            MPIcons.IcBin(mediumIconModifier)
        }

        val addButtonState = viewModel.addButtonState.collectAsState()
        ControllableIcon(Modifier.padding(start = 16.dp), addButtonState.value, clickAction = { viewModel.addNewFrame() }) {
            MPIcons.IcAddFile(mediumIconModifier)
        }

        val layersButtonState = viewModel.layersButtonState.collectAsState()
        ControllableIcon(Modifier.padding(start = 16.dp), layersButtonState.value, clickAction = { }) {
            MPIcons.IcLayers(mediumIconModifier)
        }

        Spacer(modifier = Modifier.weight(1f))

        val pauseButtonState = viewModel.pauseButtonState.collectAsState()
        ControllableIcon(Modifier, pauseButtonState.value, clickAction = { viewModel.pause() }) {
            MPIcons.IcPause(mediumIconModifier)
        }

        val playButtonState = viewModel.playButtonState.collectAsState()
        ControllableIcon(Modifier.padding(start = 8.dp), playButtonState.value, clickAction = { viewModel.play() }) {
            MPIcons.IcPlay(mediumIconModifier)
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
            ControllableIcon(Modifier, penButtonState.value, clickAction = { viewModel.selectTool(ControlTool.PEN) }) {
                MPIcons.IcPencil(modifier = mediumIconModifier)
            }

            val brushState = viewModel.brushButtonState.collectAsState()
            ControllableIcon(Modifier, brushState.value, clickAction = { viewModel.selectTool(ControlTool.BRUSH) }) {
                MPIcons.IcBrush(modifier = mediumIconModifier)
            }

            val eraseState = viewModel.eraserButtonState.collectAsState()
            ControllableIcon(Modifier, eraseState.value, clickAction = { viewModel.selectTool(ControlTool.ERASER) }) {
                MPIcons.IcErase(modifier = mediumIconModifier)
            }

            val editState = viewModel.editButtonState.collectAsState()
            ControllableIcon(mediumIconModifier, editState.value, clickAction = { viewModel.selectTool(ControlTool.SHAPES) }) {
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