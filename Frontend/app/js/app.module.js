// Module imports.
require('./templates');
require('./directives/directives.module');
require('./services/services.module');
require('./filters/filters.module');
require('./environment/environment.module');
require('./navigation/navigation.module');
require('./rest/rest.module');
require('./selector/selector.module');
require('./scheduler/scheduler.module');
require('./editor/editor.module');

// App module configuration.
const appController = require('./app.controller');
const appConfig = require('./app.config');
const appRun = require('./app.run');
const toastr = require('toastr');
const toastrConfig = require('./toastr.config');

const requiredModules = [
    // Third party module dependencies.
    'ngSanitize',
    'ng.shims.placeholder',
    'ui.bootstrap',
    'ui.router',
    'ui.select',
    'cfp.hotkeys',
    'daterangepicker',

    // App module dependencies.
    'templates',
    'app.directives',
    'app.services',
    'app.environment',
    'app.navigation',
    'app.rest',
    'app.selector',
    'app.scheduler',
    'app.editor'
];

const appModule = angular.module('app', requiredModules);

appModule
    .constant('moment', window.moment)
    .constant('toastr', toastr)
    .constant('_', window._)
    .config(appConfig)
    .config(toastrConfig)
    .run(appRun)
    .controller(appController.name, appController.fn);

module.exports = appModule;
