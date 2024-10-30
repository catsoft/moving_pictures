package com.movingPictures.data

data class Gif(val frames: MutableList<Frame>)

class Frame(
    // in ms
    val duration: Long,
    val history: MutableList<Action>,
    val currentState: MutableList<DrawableItem>,
)

class Point(
    val x: Float,
    val y: Float,
)

class PointColors(
    val point: Point,
    val color: Int,
)