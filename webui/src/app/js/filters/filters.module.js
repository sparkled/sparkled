const angular = require('angular');
const timeFilter = require('./time.filter');

const filterModule = angular.module('app.filters', []);

filterModule.filter(timeFilter.name, timeFilter.fn);

module.exports = filterModule;
