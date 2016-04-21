/*global angular*/
(function () {
    'use strict';
    angular.module('ledStripAnimator.component')
        .directive('effectChannel', function () {
            return {
                restrict: 'E',
                replace: true,
                templateUrl: 'app/components/directives/effectChannel.template.html',
                scope: {
                    duration: '=',
                    channel: '=',
                    currentFrame: '=',
                    getCurrentEffect: '=',
                    setCurrentEffect: '='
                },
                link: function (scope, element, attrs) {
                    scope.width = 0;

                    $(element).click(function (event) {
                        scope.$apply(function () {
                            var currentFrame = (event.originalEvent.offsetX - 2) / 2;
                            scope.currentFrame = 2 * Math.floor(currentFrame / 2);
                        });
                    });

                    scope.$watch('duration', function (newVal) {
                        if (newVal != null) {
                            var frameCount = newVal * 60;
                            var width = frameCount * 2 + 1;
                            $(element).css('width', width);
                        }
                    }, true);
                }
            }
        });
})();
