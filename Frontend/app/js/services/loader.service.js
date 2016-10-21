function loaderService() {
    const vm = this;
    vm.state = {
        loading: true
    };
}

module.exports = {
    name: 'loaderService',
    fn: loaderService
};
