function appConfig(applicationStates,
                   $compileProvider,
                   $locationProvider,
                   $stateProvider,
                   $urlRouterProvider) {
    'ngInject';

    if (process.env.NODE_ENV === 'production') {
        $compileProvider.debugInfoEnabled(false);
    }

    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });

    $stateProvider
        .state(applicationStates.page, {
            abstract: true,
            templateUrl: 'content.html'
        });

    $urlRouterProvider.otherwise('/selector');
}

module.exports = appConfig;
