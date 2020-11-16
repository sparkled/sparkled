package io.sparkled.rest

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.spring.tx.annotation.Transactional
import io.sparkled.persistence.DbService
import io.sparkled.viewmodel.ViewModelConverterService
import io.sparkled.viewmodel.dashboard.DashboardViewModel

@Controller("/api/dashboard")
open class DashboardController(
    private val db: DbService,
    private val vm: ViewModelConverterService
) {

    @Get("/")
    @Transactional(readOnly = true)
    open fun getDashboard(): HttpResponse<Any> {
        val dashboard = DashboardViewModel(
            stages = vm.stageSearch.toViewModels(db.stage.getAllStages()),
            songs = db.song.getAllSongs().map { vm.song.toViewModel(it) },
            sequences = vm.sequenceSearch.toViewModels(db.sequence.getAllSequences()),
            playlists = vm.playlistSearch.toViewModels(db.playlist.getAllPlaylists()),
            scheduledTasks = vm.scheduledJobSearch.toViewModels(db.scheduledJob.getAllScheduledJobs()),
        )

        return HttpResponse.ok(dashboard)
    }
}
