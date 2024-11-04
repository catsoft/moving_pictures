package com.movingPictures.ui.screens.canvas

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movingPictures.data.GiftComposer
import com.movingPictures.data.dto.AddAction
import com.movingPictures.data.dto.CanvasState
import com.movingPictures.data.dto.DrawableItem
import com.movingPictures.data.dto.DrawableItemFactory
import com.movingPictures.data.dto.DrawableItemState
import com.movingPictures.data.dto.Frame
import com.movingPictures.data.dto.MoveCanvasAction
import com.movingPictures.data.dto.PenDrawableItem
import com.movingPictures.data.dto.Point
import com.movingPictures.data.generators.ArrowForestGenerator
import com.movingPictures.data.generators.Generator
import com.movingPictures.data.generators.MovingPicturesGenerator
import com.movingPictures.ui.screens.canvas.canvas.EraserActionController
import com.movingPictures.ui.screens.canvas.canvas.MoveActionController
import com.movingPictures.ui.screens.canvas.canvas.PenActionController
import com.movingPictures.ui.screens.canvas.widgets.ControllableState
import com.movingPictures.ui.screens.canvas.widgets.Shape
import com.movingPictures.ui.screens.canvas.widgets.activeOrIdle
import com.movingPictures.ui.screens.canvas.widgets.idleOrDisabled
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// todo need to split view model to states and use cases
@OptIn(ExperimentalCoroutinesApi::class)
class CanvasViewModel() : ViewModel() {
    val drawSettings: MutableStateFlow<DrawSettings> = MutableStateFlow(DrawSettings())

    val gifComposer = GiftComposer()
    val gifState = GifState(gifComposer)
    val gifPlayer = GifPlayer(gifComposer, gifState)

    val penController = PenActionController(this)
    val eraserController = EraserActionController(this)
    val moveController = MoveActionController(this)

    val previousFrame = gifState.previousFrame
    val currentFrame = gifState.currentFrame

    val currentTool: MutableStateFlow<ControlTool> = MutableStateFlow(ControlTool.PEN)
    val fullPalettePopup: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val layersPopup: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val settingsPopup: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val undoButtonState = currentFrame.flatMapLatest { it?.canUndo ?: MutableStateFlow(false) }.combineStateWithPlayState {
        it.idleOrDisabled()
    }
    val redoButtonState = currentFrame.flatMapLatest { it?.canRedo ?: MutableStateFlow(false) }.combineStateWithPlayState {
        it.idleOrDisabled()
    }

