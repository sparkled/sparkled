function editorService(editorConstants) {
    'ngInject';

    const service = {
        song: {},
        songUrl: undefined,
        animationData: {},
        currentChannel: {},
        currentEffect: {},
        currentFrame: 0,
        effectMoving: false,
        setCurrentFrame: setCurrentFrame
    };

    return service;

    function setCurrentFrame(newFrame) {
        newFrame = Math.round(newFrame);
        const minFrame = 0;
        const maxFrame = service.song.durationSeconds * editorConstants.framesPerSecond - 1;
        service.currentFrame = Math.min(Math.max(newFrame, minFrame), maxFrame);
    }
}

module.exports = {
    name: 'editorService',
    fn: editorService
};
