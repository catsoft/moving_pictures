package com.movingPictures.ui.screens.canvas.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.movingPictures.ui.theme.typography.MPTypography
import com.movingPictures.ui.screens.canvas.CanvasViewModel

@Composable
fun BoxScope.ActionsPopup(
    modifier: Modifier = Modifier,
    show: Boolean,
    viewModel: CanvasViewModel,
) {
    Popup(
        modifier
            .wrapContentSize(), isShown = show
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ClickTitle("Generate Moving Pictures") {
                viewModel.generateMovingPictures()
            }

            ClickTitle("Generate Arrow Forest") {
                viewModel.generateArrowForest()
            }

            ClickTitle("Clear everything") {
                viewModel.startOver()
            }
        }
    }
}

@Composable
private fun ClickTitle(
    title: String,
    action: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clickable { action() }
            .clip(RoundedCornerShape(4.dp))
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = title,
            style = MPTypography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}