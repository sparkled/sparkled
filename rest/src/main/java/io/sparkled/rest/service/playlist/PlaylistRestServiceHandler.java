package io.sparkled.rest.service.playlist;

import com.google.inject.persist.Transactional;
import io.sparkled.model.entity.Playlist;
import io.sparkled.model.entity.PlaylistSequence;
import io.sparkled.persistence.playlist.PlaylistPersistenceService;
import io.sparkled.rest.response.IdResponse;
import io.sparkled.rest.service.RestServiceHandler;
import io.sparkled.viewmodel.playlist.PlaylistViewModel;
import io.sparkled.viewmodel.playlist.PlaylistViewModelConverter;
import io.sparkled.viewmodel.playlist.search.PlaylistSearchViewModel;
import io.sparkled.viewmodel.playlist.search.PlaylistSearchViewModelConverter;
import io.sparkled.viewmodel.playlist.sequence.PlaylistSequenceViewModel;
import io.sparkled.viewmodel.playlist.sequence.PlaylistSequenceViewModelConverter;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class PlaylistRestServiceHandler extends RestServiceHandler {

    private final PlaylistPersistenceService playlistPersistenceService;
    private final PlaylistViewModelConverter playlistViewModelConverter;
    private final PlaylistSearchViewModelConverter playlistSearchViewModelConverter;
    private final PlaylistSequenceViewModelConverter playlistSequenceViewModelConverter;

    @Inject
    public PlaylistRestServiceHandler(PlaylistPersistenceService playlistPersistenceService,
                                      PlaylistViewModelConverter playlistViewModelConverter,
                                      PlaylistSearchViewModelConverter playlistSearchViewModelConverter,
                                      PlaylistSequenceViewModelConverter playlistSequenceViewModelConverter) {
        this.playlistPersistenceService = playlistPersistenceService;
        this.playlistViewModelConverter = playlistViewModelConverter;
        this.playlistSearchViewModelConverter = playlistSearchViewModelConverter;
        this.playlistSequenceViewModelConverter = playlistSequenceViewModelConverter;
    }

    @Transactional
    Response createPlaylist(PlaylistViewModel playlistViewModel) {
        Playlist playlist = playlistViewModelConverter.toModel(playlistViewModel);
        playlist = playlistPersistenceService.createPlaylist(playlist);
        return respondOk(new IdResponse(playlist.getId()));
    }

    Response getAllPlaylists() {
        List<Playlist> playlists = playlistPersistenceService.getAllPlaylists();
        List<PlaylistSearchViewModel> results = playlistSearchViewModelConverter.toViewModels(playlists);

        return respondOk(results);
    }

    Response getPlaylist(int playlistId) {
        Optional<Playlist> playlistOptional = playlistPersistenceService.getPlaylistById(playlistId);

        if (playlistOptional.isPresent()) {
            Playlist playlist = playlistOptional.get();
            PlaylistViewModel viewModel = playlistViewModelConverter.toViewModel(playlist);

            List<PlaylistSequenceViewModel> playlistSequences = playlistPersistenceService
                    .getPlaylistSequencesByPlaylistId(playlistId)
                    .stream()
                    .map(playlistSequenceViewModelConverter::toViewModel)
                    .collect(toList());
            viewModel.setSequences(playlistSequences);

            return respondOk(viewModel);
        }

        return respond(Response.Status.NOT_FOUND, "Playlist not found.");
    }

    @Transactional
    Response updatePlaylist(int id, PlaylistViewModel playlistViewModel) {
        playlistViewModel.setId(id); // Prevent client-side ID tampering.

        Playlist playlist = playlistViewModelConverter.toModel(playlistViewModel);
        List<PlaylistSequence> playlistSequences = playlistViewModel.getSequences()
                .stream()
                .map(playlistSequenceViewModelConverter::toModel)
                .map(ps -> ps.setPlaylistId(id))
                .collect(toList());

        playlistPersistenceService.savePlaylist(playlist, playlistSequences);
        return respondOk();
    }

    @Transactional
    Response deletePlaylist(int id) {
        playlistPersistenceService.deletePlaylist(id);
        return respondOk();
    }
}