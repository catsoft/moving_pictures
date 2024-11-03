package com.movingPictures.ui.screens.canvas.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.movingPictures.ui.theme.colors.MidGray

@Composable
fun BoxScope.Popup(
    modifier: Modifier,
    isShown: Boolean,
    content: @Composable BoxScope.() -> Unit
) {
    if (isShown)
        Box(
            modifier = modifier
                .align(Alignment.BottomCenter)
                .border(1.dp, MidGray.copy(1 - 0.16F), shape = RoundedCornerShape(4.dp))
                .clip(RoundedCornerShape(4.dp))
        ) {
            BlurSurface(Modifier.matchParentSize())
            Box(
                Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.14F), shape = RoundedCornerShape(4.dp))
            ) { }

            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                content()
            }
        }
}