package io.sparkled.viewmodel.sequence.channel;

import com.google.gson.Gson;
import io.sparkled.model.animation.SequenceChannelEffects;
import io.sparkled.model.animation.effect.Effect;
import io.sparkled.model.entity.SequenceChannel;
import io.sparkled.persistence.sequence.SequencePersistenceService;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

public class SequenceChannelViewModelConverterImpl implements SequenceChannelViewModelConverter {

    private SequencePersistenceService sequencePersistenceService;
    private Gson gson = new Gson();

    @Inject
    public SequenceChannelViewModelConverterImpl(SequencePersistenceService sequencePersistenceService) {
        this.sequencePersistenceService = sequencePersistenceService;
    }

    @Override
    public SequenceChannelViewModel toViewModel(SequenceChannel model) {
        SequenceChannelEffects sequenceChannelEffects = gson.fromJson(model.getChannelJson(), SequenceChannelEffects.class);
        List<Effect> effects = sequenceChannelEffects == null ? Collections.emptyList() : sequenceChannelEffects.getEffects();

        return new SequenceChannelViewModel()
                .setUuid(model.getUuid())
                .setSequenceId(model.getSequenceId())
                .setStagePropUuid(model.getStagePropUuid())
                .setName(model.getName())
                .setDisplayOrder(model.getDisplayOrder())
                .setEffects(effects);
    }

    @Override
    public SequenceChannel fromViewModel(SequenceChannelViewModel viewModel) {
        SequenceChannel model = sequencePersistenceService.getSequenceChannelByUuid(viewModel.getSequenceId(), viewModel.getUuid())
                .orElseGet(SequenceChannel::new);

        String channelJson = gson.toJson(new SequenceChannelEffects().setEffects(viewModel.getEffects()));
        return model
                .setUuid(viewModel.getUuid())
                .setSequenceId(viewModel.getSequenceId())
                .setStagePropUuid(viewModel.getStagePropUuid())
                .setName(viewModel.getName())
                .setDisplayOrder(viewModel.getDisplayOrder())
                .setChannelJson(channelJson);
    }
}
