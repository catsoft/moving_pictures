package com.movingPictures.ui.screens.canvas

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movingPictures.data.FrameComposer
import com.movingPictures.data.GiftComposer
import com.movingPictures.data.dto.AddAction
import com.movingPictures.data.dto.CircleDrawableItem
import com.movingPictures.data.dto.DrawableItem
import com.movingPictures.data.dto.DrawableItemFactory
import com.movingPictures.data.dto.DrawableItemState
import com.movingPictures.data.dto.Frame
import com.movingPictures.data.dto.LineDrawableItem
import com.movingPictures.data.dto.PenDrawableItem
import com.movingPictures.data.dto.Point
import com.movingPictures.data.dto.PointColors
import com.movingPictures.data.dto.SquareDrawableItem
import com.movingPictures.data.dto.TriangleDrawableItem
import com.movingPictures.ui.screens.canvas.widgets.ControllableState
import com.movingPictures.ui.screens.canvas.widgets.Shape
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CanvasViewModel() : ViewModel() {
    val drawSettings: MutableStateFlow<DrawSettings> = MutableStateFlow(DrawSettings())

    val gifComposer = GiftComposer()
    val gifState = GifState(gifComposer)
    val gifPlayer = GifPlayer(gifComposer, gifState)

    val previousFrame = gifState.previousFrame
    val currentFrame = gifState.currentFrame

    val currentTool: MutableStateFlow<ControlTool> = MutableStateFlow(ControlTool.PEN)
    val fullPalette: MutableStateFlow<Boolean> = MutableStateFlow(false)

    //todo map refactor
    val undoButtonState = currentFrame.flatMapLatest { it?.canUndo ?: MutableStateFlow(false) }
        .map { if (it) ControllableState.IDLE else ControllableState.DISABLED }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ControllableState.DISABLED)
    val redoButtonState = currentFrame.flatMapLatest { it?.canRedo ?: MutableStateFlow(false) }
        .map { if (it) ControllableState.IDLE else ControllableState.DISABLED }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ControllableState.DISABLED)

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

    val deleteButtonState = gifComposer.frames.map { if (it.size > 1) ControllableState.IDLE else ControllableState.DISABLED }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ControllableState.DISABLED)
    val addButtonState = MutableStateFlow(ControllableState.IDLE)
    val layersButtonState = MutableStateFlow(ControllableState.IDLE)

    val penButtonState = currentTool.map { it == ControlTool.PEN }
        .map { if (it) ControllableState.ACTIVE else ControllableState.IDLE }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ControllableState.IDLE)
    val brushButtonState = currentTool.map { it == ControlTool.BRUSH }
        .map { if (it) ControllableState.ACTIVE else ControllableState.IDLE }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ControllableState.IDLE)
    val eraserButtonState = currentTool.map { it == ControlTool.ERASER }
        .map { if (it) ControllableState.ACTIVE else ControllableState.IDLE }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ControllableState.IDLE)
    val editButtonState = currentTool.map { it == ControlTool.SHAPES }
        .map { if (it) ControllableState.ACTIVE else ControllableState.IDLE }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ControllableState.IDLE)
    val colorButtonState = currentTool.map { it == ControlTool.COLOR_PICKER }
        .map { if (it) ControllableState.ACTIVE else ControllableState.IDLE }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ControllableState.IDLE)

    init {
        gifComposer.addFrame(Frame())
        gifState.selectLastFrame()
    }

    fun selectTool(tool: ControlTool) {
        if (tool != ControlTool.COLOR_PICKER) {
            fullPalette.value = false
        }
        currentTool.value = tool
    }


    fun addDrawable(drawableItem: DrawableItem<*>) {
        currentFrame.value?.applyAction(AddAction(drawableItem))
    }

    fun undo() {
        currentFrame.value?.undo()
    }

    fun redo() {
        currentFrame.value?.redo()
    }

    fun selectColor(color: Color) {
        drawSettings.value = drawSettings.value.copy(color = color)
    }

    fun setPenSize(value: Float) {
        drawSettings.value = drawSettings.value.copy(penSize = value)
    }

    fun setEraseSize(value: Float) {
        drawSettings.value = drawSettings.value.copy(eraseSize = value)
    }

    fun addNewFrame() {
        currentFrame.value ?: return
        gifComposer.addFrame(currentFrame.value!!.composeFrame())
        gifState.selectLastFrame()
    }

    fun deleteFrame() {
        currentFrame.value ?: return
        gifComposer.removeFrame(currentFrame.value!!.id)
        gifState.selectLastFrame()
    }

    fun play() = gifPlayer.play()

    fun pause() = gifPlayer.pause()

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
    }

    fun setCanvasSize(size: IntSize) {
        drawSettings.value = drawSettings.value.copy(centerX = size.width / 2F, centerY = size.height / 2F, shapeSize = size.width.toFloat() * 0.2F)
    }

    fun doTest(frame: FrameComposer?) {
        frame?.let {
            val red = Color.Red.toArgb()
            val blue = Color.Blue.toArgb()
            val green = Color.Green.toArgb()
            val yellow = Color.Yellow.toArgb()

            it.applyAction(AddAction(LineDrawableItem(DrawableItemState(Point(50f, 50f), red), 5F, Point(150f, 50f))))
            it.applyAction(AddAction(LineDrawableItem(DrawableItemState(Point(150f, 50f), blue), 5F, Point(150f, 150f))))
            it.applyAction(AddAction(LineDrawableItem(DrawableItemState(Point(150f, 150f), green), 5F, Point(50f, 150f))))
            it.applyAction(AddAction(LineDrawableItem(DrawableItemState(Point(50f, 150f), yellow), 5F, Point(50f, 50f))))
        }
    }

    fun doTest2(frame: FrameComposer?) {
        frame?.let {
            // Параметры цветов
            val red = Color.Red.toArgb()
            val blue = Color.Blue.toArgb()
            val green = Color.Green.toArgb()
            val yellow = Color.Yellow.toArgb()
            val purple = Color.Magenta.toArgb()
            val orange = Color(0xFFFFA500).toArgb()

            val penPoints1 = listOf(PointColors(Point(50f, 50f), red))
            val penPoints2 = listOf(PointColors(Point(200f, 50f), blue))
            it.applyAction(AddAction(PenDrawableItem(DrawableItemState(Point(50f, 50f), red), 5f, penPoints1)))
            it.applyAction(AddAction(PenDrawableItem(DrawableItemState(Point(200f, 50f), blue), 5f, penPoints2)))

            it.applyAction(AddAction(LineDrawableItem(DrawableItemState(Point(50f, 150f), green), 5F, Point(150f, 150f))))
            it.applyAction(AddAction(LineDrawableItem(DrawableItemState(Point(200f, 150f), yellow), 5F, Point(300f, 150f))))

            it.applyAction(AddAction(CircleDrawableItem(DrawableItemState(Point(100f, 250f), purple), 40F)))
            it.applyAction(AddAction(CircleDrawableItem(DrawableItemState(Point(250f, 250f), orange), 40F)))

            it.applyAction(AddAction(SquareDrawableItem(DrawableItemState(Point(50f, 350f), red), 60F)))
            it.applyAction(AddAction(SquareDrawableItem(DrawableItemState(Point(200f, 350f), green), 60F)))

            it.applyAction(AddAction(TriangleDrawableItem(DrawableItemState(Point(100f, 450f), blue), 50F)))
            it.applyAction(AddAction(TriangleDrawableItem(DrawableItemState(Point(250f, 450f), yellow), 50F)))
        }
    }
}