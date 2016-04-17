/*global angular*/
(function () {
    'use strict';

    angular.module('ledStripAnimator.rest')
        .factory('RestService', RestService);

    function RestService(Restangular) {
        Restangular.setBaseUrl('http://192.168.1.111:8080/xmas/rest/');
        return Restangular;
    }
}());
