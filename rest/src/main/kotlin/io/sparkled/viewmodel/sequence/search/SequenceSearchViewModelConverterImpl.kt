package io.sparkled.viewmodel.sequence.search

import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.Song
import io.sparkled.model.entity.Stage
import io.sparkled.persistence.song.SongPersistenceService
import io.sparkled.persistence.stage.StagePersistenceService

import javax.inject.Inject

import io.sparkled.model.constant.ModelConstants.MS_PER_SECOND
import java.util.stream.Collectors.toList
import java.util.stream.Collectors.toMap

class SequenceSearchViewModelConverterImpl @Inject
constructor(private val songPersistenceService: SongPersistenceService,
            private val stagePersistenceService: StagePersistenceService) : SequenceSearchViewModelConverter() {

    @Override
    fun toViewModels(models: Collection<Sequence>): List<SequenceSearchViewModel> {
        val songs = songMap
        val stages = stageMap

        return models.stream()
                .map({ model -> toViewModel(model, songs, stages) })
                .collect(toList())
    }

    private val stageMap: Map<Integer, Stage>
        get() {
            return stagePersistenceService.getAllStages()
                    .stream()
                    .collect(toMap(???({ Stage.getId() }), { s -> s }))
        }

    private val songMap: Map<Integer, Song>
        get() {
            return songPersistenceService.getAllSongs()
                    .stream()
                    .collect(toMap(???({ Song.getId() }), { s -> s }))
        }

    private fun toViewModel(model: Sequence, songs: Map<Integer, Song>, stages: Map<Integer, Stage>): SequenceSearchViewModel {
        val song = songs[model.getSongId()]
        val stage = stages[model.getStageId()]

        return SequenceSearchViewModel()
                .setId(model.getId())
                .setName(model.getName())
                .setSongName(song.getName())
                .setStageName(stage.getName())
                .setFramesPerSecond(model.getFramesPerSecond())
                .setDurationSeconds(song.getDurationMs() / MS_PER_SECOND)
                .setStatus(model.getStatus())
    }
}
