package com.movingPictures.data.dto

import java.util.UUID

data class Gif(val frames: List<Frame>)

data class Frame(
    // in ms
    val duration: Long = 5,
    val number: Long = 1,
    val history: MutableList<Action> = mutableListOf(),
    val currentState: MutableList<DrawableItem<*>> = mutableListOf(),
    val id: String = UUID.randomUUID().toString(),
)

data class Point(
    val x: Float,
    val y: Float,
)

data class PointColors(
    val point: Point,
    val color: Int,
)