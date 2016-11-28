function editorService(editorConstants,
                       toastr,
                       _) {
    'ngInject';

    const service = {
        song: {},
        songUrl: undefined,
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
        const maxFrame = service.song.durationSeconds * editorConstants.framesPerSecond - 1;
        service.currentFrame = Math.min(Math.max(newFrame, minFrame), maxFrame);
    }

    function addAnimationEffect() {
        const effects = service.currentChannel.effects;
        const effectCount = effects.length;
        const insertionIndex = getInsertionIndex(effects);

        // Ensure effect doesn't go past end of song or collide with another frame.
        var endFrame = Math.min(
            service.currentFrame + editorConstants.minimumEffectFrames,
            service.song.durationSeconds * editorConstants.framesPerSecond
        );

        if (insertionIndex < effectCount) {
            const nextElement = effects[insertionIndex];

            if (service.currentFrame >= nextElement.startFrame && service.currentFrame <= nextElement.endFrame) {
                toastr.error('There is already an animation effect on the current frame.');
                return;
            }

            endFrame = Math.min(endFrame, nextElement.startFrame);
        }

        if (endFrame - service.currentFrame < editorConstants.minimumEffectFrames) {
            toastr.error(`Effect must be at least ${editorConstants.minimumEffectFrames} frames long.`);
            return;
        }

        addEffectToChannel(insertionIndex, endFrame);
    }

    function getInsertionIndex(effects) {
        for (var i = 0; i < effects.length; i++) {
            if (effects[i].endFrame > service.currentFrame) {
                return i;
            }
        }

        return effects.length;
    }

    function addEffectToChannel(insertionIndex, endFrame) {
        var newEffect = {
            effectType: '',
            easingType: 'LINEAR',
            startFrame: service.currentFrame,
            endFrame: endFrame,
            repetitions: 1,
            reverse: false,
            params: []
        };

        service.currentChannel.effects.splice(insertionIndex, 0, newEffect);
        service.currentEffect = newEffect;
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
