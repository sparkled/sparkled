/*global angular*/
(function () {
    'use strict';
    angular.module('ledStripAnimator.component')
        .directive('waveform', function () {
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

                    scope.$watch('duration', function (newVal, oldVal) {
                        if (newVal != null) {
                            var frameCount = newVal * 60;
                            var width = frameCount * 2 + 1;
                            $(element).css('width', width);
                        }
                    }, true);

                    scope.$watch('mp3Url', function (newVal, oldVal) {
                        if (newVal != null) {
                            var wavesurfer = WaveSurfer.create({
                                container: element[0],
                                renderer: 'MultiCanvas',
                                maxCanvasWidth: 16000,
                                waveColor: '#ff9900',
                                progressColor: '#999',
                                height: 50
                            });

                            wavesurfer.load(newVal);

                            var updateCurrentFrame = function () {
                                scope.$apply(function () {
                                    var progressWidth = $(element).find('wave wave').width();
                                    scope.currentFrame = 2 * Math.round(progressWidth / 2) / 2;
                                    if (scope.currentFrame >= scope.duration * 60) {
                                        scope.currentFrame = scope.duration * 60 - 1;
                                    }

                                    // Ensure progress wave width is in sync with current frame to prevent small gaps.
                                    $(element).find('wave wave').width(scope.currentFrame * 2);
                                });
                            };

                            wavesurfer.on('audioprocess', updateCurrentFrame);
                            wavesurfer.on('seek', updateCurrentFrame);
                            wavesurfer.on('ready', function () {
                                //wavesurfer.play();
                            });
                        }
                    }, true);
                }
            }
        });
})();
