package io.sparkled.viewmodel.sequence.channel

import io.sparkled.model.animation.SequenceChannelEffects
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.model.util.GsonProvider
import io.sparkled.persistence.sequence.SequencePersistenceService

import javax.inject.Inject
import java.util.Collections

class SequenceChannelViewModelConverterImpl @Inject
constructor(private val sequencePersistenceService: SequencePersistenceService) : SequenceChannelViewModelConverter() {

    @Override
    fun toViewModel(model: SequenceChannel): SequenceChannelViewModel {
        val sequenceChannelEffects = GsonProvider.get().fromJson(model.getChannelJson(), SequenceChannelEffects::class.java)
        val effects = if (sequenceChannelEffects == null) Collections.emptyList() else sequenceChannelEffects!!.getEffects()

        return SequenceChannelViewModel()
                .setUuid(model.getUuid())
                .setSequenceId(model.getSequenceId())
                .setStagePropUuid(model.getStagePropUuid())
                .setName(model.getName())
                .setDisplayOrder(model.getDisplayOrder())
                .setEffects(effects)
    }

    @Override
    fun toModel(viewModel: SequenceChannelViewModel): SequenceChannel {
        val model = sequencePersistenceService.getSequenceChannelByUuid(viewModel.getSequenceId(), viewModel.getUuid())
                .orElseGet(???({ SequenceChannel() }))

        val channelJson = GsonProvider.get().toJson(SequenceChannelEffects().setEffects(viewModel.getEffects()))
        return model
                .setUuid(viewModel.getUuid())
                .setSequenceId(viewModel.getSequenceId())
                .setStagePropUuid(viewModel.getStagePropUuid())
                .setName(viewModel.getName())
                .setDisplayOrder(viewModel.getDisplayOrder())
                .setChannelJson(channelJson)
    }
}
