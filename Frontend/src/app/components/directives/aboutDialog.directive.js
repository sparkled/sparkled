/*global angular*/
(function () {
    'use strict';
    angular.module('sparkled.component')
        .directive('aboutDialog', function () {
            'ngInject';

            return {
                restrict: 'A',
                link: function (scope, element, attrs) {
                    $(element).click(function () {
                        sweetAlert({
                            customClass: 'about-dialog',
                            html: '<img src="/assets/images/logos/logo-large.svg"/>' +
                            '<h4>Sparkled: The web-based LED animation sequencer</h4>' +
                            '<a href="https://github.com/chrisparton1991/sparkled" class="github-link" title="View GitHub Project" target="_blank">' +
                            '<i class="fa fa-github"></i>' +
                            '</a>'
                        });
                    });
                }
            }
        });
})();

