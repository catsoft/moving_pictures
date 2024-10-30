package com.movingPictures.data.dto

open class Action()

data class AddAction(
    val item: DrawableItem<*>,
) : Action()

data class MoveAction(
    val drawableId: String,
    val newPosition: Point,
) : Action()

data class RemoveAction(
    val drawableId: String,
) : Action()

data class ChangeColorAction(
    val drawableId: String,
    val newColor: Int,
) : Action()

data class RotateAction(
    val drawableId: String,
    val rotationAngle: Float,
) : Action()

data class ScaleAction(
    val drawableId: String,
    val newScale: Float,
) : Action()