    val playButtonState = gifPlayer.playState.combine(gifPlayer.playerState) { playState, playerState ->
        when {
            playerState == PlayerState.NOT_READY -> ControllableState.DISABLED
            playState == PlayState.PLAYING -> ControllableState.ACTIVE
            else -> ControllableState.IDLE
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, ControllableState.DISABLED)
    val pauseButtonState = gifPlayer.playState.combine(gifPlayer.playerState) { playState, playerState ->
        when {
            playerState == PlayerState.NOT_READY -> ControllableState.DISABLED
            playState == PlayState.PAUSED -> ControllableState.DISABLED
            else -> ControllableState.IDLE
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, ControllableState.DISABLED)

    val deleteButtonState = gifComposer.frames.combineStateWithPlayState { (it.size > 1).idleOrDisabled() }
    val addNewButtonState = MutableStateFlow(ControllableState.IDLE).combineStateWithPlayState { it }
    val copyButtonState = MutableStateFlow(ControllableState.IDLE).combineStateWithPlayState { it }
    val layersButtonState = layersPopup.combineStateWithPlayState { it.activeOrIdle() }

    val moveButtonState = currentTool.combineStateWithPlayState { (it == ControlTool.MOVE).activeOrIdle() }
    val penButtonState = currentTool.combineStateWithPlayState { (it == ControlTool.PEN).activeOrIdle() }
    val brushButtonState = currentTool.combineStateWithPlayState { (it == ControlTool.BRUSH).activeOrIdle() }
    val eraserButtonState = currentTool.combineStateWithPlayState { (it == ControlTool.ERASER).activeOrIdle() }
    val colorButtonState = currentTool.combineStateWithPlayState { (it == ControlTool.COLOR_PICKER).activeOrIdle() }
    val editButtonState = currentTool.combineStateWithPlayState { (it == ControlTool.SHAPES).activeOrIdle() }
    val settingsButtonState = settingsPopup.combineStateWithPlayState { it.activeOrIdle() }

    init {
        gifComposer.addFrame(Frame())
        gifState.selectLastFrame()
    }

    fun selectTool(tool: ControlTool) {
        if (tool != ControlTool.COLOR_PICKER) {
            fullPalettePopup.value = false
        }
        currentTool.value = tool
    }

    fun addDrawable(drawableItem: DrawableItem<*>) {
        currentFrame.value?.applyAction(AddAction(drawableItem))
    }

    fun moveCanvas(offset: CanvasState) {
        currentFrame.value?.applyAction(MoveCanvasAction(offset))
    }

    fun undo() {
        currentFrame.value?.undo()
    }

    fun redo() {
        currentFrame.value?.redo()
    }

    fun updateSettings(action: (DrawSettings) -> DrawSettings) {
        drawSettings.value = action(drawSettings.value)
    }

    fun addNewFrame() {
        closeAllPopups()
        currentFrame.value ?: return
        val newFrame = Frame()
        val id = gifComposer.addFrame(newFrame, currentFrame.value!!.id)
        gifState.selectFrame(id)
    }

    fun copyFrame() {
        closeAllPopups()
        currentFrame.value ?: return
        val frame = currentFrame.value!!.composeFrame()
        val id = gifComposer.addFrame(frame, currentFrame.value!!.id)
        gifState.selectFrame(id)
    }

    fun deleteFrame() {
        closeAllPopups()
        currentFrame.value ?: return
        val previousId = previousFrame.value!!.id
        val id = currentFrame.value!!.id
        gifComposer.removeFrame(id)
        gifState.selectFrame(previousId)
    }

    fun play() {
        closeAllPopups()
        gifPlayer.play()
    }

    fun selectFrameFromList(id: String) {
        gifState.selectFrame(id)
        closeAllPopups()
    }

    fun pause() {
        gifPlayer.pause()
        gifState.selectLastFrame()
    }

    fun addShapes(shape: Shape) {
        val settings = drawSettings.value
        val drawableItem = when (shape) {
            Shape.LINE -> DrawableItemFactory.createLine(settings)
            Shape.RECTANGLE -> DrawableItemFactory.createSquare(settings)
            Shape.CIRCLE -> DrawableItemFactory.createCircle(settings)
            Shape.TRIANGLE -> DrawableItemFactory.createTriangle(settings)
            Shape.ARROW -> DrawableItemFactory.createArrow(settings)
        }
        addDrawable(drawableItem)
        // crunch for canvas update ???
        penController.drawable.value = PenDrawableItem(DrawableItemState(Point(0F, 0F), Color.Transparent.toArgb()), 0F, mutableListOf())
        viewModelScope.launch {
            //lolos
            delay(2)
            penController.drawable.value = null
        }
    }

    fun setCanvasSize(size: IntSize) {
        drawSettings.value = drawSettings.value.copy(centerX = size.width / 2F, centerY = size.height / 2F, shapeSize = size.width.toFloat() * 0.32F)
    }

    fun openSettings() {
        val newState = settingsPopup.value
        closeAllPopups()
        settingsPopup.value = newState.not()
    }

    fun startOver() {
        closeAllPopups()
        gifComposer.clear()
        gifComposer.addFrame(Frame())
        gifState.selectLastFrame()
    }

    fun generateArrowForest() = generate(ArrowForestGenerator(gifComposer))

    fun generateMovingPictures() = generate(MovingPicturesGenerator(gifComposer))

    private fun generate(generator: Generator) {
        closeAllPopups()
        viewModelScope.launch {
            generator.generate(drawSettings.value)
            gifState.selectLastFrame()
        }
    }

    private fun closeAllPopups() {
        fullPalettePopup.value = false
        layersPopup.value = false
        settingsPopup.value = false
    }

    private fun <T> Flow<T>.combineStateWithPlayState(getState: (T) -> ControllableState): StateFlow<ControllableState> {
        return combine(gifPlayer.playState) { first, second ->
            first to second
        }.map { (secondState, isPlaying) ->
            when {
                isPlaying == PlayState.PLAYING -> ControllableState.DISABLED
                else -> getState(secondState)
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, ControllableState.DISABLED)
    }
}