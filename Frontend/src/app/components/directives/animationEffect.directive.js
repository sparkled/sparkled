/*global angular*/
(function () {
    'use strict';
    angular.module('ledStripAnimator.component')
        .directive('animationEffect', function () {
            return {
                restrict: 'E',
                replace: true,
                templateUrl: 'app/components/directives/animationEffect.template.html',
                scope: {
                    channel: '=',
                    effect: '=',
                    duration: '='
                },
                link: function (scope, element, attrs) {
                    $(element).draggable({
                        stop: function () {
                            handleDrop(scope, element);
                        }
                    });
                }
            }
        });

    var handleDrop = function (scope, element) {
        var duration = scope.effect.endFrame - scope.effect.startFrame,
            newStartFrame = Math.round(
                ($(element).closest('.channels').scrollLeft() + $(element).position().left) / 2
            ),
            newEndFrame = newStartFrame + duration,
            validPlacement = isValidPlacement(scope, newStartFrame, newEndFrame);

        // Remove css added by jQuery, let Angular recalculate it.
        $(element).css('top', '').css('left', '');

        if (validPlacement) {
            scope.$apply(function () {
                scope.effect.startFrame = newStartFrame;
                scope.effect.endFrame = newEndFrame;
            });
        } else {
            // Force angular to revert the effect's position to before the drag.
            scope.$apply(function () {
                scope.effect.endFrame--;
            });
            scope.$apply(function () {
                scope.effect.endFrame++;
            });
        }
    };

    // Ensure new effect position is within the timeline and not colliding with another effect.
    var isValidPlacement = function (scope, newStartFrame, newEndFrame) {
        var frameCount = scope.duration * 60,
            validPlacement = true;

        if (newStartFrame < 0 || newEndFrame > frameCount) {
            validPlacement = false;
        } else {
            validPlacement = !isColliding(scope, newStartFrame, newEndFrame);
        }

        return validPlacement;
    };

    var isColliding = function (scope, newStartFrame, newEndFrame) {
        for (var i = 0; i < scope.channel.effects.length - 1; i++) {
            var channelEffect = scope.channel.effects[i];

            var coords = {
                r1left: channelEffect.startFrame,
                r1right: channelEffect.endFrame,
                r2left: newStartFrame,
                r2right: newEndFrame
            };

            if ((coords.r2left <= coords.r1right && coords.r2right >= coords.r1left)) {
                return true;
            }
        }

        return false;
    }
})();

