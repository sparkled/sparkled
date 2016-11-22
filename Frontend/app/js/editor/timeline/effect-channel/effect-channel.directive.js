function effectChannel(editorConstants,
                       editorService,
                       $timeout) {
    'ngInject';

    return {
        restrict: 'E',
        replace: true,
        templateUrl: 'editor/timeline/effect-channel/effect-channel.directive.html',
        scope: {
            channel: '='
        },
        link: (scope, element) => {
            scope.editorService = editorService;

            scope.setCurrentChannel = () => {
                editorService.currentChannel = scope.channel;
                editorService.currentEffect = null;
            };

            $(element).click(event => {
                $timeout(() => {
                    const currentFrame = (event.originalEvent.offsetX - 2) / editorConstants.pixelsPerFrame;
                    editorService.setCurrentFrame(currentFrame);
                });
            });

            scope.$watch(() => editorService.song.durationSeconds, newVal => {
                if (newVal != null) {
                    var frameCount = newVal * editorConstants.framesPerSecond;
                    var width = frameCount * editorConstants.pixelsPerFrame + 1;
                    $(element).css('width', width);
                }
            }, true);
        }
    }
}

module.exports = {
    name: 'effectChannel',
    fn: effectChannel
};
