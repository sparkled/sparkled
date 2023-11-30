package io.sparkled.model.render

@Suppress("ArrayInDataClass")
data class RenderedFrame(
    private val startFrame: Int,
    val frameIndex: Int,
    val ledCount: Int,
    private val data: ByteArray, // TODO use ByteBuffer to fix warning.
    private val dummyFrame: Boolean = false,
) {

    fun getData(): ByteArray {
        if (dummyFrame) {
            // Dummy frames should only ever appear in preview renders for stateful effects.
            throw RuntimeException("Attempted to retrieve data from dummy frame.")
        }

        val bytesPerFrame = ledCount * Led.BYTES_PER_LED
        val offset = (frameIndex - startFrame) * bytesPerFrame

        val frameData = ByteArray(bytesPerFrame)
        System.arraycopy(data, offset, frameData, 0, bytesPerFrame)

        return frameData
    }

    fun getLed(pixelIndex: Int): Led {
        return if (dummyFrame) {
            Led(byteArrayOf(0, 0, 0), 0, 0)
        } else {
            val bytesPerFrame = ledCount * Led.BYTES_PER_LED
            val offset = (frameIndex - startFrame) * bytesPerFrame
            Led(data, pixelIndex, offset)
        }
    }
}
