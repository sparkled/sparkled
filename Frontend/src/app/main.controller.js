/*global angular*/
(function () {
    'use strict';
    angular.module('ledStripAnimator.core')
        .controller('MainCtrl', function ($rootScope, $scope) {
            'ngInject';

            var self = this;
            this.loading = true;

            $rootScope.$on('$stateChangeStart', function () {
                self.loading = true;
            });

            $scope.$on('loading', function(event, args) {
                self.loading = args.loading;
            });
        });
})();