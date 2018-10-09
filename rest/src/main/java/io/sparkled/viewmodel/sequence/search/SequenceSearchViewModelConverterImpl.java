package io.sparkled.viewmodel.sequence.search;

import io.sparkled.model.entity.Sequence;

public class SequenceSearchViewModelConverterImpl extends SequenceSearchViewModelConverter {

    @Override
    public SequenceSearchViewModel toViewModel(Sequence model) {
        return new SequenceSearchViewModel()
                .setId(model.getId())
                .setName(model.getName())
                .setArtist(model.getArtist())
                .setAlbum(model.getAlbum())
                .setDurationFrames(model.getDurationFrames())
                .setFramesPerSecond(model.getFramesPerSecond())
                .setStatus(model.getStatus());
    }
}
