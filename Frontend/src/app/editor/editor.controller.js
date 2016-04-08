/*global angular*/
(function () {
    'use strict';

    angular.module('ledStripAnimator.editor')
        .controller('EditorController', EditorController);

    function EditorController($scope, $stateParams, hotkeys, RestService) {
        var self = this;
        this.animationEffects = null;
        this.currentSong = null;
        this.currentFrame = 0;
        this.mp3Url = null;

        hotkeys.bindTo($scope)
            .add({
                combo: 'alt+left',
                description: 'Previous Frame',
                callback: function (event) {
                    if (self.currentFrame > 0) {
                        self.currentFrame--;
                    }

                    event.preventDefault();
                }
            })
            .add({
                combo: 'ctrl+alt+left',
                description: 'Previous Second',
                callback: function (event) {
                    if (self.currentFrame > 60) {
                        self.currentFrame = Math.ceil((self.currentFrame - 60) / 60) * 60;
                    } else if (self.currentFrame > 0) {
                        self.currentFrame = 0;
                    }

                    event.preventDefault();
                }
            })
            .add({
                combo: 'alt+right',
                description: 'Next Frame',
                callback: function (event) {
                    var duration = (self.currentSong.durationSeconds || 0) * 60;
                    if (self.currentFrame < duration - 1) {
                        self.currentFrame++;
                    }

                    event.preventDefault();
                }
            })
            .add({
                combo: 'ctrl+alt+right',
                description: 'Next Second',
                callback: function (event) {
                    var duration = (self.currentSong.durationSeconds || 0) * 60;

                    if (self.currentFrame < duration - 61) {
                        self.currentFrame = Math.floor((self.currentFrame + 60) / 60) * 60;
                    } else if (self.currentFrame > 0) {
                        self.currentFrame = duration - 1;
                    }

                    event.preventDefault();
                }
            });

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
