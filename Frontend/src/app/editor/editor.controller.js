/*global angular*/
(function () {
    'use strict';

    angular.module('sparkled.editor')
        .controller('EditorController', EditorController);

    function EditorController($log, $rootScope, $scope, $state, $stateParams, hotkeys, RestService) {
        'ngInject';

        var self = this;
        this.stageSvg = null;
        this.stageExpanded = true;
        this.animationEffectTypes = null;
        this.currentSong = null;
        this.currentChannel = null;
        this.currentEffect = null;
        this.animationData = [];
        this.frameBeforeAnimation = null;
        this.currentFrame = 0;
        this.mp3Url = null;
        this.previewing = false;

        $scope.$watch('editor.currentFrame', function (newVal) {
            if (self.currentSong === null) {
                return;
            }

            var maxDuration = self.currentSong.durationSeconds * 60 - 1;
            if (newVal < 0) {
                self.currentFrame = 0;
            } else if (newVal > maxDuration) {
                self.currentFrame = maxDuration;
            }
        });

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
            })
            .add({
                combo: 'ctrl+backspace',
                description: 'Delete Selected Animation Effect',
                callback: function (event) {
                    self.deleteCurrentEffect();
                    event.preventDefault();
                }
            });

        this.getCurrentChannel = function () {
            return self.currentChannel;
        };

        this.setCurrentChannel = function (channel) {
            self.currentChannel = channel;
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
                    toastr['error']('There is already an animation effect on the current frame.');
                    return;
                }

                endFrame = Math.min(endFrame, self.currentChannel.effects[destIndex].startFrame);
            }

            var minFrames = 10;
            if (endFrame - self.currentFrame < minFrames) {
                toastr['error']('For usability purposes, effects must be at least ' + minFrames + ' frames long.');
                return;
            }

            var newEffect = {
                effectType: '',
                startFrame: self.currentFrame,
                endFrame: endFrame,
                params: []
            };

            self.currentChannel.effects.splice(destIndex, 0, newEffect);
            self.setCurrentEffect(self.getCurrentChannel(), newEffect);
        };

        this.getCurrentEffect = function () {
            return self.currentEffect;
        };

        this.setCurrentEffect = function (channel, effect) {
            self.setCurrentChannel(channel);
            self.currentEffect = effect;
        };

        this.getParamName = function (effectTypeCode, paramCode) {
            var effectType = _.find(self.animationEffectTypes, function (item) {
                return item.code === effectTypeCode;
            });

            var param = _.find(effectType.parameters, function (item) {
                return item.code === paramCode
            });

            return param.name;
        };

        this.deleteCurrentEffect = function () {
            if (self.currentEffect != null) {
                self.currentChannel.effects.splice(
                    $.inArray(self.currentEffect, self.currentChannel.effects), 1
                );

                self.currentEffect = null;
            }
        };

        this.saveAnimationData = function (successCallback) {
            self.currentSong.animationData = angular.toJson(self.animationData);

            RestService.one('song').withHttpConfig({
                    transformRequest: angular.identity
                })
                .customPOST(JSON.stringify(self.currentSong), undefined, undefined, {'Content-Type': 'application/json'})
                .then(function () {
                    if (successCallback !== undefined) {
                        successCallback();
                    } else {
                        toastr['success']('Song saved successfully');
                    }
                }, function (response) {
                    toastr['error'](response.data, 'Failed to save song');
                });
        };

        this.previewAnimation = function () {
            self.saveAnimationData(function () {
                RestService.one('song/render', self.currentSong.id)
                    .customGET('', {
                        'duration-seconds': 5,
                        'start-frame': self.currentFrame
                    })
                    .then(function (renderData) {
                        self.frameBeforeAnimation = self.currentFrame;
                        self.previewing = true;

                        $rootScope.$broadcast('animationPreview', {
                            startFrame: self.currentFrame,
                            renderData: renderData
                        });
                    });
            });
        };

        this.cancelAnimationPreview = function () {
            $log.info('Cancelling animation preview.');
            $rootScope.$broadcast('animationPreviewCancelled', {});
        };

        var unregisterAnimationPreviewFinished = $rootScope.$on('animationPreviewFinished', function (event, args) {
            self.previewing = false;
            self.currentFrame = self.frameBeforeAnimation;
            self.frameBeforeAnimation = null;
        });

        $scope.$on('$destroy', function () {
            unregisterAnimationPreviewFinished();
        });

        this.getStageSvg = function () {
            RestService.one('stage', 1).get().then(function (stage) {
                self.stageSvg = stage.svg;
            });
        };

        this.getCurrentSong = function () {
            RestService.one('song', $stateParams.id).get().then(function (song) {
                self.currentSong = song;
                self.animationData = JSON.parse(song.animationData);
                self.setCurrentChannel(self.animationData.channels[0]);
                self.mp3Url = RestService.configuration.baseUrl + '/song/data/' + song.id;
            },
            function () {
                $state.go('selector');
                toastr['error']('You have been returned to the song selector.', 'Song #' + $stateParams.id + ' not found.');
            });
        };

        this.getAllAnimationEffectTypes = function () {
            RestService.service('animation-effect-type').getList().then(function (animationEffectTypes) {
                self.animationEffectTypes = animationEffectTypes;
            });
        };

        this.getStageSvg();
        this.getCurrentSong();
        this.getAllAnimationEffectTypes();
    }
}());
