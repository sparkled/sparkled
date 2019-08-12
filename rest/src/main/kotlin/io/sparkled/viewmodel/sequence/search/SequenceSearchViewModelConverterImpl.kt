package io.sparkled.viewmodel.sequence.search

import io.sparkled.model.constant.ModelConstants.MS_PER_SECOND
import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.Song
import io.sparkled.model.entity.Stage
import io.sparkled.persistence.song.SongPersistenceService
import io.sparkled.persistence.stage.StagePersistenceService
import javax.inject.Singleton

@Singleton
class SequenceSearchViewModelConverterImpl(
    private val songPersistenceService: SongPersistenceService,
    private val stagePersistenceService: StagePersistenceService
) : SequenceSearchViewModelConverter() {

    override fun toViewModels(models: Collection<Sequence>): List<SequenceSearchViewModel> {
        val songs = getSongMap()
        val stages = getStageMap()
        return models.map { model -> toViewModel(model, songs, stages) }.toList()
    }

    private fun getStageMap(): Map<Int, Stage> {
        return stagePersistenceService.getAllStages().associateBy({ it.getId()!! }, { it })
    }

    private fun getSongMap(): Map<Int, Song> {
        return songPersistenceService.getAllSongs().associateBy({ it.getId()!! }, { it })
    }

    private fun toViewModel(model: Sequence, songs: Map<Int, Song>, stages: Map<Int, Stage>): SequenceSearchViewModel {
        val song = songs[model.getSongId()]!!
        val stage = stages[model.getStageId()]!!

        return SequenceSearchViewModel()
            .setId(model.getId())
            .setName(model.getName())
            .setSongName(song.getName())
            .setStageName(stage.getName())
            .setFramesPerSecond(model.getFramesPerSecond())
            .setDurationSeconds(song.getDurationMs()?.div(MS_PER_SECOND))
            .setStatus(model.getStatus())
    }
}
