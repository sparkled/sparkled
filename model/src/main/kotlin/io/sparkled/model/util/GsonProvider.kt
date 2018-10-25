package io.sparkled.model.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonProvider {
    private val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
    private val gson = GsonBuilder()
            .setDateFormat(DATE_FORMAT)
            .registerTypeHierarchyAdapter(ByteArray::class.java, UnsignedByteArrayTypeAdapter())
            .create()

    fun get(): Gson {
        return gson
    }
}
