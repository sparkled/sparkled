function effectChannel(editorService) {
    'ngInject';

    return {
        restrict: 'E',
        templateUrl: 'editor/timeline/effect-channel/effect-channel.directive.html',
        scope: {
            channel: '='
        },
        link: scope => {
            scope.editorService = editorService;
        }
    }
}

module.exports = {
    name: 'effectChannel',
    fn: effectChannel
};
