package com.movingPictures.ui.screens.canvas.canvas

import androidx.compose.ui.geometry.Offset
import com.movingPictures.data.dto.CanvasState
import com.movingPictures.ui.screens.canvas.CanvasViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MoveActionController(canvasViewModel: CanvasViewModel) {
    private var startPoint: Offset? = null

    val state = MutableStateFlow<CanvasState?>(null)

    val onDrawUpdate: (Offset) -> Unit = { offset ->
        if (startPoint == null) {
            startPoint = offset
        } else {
            state.value = CanvasState(offset.x - startPoint!!.x, offset.y - startPoint!!.y)
        }
    }
    val onCancel: () -> Unit = {
        state.value?.let {
            canvasViewModel.moveCanvas(state.value!!)
            startPoint = null
            state.value = null
        }
    }
}