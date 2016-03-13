/*global angular*/
(function () {
    'use strict';
    angular.module('ledStripAnimator.component')
        .directive('songEntry', function () {
            return {
                restrict: 'E',
                scope: {
                    song: '=',
                    remove: '&'
                },
                replace: true,
                templateUrl: 'app/components/directives/songEntry.template.html'
            }
        });
})();