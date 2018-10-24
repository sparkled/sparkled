package io.sparkled.rest.service.song;

import com.google.inject.persist.Transactional;
import io.sparkled.model.entity.Song;
import io.sparkled.persistence.song.SongPersistenceService;
import io.sparkled.rest.response.IdResponse;
import io.sparkled.rest.service.RestServiceHandler;
import io.sparkled.viewmodel.song.SongViewModel;
import io.sparkled.viewmodel.song.SongViewModelConverter;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

class SongRestServiceHandler extends RestServiceHandler {

    private final SongPersistenceService songPersistenceService;
    private final SongViewModelConverter songViewModelConverter;

    @Inject
    SongRestServiceHandler(SongPersistenceService songPersistenceService,
                           SongViewModelConverter songViewModelConverter) {
        this.songPersistenceService = songPersistenceService;
        this.songViewModelConverter = songViewModelConverter;
    }

    @Transactional
    Response createSong(String songViewModelJson, InputStream inputStream) throws IOException {
        SongViewModel songViewModel = gson.fromJson(songViewModelJson, SongViewModel.class);
        songViewModel.setId(null);

        Song song = songViewModelConverter.toModel(songViewModel);
        byte[] audioData = loadAudioData(inputStream);
        song = songPersistenceService.createSong(song, audioData);

        return respondOk(new IdResponse(song.getId()));
    }

    // TODO Use IOUtils.toByteArray() after moving to Java 11.
    private byte[] loadAudioData(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int offset;
        byte[] buffer = new byte[4096];
        while (-1 != (offset = inputStream.read(buffer))) {
            outputStream.write(buffer, 0, offset);
        }

        return outputStream.toByteArray();
    }

    Response getAllSongs() {
        List<Song> songs = songPersistenceService.getAllSongs();
        List<SongViewModel> results = songs.stream()
                .map(songViewModelConverter::toViewModel)
                .collect(toList());

        return respondOk(results);
    }

    Response getSong(int songId) {
        Optional<Song> songOptional = songPersistenceService.getSongById(songId);

        if (songOptional.isPresent()) {
            Song song = songOptional.get();
            SongViewModel viewModel = songViewModelConverter.toViewModel(song);
            return respondOk(viewModel);
        }

        return respond(Response.Status.NOT_FOUND, "Song not found.");
    }

    @Transactional
    Response deleteSong(int id) {
        songPersistenceService.deleteSong(id);
        return respondOk();
    }
}