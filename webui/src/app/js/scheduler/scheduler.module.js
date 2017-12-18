const angular = require('angular');
const schedulerConfig = require('./scheduler.config');
const schedulerController = require('./scheduler.controller');

const schedulerModule = angular.module('app.scheduler', [
    'app.directives',
    'app.filters',
    'app.navigation',
    'app.rest'
]);

schedulerModule
    .config(schedulerConfig)
    .controller(schedulerController.name, schedulerController.fn);

module.exports = schedulerModule;
