package io.sparkled.rest.json

import io.sparkled.model.util.GsonProvider
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test

class UnsignedByteArrayTypeAdapterTest {

    @Test
    @Throws(Exception::class)
    fun can_serialize_bytes_as_unsigned_values() {
        val bytes = byteArrayOf(0, 1, 127, -128, -2, -1)
        val json = GsonProvider.get().toJson(bytes)
        assertThat(json, `is`("[0,1,127,128,254,255]"))
    }
}