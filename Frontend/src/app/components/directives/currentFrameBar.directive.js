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
                }
            }
        });
})();
