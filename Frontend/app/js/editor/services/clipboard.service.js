function clipboardService(editorService,
                          _) {
    'ngInject';

    var copiedEffect = null;

    return {
        copyCurrentEffect: copyCurrentEffect,
        pasteCopiedEffect: pasteCopiedEffect
    };

    function copyCurrentEffect() {
        copiedEffect = _.cloneDeep(editorService.currentEffect);
    }

    function pasteCopiedEffect() {
        if (copiedEffect) {
            const newEffect = _.cloneDeep(copiedEffect);
            const frameDifference = editorService.currentFrame - newEffect.startFrame;
            newEffect.startFrame += frameDifference;
            newEffect.endFrame += frameDifference;
            editorService.addAnimationEffect(newEffect);
        }
    }
}

module.exports = {
    name: 'clipboardService',
    fn: clipboardService
};
