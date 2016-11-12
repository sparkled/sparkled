function animationEffect(editorConstants,
                         editorService,
                         $timeout) {
    'ngInject';

    return {
        restrict: 'E',
        replace: true,
        templateUrl: 'editor/timeline/animation-effect/animation-effect.directive.html',
        scope: {
            channel: '=',
            effect: '='
        },
        link: (scope, element) => {
            scope.editorConstants = editorConstants;
            scope.editorService = editorService;
            scope.setCurrentEffect = () => {
                editorService.currentChannel = scope.channel;
                editorService.currentEffect = scope.effect;
            };

            $(element).resizable({
                handles: 'e, w',
                grid: [editorConstants.pixelsPerFrame, 1],
                start: () => {
                    $timeout(onDrag);
                },
                stop: (event, ui) => {
                    $timeout(() => onDrop(ui));
                }
            });

            $(element).draggable({
                distance: editorConstants.pixelsPerFrame + 1,
                grid: [editorConstants.pixelsPerFrame , 1],
                axis: 'x',
                start: () => {
                    $timeout(onDrag);
                },
                stop: (event, ui) => {
                    $timeout(() => onDrop(ui));
                }
            });

            function onDrag() {
                scope.setCurrentEffect();
                editorService.effectMoving = true;
            }

            function onDrop(ui) {
                editorService.effectMoving = false;

                const duration = Math.round($(element).width() / editorConstants.pixelsPerFrame);
                const scrollLeft = $('.channels').scrollLeft();
                const elementLeft = $(element).position().left;
                const newStartFrame = Math.round((scrollLeft + elementLeft) / editorConstants.pixelsPerFrame);
                const newEndFrame = newStartFrame + duration;
                const validPlacement = isValidPlacement(newStartFrame, newEndFrame);

                if (validPlacement) {
                    scope.effect.startFrame = newStartFrame;
                    scope.effect.endFrame = newEndFrame;
                    ensureOrderedElements();
                } else {
                    ui.originalPosition && element.css(ui.originalPosition);
                    ui.originalSize && element.css(ui.originalSize);
                }
            }

            // Ensure new effect position is within the timeline and not colliding with another effect.
            function isValidPlacement(newStartFrame, newEndFrame) {
                const frameCount = editorService.song.durationSeconds * editorConstants.framesPerSecond;
                const inBounds = newStartFrame > 0 && newEndFrame < frameCount;
                return inBounds && !isColliding(newStartFrame, newEndFrame);
            }

            function isColliding(newStartFrame, newEndFrame) {
                for (var i = 0; i < scope.channel.effects.length; i++) {
                    const channelEffect = scope.channel.effects[i];

                    if (channelEffect === scope.effect) {
                        continue;
                    }

                    const coords = {
                        r1left: channelEffect.startFrame,
                        r1right: channelEffect.endFrame,
                        r2left: newStartFrame,
                        r2right: newEndFrame
                    };

                    if (coords.r1left < coords.r2right && coords.r1right > coords.r2left) {
                        return true;
                    }
                }

                return false;
            }

            function ensureOrderedElements() {
                scope.channel.effects.sort((lhs, rhs) => lhs.startFrame - rhs.startFrame);
            }
        }
    }
}

module.exports = {
    name: 'animationEffect',
    fn: animationEffect
};
