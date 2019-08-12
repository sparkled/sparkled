package io.sparkled.app.json

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is` as eq
import org.junit.jupiter.api.Test

class UnsignedByteArraySerializerTest {

    @Test
    fun can_write_unsigned_byte_array() {
        val mapper = ObjectMapper().registerModule(JacksonModule())

        assertJson(mapper, byteArrayOf(), "[]")
        assertJson(mapper, byteArrayOf(0, 1, 127, -128, -2, -1), "[0,1,127,128,254,255]")
    }

    private fun assertJson(mapper: ObjectMapper, bytes: ByteArray, expected: String) {
        val json = mapper.writeValueAsString(bytes)
        assertThat(json, eq(expected))
    }
}
