function editorService() {
    'ngInject';

    return {
        song: undefined,
        songUrl: undefined,
        animationData: {},
        currentChannel: {},
        currentEffect: {},
        currentFrame: 0
    };
}

module.exports = {
    name: 'editorService',
    fn: editorService
};
