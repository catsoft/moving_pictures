package com.movingPictures.ui.screens.canvas

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movingPictures.data.FrameComposer
import com.movingPictures.data.GiftComposer
import com.movingPictures.data.dto.AddAction
import com.movingPictures.data.dto.ArrowDrawableItem
import com.movingPictures.data.dto.CircleDrawableItem
import com.movingPictures.data.dto.DrawableItem
import com.movingPictures.data.dto.DrawableItemState
import com.movingPictures.data.dto.Frame
import com.movingPictures.data.dto.LineDrawableItem
import com.movingPictures.data.dto.PenDrawableItem
import com.movingPictures.data.dto.Point
import com.movingPictures.data.dto.PointColors
import com.movingPictures.data.dto.SquareDrawableItem
import com.movingPictures.data.dto.TriangleDrawableItem
import com.movingPictures.ui.screens.canvas.widgets.ControllableState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class CanvasViewModel() : ViewModel() {
    val drawSettings: MutableStateFlow<DrawSettings> = MutableStateFlow(DrawSettings())

    val gifComposer = GiftComposer()

    val previousFrame: MutableStateFlow<FrameComposer?> = MutableStateFlow(null)
    val currentFrame: MutableStateFlow<FrameComposer?> = MutableStateFlow(null)

    val currentTool: MutableStateFlow<ControlTool> = MutableStateFlow(ControlTool.PEN)
    val fullPalette: MutableStateFlow<Boolean> = MutableStateFlow(false)

    //todo map refactor
    val undoButtonState = currentFrame.flatMapLatest { it?.canUndo ?: MutableStateFlow(false) }
        .map { if (it) ControllableState.IDLE else ControllableState.DISABLED }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ControllableState.DISABLED)
    val redoButtonState = currentFrame.flatMapLatest { it?.canRedo ?: MutableStateFlow(false) }
        .map { if (it) ControllableState.IDLE else ControllableState.DISABLED }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ControllableState.DISABLED)

    val playButtonState = MutableStateFlow(ControllableState.DISABLED)
    val pauseButtonState = MutableStateFlow(ControllableState.DISABLED)

    val deleteButtonState = gifComposer.canRemove.map { if (it) ControllableState.IDLE else ControllableState.DISABLED }
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
    val editButtonState = MutableStateFlow(ControllableState.IDLE)
    val colorButtonState = currentTool.map { it == ControlTool.COLOR_PICKER }
        .map { if (it) ControllableState.ACTIVE else ControllableState.IDLE }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ControllableState.IDLE)

    init {
        gifComposer.addFrame(Frame())
        selectFrame(gifComposer.frames.value.last().id)
    }

    fun selectFrame(frameId: String) {
        val index = gifComposer.frames.value.indexOfFirst { it.id == frameId }
        previousFrame.value =
            gifComposer.frames.value.getOrNull(index - 1) ?: if (gifComposer.frames.value.size > 1) gifComposer.frames.value.lastOrNull() else null
        currentFrame.value = gifComposer.frames.value.getOrNull(index)
    }

    fun selectLastFrame() {
        selectFrame(gifComposer.frames.value.last().id)
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
        selectLastFrame()
    }

    fun deleteFrame() {
        currentFrame.value ?: return
        gifComposer.removeFrame(currentFrame.value!!.id)
        selectLastFrame()
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

            it.applyAction(AddAction(CircleDrawableItem(DrawableItemState(Point(100f, 250f), purple), 40)))
            it.applyAction(AddAction(CircleDrawableItem(DrawableItemState(Point(250f, 250f), orange), 40)))

            it.applyAction(AddAction(SquareDrawableItem(DrawableItemState(Point(50f, 350f), red), 60)))
            it.applyAction(AddAction(SquareDrawableItem(DrawableItemState(Point(200f, 350f), green), 60)))

            it.applyAction(AddAction(TriangleDrawableItem(DrawableItemState(Point(100f, 450f), blue), 50)))
            it.applyAction(AddAction(TriangleDrawableItem(DrawableItemState(Point(250f, 450f), yellow), 50)))

            val arrowPoints1 = listOf(PointColors(Point(0f, 0f), purple), PointColors(Point(50f, 50f), purple))
            val arrowPoints2 = listOf(PointColors(Point(0f, 0f), orange), PointColors(Point(50f, -50f), orange))
            it.applyAction(AddAction(ArrowDrawableItem(DrawableItemState(Point(50f, 500f), purple), arrowPoints1)))
            it.applyAction(AddAction(ArrowDrawableItem(DrawableItemState(Point(200f, 500f), orange), arrowPoints2)))
        }
    }

}

