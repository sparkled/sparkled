const angular = require('angular');

function songRestService(Restangular) {
    'ngInject';

    const restService = Restangular.service('song');

    return {
        getSongUrl: getSongUrl,
        getSongs: getSongs,
        getSong: getSong,
        saveMp3: saveMp3,
        saveSong: saveSong,
        deleteSong: deleteSong,
        renderSong: renderSong
    };

    function getSongUrl(song) {
        return `${Restangular.configuration.baseUrl}/song/data/${song.id}`;
    }

    function getSongs() {
        return restService.getList();
    }

    function getSong(id) {
        return restService.one(id).get();
    }

    function saveMp3(file, songData) {
        var formData = new FormData();
        formData.append('song', JSON.stringify(songData));
        formData.append('mp3', file);

        const httpConfig = {transformRequest: angular.identity};
        return restService.one().withHttpConfig(httpConfig)
            .customPUT(formData, undefined, undefined, {'Content-Type': undefined});
    }

    function saveSong(song, animationData) {
        song.animationData = angular.toJson(animationData);

        return restService.one().withHttpConfig({
            transformRequest: angular.identity
        }).customPOST(JSON.stringify(song), undefined, undefined, {'Content-Type': 'application/json'});
    }

    function deleteSong(song) {
        return restService.one()
            .customDELETE(song.id, undefined, undefined, undefined);
    }

    function renderSong(song, durationSeconds, startFrame) {
        return restService.one('render')
            .customGET(song.id, {
                'duration-seconds': durationSeconds,
                'start-frame': startFrame
            });
    }
}

module.exports = {
    name: 'songRestService',
    fn: songRestService
};
