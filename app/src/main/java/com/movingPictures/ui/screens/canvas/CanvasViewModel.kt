package com.movingPictures.ui.screens.canvas

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.movingPictures.data.FrameComposer
import com.movingPictures.data.GiftComposer
import com.movingPictures.data.dto.AddAction
import com.movingPictures.data.dto.ArrowDrawableItem
import com.movingPictures.data.dto.CircleDrawableItem
import com.movingPictures.data.dto.DrawableItemState
import com.movingPictures.data.dto.Frame
import com.movingPictures.data.dto.LineDrawableItem
import com.movingPictures.data.dto.PenDrawableItem
import com.movingPictures.data.dto.Point
import com.movingPictures.data.dto.PointColors
import com.movingPictures.data.dto.SquareDrawableItem
import com.movingPictures.data.dto.TriangleDrawableItem
import kotlinx.coroutines.flow.MutableStateFlow

class CanvasViewModel() {
    val undoButtonState = MutableStateFlow(ControllableState.DISABLED)
    val redoButtonState = MutableStateFlow(ControllableState.DISABLED)

    val playButtonState = MutableStateFlow(ControllableState.DISABLED)
    val pauseButtonState = MutableStateFlow(ControllableState.DISABLED)

    val deleteButtonState = MutableStateFlow(ControllableState.IDLE)
    val addButtonState = MutableStateFlow(ControllableState.IDLE)
    val layersButtonState = MutableStateFlow(ControllableState.IDLE)

    val penButtonState = MutableStateFlow(ControllableState.IDLE)
    val brushButtonState = MutableStateFlow(ControllableState.IDLE)
    val eraserButtonState = MutableStateFlow(ControllableState.IDLE)
    val editButtonState = MutableStateFlow(ControllableState.IDLE)
    val colorButtonState = MutableStateFlow(ControllableState.ACTIVE)


    val gifComposer = GiftComposer()

    val previousFrame: MutableStateFlow<FrameComposer?> = MutableStateFlow(null)
    val currentFrame: MutableStateFlow<FrameComposer?> = MutableStateFlow(null)

    init {
        gifComposer.addFrame(Frame())
        selectFrame(gifComposer.frames.first().id)

        doTest()
        doTest2()
    }

    fun selectFrame(frameId: String) {
        val index = gifComposer.frames.indexOfFirst { it.id == frameId }
        previousFrame.value = gifComposer.frames.getOrNull(index - 1) ?: if (gifComposer.frames.size > 1) gifComposer.frames.lastOrNull() else null
        currentFrame.value = gifComposer.frames.getOrNull(index)
    }

    fun doTest() {
        currentFrame.value?.let {
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

    fun doTest2() {
        currentFrame.value?.let {
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