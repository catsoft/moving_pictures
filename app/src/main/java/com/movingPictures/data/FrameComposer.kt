package com.movingPictures.data

import com.movingPictures.data.dto.Action
import com.movingPictures.data.dto.AddAction
import com.movingPictures.data.dto.CanvasState
import com.movingPictures.data.dto.ChangeColorAction
import com.movingPictures.data.dto.DrawableItem
import com.movingPictures.data.dto.DrawableItemState
import com.movingPictures.data.dto.Frame
import com.movingPictures.data.dto.MoveAction
import com.movingPictures.data.dto.MoveCanvasAction
import com.movingPictures.data.dto.RemoveAction
import com.movingPictures.data.dto.RotateAction
import com.movingPictures.data.dto.ScaleAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.UUID

class FrameComposer(val initFrame: Frame = Frame()) {
    private val scope = CoroutineScope(Dispatchers.Default)

    val id: String = UUID.randomUUID().toString()
    var number: Long = initFrame.number + 1

    val duration = initFrame.duration
    private var historyPosition: MutableStateFlow<Int> = MutableStateFlow(initFrame.history.size - 1)
    private val history = initFrame.history.toMutableList()
    private var state = initFrame.currentState.toMutableList()

    private val _drawableState = MutableSharedFlow<List<DrawableItem<*>>>(replay = 1)
    val drawableState: SharedFlow<List<DrawableItem<*>>> = _drawableState

    private var _canvasState = initFrame.canvasState
    val canvasState = MutableStateFlow(_canvasState)

    val canUndo: StateFlow<Boolean> = historyPosition.map { it > -1 }.stateIn(scope, SharingStarted.Eagerly, false)
    val canRedo: StateFlow<Boolean> = historyPosition.map { it < history.size - 1 }.stateIn(scope, SharingStarted.Eagerly, false)

    init {
        invalidateHistory()
    }

    fun undo() {
        if (historyPosition.value == -1) return
        historyPosition.value--
        invalidateHistory()
    }

    fun redo() {
        if (historyPosition.value >= history.size) return
        historyPosition.value++
        invalidateHistory()
    }

    private fun invalidateHistory() {
        state = mutableListOf()
        _canvasState = CanvasState()
        history.take(historyPosition.value + 1).forEach { applyActionImpl(it) }
        _drawableState.tryEmit(state)
        canvasState.value = _canvasState
    }

    fun applyAction(action: Action) {
        applyActionImpl(action)
        if (historyPosition.value < history.size - 1) {
            history.subList(historyPosition.value + 1, history.size).clear()
        }
        history.add(action)
        historyPosition.value++
        _drawableState.tryEmit(state)
        canvasState.value = _canvasState
    }

    private fun applyActionImpl(action: Action) {
        when (action) {
            is AddAction -> applyAddAction(action)
            is MoveAction -> applyMoveAction(action)
            is RemoveAction -> applyRemoveAction(action)
            is ChangeColorAction -> applyChangeColorAction(action)
            is RotateAction -> applyRotateAction(action)
            is ScaleAction -> applyScaleAction(action)
            is MoveCanvasAction -> moveCanvas(action)
        }
    }

    fun composeFrame(): Frame = Frame(duration, number, history, state, _canvasState, id)

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

    private fun moveCanvas(offset: MoveCanvasAction) {
        _canvasState = CanvasState(_canvasState.offsetX + offset.newOffset.offsetX, _canvasState.offsetY + offset.newOffset.offsetY)
    }

    private fun replaceDrawable(drawableId: String, newDrawable: DrawableItem<*>) {
        val index = state.indexOfFirst { it.id == drawableId }
        state[index] = newDrawable
    }
}

