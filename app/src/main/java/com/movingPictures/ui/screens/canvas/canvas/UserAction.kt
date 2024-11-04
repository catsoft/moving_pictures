package com.movingPictures.ui.screens.canvas.canvas

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import com.movingPictures.ui.screens.canvas.CanvasViewModel
import com.movingPictures.ui.screens.canvas.ControlTool

@Composable
fun onCurrentTool(viewModel: CanvasViewModel): Modifier {
    val tool = viewModel.currentTool.collectAsState()

    val penActionController = viewModel.penController
    val eraserActionController = viewModel.eraserController
    val moveActionController = viewModel.moveController

    fun onDrawUpdate(offset: Offset) {
        when (tool.value) {
            ControlTool.PEN -> penActionController.onDrawUpdate(offset)
            ControlTool.ERASER -> eraserActionController.onDrawUpdate(offset)
            ControlTool.MOVE -> moveActionController.onDrawUpdate(offset)
            else -> {}
        }
    }

    fun onCancel() {
        when (tool.value) {
            ControlTool.PEN -> penActionController.onCancel()
            ControlTool.ERASER -> eraserActionController.onCancel()
            ControlTool.MOVE -> moveActionController.onCancel()
            else -> {}
        }
    }

    // todo gonna be work better with onTouchEvent because onDragUpdate have a slope
    return Modifier.pointerInput(Unit) {
        detectDragGestures(
            onDragStart = { offset -> onDrawUpdate(offset) },
            onDragEnd = { onCancel() },
            onDragCancel = { onCancel() },
            onDrag = { change, dragAmount ->
                change.consume()
                onDrawUpdate(change.position)
            }
        )
    }
}

