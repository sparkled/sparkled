function AppCtrl(loaderService) {
    'ngInject';

    const vm = this;
    vm.loader = loaderService.state;
}

module.exports = {
    name: 'AppCtrl',
    fn: AppCtrl
};
