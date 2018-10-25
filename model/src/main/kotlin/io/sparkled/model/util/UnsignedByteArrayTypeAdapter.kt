package io.sparkled.model.util

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer

import java.lang.reflect.Type

/**
 * Serialises bytes as unsigned (0-255) instead of signed (-128-127). Code producing the bytes that will be serialised
 * must take this conversion into consideration.
 */
class UnsignedByteArrayTypeAdapter : JsonSerializer<ByteArray> {

    fun serialize(src: ByteArray, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val array = JsonArray()
        for (b in src) {
            array.add(b and 0xFF)
        }
        return array
    }
}
