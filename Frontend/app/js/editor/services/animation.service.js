function animationService(editorService,
                          songRestService,
                          $timeout) {
    'ngInject';

    const durationSeconds = 5;
    const startFrame = 0;

    var service = {
        running: false,
        previewAnimation: previewAnimation,
        getFrame: getFrame,
        cancelAnimation: cancelAnimation,
        renderData: [],
        animationStartTime: undefined
    };

    function previewAnimation() {
        if (!service.running) {
            songRestService.renderSong(editorService.song, durationSeconds, startFrame)
                .then(renderData => {
                    service.running = true;
                    service.animationStartTime = new Date().getTime();
                    service.renderData = renderData;
                });
        }
    }

    function getFrame() {
        const now = new Date().getTime();
        const frameCount = service.renderData.length;
        const durationMs = 1000 / 60 * frameCount;
        const frameIndex = Math.floor((now - service.animationStartTime) / durationMs * frameCount);

        if (!service.running || frameIndex >= frameCount) {
            $timeout(() => service.running = false);
            return undefined;
        }

        return service.renderData[frameIndex];
    }

    function cancelAnimation() {
        service.running = false;
    }

    return service;
}

module.exports = {
    name: 'animationService',
    fn: animationService
};
