function songRestService(Restangular) {
    'ngInject';

    const restService = Restangular.service('song');

    return {
        getSongs: getSongs,
        saveSong: saveSong,
        deleteSong: deleteSong
    };

    function getSongs() {
        return restService.getList();
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
}

module.exports = {
    name: 'songRestService',
    fn: songRestService
};
