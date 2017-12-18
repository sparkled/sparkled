function EditorCtrl(animationService,
                    clipboardService,
                    editorService,
                    hotkeys,
                    loaderService,
                    songRestService,
                    $scope,
                    song,
                    songAnimation) {
    'ngInject';

    const vm = this;
    vm.editorService = editorService;
    vm.animationService = animationService;

    // Loading is completed when the MP3 is loaded by the waveform directive.
    loaderService.loading = true;

    editorService.song = song;
    editorService.songUrl = songRestService.getSongUrl(song);
    editorService.animationData = JSON.parse(songAnimation.animationData);
    editorService.currentChannel = editorService.animationData.channels[0];

    hotkeys.bindTo($scope)
        .add({
            combo: 'left',
            description: 'Previous Frame',
            callback: event => {
                if (!animationService.running) {
                    editorService.setCurrentFrame(editorService.currentFrame - 1);
                }
                event.preventDefault();
            }
        })
        .add({
            combo: 'alt+left',
            description: 'Previous Second',
            callback: event => {
                if (!animationService.running) {
                    const newFrame = nextSecond(editorService.currentFrame - editorService.song.framesPerSecond - 1);
                    editorService.setCurrentFrame(newFrame);
                }
                event.preventDefault();
            }
        })
        .add({
            combo: 'right',
            description: 'Next Frame',
            callback: event => {
                if (!animationService.running) {
                    editorService.setCurrentFrame(editorService.currentFrame + 1);
                }
                event.preventDefault();
            }
        })
        .add({
            combo: 'alt+right',
            description: 'Next Second',
            callback: event => {
                if (!animationService.running) {
                    const newFrame = nextSecond(editorService.currentFrame);
                    editorService.setCurrentFrame(newFrame);
                }
                event.preventDefault();
            }
        })
        .add({
            combo: 'mod+c',
            description: 'Copy Effect',
            callback: event => {
                if (!animationService.running) {
                    clipboardService.copyCurrentEffect();
                }
                event.preventDefault();
            }
        })
        .add({
            combo: 'mod+v',
            description: 'Paste Effect',
            callback: event => {
                if (!animationService.running) {
                    clipboardService.pasteCopiedEffect();
                }
                event.preventDefault();
            }
        })
        .add({
            combo: 'backspace',
            description: 'Delete Selected Animation Effect',
            callback: event => {
                if (!animationService.running) {
                    editorService.deleteCurrentEffect();
                }
                event.preventDefault();
            }
        })
        .add({
            combo: 'space',
            description: 'Preview Animation',
            callback: event => {
                if (!animationService.running) {
                    animationService.previewAnimation();
                }
                event.preventDefault();
            }
        })
        .add({
            combo: 'escape',
            description: 'Cancel Animation Preview',
            callback: event => {
                animationService.cancelAnimation();
                event.preventDefault();
            }
        });

    function nextSecond(currentFrame) {
        let newFrame = currentFrame + editorService.song.framesPerSecond;
        newFrame -= newFrame % editorService.song.framesPerSecond;
        return newFrame;
    }
}

module.exports = {
    name: 'EditorCtrl',
    fn: EditorCtrl
};
