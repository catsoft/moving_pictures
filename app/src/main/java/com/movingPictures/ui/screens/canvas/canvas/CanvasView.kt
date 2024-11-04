package com.movingPictures.ui.screens.canvas.canvas

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawTransform
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.movingPictures.data.FrameComposer
import com.movingPictures.data.dto.AddAction
import com.movingPictures.data.dto.ArrowDrawableItem
import com.movingPictures.data.dto.CircleDrawableItem
import com.movingPictures.data.dto.EraserDrawableItem
import com.movingPictures.data.dto.LineDrawableItem
import com.movingPictures.data.dto.PenDrawableItem
import com.movingPictures.data.dto.SquareDrawableItem
import com.movingPictures.data.dto.TriangleDrawableItem
import com.movingPictures.ui.screens.canvas.CanvasViewModel
import com.movingPictures.ui.screens.canvas.PlayState
import com.movingpictures.R
import onCurrentTool

// todo need objects in draw optimization
@Composable
fun CanvasView(modifier: Modifier = Modifier, viewModel: CanvasViewModel) {
    val previousFrame = viewModel.previousFrame.collectAsState()
    val currentFrame = viewModel.currentFrame.collectAsState()
    val playState = viewModel.gifPlayer.playState.collectAsState()

    Box(
        modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(20.dp))
                .then(onCurrentTool(viewModel))
                .onGloballyPositioned {
                    viewModel.setCanvasSize(it.size)
                },
            contentAlignment = Alignment.Center
        ) {
            // duplication for right size
            Image(
                painter = painterResource(R.drawable.canvas),
                contentDescription = "Canvas",
                contentScale = ContentScale.Fit,
                modifier = Modifier.wrapContentSize()
            )

            // z index for right drawing order
            if (previousFrame.value != null && playState.value == PlayState.PAUSED) {
                Log.d("CanvasView", "draw previousFrame")
                DrawFrame(
                    Modifier
                        .alpha(0.3f)
                        .zIndex(4F), previousFrame.value!!
                )
            }

            if (currentFrame.value != null) {
                Log.d("CanvasView", "draw currentFrame")
                val currentMoveAction = viewModel.moveController.state.collectAsState()
                DrawFrame(Modifier.zIndex(1F), currentFrame.value!!) {
                    currentMoveAction.value?.let {
                        translate(left = currentMoveAction.value?.offsetX ?: 0F, top = currentMoveAction.value?.offsetY ?: 0F)
                    }
                }
            }

            val currentUserDrawableItem = viewModel.penController.drawable
            if (currentUserDrawableItem.value != null) {
                Log.d("CanvasView", "draw userDrawableItem")
                val frameComposer = FrameComposer()
                frameComposer.applyAction(AddAction(currentUserDrawableItem.value!!))
                DrawFrame(Modifier.zIndex(2F), frameComposer)
            }

            val currentEraserDrawableItem = viewModel.eraserController.drawable
            if (currentEraserDrawableItem.value != null) {
                Log.d("CanvasView", "draw eraser")
                val frameComposer = FrameComposer()
                frameComposer.applyAction(AddAction(currentEraserDrawableItem.value!!))
                DrawFrame(Modifier.zIndex(3F), frameComposer)
            }

            // duplication for right clearing the canvas
            CanvasWithImage(Modifier.zIndex(4F))
        }
    }
}

