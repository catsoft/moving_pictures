package com.movingPictures.data.generators

import androidx.compose.ui.graphics.toArgb
import com.movingPictures.data.FrameComposer
import com.movingPictures.data.GiftComposer
import com.movingPictures.data.dto.AddAction
import com.movingPictures.data.dto.ArrowDrawableItem
import com.movingPictures.data.dto.CircleDrawableItem
import com.movingPictures.data.dto.DrawableItemState
import com.movingPictures.data.dto.Point
import com.movingPictures.ui.screens.canvas.DrawSettings

class ArrowForestGenerator(private val gifComposer: GiftComposer): Generator {

    override fun generate(settings: DrawSettings) {
        gifComposer.clear()

        val centerPosition = Point(settings.centerX, settings.centerY)
        val circle = CircleDrawableItem(
            state = DrawableItemState(position = centerPosition, color = settings.color.toArgb()),
            radius = settings.shapeSize / 2,
            width = settings.shapeWidth,
        )

        for (i in 0 until 10) {
            val frameComposer = FrameComposer()
            frameComposer.applyAction(AddAction(circle))

            val angleStep = 12f
            for (j in 0 until 5) {
                val size = settings.shapeSize / 4

                val angle = j * 72f + i * angleStep
                val endPosition = Point(
                    x = centerPosition.x + size * kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat(),
                    y = centerPosition.y + size * kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat()
                )
                val arrow = ArrowDrawableItem(
                    state = DrawableItemState(position = centerPosition, color = settings.color.toArgb()),
                    size = size,
                    width = settings.shapeWidth / 4
                )
                frameComposer.applyAction(AddAction(arrow.copyWithState(arrow.state.copy(position = endPosition))))
            }

            gifComposer.addFrame(frameComposer.composeFrame())
        }
    }
}