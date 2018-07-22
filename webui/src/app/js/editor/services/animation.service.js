function animationService(editorService,
                          songRestService,
                          $timeout,
                          _) {
    'ngInject';

    var animationStartFrame = undefined;
    const durationSeconds = 5;

    var service = {
        running: false,
        playbackSpeed: 1,
        previewAnimation: previewAnimation,
        getFrame: getFrame,
        cancelAnimation: cancelAnimation,
        renderData: {},
        animationStartTime: undefined
    };

    function previewAnimation() {
        if (!service.running) {
            animationStartFrame = editorService.currentFrame;

            const durationFrames = durationSeconds * editorService.song.framesPerSecond;
            songRestService.renderSong({songId: editorService.song.id, animationData: JSON.stringify(editorService.animationData)}, animationStartFrame, durationFrames).then(renderData => {
                service.running = true;
                service.animationStartTime = new Date().getTime();
                service.renderData = renderData.plain();
            });
        }
    }

    function getFrame() {
        const now = new Date().getTime();

        const firstKey = Object.keys(service.renderData)[0];
        const channel = service.renderData[firstKey];
        const frameCount = channel.data.length / channel.ledCount / 3;
        const durationMs = 1000 / editorService.song.framesPerSecond * frameCount;
        const frameIndex = Math.floor((now - service.animationStartTime) / durationMs * service.playbackSpeed * frameCount);

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
            const ledData = value.data;
            frame[key] = [];

            const start = frameIndex * value.ledCount * 3;
            for (let i = start; i < ledData.length; i += 3) {
                frame[key].push({
                    r: ledData[i],
                    g: ledData[i + 1],
                    b: ledData[i + 2]
                });
            }
        });

        return frame;
    }

    return service;
}

module.exports = {
    name: 'animationService',
    fn: animationService
};
