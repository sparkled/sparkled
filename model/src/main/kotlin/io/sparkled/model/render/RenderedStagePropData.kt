package io.sparkled.model.render

import java.util.ArrayList

class RenderedStagePropData(startFrame: Int, endFrame: Int, val ledCount: Int, val data: ByteArray) {

    // Don't serialise frames to JSON, as each frame contains a reference to the (very large) data array
    @Transient val frames: List<RenderedFrame>

    /**
     * Default constructor required for Gson.
     */
    @SuppressWarnings("unused")
    constructor() : this(0, 0, 0, byteArrayOf()) {
    }

    init {

        val frameCount = endFrame - startFrame + 1
        this.frames = ArrayList(frameCount)

        for (frameIndex in startFrame..endFrame) {
            this.frames.add(RenderedFrame(startFrame, frameIndex, ledCount, data))
        }
    }

    companion object {

        val EMPTY = RenderedStagePropData()
    }
}
