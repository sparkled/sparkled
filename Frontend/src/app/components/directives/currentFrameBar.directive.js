/*global angular*/
(function () {
    'use strict';
    angular.module('ledStripAnimator.component')
        .directive('currentFrameBar', function () {
            return {
                restrict: 'E',
                replace: true,
                templateUrl: 'app/components/directives/currentFrameBar.template.html',
                scope: {
                    currentFrame: '='
                },
                link: function (scope, element, attrs) {
                    $(element).draggable({
                        distance: 3,
                        grid: [2, 1],
                        axis: 'x',
                        start: function () {
                            $('*:hover').css('cursor', 'col-resize');
                        },
                        drag: function () {
                            updateCurrentFrame(element, scope);
                        },
                        stop: function () {
                            $('*:hover').css('cursor', '');
                            updateCurrentFrame(element, scope);
                            forcePositionUpdate(element, scope);
                        }
                    });
                }
            }
        });

        var updateCurrentFrame = function(element, scope) {
            scope.$apply(function () {
                var left = $(element).css('left');
                scope.currentFrame = (parseInt(left)) / 2;
            });
        };

        var forcePositionUpdate = function(element, scope) {
            $(element).css('left', '');

            var currentFrame = scope.currentFrame;

            scope.$apply(function () {
                if (scope.currentFrame > 0) {
                    scope.currentFrame--;
                } else {
                    scope.currentFrame++;
                }
            });

            scope.$apply(function () {
                scope.currentFrame = currentFrame;
            });
        }
})();
