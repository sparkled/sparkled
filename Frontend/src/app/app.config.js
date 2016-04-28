/*global angular*/
(function () {
    'use strict';

    function stateConfig($stateProvider, $urlRouterProvider) {
        'ngInject';

        addState($stateProvider, '/selector', 'selector', 'Selector');
        addState($stateProvider, '/editor/:id', 'editor', 'Editor');

        $urlRouterProvider.otherwise('/selector');
    }

    function addState($stateProvider, url, name, title) {
        $stateProvider.state(name, {
            url: url,
            templateUrl: 'app/' + name + '/' + name + '.html',
            data: {
                pageTitle: title
            }
        });
    }

    function resourceConfig($resourceProvider) {
        'ngInject';

        $resourceProvider.defaults.timeout = 5000;
    }

    angular.module('sparkled').config(stateConfig, resourceConfig);
}());
