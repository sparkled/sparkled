const angular = require('angular');
const environmentConstants = require('./environment.constants');

const environmentModule = angular.module('app.environment', []);

environmentModule
    .constant(environmentConstants.name, environmentConstants.obj);

module.exports = environmentModule;
