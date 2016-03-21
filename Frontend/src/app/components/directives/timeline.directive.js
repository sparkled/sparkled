/*global angular*/
(function () {
    'use strict';
    angular.module('ledStripAnimator.component')
        .directive('timeline', function () {
            return {
                restrict: 'E',
                replace: true,
                templateUrl: 'app/components/directives/timeline.template.html',
                scope: {
                    duration: '='
                },
                link: function (scope, element, attrs) {
                    scope.width = 0;

                    scope.$watch('duration', function (newVal, oldVal) {
                        if (newVal != null) {
                            var frameCount = (newVal - 1) * 60;
                            var width = frameCount * 2 + 1;
                            $(element).css('width', width);
                        }
                    }, true);
                }
            }
        });
})();
