const angular = require('angular');

function songRestService(Restangular) {
    'ngInject';

    const restService = Restangular.service('song');
    const renderRestService = Restangular.service('renderPreview');

    return {
        getSongUrl: getSongUrl,
        getSongs: getSongs,
        getSong: getSong,
        getSongAnimation: getSongAnimation,
        saveMp3: saveMp3,
        saveSong: saveSong,
        publishSong: publishSong,
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

    function getSongAnimation(songId) {
        return restService.one(songId).one('animation').get();
    }

    function saveMp3(file, songData) {
        const formData = new FormData();
        formData.append('song', JSON.stringify(songData));
        formData.append('mp3', file);

        const httpConfig = {transformRequest: angular.identity};
        return restService.one().withHttpConfig(httpConfig)
            .customPOST(formData, undefined, undefined, {'Content-Type': undefined});
    }

    function saveSong(song, animationData) {
        return restService.one(song.id).one('animation')
            .customPUT({songId: song.id, animationData: JSON.stringify(animationData)}, undefined, undefined, {});
    }

    function publishSong(song, animationData) {
        return restService.one(song.id).one('render')
            .customPUT({songId: song.id, animationData: JSON.stringify(animationData)}, undefined, undefined, {});
    }

    function deleteSong(song) {
        return restService.one().customDELETE(song.id);
    }

    function renderSong(songAnimation, startFrame, durationFrames) {
        const queryParams = {
            startFrame: startFrame,
            durationFrames: durationFrames
        };

        return renderRestService.one()
            .customPOST(songAnimation, void 0, queryParams, {});
    }
}

module.exports = {
    name: 'songRestService',
    fn: songRestService
};
