/*global angular*/
(function () {
    'use strict';

    angular.module('ledStripAnimator.selector')
        .controller('SelectorController', SelectorController);

    function SelectorController($rootScope, RestService) {
        var self = this;
        this.songs = null;

        this.removeSong = function (song) {
            sweetAlert({
                    title: 'Remove Song',
                    text: 'Are you sure you want to remove this song?',
                    type: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    cancelButtonColor: '#3085d6',
                    confirmButtonText: 'Remove',
                    closeOnConfirm: false
                },
                function (isConfirm) {
                    if (isConfirm) {
                        song.remove().then(function () {
                            var songIndex = self.songs.indexOf(song);
                            self.songs.splice(songIndex, 1);
                            swal(
                                'Song Deleted',
                                'Your song has been deleted.',
                                'success'
                            );
                        });
                    }
                });
        };

        this.addSong = function (file, songData) {
            var formData = new FormData();
            formData.append('song', JSON.stringify(songData));
            formData.append('mp3', file);

            RestService.one('song').withHttpConfig({
                    transformRequest: angular.identity
                })
                .customPUT(formData, undefined, undefined, {'Content-Type': undefined}).then(function (response) {
                self.getAllSongs();
                sweetAlert(
                    'Song Added',
                    'Your song has been added.',
                    'success'
                );
            });
        };

        this.getAllSongs = function () {
            RestService.service('song').getList().then(function (songs) {
                self.songs = songs;
                $rootScope.$broadcast('loading', {loading: false});
            });
        };

        this.getAllSongs();
    }
}());
