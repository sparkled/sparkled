const sweetAlert = require('sweetalert2');

function SelectorCtrl(loaderService,
                      songRestService) {
    'ngInject';

    const vm = this;
    vm.songs = [];

    const loadSongs = () => {
        loaderService.state.loading = true;
        songRestService.getSongs()
            .then(songs => vm.songs = songs)
            .finally(() => loaderService.state.loading = false);
    };

    const onSongAdd = (file, songData) => {
        songRestService.saveSong(file, songData)
            .then(() => {
                sweetAlert('Song Added', 'Your song has been added.', 'success');
                loadSongs();
            });
    };

    const deleteSong = song => {
        sweetAlert({
            title: 'Delete Song',
            text: 'Are you sure you want to delete this song?',
            type: 'warning',
            showCancelButton: true
        }).then(isConfirm => {
            if (isConfirm) {
                songRestService.deleteSong(song)
                    .then(() => {
                        sweetAlert('Song Deleted', 'Your song has been deleted.', 'success');
                        loadSongs();
                    });
            }
        }).catch(sweetAlert.noop);
    };

    vm.onSongAdd = onSongAdd;
    vm.deleteSong = deleteSong;
    loadSongs();
}

module.exports = {
    name: 'SelectorCtrl',
    fn: SelectorCtrl
};
