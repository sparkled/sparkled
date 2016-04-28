/*global angular*/
(function () {
    'use strict';

    angular.module('sparkled.context', [])
        .factory('ContextService', ContextService);

    function ContextService() {
        return {
            songs: null,
            currentSong: null
        }
    }
}());
