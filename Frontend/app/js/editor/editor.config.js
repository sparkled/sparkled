function editorConfig(applicationStates,
                      $stateProvider) {
    'ngInject';

    $stateProvider
        .state(applicationStates.editor, {
            url: '/editor/:id',
            controller: 'EditorCtrl as editor',
            templateUrl: 'editor/editor.html',
            title: 'Editor'
        });
}

module.exports = editorConfig;
