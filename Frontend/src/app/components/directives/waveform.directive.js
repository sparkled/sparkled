/*global angular*/
(function () {
    'use strict';
    angular.module('ledStripAnimator.component')
        .directive('waveform', function ($rootScope, $timeout) {
            return {
                restrict: 'E',
                replace: true,
                templateUrl: 'app/components/directives/waveform.template.html',
                scope: {
                    currentFrame: '=',
                    duration: '=',
                    mp3Url: '='
                },
                link: function (scope, element, attrs) {
                    scope.width = 0;
                    var wavesurfer = null;

                    scope.$watch('currentFrame', function (newVal, oldVal) {
                        if (wavesurfer != null && newVal !== oldVal) {
                            scope.isUpdatingCurrentFrame = true;
                            if (!wavesurfer.isPlaying()) {
                                wavesurfer.seekTo(newVal / (scope.duration * 60));
                            }
                            scope.isUpdatingCurrentFrame = false;
                        }
                    }, false);

                    scope.$watch('duration', function (newVal, oldVal) {
                        if (newVal != null) {
                            var frameCount = newVal * 60;
                            var width = frameCount * 2 + 1;
                            $(element).css('width', width);
                        }
                    }, true);

                    scope.$watch('mp3Url', function (newVal, oldVal) {
                        if (newVal != null) {
                            wavesurfer = WaveSurfer.create({
                                container: element[0],
                                renderer: 'MultiCanvas',
                                maxCanvasWidth: 16000,
                                waveColor: '#ebebeb',
                                progressColor: '#4e5d6c',
                                height: 50,
                                barWidth: 1
                            });

                            wavesurfer.load(newVal);

                            var updateCurrentFrame = function () {
                                if (!scope.isUpdatingCurrentFrame) {
                                    $timeout(function () {
                                        scope.$apply(function () {
                                            var progressWidth = $(element).find('wave wave').width();
                                            scope.currentFrame = 2 * Math.round(progressWidth / 2) / 2;
                                            if (scope.currentFrame >= scope.duration * 60) {
                                                scope.currentFrame = scope.duration * 60 - 1;
                                            }

                                            // Ensure progress wave width is in sync with current frame to prevent small gaps.
                                            $(element).find('wave wave').width(scope.currentFrame * 2);
                                        });
                                    });
                                }
                            };

                            wavesurfer.on('audioprocess', updateCurrentFrame);
                            wavesurfer.on('seek', function () {
                                updateCurrentFrame();
                            });
                            wavesurfer.on('ready', function () {
                                scope.$apply(function () {
                                    $rootScope.$broadcast('loading', {loading: false});
                                });
                            });
                        }
                    }, true);
                }
            }
        });
})();
