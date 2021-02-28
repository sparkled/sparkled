package io.sparkled.app.json

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class UnsignedByteArraySerializerTest : StringSpec() {

    init {
        "can write unsigned byte array" {
            val mapper = ObjectMapper().registerModule(JacksonModule())

            assertJson(mapper, byteArrayOf(), "[]")
            assertJson(mapper, byteArrayOf(0, 1, 127, -128, -2, -1), "[0,1,127,128,254,255]")
        }
    }

    private fun assertJson(mapper: ObjectMapper, bytes: ByteArray, expected: String) {
        val json = mapper.writeValueAsString(bytes)
        json shouldBe expected
    }
}
