package com.movingPictures.data

import com.movingPictures.data.dto.Frame
import com.movingPictures.data.dto.Gif

class GiftComposer(initGif: Gif = Gif(listOf())) {
    private val _frames = initGif.frames.map { FrameComposer(it) }.toMutableList()
    val frames: List<FrameComposer> = _frames

    fun addFrame(frame: Frame, position: Int? = null) {
        if (position != null) {
            _frames.add(position, FrameComposer(frame))
        } else {
            _frames.add(FrameComposer(frame))
        }
        for (i in _frames.indices) {
            _frames[i].number = i + 1L
        }
    }

    fun moveFrame(frameId: String, newPosition: Int) {
        val frame = _frames.removeAt(_frames.indexOfFirst { it.id == frameId })
        _frames.add(newPosition, frame)
    }

    fun removeFrame(frameId: String) {
        _frames.removeAt(_frames.indexOfFirst { it.id == frameId })
    }

    fun composeGif(): Gif = Gif(_frames.map { it.composeFrame() })
}