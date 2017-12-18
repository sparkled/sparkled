function schedulerConfig(applicationStates,
                        $stateProvider) {
    'ngInject';

    $stateProvider
        .state(applicationStates.scheduler, {
            url: '/scheduler',
            controller: 'schedulerCtrl as scheduler',
            templateUrl: 'scheduler/scheduler.html',
            title: 'scheduler'
        });
}

module.exports = schedulerConfig;
