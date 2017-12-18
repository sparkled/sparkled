const angular = require('angular');
const aboutDialog = require('./about-dialog.directive');

const directivesModule = angular.module('app.directives', []);
directivesModule.directive(aboutDialog.name, aboutDialog.fn);

module.exports = directivesModule;
