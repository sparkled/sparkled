package io.sparkled.rest.service.song

import com.google.inject.persist.Transactional
import io.sparkled.model.entity.Song
import io.sparkled.persistence.song.SongPersistenceService
import io.sparkled.rest.response.IdResponse
import io.sparkled.rest.service.RestServiceHandler
import io.sparkled.viewmodel.song.SongViewModel
import io.sparkled.viewmodel.song.SongViewModelConverter

import javax.inject.Inject
import javax.ws.rs.core.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.Optional

import java.util.stream.Collectors.toList

internal class SongRestServiceHandler @Inject
constructor(private val songPersistenceService: SongPersistenceService,
            private val songViewModelConverter: SongViewModelConverter) : RestServiceHandler() {

    @Transactional
    @Throws(IOException::class)
    fun createSong(songViewModelJson: String, inputStream: InputStream): Response {
        val songViewModel = gson.fromJson(songViewModelJson, SongViewModel::class.java)
        songViewModel.setId(null)

        var song = songViewModelConverter.toModel(songViewModel)
        val audioData = loadAudioData(inputStream)
        song = songPersistenceService.createSong(song, audioData)

        return respondOk(IdResponse(song.getId()))
    }

    // TODO Use IOUtils.toByteArray() after moving to Java 11.
    @Throws(IOException::class)
    private fun loadAudioData(inputStream: InputStream): ByteArray {
        val outputStream = ByteArrayOutputStream()
        var offset: Int
        val buffer = ByteArray(4096)
        while (-1 != (offset = inputStream.read(buffer))) {
            outputStream.write(buffer, 0, offset)
        }

        return outputStream.toByteArray()
    }

    val allSongs: Response
        get() {
            val songs = songPersistenceService.getAllSongs()
            val results = songs.stream()
                    .map(???({ songViewModelConverter.toViewModel() }))
            .collect(toList())

            return respondOk(results)
        }

    fun getSong(songId: Int): Response {
        val songOptional = songPersistenceService.getSongById(songId)

        if (songOptional.isPresent()) {
            val song = songOptional.get()
            val viewModel = songViewModelConverter.toViewModel(song)
            return respondOk(viewModel)
        }

        return respond(Response.Status.NOT_FOUND, "Song not found.")
    }

    @Transactional
    fun deleteSong(id: Int): Response {
        songPersistenceService.deleteSong(id)
        return respondOk()
    }
}