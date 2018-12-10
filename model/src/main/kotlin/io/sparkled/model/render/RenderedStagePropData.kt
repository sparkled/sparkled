package io.sparkled.model.render

import java.util.ArrayList

class RenderedStagePropData(startFrame: Int, endFrame: Int, val ledCount: Int, val data: ByteArray) {

    // Don't serialise frames to JSON, as each frame contains a reference to the (very large) data array
    @Transient
    val frames: List<RenderedFrame>

    init {
        val frameCount = endFrame - startFrame + 1
        this.frames = ArrayList(frameCount)

        for (frameIndex in startFrame..endFrame) {
            this.frames.add(RenderedFrame(startFrame, frameIndex, ledCount, data))
        }
    }
}
