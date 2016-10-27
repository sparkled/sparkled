function EditorCtrl(animationService,
                    editorService,
                    loaderService,
                    songRestService,
                    $stateParams) {
    'ngInject';

    const vm = this;
    vm.animationService = animationService;

    loaderService.loading = true;
    songRestService.getSong($stateParams.id)
        .then(response => editorService.song = response)
        .finally(() => loaderService.loading = false);
}

module.exports = {
    name: 'EditorCtrl',
    fn: EditorCtrl
};
