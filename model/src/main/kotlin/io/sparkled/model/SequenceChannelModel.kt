package io.sparkled.model

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.TypeDef
import io.micronaut.data.model.DataType
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.util.IdUtils.uniqueId
import jakarta.inject.Singleton
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Id
import java.time.Instant

@MappedEntity("SEQUENCE_CHANNEL")
data class SequenceChannelModel(
    @Id
    override var id: UniqueId = uniqueId(),
    override var createdAt: Instant = Instant.now(),
    override var updatedAt: Instant = Instant.now(),

    var sequenceId: String,
    var stagePropId: String,

    var name: String,
    var displayOrder: Int,
    var channelData: ChannelData = ChannelData.empty
) : Model


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

    override fun convertToDatabaseColumn(attribute: ChannelData?): String {
        return objectMapper.writeValueAsString(attribute ?: emptyList<String>())
    }

    override fun convertToEntityAttribute(dbData: String?): ChannelData? {
        return if (dbData == null) {
            null
        } else {
            val list = objectMapper.readValue<List<Effect>>(dbData)
            ChannelData.of(list)
        }
    }
}
