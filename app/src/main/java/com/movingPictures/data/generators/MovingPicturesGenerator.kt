package com.movingPictures.data.generators

import androidx.compose.ui.graphics.toArgb
import com.movingPictures.data.FrameComposer
import com.movingPictures.data.GiftComposer
import com.movingPictures.data.dto.AddAction
import com.movingPictures.data.dto.ArrowDrawableItem
import com.movingPictures.data.dto.CircleDrawableItem
import com.movingPictures.data.dto.DrawableItemState
import com.movingPictures.data.dto.LineDrawableItem
import com.movingPictures.data.dto.PenDrawableItem
import com.movingPictures.data.dto.Point
import com.movingPictures.data.dto.PointColors
import com.movingPictures.data.dto.RotateAction
import com.movingPictures.data.dto.TriangleDrawableItem
import com.movingPictures.ui.screens.canvas.DrawSettings
import java.util.UUID
import kotlin.math.cos
import kotlin.math.sin

class MovingPicturesGenerator(private val gifComposer: GiftComposer) : Generator {

    override fun generate(settings: DrawSettings) {
        gifComposer.clear()

        val centerPosition = Point(settings.centerX, settings.centerY)
        val circle = CircleDrawableItem(
            state = DrawableItemState(position = centerPosition, color = settings.color.toArgb()),
            radius = settings.shapeSize,
            width = settings.shapeWidth,
        )

        val triangle = TriangleDrawableItem(
            state = DrawableItemState(position = centerPosition, color = settings.color.toArgb()),
            size = settings.shapeSize / 4,
            width = settings.shapeSize / 4,
        )

        for (i in 0 until 10) {
            val frameComposer = FrameComposer()
            frameComposer.applyAction(AddAction(circle.copy()))
            frameComposer.applyAction(AddAction(triangle.copy()))

            val angleStep = 8F
            for (j in 0 until 8) {
                val pulse = if ((j + i) % 2 == 0) 0 else 1
                val size = settings.shapeSize / 4
                val pulseValue = pulse * size / 5
                val offset = circle.radius / 2 + size / 2 + pulseValue

                val angle = j * (360 / 8) + i * angleStep
                val endPosition = getPointOnCircle(centerPosition, offset, angle)
                val arrow = ArrowDrawableItem(
                    state = DrawableItemState(position = endPosition, color = settings.color.toArgb()),
                    size = size,
                    width = settings.shapeWidth
                )

                val pointPosition = getPointOnCircle(centerPosition, offset + size / 2 + 16, angle)
                val point = PenDrawableItem(
                    state = DrawableItemState(position = pointPosition, color = settings.color.toArgb()),
                    radius = settings.shapeWidth * 2,
                    points = mutableListOf(PointColors(Point(0F, 0F), settings.color.toArgb()))
                )

                val legsPosition = getPointOnCircle(centerPosition, offset - size / 2, angle + 90)
                val leftLine = LineDrawableItem(
                    state = DrawableItemState(position = legsPosition, color = settings.color.toArgb()),
                    width = settings.shapeWidth,
                    endPoint = Point(settings.shapeSize / 13, settings.shapeSize / 13)
                )
                val rightLine = leftLine.copyWithState(leftLine.state.copy(id = UUID.randomUUID().toString()))

                frameComposer.applyAction(AddAction(arrow))
                frameComposer.applyAction(RotateAction(arrow.id, angle))
                frameComposer.applyAction(AddAction(point))
                frameComposer.applyAction(AddAction(leftLine))
                frameComposer.applyAction(RotateAction(leftLine.id, angle + 90))
                frameComposer.applyAction(AddAction(rightLine))
                frameComposer.applyAction(RotateAction(rightLine.id, angle - 180))
            }

            gifComposer.addFrame(frameComposer.composeFrame())
        }
    }
}

fun getPointOnCircle(center: Point, radius: Float, angle: Float): Point {
    val angleInRadians = Math.toRadians(angle.toDouble())
    val x = center.x + radius * cos(angleInRadians)
    val y = center.y + radius * sin(angleInRadians)
    return Point(x.toFloat(), y.toFloat())
}