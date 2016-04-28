/*global angular*/
(function () {
    'use strict';

    angular.module('sparkled.component')
        .filter('time', function () {
            return function (input) {
                var minutes = Math.floor(input / 60);
                var seconds = input % 60;

                if (seconds < 10) {
                    seconds = "0" + seconds;
                }
                return minutes + ':' + seconds;
            };
        });
}());