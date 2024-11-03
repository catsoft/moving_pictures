package com.movingPictures.data

import com.movingPictures.data.dto.Frame
import com.movingPictures.data.dto.Gif
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class GiftComposer(initGif: Gif = Gif(listOf())) {
    private val _frames = initGif.frames.map { FrameComposer(it) }.toMutableList()
    val frames: MutableStateFlow<List<FrameComposer>> = MutableStateFlow(_frames)

    val canRemove = frames.map { it.size > 1 }

    fun addFrame(frame: Frame, position: Int? = null) {
        if (position != null) {
            _frames.add(position, FrameComposer(frame))
        } else {
            _frames.add(FrameComposer(frame))
        }
        for (i in _frames.indices) {
            _frames[i].number = i + 1L
        }
        frames.value = _frames
    }

    fun moveFrame(frameId: String, newPosition: Int) {
        val frame = _frames.removeAt(_frames.indexOfFirst { it.id == frameId })
        _frames.add(newPosition, frame)
        frames.value = _frames
    }

    fun removeFrame(frameId: String) {
        _frames.removeAt(_frames.indexOfFirst { it.id == frameId })
        frames.value = _frames
    }

    fun composeGif(): Gif = Gif(_frames.map { it.composeFrame() })
}