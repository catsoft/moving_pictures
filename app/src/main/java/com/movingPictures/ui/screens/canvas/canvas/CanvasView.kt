package com.movingPictures.ui.screens.canvas.canvas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.movingPictures.data.FrameComposer
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
            DrawFrame(Modifier, previousFrame.value!!)
        }

        if (currentFrame.value != null) {
            DrawFrame(Modifier, currentFrame.value!!)
        }
    }
}

@Composable
private fun DrawFrame(modifier: Modifier = Modifier, composer: FrameComposer) {

    val frameState = composer.drawableState.collectAsState()

    Box(modifier.fillMaxSize()) {
        //todo draw
    }
}