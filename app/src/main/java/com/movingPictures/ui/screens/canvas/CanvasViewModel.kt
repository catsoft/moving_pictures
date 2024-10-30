package com.movingPictures.ui.screens.canvas

import kotlinx.coroutines.flow.MutableStateFlow

class CanvasViewModel() {
    val undoButtonState = MutableStateFlow(ControllableState.DISABLED)
    val redoButtonState = MutableStateFlow(ControllableState.DISABLED)

    val playButtonState = MutableStateFlow(ControllableState.DISABLED)
    val pauseButtonState = MutableStateFlow(ControllableState.DISABLED)

    val deleteButtonState = MutableStateFlow(ControllableState.IDLE)
    val addButtonState = MutableStateFlow(ControllableState.IDLE)
    val layersButtonState = MutableStateFlow(ControllableState.IDLE)

    val penButtonState = MutableStateFlow(ControllableState.IDLE)
    val brushButtonState = MutableStateFlow(ControllableState.IDLE)
    val eraserButtonState = MutableStateFlow(ControllableState.IDLE)
    val editButtonState = MutableStateFlow(ControllableState.IDLE)
    val colorButtonState = MutableStateFlow(ControllableState.ACTIVE)

}