function SelectorCtrl(loaderService,
                      songRestService) {
    'ngInject';

    loaderService.state.loading = true;

    const vm = this;
    vm.songs = [];

    songRestService.getSongs()
        .then(songs => vm.songs = songs)
        .finally(() => loaderService.state.loading = false);
}

module.exports = {
    name: 'SelectorCtrl',
    fn: SelectorCtrl
};
