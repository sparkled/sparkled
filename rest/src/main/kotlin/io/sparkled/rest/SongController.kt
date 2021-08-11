package io.sparkled.rest

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.multipart.CompletedFileUpload
import io.sparkled.model.entity.v2.SongEntity
import io.sparkled.persistence.*
import io.sparkled.persistence.query.song.DeleteSongsQuery
import io.sparkled.rest.response.IdResponse
import io.sparkled.viewmodel.SongViewModel
import org.springframework.transaction.annotation.Transactional

@Controller("/api/songs")
open class SongController(
    private val db: DbService,
    private val file: FileService,
    private val objectMapper: ObjectMapper,
) {

    @Get("/")
    @Transactional(readOnly = true)
    open fun getAllSongs(): HttpResponse<Any> {
        val songs = db.getAll<SongEntity>(orderBy = "name")
        val viewModels = songs.map(SongViewModel::fromModel)
        return HttpResponse.ok(viewModels)
    }

    @Get("/{id}")
    @Transactional(readOnly = true)
    open fun getSong(id: Int): HttpResponse<Any> {
        val song = db.getById<SongEntity>(id)

        return if (song != null) {
            val viewModel = SongViewModel.fromModel(song)
            HttpResponse.ok(viewModel)
        } else HttpResponse.notFound("Song not found.")
    }

    @Post("/", consumes = [MediaType.MULTIPART_FORM_DATA])
    @Transactional
    open fun createSong(song: String, mp3: CompletedFileUpload): HttpResponse<Any> {
        val songViewModel = objectMapper.readValue(song, SongViewModel::class.java)
        val songId = db.insert(songViewModel.toModel()).toInt()

        file.writeSongAudio(songId, mp3.bytes)
        return HttpResponse.ok(IdResponse(songId))
    }

    @Delete("/{id}")
    @Transactional
    open fun deleteSong(id: Int): HttpResponse<Any> {
        db.query(DeleteSongsQuery(listOf(id)))
        file.deleteSongAudio(id)
        return HttpResponse.ok()
    }
}
