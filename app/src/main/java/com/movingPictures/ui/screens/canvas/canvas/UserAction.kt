package com.movingPictures.ui.screens.canvas.canvas

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import com.movingPictures.compose.toPoint
import com.movingPictures.data.dto.DrawableItemState
import com.movingPictures.data.dto.EraserDrawableItem
import com.movingPictures.data.dto.PenDrawableItem
import com.movingPictures.data.dto.PointColors
import com.movingPictures.ui.screens.canvas.CanvasViewModel
import com.movingPictures.ui.screens.canvas.ControlTool

@Composable
fun onCurrentTool(
    viewModel: CanvasViewModel,
    penDrawable: MutableState<PenDrawableItem?>,
    eraserDrawable: MutableState<EraserDrawableItem?>
): Modifier {
    val drawSettings = viewModel.drawSettings.collectAsState()
    val tool = viewModel.currentTool.collectAsState()

    val onPenDrawUpdate: (Offset) -> Unit = { offset ->
        if (penDrawable.value != null) {
            penDrawable.value = penDrawable.value!!.copy(
                points = penDrawable.value!!.points + PointColors(
                    offset.toPoint(penDrawable.value!!.state.position),
                    drawSettings.value.color.toArgb()
                )
            )
        } else {
            penDrawable.value = PenDrawableItem(
                DrawableItemState(
                    position = offset.toPoint(),
                    color = drawSettings.value.color.toArgb()
                ),
                drawSettings.value.width,
                mutableListOf(PointColors(offset.toPoint(offset.toPoint()), drawSettings.value.color.toArgb()))
            )
        }
    }
    val onPenCancel: () -> Unit = {
        penDrawable.value?.let {
            penDrawable.value = null
            viewModel.addDrawable(it)
        }
    }

    val onEraserDrawUpdate: (Offset) -> Unit = { offset ->
        if (eraserDrawable.value != null) {
            eraserDrawable.value = eraserDrawable.value!!.copy(
                points = eraserDrawable.value!!.points + PointColors(
                    offset.toPoint(eraserDrawable.value!!.state.position),
                    drawSettings.value.eraseColor.toArgb()
                )
            )
        } else {
            eraserDrawable.value = EraserDrawableItem(
                DrawableItemState(
                    position = offset.toPoint(),
                    color = drawSettings.value.eraseColor.toArgb()
                ),
                drawSettings.value.eraseSize,
                mutableListOf(PointColors(offset.toPoint(offset.toPoint()), drawSettings.value.eraseColor.toArgb()))
            )
        }
    }

    val onEraserCancel: () -> Unit = {
        eraserDrawable.value?.let {
            eraserDrawable.value = null
            viewModel.addDrawable(it)
        }
    }

    return Modifier.pointerInput(Unit) {
        detectDragGestures(
            onDragStart = { offset ->
                when (tool.value) {
                    ControlTool.PEN -> onPenDrawUpdate(offset)
                    ControlTool.ERASER -> onEraserDrawUpdate(offset)
                    else -> {}
                }
            },
            onDragEnd = {
                when (tool.value) {
                    ControlTool.PEN -> onPenCancel()
                    ControlTool.ERASER -> onEraserCancel()
                    else -> {}
                }
            },
            onDragCancel = {
                when (tool.value) {
                    ControlTool.PEN -> onPenCancel()
                    ControlTool.ERASER -> onEraserCancel()
                    else -> {}
                }
            },
            onDrag = { change, dragAmount ->
                change.consume()
                when (tool.value) {
                    ControlTool.PEN -> onPenDrawUpdate(change.position)
                    ControlTool.ERASER -> onEraserDrawUpdate(change.position)
                    else -> {}
                }
            }
        )
    }
}