package io.sparkled.rest;

import io.sparkled.model.entity.Playlist;
import io.sparkled.model.entity.PlaylistSequence;
import io.sparkled.persistence.playlist.PlaylistPersistenceService;
import io.sparkled.rest.response.IdResponse;
import io.sparkled.viewmodel.playlist.PlaylistViewModel;
import io.sparkled.viewmodel.playlist.PlaylistViewModelConverter;
import io.sparkled.viewmodel.playlist.sequence.PlaylistSequenceViewModel;
import io.sparkled.viewmodel.playlist.sequence.PlaylistSequenceViewModelConverter;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/playlists")
public class PlaylistRestService extends RestService {

    private final PlaylistPersistenceService playlistPersistenceService;
    private final PlaylistViewModelConverter playlistViewModelConverter;
    private final PlaylistSequenceViewModelConverter playlistSequenceViewModelConverter;

    @Inject
    public PlaylistRestService(PlaylistPersistenceService playlistPersistenceService,
                               PlaylistViewModelConverter playlistViewModelConverter,
                               PlaylistSequenceViewModelConverter playlistSequenceViewModelConverter) {
        this.playlistPersistenceService = playlistPersistenceService;
        this.playlistViewModelConverter = playlistViewModelConverter;
        this.playlistSequenceViewModelConverter = playlistSequenceViewModelConverter;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPlaylist(PlaylistViewModel playlistViewModel) {
        Playlist playlist = playlistViewModelConverter.toModel(playlistViewModel);
        playlistViewModel.setId(null);
        int playlistId = playlistPersistenceService.createPlaylist(playlist);
        return getJsonResponse(new IdResponse(playlistId));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPlaylists() {
        List<Playlist> playlists = playlistPersistenceService.getAllPlaylists();
        return getJsonResponse(playlists);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaylist(@PathParam("id") int playlistId) {
        Optional<Playlist> playlistOptional = playlistPersistenceService.getPlaylistById(playlistId);

        if (playlistOptional.isPresent()) {
            Playlist playlist = playlistOptional.get();
            PlaylistViewModel viewModel = playlistViewModelConverter.toViewModel(playlist);

            List<PlaylistSequenceViewModel> playlistSequences = playlistPersistenceService
                    .getPlaylistSequencesByPlaylistId(playlistId)
                    .stream()
                    .map(playlistSequenceViewModelConverter::toViewModel)
                    .collect(Collectors.toList());
            viewModel.setSequences(playlistSequences);

            return getJsonResponse(viewModel);
        }

        return getJsonResponse(Response.Status.NOT_FOUND);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePlaylist(@PathParam("id") int id, PlaylistViewModel playlistViewModel) {
        playlistViewModel.setId(id); // Prevent client-side ID tampering.

        Playlist playlist = playlistViewModelConverter.toModel(playlistViewModel);
        List<PlaylistSequence> playlistSequences = playlistViewModel.getSequences()
                .stream()
                .map(playlistSequenceViewModelConverter::toModel)
                .map(ps -> ps.setPlaylistId(id))
                .collect(Collectors.toList());

        Integer savedId = playlistPersistenceService.savePlaylist(playlist, playlistSequences);
        if (savedId == null) {
            return getJsonResponse(Response.Status.BAD_REQUEST, "Failed to save playlist.");
        } else {
            return getResponse(Response.Status.OK);
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePlaylist(@PathParam("id") int id) {
        playlistPersistenceService.deletePlaylist(id);
        return getResponse(Response.Status.OK);
    }
}