/*global angular*/
(function () {
    'use strict';

    angular.module('ledStripAnimator.rest')
        .factory('RestService', RestService);

    function RestService(Restangular) {
        Restangular.setBaseUrl('http://localhost:8080/xmas/rest/');
        return Restangular;
    }
}());