@Composable
private fun BoxScope.DrawFrame(
    modifier: Modifier = Modifier,
    composer: FrameComposer,
    transformBlock: DrawTransform.() -> Unit = {}
) {
    val frameState = composer.drawableState.collectAsState(listOf())
    val canvasState = composer.canvasState.collectAsState()

    Canvas(
        modifier
            .matchParentSize()
    ) {
        withTransform({
            transformBlock()
            translate(left = canvasState.value.offsetX, top = canvasState.value.offsetY)
        }) {
            for (drawable in frameState.value) {
                withTransform({
                    translate(left = drawable.state.position.x, top = drawable.state.position.y)
                    // have to change +90F because of the android coordinate system
                    rotate(drawable.state.rotation + 90F, Offset(0F, 0F))
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
}


private fun DrawScope.drawPen(drawable: PenDrawableItem) {
    for (pointColor in drawable.points) {
        drawCircle(
            color = Color(pointColor.color),
            radius = drawable.radius,
            center = Offset(pointColor.point.x, pointColor.point.y)
        )
    }

    if (drawable.points.size < 2) return

    for (i in 0 until drawable.points.size - 1) {
        val startPoint = drawable.points[i]
        val endPoint = drawable.points[i + 1]

        drawLine(
            color = Color(startPoint.color),
            start = Offset(startPoint.point.x, startPoint.point.y),
            end = Offset(endPoint.point.x, endPoint.point.y),
            strokeWidth = drawable.radius * 2
        )
    }
}

private fun DrawScope.drawEraser(drawable: EraserDrawableItem) {
    for (pointColor in drawable.points) {
        drawCircle(
            color = Color(pointColor.color),
            radius = drawable.radius,
            center = Offset(pointColor.point.x, pointColor.point.y),
            blendMode = BlendMode.Clear
        )
    }

    if (drawable.points.size < 2) return

    for (i in 0 until drawable.points.size - 1) {
        val startPoint = drawable.points[i]
        val endPoint = drawable.points[i + 1]

        drawLine(
            color = Color(startPoint.color),
            start = Offset(startPoint.point.x, startPoint.point.y),
            end = Offset(endPoint.point.x, endPoint.point.y),
            strokeWidth = drawable.radius * 2,
            blendMode = BlendMode.Clear
        )
    }
}

private fun DrawScope.drawLine(drawable: LineDrawableItem) {
    drawLine(
        color = Color(drawable.state.color),
        start = Offset(0F, 0F),
        end = Offset(drawable.endPoint.x, drawable.endPoint.y),
        strokeWidth = drawable.width
    )
}

private fun DrawScope.drawCircle(drawable: CircleDrawableItem) {
    drawCircle(
        color = Color(drawable.state.color),
        radius = drawable.radius,
        center = Offset(0F, 0F),
        style = drawable.width.toStroke()
    )
}

private fun DrawScope.drawSquare(drawable: SquareDrawableItem) {
    drawRect(
        color = Color(drawable.state.color),
        size = Size(drawable.size, drawable.size),
        topLeft = Offset(-drawable.size / 2, -drawable.size / 2),
        style = drawable.width.toStroke()
    )
}

private fun DrawScope.drawTriangle(drawable: TriangleDrawableItem) {
    val halfSize = drawable.size / 2f
    val path = Path().apply {
        moveTo(0F, 0F - halfSize)
        lineTo(0F - halfSize, 0F + halfSize)
        lineTo(0F + halfSize, 0F + halfSize)
        close()
    }
    drawPath(path = path, color = Color(drawable.state.color), style = drawable.width.toStroke())
}

private fun DrawScope.drawArrow(drawable: ArrowDrawableItem) {
    val height = drawable.size
    val width = drawable.size * 0.68F

    val path = Path().apply {
        moveTo(0F, height / 2)
        lineTo(0F, -height / 2)
        lineTo(-width / 2, -(width / 2) * 0.70F)
        moveTo(0F, -height / 2)
        lineTo(width / 2, -(width / 2) * 0.70F)
    }
    drawPath(path = path, color = Color(drawable.state.color), style = drawable.width.toStroke())
}

@Composable
fun CanvasWithImage(modifier: Modifier = Modifier) {
    val image = ImageBitmap.imageResource(R.drawable.canvas)
    val painter = painterResource(id = R.drawable.canvas)

    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val imageWidth = painter.intrinsicSize.width
        val imageHeight = painter.intrinsicSize.height

        val scale = minOf(canvasWidth / imageWidth, canvasHeight / imageHeight)
        val offsetX = (canvasWidth - imageWidth * scale) / 2
        val offsetY = (canvasHeight - imageHeight * scale) / 2

        withTransform({
            translate(left = offsetX, top = offsetY)
            scale(scaleX = scale, scaleY = scale)
        }) {
            drawImage(
                image,
                dstSize = IntSize(size.width.toInt(), size.height.toInt()),
                blendMode = BlendMode.DstOver,
            )
        }
    }
}

fun Float.toStroke() = Stroke(this, cap = StrokeCap.Round, join = Round)