/*global angular*/
(function () {
    'use strict';

    angular
        .module('ledStripAnimator.core')
        .factory('applicationContextService', applicationContextService);

    function applicationContextService() {

        return {
            getVar: getVar
        };

        function getVar() {
            return "someVar";
        }
    }
}());
