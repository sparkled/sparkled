function animationEffect(editorService) {
    'ngInject';

    return {
        restrict: 'E',
        replace: true,
        templateUrl: 'editor/timeline/animation-effect/animation-effect.directive.html',
        scope: {
            channel: '=',
            effect: '=',
            duration: '='
        },
        link: scope => {
            scope.minWidePx = 45;
            scope.pxPerFrame = 2;

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
