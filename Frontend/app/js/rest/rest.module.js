const angular = require('angular');
const restConfig = require('./rest.config');
const animationRestService = require('./animation-rest.service');
const songRestService = require('./song-rest.service');
const stageRestService = require('./stage-rest.service');

const restModule = angular.module('app.rest', [
    'app.environment',
    'restangular'
]);

restModule
    .config(restConfig)
    .service(animationRestService.name, animationRestService.fn)
    .service(songRestService.name, songRestService.fn)
    .service(stageRestService.name, stageRestService.fn);

module.exports = restModule;
