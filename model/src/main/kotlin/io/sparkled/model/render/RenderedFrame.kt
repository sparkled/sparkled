package io.sparkled.model.render

@Suppress("ArrayInDataClass")
data class RenderedFrame(
    private val startFrame: Int,
    val frameNumber: Int,
    val ledCount: Int,
    private val data: ByteArray,
    private val dummyFrame: Boolean = false
) {

    fun getData(): ByteArray {
        if (dummyFrame) {
            // Dummy frames should only ever appear in preview renders for stateful effects.
            throw RuntimeException("Attempted to retrieve data from dummy frame.")
        }

        val bytesPerFrame = ledCount * Led.BYTES_PER_LED
        val offset = (frameNumber - startFrame) * bytesPerFrame

        val frameData = ByteArray(bytesPerFrame)
        System.arraycopy(data, offset, frameData, 0, bytesPerFrame)

        return frameData
    }

    fun getLed(ledIndex: Int): Led {
        return if (dummyFrame) {
            Led(byteArrayOf(0, 0, 0), 0, 0)
        } else {
            val bytesPerFrame = ledCount * Led.BYTES_PER_LED
            val offset = (frameNumber - startFrame) * bytesPerFrame
            Led(data, ledIndex, offset)
        }
    }
}
