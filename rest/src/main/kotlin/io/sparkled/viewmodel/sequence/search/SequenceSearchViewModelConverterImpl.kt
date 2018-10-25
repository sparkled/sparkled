package io.sparkled.viewmodel.sequence.search;

import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.Song;
import io.sparkled.model.entity.Stage;
import io.sparkled.persistence.song.SongPersistenceService;
import io.sparkled.persistence.stage.StagePersistenceService;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static io.sparkled.model.constant.ModelConstants.MS_PER_SECOND;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class SequenceSearchViewModelConverterImpl extends SequenceSearchViewModelConverter {

    private final SongPersistenceService songPersistenceService;
    private final StagePersistenceService stagePersistenceService;

    @Inject
    public SequenceSearchViewModelConverterImpl(SongPersistenceService songPersistenceService,
                                                StagePersistenceService stagePersistenceService) {
        this.songPersistenceService = songPersistenceService;
        this.stagePersistenceService = stagePersistenceService;
    }

    @Override
    public List<SequenceSearchViewModel> toViewModels(Collection<Sequence> models) {
        Map<Integer, Song> songs = getSongMap();
        Map<Integer, Stage> stages = getStageMap();

        return models.stream()
                .map(model -> toViewModel(model, songs, stages))
                .collect(toList());
    }

    private Map<Integer, Stage> getStageMap() {
        return stagePersistenceService.getAllStages()
                .stream()
                .collect(toMap(Stage::getId, s -> s));
    }

    private Map<Integer, Song> getSongMap() {
        return songPersistenceService.getAllSongs()
                .stream()
                .collect(toMap(Song::getId, s -> s));
    }

    private SequenceSearchViewModel toViewModel(Sequence model, Map<Integer, Song> songs, Map<Integer, Stage> stages) {
        Song song = songs.get(model.getSongId());
        Stage stage = stages.get(model.getStageId());

        return new SequenceSearchViewModel()
                .setId(model.getId())
                .setName(model.getName())
                .setSongName(song.getName())
                .setStageName(stage.getName())
                .setFramesPerSecond(model.getFramesPerSecond())
                .setDurationSeconds(song.getDurationMs() / MS_PER_SECOND)
                .setStatus(model.getStatus());
    }
}
