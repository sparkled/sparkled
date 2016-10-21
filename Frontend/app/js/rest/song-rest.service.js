function songRestService(Restangular) {
    'ngInject';

    return {
        getSongs: getSongs
    };

    function getSongs() {
        return Restangular.service('song').getList();
    }
}

module.exports = {
    name: 'songRestService',
    fn: songRestService
};
