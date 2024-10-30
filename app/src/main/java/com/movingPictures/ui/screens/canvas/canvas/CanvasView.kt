package com.movingPictures.ui.screens.canvas.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.movingPictures.data.FrameComposer
import com.movingPictures.data.dto.*
import com.movingPictures.ui.screens.canvas.CanvasViewModel
import com.movingpictures.R

@Composable
fun CanvasView(modifier: Modifier = Modifier, viewModel: CanvasViewModel) {

    val previousFrame = viewModel.previousFrame.collectAsState()
    val currentFrame = viewModel.currentFrame.collectAsState()

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.canvas),
            contentDescription = "Canvas",
            modifier = Modifier.fillMaxSize()
        )

        if (previousFrame.value != null) {
            DrawFrame(Modifier.alpha(0.5f), previousFrame.value!!)
        }

        if (currentFrame.value != null) {
            DrawFrame(Modifier, currentFrame.value!!)
        }
    }
}

@Composable
private fun DrawFrame(modifier: Modifier = Modifier, composer: FrameComposer) {
    val frameState = composer.drawableState.collectAsState()
    Canvas(modifier.fillMaxSize()) {
        for (drawable in frameState.value) {
            withTransform({
                translate(left = drawable.state.position.x, top = drawable.state.position.y)
                rotate(drawable.state.rotation)
            }) {
                when (drawable) {
                    is PenDrawableItem -> drawPen(drawable)
                    is EraserDrawableItem -> drawEraser(drawable)
                    is LineDrawableItem -> drawLine(drawable)
                    is CircleDrawableItem -> drawCircle(drawable)
                    is SquareDrawableItem -> drawSquare(drawable)
                    is TriangleDrawableItem -> drawTriangle(drawable)
                    is ArrowDrawableItem -> drawArrow(drawable)
                    else -> {
                        throw IllegalArgumentException("Unknown drawable type: ${drawable::class.java.simpleName}")
                    }
                }
            }
        }
    }
}


private fun DrawScope.drawPen(drawable: PenDrawableItem) {
    for (pointColor in drawable.points) {
        drawCircle(
            color = Color(pointColor.color),
            radius = drawable.radius,
            center = Offset(pointColor.point.x, pointColor.point.y)
        )
    }
}

private fun DrawScope.drawEraser(drawable: EraserDrawableItem) {
    for (pointColor in drawable.points) {
        drawCircle(
            color = Color.White,
            radius = drawable.radius,
            center = Offset(pointColor.point.x, pointColor.point.y)
        )
    }
}

private fun DrawScope.drawLine(drawable: LineDrawableItem) {
    drawLine(
        color = Color(drawable.state.color),
        start = Offset(drawable.state.position.x, drawable.state.position.y),
        end = Offset(drawable.endPoint.x, drawable.endPoint.y),
        strokeWidth = drawable.width
    )
}

private fun DrawScope.drawCircle(drawable: CircleDrawableItem) {
    drawCircle(
        color = Color(drawable.state.color),
        radius = drawable.radius.toFloat(),
        center = Offset(drawable.state.position.x, drawable.state.position.y)
    )
}

private fun DrawScope.drawSquare(drawable: SquareDrawableItem) {
    drawRect(
        color = Color(drawable.state.color),
        topLeft = Offset(drawable.state.position.x, drawable.state.position.y),
        size = Size(drawable.size.toFloat(), drawable.size.toFloat())
    )
}

private fun DrawScope.drawTriangle(drawable: TriangleDrawableItem) {
    val halfSize = drawable.size / 2f
    val path = androidx.compose.ui.graphics.Path().apply {
        moveTo(drawable.state.position.x, drawable.state.position.y - halfSize)
        lineTo(drawable.state.position.x - halfSize, drawable.state.position.y + halfSize)
        lineTo(drawable.state.position.x + halfSize, drawable.state.position.y + halfSize)
        close()
    }
    drawPath(path = path, color = Color(drawable.state.color))
}

private fun DrawScope.drawArrow(drawable: ArrowDrawableItem) {
    if (drawable.points.size < 2) return
    val (start, end) = drawable.points.let { it.first().point to it.last().point }
    drawLine(
        color = Color(drawable.state.color),
        start = Offset(start.x, start.y),
        end = Offset(end.x, end.y),
        strokeWidth = 5f
    )
    val arrowSize = 10f
    val angle = kotlin.math.atan2(end.y - start.y, end.x - start.x)
    drawLine(
        color = Color(drawable.state.color),
        start = Offset(end.x, end.y),
        end = Offset(
            end.x - arrowSize * kotlin.math.cos(angle - Math.PI / 6).toFloat(),
            end.y - arrowSize * kotlin.math.sin(angle - Math.PI / 6).toFloat()
        ),
        strokeWidth = 5f
    )
    drawLine(
        color = Color(drawable.state.color),
        start = Offset(end.x, end.y),
        end = Offset(
            end.x - arrowSize * kotlin.math.cos(angle + Math.PI / 6).toFloat(),
            end.y - arrowSize * kotlin.math.sin(angle + Math.PI / 6).toFloat()
        ),
        strokeWidth = 5f
    )
}