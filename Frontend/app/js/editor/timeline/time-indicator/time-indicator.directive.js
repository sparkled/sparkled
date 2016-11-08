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

            function update(song) {
                const duration = !!song ? song.durationSeconds : 0;

                scope.seconds = [];
                for (var i = 0; i <= duration; i++) {
                    scope.seconds.push(i);
                }

                const frameCount = duration * editorConstants.framesPerSecond;
                scope.width = frameCount * editorConstants.pixelsPerFrame + 1;
                scope.patternWidth = frameCount * editorConstants.pixelsPerFrame + 1;
                if (scope.patternWidth > .5) {
                    scope.patternWidth -= .5;
                }
            }
        }
    };
}

module.exports = {
    name: 'timeIndicator',
    fn: timeIndicator
};
