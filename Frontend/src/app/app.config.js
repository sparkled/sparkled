/*global angular*/
(function () {
    'use strict';

    function resourceConfig($resourceProvider) {
        $resourceProvider.defaults.timeout = 5000;
    }

    angular.module('ledStripAnimator').config(resourceConfig);
}());
