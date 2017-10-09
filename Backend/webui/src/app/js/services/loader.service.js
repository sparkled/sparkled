function loaderService() {
    const vm = this;
    vm.loading = false;
}

module.exports = {
    name: 'loaderService',
    fn: loaderService
};
