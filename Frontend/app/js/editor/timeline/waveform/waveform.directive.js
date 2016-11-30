const $ = require('jquery');
const WaveSurfer = require('wavesurfer.js');

function waveform(animationService,
                  editorConstants,
                  editorService,
                  loaderService,
                  $timeout) {
    'ngInject';

    return {
        restrict: 'E',
        replace: true,
        template: '<div class="waveform" ng-attr-width="width"></div>',
        link: (scope, element) => {
            scope.width = 0;
            var wavesurfer = null;

            scope.$watch(() => animationService.running, isRunning => {
                if (wavesurfer) {
                    if (isRunning) {
                        wavesurfer.play();
                    } else {
                        wavesurfer.pause();
                    }
                }
            });

            scope.$on('$destroy', () => {
                wavesurfer && wavesurfer.destroy();
            });

            scope.$watch(() => editorService.currentFrame, seekToFrame);

            scope.$watch(() => editorService.song.durationFrames, durationFrames => {
                if (durationFrames) {
                    const frameCount = durationFrames;
                    const width = frameCount * editorConstants.pixelsPerFrame + 1;
                    $(element).css('width', width);
                }
            });

            scope.$watch(() => editorService.songUrl, newUrl => {
                if (newUrl != null) {
                    wavesurfer = WaveSurfer.create({
                        container: element[0],
                        renderer: 'MultiCanvas',
                        maxCanvasWidth: 16000,
                        waveColor: '#ebebeb',
                        progressColor: '#4e5d6c',
                        height: 50
                    });

                    wavesurfer.load(newUrl);
                    wavesurfer.on('audioprocess', updateCurrentFrame);
                    wavesurfer.on('seek', updateCurrentFrame);
                    wavesurfer.on('ready', () => {
                        $timeout(() => {
                            loaderService.loading = false;
                            seekToFrame(editorService.currentFrame);
                        });
                    });
                }
            });

            function updateCurrentFrame() {
                if (!scope.isUpdatingCurrentFrame) {
                    $timeout(() => {
                        const innerWave = $(element).find('wave wave');
                        const progressWidth = innerWave.width();
                        editorService.setCurrentFrame(progressWidth / editorConstants.pixelsPerFrame);

                        // Ensure progress wave width is in sync with current frame to prevent small gaps.
                        innerWave.width(scope.currentFrame * editorConstants.pixelsPerFrame);
                    });
                }
            }

            function seekToFrame(frame) {
                if (wavesurfer != null) {
                    scope.isUpdatingCurrentFrame = true;

                    if (!wavesurfer.isPlaying()) {
                        var location = frame / (editorService.song.durationFrames);
                        wavesurfer.seekTo(location);
                    }

                    scope.isUpdatingCurrentFrame = false;
                }
            }
        }
    }
}

module.exports = {
    name: 'waveform',
    fn: waveform
};
