/*global angular*/
(function () {
    'use strict';
    angular.module('ledStripAnimator.component')
        .directive('animationPreview', function () {
            return {
                restrict: 'E',
                replace: true,
                templateUrl: 'app/components/directives/animationPreview.template.html',
                scope: {
                    stageExpanded: '='
                },
                link: function (scope, element, attrs) {
                    $(element).find('.stage-resize').click(function () {
                        scope.$apply(function () {
                            scope.stageExpanded = !scope.stageExpanded;
                        });
                    });
                }
            }
        });
})();

