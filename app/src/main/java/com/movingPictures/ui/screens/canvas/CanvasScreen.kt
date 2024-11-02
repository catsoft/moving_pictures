package com.movingPictures.ui.screens.canvas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movingPictures.ui.theme.colors.ColorPalette
import com.movingPictures.ui.theme.themes.MPTheme
import com.movingPictures.compose.MPIcons
import com.movingPictures.ui.screens.canvas.canvas.CanvasView

@Composable
fun CanvasScreen(modifier: Modifier = Modifier, viewModel: CanvasViewModel = CanvasViewModel()) {
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
            MPIcons.IcBin(modifier = mediumIconModifier)
        }

        val addButtonState = viewModel.addButtonState.collectAsState()
        ControllableIcon(addButtonState.value) {
            MPIcons.IcAddFile(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .then(mediumIconModifier)
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
                MPIcons.IcPencil(modifier = mediumIconModifier)
            }

            val brushState = viewModel.brushButtonState.collectAsState()
            ControllableIcon(brushState.value) {
                MPIcons.IcBrush(modifier = mediumIconModifier)
            }

            val eraseState = viewModel.eraserButtonState.collectAsState()
            ControllableIcon(eraseState.value) {
                MPIcons.IcErase(modifier = mediumIconModifier)
            }

            val editState = viewModel.editButtonState.collectAsState()
            ControllableIcon(editState.value) {
                MPIcons.IcEdit(modifier = mediumIconModifier)
            }

            val colorState = viewModel.colorButtonState.collectAsState()
            ColorItem(colorState.value)
        }
    }
}

@Composable
fun ColorItem(state: ControllableState, color: Color = ColorPalette.currentPalette.primary) {
    Box(
        modifier = mediumIconModifier
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MPTheme {
        CanvasScreen()
    }
}


