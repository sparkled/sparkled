/*global angular*/
(function () {
    'use strict';
    angular.module('sparkled.core')
        .controller('MainCtrl', function ($rootScope, $scope) {
            'ngInject';

            var self = this;
            this.loading = true;

            var unregisterStateChangeStart = $rootScope.$on('$stateChangeStart', function () {
                self.loading = true;
            });

            $scope.$on('$destroy', function () {
                unregisterStateChangeStart();
            });

            $scope.$on('loading', function(event, args) {
                self.loading = args.loading;
            });
        });
})();