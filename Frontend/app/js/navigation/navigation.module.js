const angular = require('angular');
const applicationStateConstants = require('./application-states.constants');

const navigationModule = angular.module('app.navigation', []);

navigationModule
    .constant(applicationStateConstants.name, applicationStateConstants.obj);

module.exports = navigationModule;
