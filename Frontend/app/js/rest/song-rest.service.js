function songRestService(Restangular) {
    'ngInject';

    const restService = Restangular.service('song');

    return {
        getSongs: getSongs,
        getSong: getSong,
        saveSong: saveSong,
        deleteSong: deleteSong,
        renderSong: renderSong
    };

    function getSongs() {
        return restService.getList();
    }

    function getSong(id) {
        return restService.one(id).get();
    }

    function saveSong(file, songData) {
        var formData = new FormData();
        formData.append('song', JSON.stringify(songData));
        formData.append('mp3', file);

        const httpConfig = {transformRequest: angular.identity};
        return restService.one().withHttpConfig(httpConfig)
            .customPUT(formData, undefined, undefined, {'Content-Type': undefined});
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
