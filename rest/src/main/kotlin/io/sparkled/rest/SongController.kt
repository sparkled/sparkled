package io.sparkled.rest

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.multipart.CompletedFileUpload
import io.micronaut.spring.tx.annotation.Transactional
import io.sparkled.persistence.song.SongPersistenceService
import io.sparkled.rest.response.IdResponse
import io.sparkled.viewmodel.song.SongViewModel
import io.sparkled.viewmodel.song.SongViewModelConverter

@Controller("/rest/songs")
open class SongController(
    private val songPersistenceService: SongPersistenceService,
    private val songViewModelConverter: SongViewModelConverter
) {

    @Get("/")
    @Transactional(readOnly = true)
    open fun getAllSongs(): HttpResponse<Any> {
        val songs = songPersistenceService.getAllSongs()
        val viewModels = songs.asSequence().map(songViewModelConverter::toViewModel).toList()
        return HttpResponse.ok(viewModels)
    }

    @Get("/{id}")
    @Transactional(readOnly = true)
    open fun getSong(id: Int): HttpResponse<Any> {
        val song = songPersistenceService.getSongById(id)

        if (song != null) {
            val viewModel = songViewModelConverter.toViewModel(song)
            return HttpResponse.ok(viewModel)
        }

        return HttpResponse.notFound("Song not found.")
    }

    @Post("/", consumes = [MediaType.MULTIPART_FORM_DATA])
    @Transactional
    open fun createSong(song: String, mp3: CompletedFileUpload): HttpResponse<Any> {
        val songViewModel = ObjectMapper().readValue(song, SongViewModel::class.java)
        songViewModel.setId(null) // Prevent client-side ID tampering.

        val songModel = songViewModelConverter.toModel(songViewModel)
        val savedSong = songPersistenceService.createSong(songModel, mp3.bytes)

        return HttpResponse.ok(IdResponse(savedSong.getId()!!))
    }

    @Delete("/{id}")
    @Transactional
    open fun deleteSong(id: Int): HttpResponse<Any> {
        songPersistenceService.deleteSong(id)
        return HttpResponse.ok()
    }
}
