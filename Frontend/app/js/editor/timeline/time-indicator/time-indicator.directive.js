function timeIndicator(editorConstants,
                       editorService,
                       $location) {
    'ngInject';

    return {
        restrict: 'E',
        replace: true,
        templateUrl: 'editor/timeline/time-indicator/time-indicator.directive.html',
        link: scope => {
            scope.width = 0;
            scope.patternWidth = 0;
            scope.seconds = [];

            // Workaround for https://github.com/angular/angular.js/issues/8934.
            scope.currentPage = $location.path();

            scope.$watch(() => editorService.song, update);
            scope.$watch(() => editorService.currentFrame, keepFrameOnScreen);

            function update(song) {
                const durationSeconds = Math.floor(song.durationFrames / song.framesPerSecond) || 0;

                scope.seconds = [];
                for (var i = 0; i <= durationSeconds; i++) {
                    scope.seconds.push(i);
                }

                const frameCount = song.durationFrames || 0;
                scope.width = frameCount * editorConstants.pixelsPerFrame + 1;
                scope.patternWidth = scope.width;

                if (scope.patternWidth > .5) {
                    scope.patternWidth -= .5;
                }
            }

            function keepFrameOnScreen() {
                const channels = $('.channels');
                const channelsWidth = channels.width();
                const boundaryWidth = Math.max(50, channelsWidth / 10);

                const channelsScrollLeft = channels.scrollLeft();
                const frameIndicatorOffset = 2;
                const leftOffset = editorService.currentFrame * editorConstants.pixelsPerFrame + frameIndicatorOffset;

                const distanceFromLeft = leftOffset - channelsScrollLeft;
                if (distanceFromLeft < boundaryWidth) {
                    channels.scrollLeft(leftOffset - boundaryWidth);
                }

                const distanceFromRight = -(leftOffset - (channelsScrollLeft + channelsWidth));
                if (distanceFromRight < boundaryWidth) {
                    channels.scrollLeft(leftOffset - channelsWidth + boundaryWidth);
                }
            }
        }
    };
}

module.exports = {
    name: 'timeIndicator',
    fn: timeIndicator
};
