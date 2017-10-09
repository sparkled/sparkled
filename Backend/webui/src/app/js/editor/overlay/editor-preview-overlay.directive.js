function editorPreviewOverlay(animationService) {
    'ngInject';

    return {
        restrict: 'E',
        replace: true,
        template: '<div id="animation-preview-overlay" ng-if="animationService.running"></div>',
        link: scope => scope.animationService = animationService
    }
}

module.exports = {
    name: 'editorPreviewOverlay',
    fn: editorPreviewOverlay
};
