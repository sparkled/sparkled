function EditorCtrl(animationService,
                    clipboardService,
                    editorConstants,
                    editorService,
                    hotkeys,
                    loaderService,
                    songRestService,
                    $scope,
                    $stateParams) {
    'ngInject';

    const vm = this;
    vm.editorService = editorService;
    vm.animationService = animationService;

    // Loading is completed when the MP3 is loaded by the waveform directive.
    loaderService.loading = true;
    songRestService.getSong($stateParams.id).then(setSong);

    function setSong(song) {
        editorService.song = song;
        editorService.songUrl = songRestService.getSongUrl(song);
        editorService.animationData = JSON.parse(song.animationData);
        editorService.currentChannel = editorService.animationData.channels[0];
    }

    hotkeys.bindTo($scope)
        .add({
            combo: 'left',
            description: 'Previous Frame',
            callback: function (event) {
                editorService.setCurrentFrame(editorService.currentFrame - 1);
                event.preventDefault();
            }
        })
        .add({
            combo: 'alt+left',
            description: 'Previous Second',
            callback: function (event) {
                const newFrame = nextSecond(editorService.currentFrame - editorConstants.framesPerSecond - 1);
                editorService.setCurrentFrame(newFrame);
                event.preventDefault();
            }
        })
        .add({
            combo: 'right',
            description: 'Next Frame',
            callback: function (event) {
                editorService.setCurrentFrame(editorService.currentFrame + 1);
                event.preventDefault();
            }
        })
        .add({
            combo: 'alt+right',
            description: 'Next Second',
            callback: function (event) {
                const newFrame = nextSecond(editorService.currentFrame);
                editorService.setCurrentFrame(newFrame);
                event.preventDefault();
            }
        })
        .add({
            combo: 'mod+c',
            description: 'Copy Effect',
            callback: function (event) {
                clipboardService.copyCurrentEffect();
                event.preventDefault();
            }
        })
        .add({
            combo: 'mod+v',
            description: 'Paste Effect',
            callback: function (event) {
                clipboardService.pasteCopiedEffect();
                event.preventDefault();
            }
        })
        .add({
            combo: 'backspace',
            description: 'Delete Selected Animation Effect',
            callback: function (event) {
                editorService.deleteCurrentEffect();
                event.preventDefault();
            }
        })
        .add({
            combo: 'space',
            description: 'Preview Animation',
            callback: function (event) {
                animationService.previewAnimation();
                event.preventDefault();
            }
        })
        .add({
            combo: 'escape',
            description: 'Cancel Animation Preview',
            callback: function (event) {
                animationService.cancelAnimation();
                event.preventDefault();
            }
        });

    function nextSecond(currentFrame) {
        var newFrame = currentFrame + editorConstants.framesPerSecond;
        newFrame -= newFrame % editorConstants.framesPerSecond;
        return newFrame;
    }
}

module.exports = {
    name: 'EditorCtrl',
    fn: EditorCtrl
};
