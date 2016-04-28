/*global angular*/
(function () {
    'use strict';

    angular.module('sparkled.component')
        .filter('trustedHtml', function($sce) {
            'ngInject';

            return $sce.trustAsHtml;
        });
}());