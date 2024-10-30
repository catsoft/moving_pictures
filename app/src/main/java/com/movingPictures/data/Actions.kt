package com.movingPictures.data

open class Action()

class AddAction(
    val item: DrawableItem,
) : Action()

class MoveAction(
    val drawableId: String,
    val newPosition: Point,
) : Action()

class RemoveAction(
    val drawableId: String,
) : Action()

class ChangeColorAction(
    val drawableId: String,
    val newColor: Int,
) : Action()

class RotateAction(
    val drawableId: String,
    val rotationAngle: Float,
) : Action()

class ScaleAction(
    val drawableId: String,
    val newScale: Float,
) : Action()