package com.movingPictures.ui.screens.canvas.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.movingPictures.data.dto.Frame
import com.movingPictures.ui.screens.canvas.CanvasViewModel
import com.movingPictures.ui.theme.colors.ColorPalette
import kotlin.math.cos
import kotlin.math.sin


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.ThicknessSlider(
    modifier: Modifier = Modifier,
    show: Boolean,
    viewModel: CanvasViewModel,
    thickness: MutableState<Float>,
) {
    Popup(modifier, isShown = show) {
        Box(
            modifier = Modifier
                .size(200.dp, (52 - 32).dp)
        ) {
            Slider(
                valueRange = 1f..50f,
                value = thickness.value,
                onValueChange = { thickness.value = it },
                thumb = { Thumb() },
                track = { Track(4.dp, 12.dp) },
            )
        }
    }
}

@Composable
private fun Thumb() {
    Box(
        modifier = Modifier
            .size(20.dp)
            .background(ColorPalette.currentPalette.secondaryContainer, shape = CircleShape)
            .clip(CircleShape)
    )
}

@Composable
private fun Track(minHeight: Dp, maxHeight: Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
            .drawWithCache {
                val gradient = Brush.horizontalGradient(
                    colors = ColorPalette.currentPalette.greenGradient,
                    startX = 0f,
                    endX = size.width
                )
                onDrawWithContent {
                    val start = minHeight.toPx()
                    val end = maxHeight.toPx()

                    val path = Path().apply {
                        moveTo(0f, size.height / 2 + start / 2)

                        lineTo(size.width - end / 2, size.height / 2 + end / 2)
                        cubicTo(
                            size.width, size.height / 2 + end / 2,
                            size.width, size.height / 2 - end / 2,
                            size.width - end / 2, size.height / 2 - end / 2
                        )

                        lineTo(0f + start / 2, size.height / 2 - start / 2)
                        cubicTo(
                            0f, size.height / 2 - start / 2,
                            0f, size.height / 2 + start / 2,
                            0f + start / 2, size.height / 2 + start / 2
                        )

                        close()
                    }
                    drawPath(path = path, brush = gradient)
                }
            }
    )
}