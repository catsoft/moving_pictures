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
    val drawable = mutableStateOf<EraserDrawableItem?>(null)

    val onDrawUpdate: (Offset) -> Unit = { offset ->
        if (drawable.value != null) {
            drawable.value = drawable.value!!.copy(
                points = drawable.value!!.points + PointColors(
                    offset.toPoint(drawable.value!!.state.position),
                    canvasViewModel.drawSettings.value.eraseColor.toArgb()
                )
            )
        } else {
            drawable.value = EraserDrawableItem(
                DrawableItemState(
                    rotation = -90F,
                    position = offset.toPoint(),
                    color = canvasViewModel.drawSettings.value.eraseColor.toArgb()
                ),
                canvasViewModel.drawSettings.value.eraseSize,
                mutableListOf(PointColors(offset.toPoint(offset.toPoint()), canvasViewModel.drawSettings.value.eraseColor.toArgb()))
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