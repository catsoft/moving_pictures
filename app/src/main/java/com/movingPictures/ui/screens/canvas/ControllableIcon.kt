package com.movingPictures.ui.screens.canvas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movingPictures.ui.theme.colors.ColorPalette
import com.example.movingPictures.ui.theme.themes.MPTheme
import com.movingPictures.compose.MPIcons

@Composable
fun CanvasScreen(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize()) {
        TopBar(modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 0.dp))
        Canvas(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        )
        BottomBar(modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp))
    }
}

@Composable
fun Canvas(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color.Gray, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
    ) {

    }
}

@Composable
fun TopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MPIcons.IcToLeft(
            modifier = Modifier
                .padding(end = 8.dp)
                .then(smallIconModifier)
        )
        MPIcons.IcToRight(
            modifier = Modifier
                .padding(end = 8.dp)
                .then(smallIconModifier)
        )

        Spacer(modifier = Modifier.weight(1f))

        MPIcons.IcBin(modifier = mediumIconModifier)
        MPIcons.IcAddFile(
            modifier = Modifier
                .padding(start = 16.dp)
                .then(mediumIconModifier)
        )
        MPIcons.IcLayers(
            modifier = Modifier
                .padding(start = 16.dp)
                .then(mediumIconModifier)
        )

        Spacer(modifier = Modifier.weight(1f))

        MPIcons.IcPause(
            modifier = Modifier
                .padding(start = 16.dp)
                .then(mediumIconModifier)
        )
        MPIcons.IcPlay(
            modifier = Modifier
                .padding(start = 8.dp)
                .then(mediumIconModifier)
        )
    }
}

@Composable
fun BottomBar(modifier: Modifier = Modifier) {
    Box(
        modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MPIcons.IcPencil(modifier = mediumIconModifier)
            MPIcons.IcBrush(modifier = mediumIconModifier)
            MPIcons.IcErase(modifier = mediumIconModifier)
            MPIcons.IcEdit(modifier = mediumIconModifier)
            ColorItem()
        }
    }
}

@Composable
fun ColorItem(color: Color = ColorPalette.currentPalette.primary) {
    Box(
        modifier = mediumIconModifier
            .background(color, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
    ) {

    }
}

val smallIconModifier = Modifier.size(24.dp)

val mediumIconModifier = Modifier.size(32.dp)

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MPTheme {
        CanvasScreen()
    }
}