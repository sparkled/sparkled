// Module imports.
require('./templates');
require('./directives/directives.module');
require('./services/services.module');
require('./filters/filters.module');
require('./environment/environment.module');
require('./navigation/navigation.module');
require('./rest/rest.module');
require('./selector/selector.module');
require('./editor/editor.module');

// App module configuration.
const appController = require('./app.controller');
const appConfig = require('./app.config');

const requiredModules = [
    // Third party module dependencies.
    'ngSanitize',
    'ng.shims.placeholder',
    'ui.bootstrap',
    'ui.router',
    'cfp.hotkeys',

    // App module dependencies.
    'templates',
    'app.directives',
    'app.services',
    'app.environment',
    'app.navigation',
    'app.rest',
    'app.selector',
    'app.editor'
];

const appModule = angular.module('app', requiredModules);

appModule
    .config(appConfig)
    .controller(appController.name, appController.fn);

module.exports = appModule;
