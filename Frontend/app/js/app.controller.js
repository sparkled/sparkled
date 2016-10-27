function AppCtrl(loaderService) {
    'ngInject';

    const vm = this;
    vm.loaderService = loaderService;
}

module.exports = {
    name: 'AppCtrl',
    fn: AppCtrl
};
