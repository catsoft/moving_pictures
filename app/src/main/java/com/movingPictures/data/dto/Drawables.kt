package com.movingPictures.data.dto

import java.util.UUID

interface DrawableItem<T> {
    val state: DrawableItemState
    val id get() = state.id

    fun copyWithState(state: DrawableItemState): DrawableItem<T>
}

data class DrawableItemState(
    val position: Point,
    val color: Int,
    val rotation: Float = 0F,
    val scale: Float = 1F,
    val id: String = UUID.randomUUID().toString(),
)

data class PenDrawableItem(
    override val state: DrawableItemState,
    val radius: Float = 5F,
    // relative to startPoint
    val points: List<PointColors>,
) : DrawableItem<PenDrawableItem> {
    override fun copyWithState(state: DrawableItemState): PenDrawableItem = PenDrawableItem(state.copy(id = UUID.randomUUID().toString()), radius, points)
}

data class EraserDrawableItem(
    override val state: DrawableItemState,
    val radius: Float = 5F,
    val points: List<PointColors>,
) : DrawableItem<EraserDrawableItem> {
    override fun copyWithState(state: DrawableItemState): EraserDrawableItem = EraserDrawableItem(state.copy(id = UUID.randomUUID().toString()), radius, points)
}

data class LineDrawableItem(
    override val state: DrawableItemState,
    val width: Float = 5F,
    // relative to startPoint
    val endPoint: Point,
) : DrawableItem<LineDrawableItem> {
    override fun copyWithState(state: DrawableItemState): LineDrawableItem = LineDrawableItem(state.copy(id = UUID.randomUUID().toString()), width, endPoint)
}

data class CircleDrawableItem(
    override val state: DrawableItemState,
    val radius: Float,
    val width: Float,
) : DrawableItem<CircleDrawableItem> {
    override fun copyWithState(state: DrawableItemState): CircleDrawableItem = CircleDrawableItem(state.copy(id = UUID.randomUUID().toString()), radius, width)
}

data class SquareDrawableItem(
    override val state: DrawableItemState,
    val size: Float,
    val width: Float,
) : DrawableItem<SquareDrawableItem> {
    override fun copyWithState(state: DrawableItemState): SquareDrawableItem = SquareDrawableItem(state.copy(id = UUID.randomUUID().toString()), size, width)
}

data class TriangleDrawableItem(
    override val state: DrawableItemState,
    val size: Float,
    val width: Float,
) : DrawableItem<TriangleDrawableItem> {
    override fun copyWithState(state: DrawableItemState): TriangleDrawableItem = TriangleDrawableItem(state.copy(id = UUID.randomUUID().toString()), size, width)
}

data class ArrowDrawableItem(
    override val state: DrawableItemState,
    val size: Float,
    val width: Float,
) : DrawableItem<ArrowDrawableItem> {
    override fun copyWithState(state: DrawableItemState): ArrowDrawableItem = ArrowDrawableItem(state.copy(id = UUID.randomUUID().toString()), size, width)
}