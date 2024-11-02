package com.movingPictures.compose

import androidx.compose.ui.geometry.Offset
import com.movingPictures.data.dto.Point

fun Offset.toPoint() = Point(x, y)

fun Offset.toPoint(offset: Point) = Point(x - offset.x, y - offset.y)