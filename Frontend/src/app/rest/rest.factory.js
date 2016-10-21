/*global angular*/
(function () {
    'use strict';

    angular.module('sparkled.rest')
        .factory('RestService', RestService);

    function RestService(Restangular) {
        'ngInject';

        Restangular.setBaseUrl('http://localhost:8080/rest/');
        return Restangular;
    }
}());
