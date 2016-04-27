/*global angular*/
(function () {
    'use strict';

    angular.module('ledStripAnimator.component')
        .filter('trustedHtml', function($sce) {
            'ngInject';

            return $sce.trustAsHtml;
        });
}());