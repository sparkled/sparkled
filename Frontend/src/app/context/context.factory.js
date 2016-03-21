/*global angular*/
(function () {
    'use strict';

    angular.module('ledStripAnimator.context', [])
        .factory('ContextService', ContextService);

    function ContextService() {
        return {
            songs: null,
            currentSong: null
        }
    }
}());
