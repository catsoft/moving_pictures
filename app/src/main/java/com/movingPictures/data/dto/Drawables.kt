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

data class EraserDrawableItem(
    override val state: DrawableItemState,
    val radius: Int,
    val points: List<PointColors>,
) : DrawableItem<EraserDrawableItem> {
    override fun copyWithState(state: DrawableItemState): EraserDrawableItem = EraserDrawableItem(state, radius, points)
}

data class PenDrawableItem(
    override val state: DrawableItemState,
    // relative to startPoint
    val points: List<PointColors>,
) : DrawableItem<PenDrawableItem> {
    override fun copyWithState(state: DrawableItemState): PenDrawableItem = PenDrawableItem(state, points)
}

data class LineDrawableItem(
    override val state: DrawableItemState,
    // relative to startPoint
    val endPoint: Point,
) : DrawableItem<LineDrawableItem> {
    override fun copyWithState(state: DrawableItemState): LineDrawableItem = LineDrawableItem(state, endPoint)
}

data class CircleDrawableItem(
    override val state: DrawableItemState,
    val radius: Int,
) : DrawableItem<CircleDrawableItem> {
    override fun copyWithState(state: DrawableItemState): CircleDrawableItem = CircleDrawableItem(state, radius)
}

data class SquareDrawableItem(
    override val state: DrawableItemState,
    val size: Int,
) : DrawableItem<SquareDrawableItem> {
    override fun copyWithState(state: DrawableItemState): SquareDrawableItem = SquareDrawableItem(state, size)
}

data class TriangleDrawableItem(
    override val state: DrawableItemState,
    val size: Int,
) : DrawableItem<TriangleDrawableItem> {
    override fun copyWithState(state: DrawableItemState): TriangleDrawableItem = TriangleDrawableItem(state, size)
}

data class ArrowDrawableItem(
    override val state: DrawableItemState,
    val points: List<PointColors>,
) : DrawableItem<ArrowDrawableItem> {
    override fun copyWithState(state: DrawableItemState): ArrowDrawableItem = ArrowDrawableItem(state, points)
}