function editorConfig(applicationStates,
                      $stateProvider) {
    'ngInject';

    $stateProvider
        .state(applicationStates.editor, {
            url: '/editor/:id',
            controller: 'EditorCtrl as editor',
            templateUrl: 'editor/editor.html',
            title: 'Editor',
            resolve: {
                song: (songRestService, $stateParams) => songRestService.getSong($stateParams.id),
                songAnimation: (songRestService, song) => songRestService.getSongAnimation(song.id)
            }
        });
}

module.exports = editorConfig;
