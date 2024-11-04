package com.movingPictures.ui.screens.canvas.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.movingPictures.ui.theme.typography.MPTypography
import com.movingPictures.ui.screens.canvas.CanvasViewModel
import com.movingPictures.ui.theme.colors.ColorPalette

@Composable
fun BoxScope.LayersPopup(
    modifier: Modifier = Modifier,
    show: Boolean,
    viewModel: CanvasViewModel,
) {
    Popup(modifier, isShown = show) {
        Box(
            modifier = Modifier
        ) {
            val list = viewModel.gifComposer.frames.collectAsState(listOf())
            val currentFrame = viewModel.gifState.currentFrame.collectAsState()
            val listState = rememberLazyListState()

            LaunchedEffect(currentFrame.value?.id, list.value) {
                val frameId = currentFrame.value?.id
                val index = list.value.indexOfFirst { it.id == frameId }
                if (index != -1) {
                    listState.animateScrollToItem(index)
                }
            }

            LazyRow(
                state = listState,
                horizontalArrangement = spacedBy(8.dp),
            ) {
                items(list.value.size) {
                    val item = list.value[it]
                    val color = if (currentFrame.value?.id != item.id) {
                        ColorPalette.currentPalette.secondaryContainer
                    } else {
                        ColorPalette.currentPalette.secondary
                    }
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(color.copy(alpha = 0.15f))
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { viewModel.selectFrameFromList(item.id) },
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text(
                            item.number.toString(),
                            style = MPTypography.bodyMedium.copy(color = ColorPalette.currentPalette.primary),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}