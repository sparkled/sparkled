function editorNavbar(animationService) {
    'ngInject';

    return {
        restrict: 'E',
        replace: true,
        templateUrl: 'editor/navbar/editor-navbar.directive.html',
        link: scope => scope.animationService = animationService
    }
}

module.exports = {
    name: 'editorNavbar',
    fn: editorNavbar
};
