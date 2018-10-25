package io.sparkled.rest.json

import io.sparkled.model.util.GsonProvider
import org.junit.jupiter.api.Test

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`

class UnsignedByteArrayTypeAdapterTest {

    @Test
    @Throws(Exception::class)
    fun serialize() {
        val bytes = byteArrayOf(0, 1, 127, -128, -2, -1)
        val json = GsonProvider.get().toJson(bytes)
        assertThat(json, `is`("[0,1,127,128,254,255]"))
    }
}