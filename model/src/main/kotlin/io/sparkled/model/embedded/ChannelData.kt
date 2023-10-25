package io.sparkled.model.embedded

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.micronaut.core.convert.ConversionContext
import io.micronaut.data.annotation.TypeDef
import io.micronaut.data.model.DataType
import io.micronaut.data.model.runtime.convert.AttributeConverter
import io.sparkled.model.animation.effect.Effect
import jakarta.inject.Singleton

@TypeDef(type = DataType.STRING, converter = ChannelDataConverter::class)
class ChannelData(initialCapacity: Int) : ArrayList<Effect>(initialCapacity) {
    companion object {
        fun of(vararg effects: Effect) = ChannelData(effects.size).apply {
            addAll(effects)
        }

        fun of(effects: Collection<Effect>) = ChannelData(effects.size).apply {
            addAll(effects)
        }

        val empty = ChannelData(0)
    }
}

@Singleton
class ChannelDataConverter(
    private val objectMapper: ObjectMapper,
) : AttributeConverter<ChannelData, String> {

    override fun convertToPersistedValue(entityValue: ChannelData?, context: ConversionContext?) =
        entityValue?.let(objectMapper::writeValueAsString)

    override fun convertToEntityValue(persistedValue: String?, context: ConversionContext?) =
        persistedValue?.let { ChannelData.of(objectMapper.readValue<List<Effect>>(persistedValue)) }
}
