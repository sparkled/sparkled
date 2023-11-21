package io.sparkled.app.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.lang.reflect.Type
import kotlin.experimental.and

/**
 * By default, Jackson serialises byte arrays as some weird kind of base64, which isn't ideal. This serializer outputs
 * bytes as an unsigned array, so for a byte array of [-128, -1, 0, 1, 127], the output is [0, 1]
 * TODO see if this is still necessary, and use unsigned bytes at any rate.
 */
class UnsignedByteArraySerializer : StdSerializer<ByteArray>(ByteArray::class.java) {

    override fun isEmpty(prov: SerializerProvider, value: ByteArray?): Boolean {
        return value == null || value.isEmpty()
    }

    override fun serialize(value: ByteArray, g: JsonGenerator, provider: SerializerProvider) {
        g.writeStartArray()
        value.forEach { g.writeNumber(it.toShort() and 0xFF) } // "and 0xFF" results in unsigned bytes (0-255).
        g.writeEndArray()
    }

    override fun getSchema(provider: SerializerProvider?, typeHint: Type?): JsonNode {
        val node = createSchemaNode("array", true)
        val itemSchema = createSchemaNode("byte") // binary values written as strings?
        return node.set("items", itemSchema)
    }

    override fun acceptJsonFormatVisitor(visitor: JsonFormatVisitorWrapper, typeHint: JavaType) {
        visitor.expectArrayFormat(typeHint)?.itemsFormat(JsonFormatTypes.INTEGER)
    }
}
