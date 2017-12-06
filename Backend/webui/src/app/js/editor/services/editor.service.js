function editorService(editorConstants,
                       toastr,
                       _) {
    'ngInject';

    const service = {
        song: {},
        songUrl: null,
        animationData: {},
        currentChannel: {},
        currentEffect: {},
        currentFrame: 0,
        effectMoving: false,
        stageExpanded: true,
        setCurrentFrame: setCurrentFrame,
        addAnimationEffect: addAnimationEffect,
        deleteCurrentEffect: deleteCurrentEffect
    };

    return service;

    function setCurrentFrame(newFrame) {
        newFrame = Math.round(newFrame);
        const minFrame = 0;
        const maxFrame = service.song.durationFrames - 1;
        service.currentFrame = Math.min(Math.max(newFrame, minFrame), maxFrame);
    }

    function addAnimationEffect(effect) {
        if (!effect) {
            effect = newEffect();
        }

        const effects = service.currentChannel.effects;
        const effectCount = effects.length;
        const insertionIndex = getInsertionIndex(effects);
        const songDuration = service.song.durationFrames;

        effect.endFrame = Math.min(effect.endFrame, songDuration);

        if (insertionIndex < effectCount) {
            const nextElement = effects[insertionIndex];

            if (service.currentFrame >= nextElement.startFrame && service.currentFrame <= nextElement.endFrame) {
                toastr.error('There is already an animation effect on the current frame.');
                return;
            }

            effect.endFrame = Math.min(effect.endFrame, nextElement.startFrame - 1);
        }

        const effectDuration = effect.endFrame - effect.startFrame + 1;
        if (effectDuration < editorConstants.minimumEffectFrames) {
            toastr.error('There is not enough room to add an animation effect.');
            return;
        }

        addEffectToChannel(effect, insertionIndex);
    }

    function newEffect() {
        return {
            type: '',
            params: [],
            easing: 'LINEAR',
            fill: {
                type: '',
                params: []
            },
            startFrame: service.currentFrame,
            endFrame: service.currentFrame + editorConstants.minimumEffectFrames - 1,
            repetitions: 1,
            reverse: false
        };
    }

    function getInsertionIndex(effects) {
        for (var i = 0; i < effects.length; i++) {
            if (effects[i].endFrame > service.currentFrame) {
                return i;
            }
        }

        return effects.length;
    }

    function addEffectToChannel(effect, insertionIndex) {
        service.currentChannel.effects.splice(insertionIndex, 0, effect);
        service.currentEffect = effect;
    }

    function deleteCurrentEffect() {
        _.remove(service.currentChannel.effects, effect => {
            return effect === service.currentEffect;
        });
    }
}

module.exports = {
    name: 'editorService',
    fn: editorService
};
