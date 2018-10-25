package io.sparkled.model.render

class RenderedFrame(private val startFrame: Int, val frameNumber: Int, val ledCount: Int, private val data: ByteArray) {

    /**
     * Default constructor required for Gson.
     */
    @SuppressWarnings("unused")
    constructor() : this(0, 0, 0, byteArrayOf()) {
    }

    fun getData(): ByteArray {
        val bytesPerFrame = ledCount * Led.BYTES_PER_LED
        val offset = (frameNumber - startFrame) * bytesPerFrame

        val frameData = ByteArray(bytesPerFrame)
        System.arraycopy(data, offset, frameData, 0, bytesPerFrame)

        return frameData
    }

    fun getLed(ledIndex: Int): Led {
        val bytesPerFrame = ledCount * Led.BYTES_PER_LED
        val offset = (frameNumber - startFrame) * bytesPerFrame
        return Led(data, ledIndex, offset)
    }
}
