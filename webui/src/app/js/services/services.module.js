const angular = require('angular');
const loaderService = require('./loader.service');

const servicesModule = angular.module('app.services', []);
servicesModule.service(loaderService.name, loaderService.fn);

module.exports = servicesModule;
