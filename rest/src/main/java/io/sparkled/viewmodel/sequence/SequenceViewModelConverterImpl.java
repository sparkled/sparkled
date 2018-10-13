package io.sparkled.viewmodel.sequence;

import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.Song;
import io.sparkled.model.util.SequenceUtils;
import io.sparkled.persistence.sequence.SequencePersistenceService;
import io.sparkled.persistence.song.SongPersistenceService;
import io.sparkled.viewmodel.exception.ViewModelConversionException;

import javax.inject.Inject;

public class SequenceViewModelConverterImpl extends SequenceViewModelConverter {

    private SequencePersistenceService sequencePersistenceService;
    private final SongPersistenceService songPersistenceService;

    @Inject
    public SequenceViewModelConverterImpl(SequencePersistenceService sequencePersistenceService,
                                          SongPersistenceService songPersistenceService) {
        this.sequencePersistenceService = sequencePersistenceService;
        this.songPersistenceService = songPersistenceService;
    }

    @Override
    public SequenceViewModel toViewModel(Sequence model) {
        return new SequenceViewModel()
                .setId(model.getId())
                .setSongId(model.getSongId())
                .setStageId(model.getStageId())
                .setName(model.getName())
                .setFramesPerSecond(model.getFramesPerSecond())
                .setFrameCount(getFrameCount(model))
                .setStatus(model.getStatus());
    }

    private Integer getFrameCount(Sequence sequence) {
        Song song = songPersistenceService.getSongBySequenceId(sequence.getId()).orElse(new Song());
        return SequenceUtils.getFrameCount(song, sequence);
    }

    @Override
    public Sequence toModel(SequenceViewModel viewModel) {
        final Integer sequenceId = viewModel.getId();
        Sequence model = getSequence(sequenceId);

        return model
                .setSongId(viewModel.getSongId())
                .setStageId(viewModel.getStageId())
                .setName(viewModel.getName())
                .setFramesPerSecond(viewModel.getFramesPerSecond())
                .setStatus(viewModel.getStatus());
    }

    private Sequence getSequence(Integer sequenceId) {
        if (sequenceId == null) {
            return new Sequence();
        }

        return sequencePersistenceService.getSequenceById(sequenceId)
                .orElseThrow(() -> new ViewModelConversionException("Sequence with ID of '" + sequenceId + "' not found."));
    }
}
