package io.sparkled.api

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.sparkled.persistence.DbService
import io.sparkled.viewmodel.DashboardViewModel
import io.sparkled.viewmodel.PlaylistSummaryViewModel
import io.sparkled.viewmodel.ScheduledTaskSummaryViewModel
import io.sparkled.viewmodel.SequenceSummaryViewModel
import io.sparkled.viewmodel.SongViewModel
import io.sparkled.viewmodel.StageSummaryViewModel
import io.micronaut.transaction.annotation.Transactional

@ExecuteOn(TaskExecutors.BLOCKING)
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/dashboard")
class DashboardController(
    private val db: DbService,
) {

    @Get("/")
    @Transactional
    fun getDashboard(): HttpResponse<Any> {
        val playlists = db.playlists.findAll().sortedBy { it.name }
        val playlistNames = playlists.associate { it.id to it.name }
        val scheduledTasks = db.scheduledActions.findAll().sortedBy { it.createdAt }
        val sequences = db.sequences.findAll().sortedBy { it.name }
        val playlistSequences = db.playlistSequences.findAll().groupBy { it.playlistId }
        val songs = db.songs.findAll().sortedBy { it.name }
        val stages = db.stages.findAll().sortedBy { it.name }

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
