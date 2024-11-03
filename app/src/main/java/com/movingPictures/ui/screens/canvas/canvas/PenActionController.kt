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
    val penDrawable = mutableStateOf<PenDrawableItem?>(null)

    val onPenDrawUpdate: (Offset) -> Unit = { offset ->
        if (penDrawable.value != null) {
            penDrawable.value = penDrawable.value!!.copy(
                points = penDrawable.value!!.points + PointColors(
                    offset.toPoint(penDrawable.value!!.state.position),
                    canvasViewModel.drawSettings.value.color.toArgb()
                )
            )
        } else {
            penDrawable.value = PenDrawableItem(
                DrawableItemState(
                    position = offset.toPoint(),
                    color = canvasViewModel.drawSettings.value.color.toArgb()
                ),
                canvasViewModel.drawSettings.value.penSize,
                mutableListOf(PointColors(offset.toPoint(offset.toPoint()), canvasViewModel.drawSettings.value.color.toArgb()))
            )
        }
    }
    val onPenCancel: () -> Unit = {
        penDrawable.value?.let {
            penDrawable.value = null
            canvasViewModel.addDrawable(it)
        }
    }
}