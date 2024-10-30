package com.movingPictures.data

import java.util.UUID

open class DrawableItem(
    val position: Point,
    val color: Int,
    val rotation: Float = 0F,
    val scale: Float = 1F,
    val id: String = UUID.randomUUID().toString(),
)

class EraserDrawableItem(
    position: Point,
    color: Int,
    val radius: Int,
    val points: MutableList<PointColors>,
) : DrawableItem(position = position, color = color)

class PenDrawableItem(
    startPoint: Point,
    color: Int,
    val radius: Int,
    // relative to startPoint
    val points: MutableList<PointColors>,
) : DrawableItem(position = startPoint, color = color)

class LineDrawableItem(
    startPoint: Point,
    color: Int,
    // relative to startPoint
    val endPoint: Point,
) : DrawableItem(position = startPoint, color = color)

class CircleDrawableItem(
    center: Point,
    color: Int,
    val radius: Int,
) : DrawableItem(position = center, color = color)

class SquareDrawableItem(
    position: Point,
    color: Int,
    val size: Int,
) : DrawableItem(position = position, color = color)

class TriangleDrawableItem(
    position: Point,
    color: Int,
    val size: Int,
) : DrawableItem(position = position, color = color)

class ArrowDrawableItem(
    position: Point,
    color: Int,
    val points: MutableList<PointColors>,
) : DrawableItem(position = position, color = color)