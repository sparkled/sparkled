package io.sparkled.app.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import jakarta.inject.Singleton

/**
 * Provides automatic trimming of all JSON string fields coming into the API.
 */
@Singleton
class StringTrimJacksonModule : SimpleModule() {
    init {
        super.addDeserializer(
            String::class.java,
            object : StdScalarDeserializer<String?>(String::class.java) {
                override fun deserialize(jsonParser: JsonParser, ctx: DeserializationContext?): String? {
                    return jsonParser.valueAsString?.trim()
                }
            },
        )
    }
}
