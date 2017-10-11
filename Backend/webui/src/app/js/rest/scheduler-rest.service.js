const angular = require('angular');

function schedulerRestService(Restangular) {
    'ngInject';

    const restService = Restangular.service('scheduledSongs');

    return {
        getScheduledSongs: getScheduledSongs,
        scheduleSong: scheduleSong,
        unscheduleSong: unscheduleSong
    };

    function getScheduledSongs(date) {
        return restService.one().get({date: date});
    }

    function scheduleSong(scheduledSong) {
        const payload = JSON.stringify(scheduledSong);
        return restService.one().withHttpConfig({
            transformRequest: angular.identity
        }).customPOST(payload, null, null, {'Content-Type': 'application/json'});
    }

    function unscheduleSong(scheduledSong) {
        return restService.one().customDELETE(scheduledSong.id);
    }
}

module.exports = {
    name: 'schedulerRestService',
    fn: schedulerRestService
};
