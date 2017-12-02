const angular = require('angular');

function musicPlayerRestService(Restangular) {
    'ngInject';

    const restService = Restangular.service('musicPlayer');

    return {
        stopCurrentSong: stopCurrentSong
    };

    function stopCurrentSong() {
        return restService.one('currentSong').customDELETE();
    }
}

module.exports = {
    name: 'musicPlayerRestService',
    fn: musicPlayerRestService
};
