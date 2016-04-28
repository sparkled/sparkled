/*global angular*/
(function () {
    'use strict';
    angular.module('sparkled.component')
        .directive('songEntry', function () {
            return {
                restrict: 'E',
                scope: {
                    song: '=',
                    edit: '&',
                    remove: '&'
                },
                replace: true,
                templateUrl: 'app/components/directives/songEntry.template.html'
            }
        });
})();