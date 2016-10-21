const angular = require('angular');
const restConfig = require('./rest.config');
const songRestService = require('./song-rest.service');

const restModule = angular.module('app.rest', [
    'app.environment',
    'restangular'
]);

restModule
    .config(restConfig)
    .service(songRestService.name, songRestService.fn);

module.exports = restModule;
