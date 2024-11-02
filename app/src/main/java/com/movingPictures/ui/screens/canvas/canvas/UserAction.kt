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
import com.movingPictures.data.dto.PenDrawableItem
import com.movingPictures.data.dto.PointColors
import com.movingPictures.ui.screens.canvas.CanvasViewModel

@Composable
fun Modifier.userDraw(viewModel: CanvasViewModel, item: MutableState<PenDrawableItem?>): Modifier {
    val drawSettings = viewModel.drawSettings.collectAsState()

    val drawUpdate: (Offset) -> Unit = { offset ->
        if (item.value != null) {
            item.value = item.value!!.copy(
                points = item.value!!.points + PointColors(offset.toPoint(item.value!!.state.position), drawSettings.value.color.toArgb())
            )
        } else {
            item.value = PenDrawableItem(
                DrawableItemState(
                    position = offset.toPoint(),
                    color = drawSettings.value.color.toArgb()
                ),
                drawSettings.value.width,
                mutableListOf(PointColors(offset.toPoint(offset.toPoint()), drawSettings.value.color.toArgb()))
            )
        }
    }
    val onCancel: () -> Unit = {
        item.value?.let {
            item.value = null
            viewModel.addDrawable(it)
        }
    }

    return pointerInput(Unit) {
        detectDragGestures(
            onDragStart = { offset -> drawUpdate(offset) },
            onDragEnd = { onCancel() },
            onDragCancel = { onCancel() },
            onDrag = { change, dragAmount ->
                change.consume()
                drawUpdate(change.position)
            }
        )
    }
}