/*global angular*/
(function () {
    'use strict';

    angular.module('ledStripAnimator.component')
        .filter('cssClass', function () {
            return function (input) {
                return input == null ? '' : input.replace(/[^a-zA-Z\d]/g,'');
            };
        });
}());