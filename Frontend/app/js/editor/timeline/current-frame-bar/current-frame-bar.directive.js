function currentFrameBar(editorConstants,
                         editorService,
                         $timeout) {
    'ngInject';

    return {
        restrict: 'E',
        replace: true,
        template: `
            <div id="current-frame-bar"
                 ng-style="{'left': editorService.currentFrame * editorConstants.pixelsPerFrame + 'px'}">
            </div>
        `,
        link: (scope, element) => {
            scope.editorConstants = editorConstants;
            scope.editorService = editorService;

            $(element).draggable({
                distance: editorConstants.pixelsPerFrame + 1,
                grid: [editorConstants.pixelsPerFrame, 1],
                axis: 'x',
                start: function () {
                    $('body').addClass('dragging-current-frame-bar');
                },
                drag: updateCurrentFrame,
                stop: function () {
                    $('body').removeClass('dragging-current-frame-bar');
                    updateCurrentFrame();
                }
            });

            function updateCurrentFrame() {
                return $timeout(() => {
                    var left = $(element).css('left');
                    editorService.setCurrentFrame(parseInt(left) / editorConstants.pixelsPerFrame);
                    $(element).css('left', editorService.currentFrame * editorConstants.pixelsPerFrame);
                });
            }
        }
    };
}

module.exports = {
    name: 'currentFrameBar',
    fn: currentFrameBar
};
