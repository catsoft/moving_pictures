package com.movingPictures.compose

import androidx.compose.ui.geometry.Offset
import com.movingPictures.data.dto.Point

fun Offset.toPoint() = Point(x, y)

fun Offset.toPoint(offset: Point) = Point(x - offset.x, y - offset.y)

fun Point.toOffset() = Offset(x, y)

fun Point.withOffset(point: Point) = Point(x - point.x, y - point.y)
