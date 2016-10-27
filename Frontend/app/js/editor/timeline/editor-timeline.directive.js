function editorTimeline() {
    return {
        restrict: 'E',
        templateUrl: 'editor/timeline/editor-timeline.directive.html'
    }
}

module.exports = {
    name: 'editorTimeline',
    fn: editorTimeline
};
