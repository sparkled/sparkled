function EditorCtrl(animationService,
                    editorService,
                    loaderService,
                    songRestService,
                    $stateParams) {
    'ngInject';

    const vm = this;
    vm.animationService = animationService;

    loaderService.loading = true;
    songRestService.getSong($stateParams.id)
        .then(setSong)
        .finally(() => loaderService.loading = false);

    function setSong(song) {
        editorService.song = song;
        editorService.songUrl = songRestService.getSongUrl(song);
        editorService.animationData = JSON.parse(song.animationData);
        editorService.currentChannel = editorService.animationData.channels[0];
    }
}

module.exports = {
    name: 'EditorCtrl',
    fn: EditorCtrl
};
