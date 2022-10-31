package io.sparkled.app.json

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.module.SimpleModule
import io.micronaut.core.annotation.Indexed
import jakarta.inject.Singleton

@Singleton
@Indexed(Module::class)
class JacksonModule : SimpleModule() {
    init {
        super.addSerializer(ByteArray::class.java, UnsignedByteArraySerializer())
    }
}
