function editorNavbar(animationService,
                      editorService,
                      songRestService,
                      toastr) {
    'ngInject';

    return {
        restrict: 'E',
        replace: true,
        templateUrl: 'editor/navbar/editor-navbar.directive.html',
        link: scope => {
            scope.animationService = animationService;
            scope.editorService = editorService;
            scope.saving = false;

            scope.saveSong = () => {
                scope.saving = true;
                return songRestService.saveSong(editorService.song, editorService.animationData)
                    .then(
                        () => toastr.success('Song saved successfully'),
                        () => toastr.error('Failed to save song')
                    )
                    .finally(() => scope.saving = false);
            };

            scope.publishSong = () => {
                scope.saving = true;
                return songRestService.publishSong(editorService.song, editorService.animationData)
                    .then(
                        () => toastr.success('Song published successfully'),
                        () => toastr.error('Failed to publish song')
                    )
                    .finally(() => scope.saving = false);
            }
        }
    }
}

module.exports = {
    name: 'editorNavbar',
    fn: editorNavbar
};
