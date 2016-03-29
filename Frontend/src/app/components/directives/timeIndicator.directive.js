/*global angular*/
(function () {
    'use strict';
    angular.module('ledStripAnimator.component')
        .directive('timeIndicator', function () {
            return {
                restrict: 'E',
                replace: true,
                templateUrl: 'app/components/directives/timeIndicator.template.html',
                scope: {
                    duration: '='
                },
                link: function (scope, element, attrs) {
                    scope.width = 0;
                    scope.patternWidth = 0;
                    scope.seconds = [];

                    scope.$watch('duration', function (newVal, oldVal) {
                        if (newVal != null) {
                            for (var i = 0; i <= newVal; i++) {
                                scope.seconds.push(i);
                            }

                            var frameCount = newVal * 60;
                            scope.width = frameCount * 2 + 1;
                            scope.patternWidth = frameCount * 2 + 1;
                            if (scope.patternWidth > .5) {
                                scope.patternWidth -= .5;
                            }
                        }
                    }, true);
                }
            }
        });
})();
