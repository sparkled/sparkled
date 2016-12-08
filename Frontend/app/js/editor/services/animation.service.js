function animationService(editorService,
                          songRestService,
                          $timeout,
                          _) {
    'ngInject';

    var animationStartFrame = undefined;
    const durationSeconds = 20;

    var service = {
        running: false,
        previewAnimation: previewAnimation,
        getFrame: getFrame,
        cancelAnimation: cancelAnimation,
        renderData: {},
        animationStartTime: undefined
    };

    function previewAnimation() {
        if (!service.running) {
            animationStartFrame = editorService.currentFrame;
            songRestService.saveSong(editorService.song, editorService.animationData).then(renderSong);
        }
    }

    function renderSong() {
        const durationFrames = durationSeconds * editorService.song.framesPerSecond;
        songRestService.renderSong(editorService.song, durationFrames, editorService.currentFrame)
            .then(renderData => {
                service.running = true;
                service.animationStartTime = new Date().getTime();
                service.renderData = renderData.plain();
            });
    }

    function getFrame() {
        const now = new Date().getTime();

        const firstKey = Object.keys(service.renderData)[0];
        const frameCount = service.renderData[firstKey].frames.length;
        const durationMs = 1000 / editorService.song.framesPerSecond * frameCount;
        const frameIndex = Math.floor((now - service.animationStartTime) / durationMs * frameCount);

        if (!service.running || frameIndex >= frameCount) {
            $timeout(cancelAnimation);
            return undefined;
        }

        return buildFrame(frameIndex);
    }

    function cancelAnimation() {
        if (animationStartFrame !== undefined) {
            editorService.setCurrentFrame(animationStartFrame);
            animationStartFrame = undefined;
        }
        service.running = false;
    }

    function buildFrame(frameIndex) {
        const frame = {};

        _(service.renderData).forOwn((value, key) => {
            frame[key] = value.frames[frameIndex].leds;
        });

        return frame;
    }

    return service;
}

module.exports = {
    name: 'animationService',
    fn: animationService
};
