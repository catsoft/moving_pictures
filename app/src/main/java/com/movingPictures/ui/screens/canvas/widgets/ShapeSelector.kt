package com.movingPictures.ui.screens.canvas.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.movingPictures.compose.MPIcons
import com.movingPictures.ui.screens.canvas.CanvasViewModel

enum class Shape {
    LINE,
    RECTANGLE,
    CIRCLE,
    TRIANGLE,
    ARROW
}

@Composable
fun BoxScope.ShapeSelector(
    modifier: Modifier = Modifier,
    show: Boolean,
    viewModel: CanvasViewModel,
) {
    Popup(modifier, isShown = show) {
        Box(
            modifier = Modifier
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                val lineState = remember { ControllableState.IDLE }
                ControllableIcon(Modifier, lineState, clickAction = { viewModel.addShapes(Shape.LINE) }) {
                    MPIcons.IcLine(smallIconModifier)
                }

                val quadState = remember { ControllableState.IDLE }
                ControllableIcon(Modifier, quadState, clickAction = { viewModel.addShapes(Shape.RECTANGLE) }) {
                    MPIcons.IcSquare(smallIconModifier)
                }

                val circleState = remember { ControllableState.IDLE }
                ControllableIcon(Modifier, circleState, clickAction = { viewModel.addShapes(Shape.CIRCLE) }) {
                    MPIcons.IcCircle(smallIconModifier)
                }

                val triangleState = remember { ControllableState.IDLE }
                ControllableIcon(Modifier, triangleState, clickAction = { viewModel.addShapes(Shape.TRIANGLE) }) {
                    MPIcons.IcTriangle(smallIconModifier)
                }

                val arrowState = remember { ControllableState.IDLE }
                ControllableIcon(Modifier, arrowState, clickAction = { viewModel.addShapes(Shape.ARROW) }) {
                    MPIcons.IcArrowUp(smallIconModifier)
                }
            }
        }
    }
}