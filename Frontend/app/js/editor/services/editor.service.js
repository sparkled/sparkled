function editorService(editorConstants) {
    'ngInject';

    const service = {
        song: undefined,
        songUrl: undefined,
        animationData: {},
        currentChannel: {},
        currentEffect: {},
        currentFrame: 1,
        setCurrentFrame: setCurrentFrame
    };

    return service;

    function setCurrentFrame(newFrame) {
        newFrame = Math.round(newFrame);
        const minFrame = 1;
        const maxFrame = service.song.durationSeconds * editorConstants.framesPerSecond;
        service.currentFrame = Math.min(Math.max(newFrame, minFrame), maxFrame);
    }
}

module.exports = {
    name: 'editorService',
    fn: editorService
};
