/*global angular*/
(function () {
    'use strict';
    angular.module('ledStripAnimator.component')
        .directive('currentEffectBounds', function ($rootScope) {
            return {
                restrict: 'E',
                replace: true,
                templateUrl: 'app/components/directives/currentEffectBounds.template.html',
                link: function (scope, element, attrs) {
                    $rootScope.$on('animationEffectMove', function(event, args) {
                        if (args.visible) {
                            showBounds(element, scope, args);
                        } else {
                            hideBounds(element, scope);
                        }
                    });
                }
            }
        });

    var showBounds = function (element, scope, args) {
        var effectElement = $(args.element);
        var $element = $(element);

        scope.interval = setInterval(function () {
            $element.show()
                .css('width', effectElement.width())
                .css('left', parseInt(effectElement.css('left')));
        }, 10);
    };

    var hideBounds = function (element, scope) {
        $(element).hide();
        clearInterval(scope.interval);
        scope.interval = null;
    };
})();
