/*global angular*/
(function () {
    'use strict';

    angular.module('ledStripAnimator.editor')
        .controller('EditorController', EditorController);

    function EditorController($stateParams, RestService) {
        var self = this;
        this.animationEffects = null;
        this.currentSong = null;
        this.currentFrame = 0;
        this.mp3Url = null;

        this.getCurrentSong = function () {
            RestService.one('song', $stateParams.id).get().then(function (song) {
                self.currentSong = song;
                self.mp3Url = RestService.configuration.baseUrl + '/song/data/' + song.id;
            });
        };

        this.getAllAnimationEffects = function () {
            RestService.service('animation-effect').getList().then(function (animationEffects) {
                self.animationEffects = animationEffects;
            });
        };

        this.getCurrentSong();
        this.getAllAnimationEffects();
    }
}());
