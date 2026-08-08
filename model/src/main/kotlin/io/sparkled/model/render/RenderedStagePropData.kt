package io.sparkled.model.render

import com.fasterxml.jackson.annotation.JsonIgnore
import io.sparkled.model.UniqueId
import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
class RenderedStagePropData(
    startFrame: Int,
    endFrame: Int,
    val ledCount: Int,
    val data: ByteArray,
    @JsonIgnore
    val stagePropRanges: Map<UniqueId, IntRange> = emptyMap(),
) {

    // Don't serialise frames to JSON, as each frame contains a reference to the (very large) data array
    @JsonIgnore
    val frames = (startFrame..endFrame).map {
        RenderedFrame(startFrame, it, ledCount, data)
    }
}
