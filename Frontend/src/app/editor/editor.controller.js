/*global angular*/
(function () {
    'use strict';

    angular.module('ledStripAnimator.editor')
        .controller('EditorController', EditorController);

    function EditorController($scope, $stateParams, hotkeys, RestService) {
        var self = this;
        this.loading = true;
        this.animationEffectTypes = null;
        this.currentSong = null;
        this.currentChannel = null;
        this.currentEffect = null;
        this.animationData = [];
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
                self.animationData = JSON.parse(song.animationData);
                self.setCurrentChannel(self.animationData.channels[0]);
                self.mp3Url = RestService.configuration.baseUrl + '/song/data/' + song.id;
            });
        };

        this.setCurrentChannel = function (channel) {
            self.currentChannel = channel;
        };

        this.isCurrentChannel = function (channel) {
            return self.currentChannel === channel;
        };

        this.getAllAnimationEffectTypes = function () {
            RestService.service('animation-effect-type').getList().then(function (animationEffects) {
                self.animationEffects = animationEffects;
            });
        };

        this.addAnimationEffect = function () {
            var effectCount = self.currentChannel.effects.length,
                destIndex = effectCount;

            for (var i = 0; i < effectCount; i++) {
                if (self.currentChannel.effects[i].endFrame > self.currentFrame) {
                    destIndex = i;
                    break;
                }
            }

            // Ensure effect doesn't go past end of song or collide with another frame.
            var endFrame = Math.min(self.currentFrame + 15, self.currentSong.durationSeconds * 60);
            if (destIndex < effectCount) {
                var nextElementStartFrame = self.currentChannel.effects[destIndex].startFrame;

                if (self.currentFrame >= nextElementStartFrame) {
                    sweetAlert(
                        'Error',
                        'There is already an animation effect on the current frame.',
                        'error'
                    );
                    return;
                }

                endFrame = Math.min(endFrame, self.currentChannel.effects[destIndex].startFrame);
            }

            if (endFrame - self.currentFrame < 5) {
                sweetAlert(
                    'Error',
                    'For usability purposes, effects must be at least 5 frames long.',
                    'error'
                );

                return;
            }

            self.currentChannel.effects.splice(destIndex, 0, {
                effectType: '',
                startFrame: self.currentFrame,
                endFrame: endFrame,
                params: []
            });
        };

        this.setCurrentEffect = function (channel, effect) {
            self.setCurrentChannel(channel);
            self.currentEffect = effect;
        };

        this.isCurrentEffect = function (effect) {
            return self.currentEffect === effect;
        };

        this.removeAnimationEffect = function () {

        };

        this.saveAnimationData = function () {
            self.currentSong.animationData = angular.toJson(self.animationData);

            RestService.one('song').withHttpConfig({
                    transformRequest: angular.identity
                })
                .customPOST(JSON.stringify(self.currentSong), undefined, undefined, {'Content-Type': 'application/json'})
                .then(function (response) {
                    console.log('Saved');
                });
        };

        this.getCurrentSong();
        this.getAllAnimationEffectTypes();
    }
}());
