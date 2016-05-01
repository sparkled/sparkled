/*global angular*/
(function () {
    'use strict';
    angular.module('sparkled.component')
        .directive('effectEditor', function () {
            return {
                restrict: 'E',
                replace: true,
                templateUrl: 'app/components/directives/effectEditor.template.html',
                scope: {
                    effect: '=',
                    effectTypes: '=',
                    deleteCurrentEffect: '=',
                    getParamName: '='
                },
                link: function (scope, element, attrs) {
                    scope.$watch('effect.effectType', function (newValue) {
                        if (scope.previousEffect !== scope.effect) {
                            scope.previousEffect = scope.effect;
                        } else {
                            if (scope.effect != null && scope.effectTypes != null) {
                                scope.effect.effectType = newValue;
                                updateParams(scope, newValue);
                            }
                        }
                    }, true);
                }
            }
        });

    var updateParams = function (scope, newValue) {
        var effectType = findEffect(scope, newValue);

        if (effectType != null) {
            updateEffect(scope, effectType);
        }
    };

    var findEffect = function (scope, newValue) {
        var effectType = null;

        for (var i = 0; i < scope.effectTypes.length; i++) {
            if (scope.effectTypes[i].code === newValue) {
                effectType = scope.effectTypes[i];
                break;
            }
        }

        return effectType;
    };

    var updateEffect = function (scope, effectType) {
        scope.effect.params = [];
        for (var i = 0; i < effectType.parameters.length; i++) {
            scope.effect.params.push({
                paramCode: effectType.parameters[i].code,
                value: ''
            });
        }
    }
})();

