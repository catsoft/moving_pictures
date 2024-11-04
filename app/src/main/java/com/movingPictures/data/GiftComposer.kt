package com.movingPictures.data

import com.movingPictures.data.dto.Frame
import com.movingPictures.data.dto.Gif
import kotlinx.coroutines.flow.MutableSharedFlow

class GiftComposer(initGif: Gif = Gif(listOf())) {
    private val _frames = initGif.frames.map { FrameComposer(it) }.toMutableList()
    val frames: MutableSharedFlow<List<FrameComposer>> = MutableSharedFlow(replay = 1)

    init {
        frames.tryEmit(_frames)
    }

    fun addFrame(frame: Frame, id: String): String {
        var index = _frames.indexOfFirst { it.id == id }
        if (index != -1) {
            index++
        }
        return addFrame(frame, index)
    }

    fun addFrame(frame: Frame, position: Int? = null): String {
        val composer = FrameComposer(frame)
        if (position != null) {
            _frames.add(position, composer)
        } else {
            _frames.add(composer)
        }
        for (i in _frames.indices) {
            _frames[i].number = i + 1L
        }
        frames.tryEmit(_frames)
        return composer.id
    }

    fun moveFrame(frameId: String, newPosition: Int) {
        val frame = _frames.removeAt(_frames.indexOfFirst { it.id == frameId })
        _frames.add(newPosition, frame)
        frames.tryEmit(_frames)
    }

    fun removeFrame(frameId: String) {
        _frames.removeAt(_frames.indexOfFirst { it.id == frameId })
        frames.tryEmit(_frames)
    }

    fun composeGif(): Gif = Gif(_frames.map { it.composeFrame() })
}