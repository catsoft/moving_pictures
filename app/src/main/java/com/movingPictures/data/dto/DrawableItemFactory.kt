package com.movingPictures.data.dto

import androidx.compose.ui.graphics.toArgb
import com.movingPictures.compose.withOffset
import com.movingPictures.ui.screens.canvas.DrawSettings

object DrawableItemFactory {
    fun createLine(drawSettings: DrawSettings): LineDrawableItem {
        val center = Point(drawSettings.centerX, drawSettings.centerY)
        val startPoint = Point(center.x, center.y - drawSettings.shapeSize / 2)
        val endPoint = Point(center.x, center.y + drawSettings.shapeSize / 2)
        return LineDrawableItem(DrawableItemState(startPoint, drawSettings.color.toArgb()), drawSettings.shapeWidth, endPoint.withOffset(startPoint))
    }

    fun createCircle(drawSettings: DrawSettings): CircleDrawableItem {
        val center = Point(drawSettings.centerX, drawSettings.centerY)
        return CircleDrawableItem(DrawableItemState(center, drawSettings.color.toArgb()), drawSettings.shapeSize / 2 * 0.7F, drawSettings.shapeWidth)
    }

    fun createSquare(drawSettings: DrawSettings): SquareDrawableItem {
        val center = Point(drawSettings.centerX, drawSettings.centerY)
        return SquareDrawableItem(DrawableItemState(center, drawSettings.color.toArgb()), drawSettings.shapeSize * 0.7F, drawSettings.shapeWidth)
    }

    fun createTriangle(drawSettings: DrawSettings): TriangleDrawableItem {
        val center = Point(drawSettings.centerX, drawSettings.centerY)
        return TriangleDrawableItem(DrawableItemState(center, drawSettings.color.toArgb()), drawSettings.shapeSize * 0.7F, drawSettings.shapeWidth)
    }

    fun createArrow(drawSettings: DrawSettings): ArrowDrawableItem {
        val center = Point(drawSettings.centerX, drawSettings.centerY)
        return ArrowDrawableItem(DrawableItemState(center, drawSettings.color.toArgb()), drawSettings.shapeSize * 0.7F, drawSettings.shapeWidth)
    }
}