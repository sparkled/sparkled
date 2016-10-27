function editorNavbar(animationService) {
    'ngInject';

    return {
        restrict: 'E',
        templateUrl: 'editor/navbar/editor-navbar.directive.html',
        link: scope => scope.animationService = animationService
    }
}

module.exports = {
    name: 'editorNavbar',
    fn: editorNavbar
};
