package com.movingPictures.compose

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.movingpictures.R

object MPIcons {

    @Composable
    fun ChatfuelIcon(
        @DrawableRes id: Int,
        modifier: Modifier = Modifier,
        tint: Color? = LocalContentColor.current,
    ) = Icon(
        painter = painterResource(id = id),
        tint = tint ?: LocalContentColor.current,
        contentDescription = null,
        modifier = modifier
    )

    @Composable
    fun IcAddFile(modifier: Modifier = Modifier, tint: Color? = LocalContentColor.current) = ChatfuelIcon(R.drawable.ic_add_file, modifier, tint)

    @Composable
    fun IcCopy(modifier: Modifier = Modifier, tint: Color? = LocalContentColor.current) = ChatfuelIcon(R.drawable.ic_copy, modifier, tint)


    @Composable
    fun IcBrush(modifier: Modifier = Modifier, tint: Color? = LocalContentColor.current) = ChatfuelIcon(R.drawable.ic_brush, modifier, tint)

    @Composable
    fun IcColorPalette(modifier: Modifier = Modifier, tint: Color? = LocalContentColor.current) = ChatfuelIcon(R.drawable.ic_color_palette, modifier, tint)

    @Composable
    fun IcErase(modifier: Modifier = Modifier, tint: Color? = LocalContentColor.current) = ChatfuelIcon(R.drawable.ic_erase, modifier, tint)

    @Composable
    fun IcLayers(modifier: Modifier = Modifier, tint: Color? = LocalContentColor.current) = ChatfuelIcon(R.drawable.ic_layers, modifier, tint)

    @Composable
    fun IcPencil(modifier: Modifier = Modifier, tint: Color? = LocalContentColor.current) = ChatfuelIcon(R.drawable.ic_pencil, modifier, tint)

    @Composable
    fun IcSquare(modifier: Modifier = Modifier, tint: Color? = LocalContentColor.current) = ChatfuelIcon(R.drawable.ic_square, modifier, tint)

    @Composable
    fun IcToLeft(modifier: Modifier = Modifier, tint: Color? = LocalContentColor.current) = ChatfuelIcon(R.drawable.ic_to_left, modifier, tint)

    @Composable
    fun IcArrowUp(modifier: Modifier = Modifier, tint: Color? = LocalContentColor.current) = ChatfuelIcon(R.drawable.ic_arrow_up, modifier, tint)

    @Composable
    fun IcLine(modifier: Modifier = Modifier, tint: Color? = LocalContentColor.current) = ChatfuelIcon(R.drawable.ic_line, modifier, tint)

    @Composable
    fun IcCircle(modifier: Modifier = Modifier, tint: Color? = LocalContentColor.current) = ChatfuelIcon(R.drawable.ic_circle, modifier, tint)

    @Composable
    fun IcLoading(modifier: Modifier = Modifier, tint: Color? = LocalContentColor.current) = ChatfuelIcon(R.drawable.ic_loading, modifier, tint)

    @Composable
    fun IcPlay(modifier: Modifier = Modifier, tint: Color? = LocalContentColor.current) = ChatfuelIcon(R.drawable.ic_play, modifier, tint)

    @Composable
    fun IcToRight(modifier: Modifier = Modifier, tint: Color? = LocalContentColor.current) = ChatfuelIcon(R.drawable.ic_to_right, modifier, tint)

    @Composable
    fun IcBin(modifier: Modifier = Modifier, tint: Color? = LocalContentColor.current) = ChatfuelIcon(R.drawable.ic_bin, modifier, tint)

    @Composable
    fun IcEdit(modifier: Modifier = Modifier, tint: Color? = LocalContentColor.current) = ChatfuelIcon(R.drawable.ic_edit, modifier, tint)

    @Composable
    fun IcPause(modifier: Modifier = Modifier, tint: Color? = LocalContentColor.current) = ChatfuelIcon(R.drawable.ic_pause, modifier, tint)

    @Composable
    fun IcShapes(modifier: Modifier = Modifier, tint: Color? = LocalContentColor.current) = ChatfuelIcon(R.drawable.ic_shapes, modifier, tint)

    @Composable
    fun IcTriangle(modifier: Modifier = Modifier, tint: Color? = LocalContentColor.current) = ChatfuelIcon(R.drawable.ic_triangle, modifier, tint)
}
