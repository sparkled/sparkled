package io.sparkled.app.json

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.module.SimpleModule
import io.micronaut.core.annotation.Indexed
import javax.inject.Singleton

@Singleton
@Indexed(Module::class)
class JacksonModule() : SimpleModule() {
    init {
        addSerializer(ByteArray::class.java, UnsignedByteArraySerializer())
    }
}
