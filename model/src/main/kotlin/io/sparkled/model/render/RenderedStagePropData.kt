package io.sparkled.model.render

import com.fasterxml.jackson.annotation.JsonIgnore

class RenderedStagePropData(startFrame: Int, endFrame: Int, val ledCount: Int, val data: ByteArray) {

    // Don't serialise frames to JSON, as each frame contains a reference to the (very large) data array
    @JsonIgnore
    val frames = (startFrame..endFrame).map {
        RenderedFrame(startFrame, it, ledCount, data)
    }
}
