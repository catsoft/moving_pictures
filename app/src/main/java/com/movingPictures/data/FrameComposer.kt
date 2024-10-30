package com.movingPictures.data

import com.movingPictures.data.dto.Action
import com.movingPictures.data.dto.AddAction
import com.movingPictures.data.dto.ChangeColorAction
import com.movingPictures.data.dto.DrawableItem
import com.movingPictures.data.dto.DrawableItemState
import com.movingPictures.data.dto.Frame
import com.movingPictures.data.dto.MoveAction
import com.movingPictures.data.dto.RemoveAction
import com.movingPictures.data.dto.RotateAction
import com.movingPictures.data.dto.ScaleAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class FrameComposer(initFrame: Frame = Frame()) {
    val id: String = UUID.randomUUID().toString()

    private val duration = initFrame.duration
    private val history = initFrame.history.toMutableList()
    private val state = initFrame.currentState.toMutableList()

    private val _drawableState = MutableStateFlow(state)
    val drawableState: StateFlow<List<DrawableItem<*>>> = _drawableState

    // todo
    fun undo() {

    }

    // todo
    fun redo() {

    }

    fun applyAction(action: Action) {
        history.add(action)
        when (action) {
            is AddAction -> applyAddAction(action)
            is MoveAction -> applyMoveAction(action)
            is RemoveAction -> applyRemoveAction(action)
            is ChangeColorAction -> applyChangeColorAction(action)
            is RotateAction -> applyRotateAction(action)
            is ScaleAction -> applyScaleAction(action)
        }
        _drawableState.value = state
    }

    fun composeFrame(): Frame = Frame(duration, history, state)

    private fun applyAddAction(action: AddAction) {
        state.add(action.item)
    }

    private fun applyMoveAction(action: MoveAction) {
        doStateUpdate(action.drawableId) { it.copy(position = action.newPosition) }
    }

    private fun applyRemoveAction(action: RemoveAction) {
        state.remove(state.find { it.id == action.drawableId })
    }

    private fun applyChangeColorAction(action: ChangeColorAction) {
        doStateUpdate(action.drawableId) { it.copy(color = action.newColor) }
    }

    private fun applyRotateAction(action: RotateAction) {
        doStateUpdate(action.drawableId) { it.copy(rotation = action.rotationAngle) }
    }

    private fun applyScaleAction(action: ScaleAction) {
        doStateUpdate(action.drawableId) { it.copy(scale = action.newScale) }
    }

    private fun doStateUpdate(drawableId: String, update: (DrawableItemState) -> DrawableItemState) {
        val drawable = state.find { it.id == drawableId } ?: return
        val newDrawable = drawable.copyWithState(state = update(drawable.state))
        replaceDrawable(drawableId, newDrawable)
    }

    private fun replaceDrawable(drawableId: String, newDrawable: DrawableItem<*>) {
        val index = state.indexOfFirst { it.id == drawableId }
        state[index] = newDrawable
    }
}

