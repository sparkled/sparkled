/*global angular*/
(function () {
    'use strict';
    angular.module('sparkled.component')
        .directive('animationEffect', function ($rootScope) {
            'ngInject';

            return {
                restrict: 'E',
                replace: true,
                templateUrl: 'app/components/directives/animationEffect.template.html',
                scope: {
                    channel: '=',
                    effect: '=',
                    duration: '=',
                    setCurrentEffect: '='
                },
                link: function (scope, element, attrs) {
                    $(element).resizable({
                        handles: 'e, w',
                        grid: [2, 1],
                        start: function () {
                            broadcastElementDimensions($rootScope, element, true);
                        },
                        stop: function () {
                            broadcastElementDimensions($rootScope, element, false);
                            handleDrop(scope, element);
                        }
                    });

                    $(element).draggable({
                        distance: 3,
                        grid: [2, 1],
                        start: function () {
                            broadcastElementDimensions($rootScope, element, true);
                        },
                        stop: function () {
                            broadcastElementDimensions($rootScope, element, false);
                            handleDrop(scope, element);
                        }
                    });
                }
            }
        });

    var broadcastElementDimensions = function ($rootScope, element, isVisible) {
        $rootScope.$broadcast('animationEffectMove', {
            visible: isVisible,
            element: element
        });
    };

    var handleDrop = function (scope, element) {
        scope.setCurrentEffect(scope.channel, scope.effect);

        var duration = Math.round($(element).width() / 2),
            newStartFrame = Math.round(
                ($(element).closest('.channels').scrollLeft() + $(element).position().left) / 2
            ),
            newEndFrame = newStartFrame + duration,
            validPlacement = isValidPlacement(scope, newStartFrame, newEndFrame);

        // Remove offsets added by jQuery, let Angular recalculate them.
        $(element).css('top', '').css('left', '');

        if (validPlacement) {
            scope.$apply(function () {
                scope.effect.startFrame = newStartFrame;
                scope.effect.endFrame = newEndFrame;
                ensureOrderedElements(scope.channel);
            });
        }

        forceRedraw(scope);
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
        for (var i = 0; i < scope.channel.effects.length; i++) {
            var channelEffect = scope.channel.effects[i],
                coords;

            if (channelEffect === scope.effect) {
                continue;
            }

            coords = {
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
    };

    var ensureOrderedElements = function (channel) {
        channel.effects.sort(function compare(lhs, rhs) {
            return lhs.startFrame - rhs.startFrame;
        });
    };

    /* In the event of an placement or unchanged coordinates, force angular to reposition the element after resetting
    the offsets added by jQuery. */
    var forceRedraw = function (scope) {
        scope.$apply(function () {
            scope.effect.endFrame--;
        });
        scope.$apply(function () {
            scope.effect.endFrame++;
        });
    }
})();

