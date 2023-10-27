package io.sparkled.api

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.sparkled.model.entity.v2.*
import io.sparkled.persistence.DbService
import io.sparkled.persistence.getAll
import io.sparkled.viewmodel.*
import org.springframework.transaction.annotation.Transactional

@ExecuteOn(TaskExecutors.IO)
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/dashboard")
class DashboardController(
    private val db: DbService
) {

    @Get("/")
    @Transactional
    fun getDashboard(): HttpResponse<Any> {
        val playlists = db.getAll<PlaylistModel>(orderBy = "name")
        val playlistNames = playlists.associate { it.id to it.name }
        val scheduledTasks = db.getAll<ScheduledActionModel>(orderBy = "id")
        val sequences = db.getAll<SequenceModel>(orderBy = "name")
        val playlistSequences = db.getAll<PlaylistSequenceModel>().groupBy { it.playlistId }
        val songs = db.getAll<SongModel>(orderBy = "name")
        val stages = db.getAll<StageModel>(orderBy = "name")

        val dashboard = DashboardViewModel(
            playlists = playlists.map {
                PlaylistSummaryViewModel.fromModel(
                    it,
                    playlistSequences = playlistSequences[it.id] ?: emptyList(),
                    sequences = sequences,
                    songs = songs,
                )
            },
            scheduledTasks = scheduledTasks.map {
                ScheduledTaskSummaryViewModel.fromModel(it, playlistNames)
            },
            sequences = sequences.map {
                SequenceSummaryViewModel.fromModel(
                    it,
                    song = songs.first { song -> song.id == it.songId },
                    stage = stages.first { stage -> stage.id == it.stageId },
                )
            },
            songs = songs.map { SongViewModel.fromModel(it) },
            stages = stages.map { StageSummaryViewModel.fromModel(it) },
        )

        return HttpResponse.ok(dashboard)
    }
}
