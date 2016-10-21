function selectorConfig(applicationStates,
                        $stateProvider) {
    'ngInject';

    $stateProvider
        .state(applicationStates.selector, {
            url: '/selector',
            controller: 'SelectorCtrl as selector',
            templateUrl: 'selector/selector.html',
            title: 'Selector'
        });
}

module.exports = selectorConfig;
