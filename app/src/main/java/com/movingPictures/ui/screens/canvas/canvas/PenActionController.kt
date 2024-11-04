package com.movingPictures.ui.screens.canvas.canvas

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.toArgb
import com.movingPictures.compose.toPoint
import com.movingPictures.data.dto.DrawableItemState
import com.movingPictures.data.dto.PenDrawableItem
import com.movingPictures.data.dto.PointColors
import com.movingPictures.ui.screens.canvas.CanvasViewModel

class PenActionController(canvasViewModel: CanvasViewModel) {
    val drawable = mutableStateOf<PenDrawableItem?>(null)

    val onDrawUpdate: (Offset) -> Unit = { offset ->
        if (drawable.value != null) {
            drawable.value = drawable.value!!.copy(
                points = drawable.value!!.points + PointColors(
                    offset.toPoint(drawable.value!!.state.position),
                    canvasViewModel.drawSettings.value.color.toArgb(),
                )
            )
        } else {
            drawable.value = PenDrawableItem(
                DrawableItemState(
                    position = offset.toPoint(),
                    color = canvasViewModel.drawSettings.value.color.toArgb()
                ),
                canvasViewModel.drawSettings.value.penSize,
                mutableListOf(PointColors(offset.toPoint(offset.toPoint()), canvasViewModel.drawSettings.value.color.toArgb()))
            )
        }
    }
    val onCancel: () -> Unit = {
        drawable.value?.let {
            drawable.value = null
            canvasViewModel.addDrawable(it)
        }
    }
}

