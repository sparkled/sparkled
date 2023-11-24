package io.sparkled.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.multipart.CompletedFileUpload
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.sparkled.model.SongModel
import io.sparkled.model.UniqueId
import io.sparkled.persistence.DbService
import io.sparkled.persistence.FileService
import io.sparkled.persistence.repository.findByIdOrNull
import io.sparkled.viewmodel.SongEditViewModel
import io.sparkled.viewmodel.SongViewModel
import io.micronaut.transaction.annotation.Transactional

@ExecuteOn(TaskExecutors.BLOCKING)
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/songs")
class SongController(
    private val db: DbService,
    private val file: FileService,
    private val objectMapper: ObjectMapper,
) {

    @Get("/")
    @Transactional
    fun getAllSongs(): HttpResponse<Any> {
        val songs = db.songs.findAll().sortedBy { it.name }
        val viewModels = songs.map(SongViewModel::fromModel)
        return HttpResponse.ok(viewModels)
    }

    @Get("/{id}")
    @Transactional
    fun getSong(id: UniqueId): HttpResponse<Any> {
        return when (val song = db.songs.findByIdOrNull(id)) {
            null -> HttpResponse.notFound("Song not found.")
            else -> HttpResponse.ok(SongViewModel.fromModel(song))
        }
    }

    @Post("/", consumes = [MediaType.MULTIPART_FORM_DATA])
    @Transactional
    fun createSong(song: String, mp3: CompletedFileUpload): HttpResponse<Any> {
        val viewModel = objectMapper.readValue<SongEditViewModel>(song)
        val saved = db.songs.save(
            SongModel(
                name = viewModel.name,
                artist = viewModel.artist,
                durationMs = viewModel.durationMs,
            )
        )

        file.writeSongAudio(saved.id, mp3.bytes)
        return HttpResponse.created(SongViewModel.fromModel(saved))
    }

    @Delete("/{id}")
    @Transactional
    fun deleteSong(id: UniqueId): HttpResponse<Any> {
        db.songs.deleteById(id)
        file.deleteSongAudio(id)
        return HttpResponse.noContent()
    }
}
