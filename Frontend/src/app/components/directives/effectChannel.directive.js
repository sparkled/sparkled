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
                    getCurrentEffect: '=',
                    setCurrentEffect: '='
                },
                link: function (scope, element, attrs) {
                    scope.width = 0;

                    scope.$watch('duration', function (newVal, oldVal) {
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
