package io.sparkled.viewmodel.playlist

import io.sparkled.model.entity.Playlist
import io.sparkled.viewmodel.ModelConverter
import io.sparkled.viewmodel.ViewModelConverter

abstract class PlaylistViewModelConverter : ModelConverter<Playlist, PlaylistViewModel>, ViewModelConverter<PlaylistViewModel, Playlist>
