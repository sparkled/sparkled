package io.sparkled.viewmodel.sequence.channel

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.model.animation.SequenceChannelEffects
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.persistence.sequence.SequencePersistenceService
import javax.inject.Singleton

@Singleton
class SequenceChannelViewModelConverterImpl(
    private val sequencePersistenceService: SequencePersistenceService,
    private val objectMapper: ObjectMapper
) : SequenceChannelViewModelConverter() {

    override fun toViewModel(model: SequenceChannel): SequenceChannelViewModel {
        val channelJson = model.getChannelJson()
        val sequenceChannelEffects = objectMapper.readValue(channelJson, SequenceChannelEffects::class.java)

        return SequenceChannelViewModel()
            .setUuid(model.getUuid())
            .setSequenceId(model.getSequenceId())
            .setStagePropUuid(model.getStagePropUuid())
            .setName(model.getName())
            .setDisplayOrder(model.getDisplayOrder())
            .setEffects(sequenceChannelEffects.effects)
    }

    override fun toModel(viewModel: SequenceChannelViewModel): SequenceChannel {
        val model = sequencePersistenceService
            .getSequenceChannelByUuid(viewModel.getSequenceId()!!, viewModel.getUuid()!!)
            ?: SequenceChannel()

        val channelJson = objectMapper.writeValueAsString(SequenceChannelEffects(viewModel.getEffects()))
        return model
            .setUuid(viewModel.getUuid())
            .setSequenceId(viewModel.getSequenceId())
            .setStagePropUuid(viewModel.getStagePropUuid())
            .setName(viewModel.getName())
            .setDisplayOrder(viewModel.getDisplayOrder())
            .setChannelJson(channelJson)
    }
}
