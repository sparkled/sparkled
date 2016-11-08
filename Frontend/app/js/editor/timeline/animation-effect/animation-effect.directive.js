function animationEffect(editorConstants,
                         editorService) {
    'ngInject';

    return {
        restrict: 'E',
        replace: true,
        templateUrl: 'editor/timeline/animation-effect/animation-effect.directive.html',
        scope: {
            channel: '=',
            effect: '='
        },
        link: scope => {
            scope.editorConstants = editorConstants;
            scope.editorService = editorService;
            scope.setCurrentEffect = () => {
                editorService.currentChannel = scope.channel;
                editorService.currentEffect = scope.effect;
            };
        }
    }
}

module.exports = {
    name: 'animationEffect',
    fn: animationEffect
};
