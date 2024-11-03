package com.movingPictures.ui.screens.canvas.canvas

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.toArgb
import com.movingPictures.compose.toPoint
import com.movingPictures.data.dto.DrawableItemState
import com.movingPictures.data.dto.EraserDrawableItem
import com.movingPictures.data.dto.PointColors
import com.movingPictures.ui.screens.canvas.CanvasViewModel

class EraserActionController(canvasViewModel: CanvasViewModel) {
    val eraserDrawable = mutableStateOf<EraserDrawableItem?>(null)

    val onEraserDrawUpdate: (Offset) -> Unit = { offset ->
        if (eraserDrawable.value != null) {
            eraserDrawable.value = eraserDrawable.value!!.copy(
                points = eraserDrawable.value!!.points + PointColors(
                    offset.toPoint(eraserDrawable.value!!.state.position),
                    canvasViewModel.drawSettings.value.eraseColor.toArgb()
                )
            )
        } else {
            eraserDrawable.value = EraserDrawableItem(
                DrawableItemState(
                    position = offset.toPoint(),
                    color = canvasViewModel.drawSettings.value.eraseColor.toArgb()
                ),
                canvasViewModel.drawSettings.value.eraseSize,
                mutableListOf(PointColors(offset.toPoint(offset.toPoint()), canvasViewModel.drawSettings.value.eraseColor.toArgb()))
            )
        }
    }

    val onEraserCancel: () -> Unit = {
        eraserDrawable.value?.let {
            eraserDrawable.value = null
            canvasViewModel.addDrawable(it)
        }
    }
}