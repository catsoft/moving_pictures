package com.movingPictures.ui.screens.canvas.canvas

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.movingPictures.ui.screens.canvas.CanvasViewModel
import com.movingPictures.ui.screens.canvas.ControlTool

@Composable
fun onCurrentTool(viewModel: CanvasViewModel): Modifier {
    val tool = viewModel.currentTool.collectAsState()

    val penActionController = viewModel.penController
    val eraserActionController = viewModel.eraserController

    return Modifier.pointerInput(Unit) {
        detectDragGestures(
            onDragStart = { offset ->
                when (tool.value) {
                    ControlTool.PEN -> penActionController.onPenDrawUpdate(offset)
                    ControlTool.ERASER -> eraserActionController.onEraserDrawUpdate(offset)
                    else -> {}
                }
            },
            onDragEnd = {
                when (tool.value) {
                    ControlTool.PEN -> penActionController.onPenCancel()
                    ControlTool.ERASER -> eraserActionController.onEraserCancel()
                    else -> {}
                }
            },
            onDragCancel = {
                when (tool.value) {
                    ControlTool.PEN -> penActionController.onPenCancel()
                    ControlTool.ERASER -> eraserActionController.onEraserCancel()
                    else -> {}
                }
            },
            onDrag = { change, dragAmount ->
                change.consume()
                when (tool.value) {
                    ControlTool.PEN -> penActionController.onPenDrawUpdate(change.position)
                    ControlTool.ERASER -> eraserActionController.onEraserDrawUpdate(change.position)
                    else -> {}
                }
            }
        )
    }
}

