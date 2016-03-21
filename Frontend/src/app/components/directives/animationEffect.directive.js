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
                    effect: '='
                }
            }
        });
})();

