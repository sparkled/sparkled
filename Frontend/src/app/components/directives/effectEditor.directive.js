/*global angular*/
(function () {
    'use strict';
    angular.module('ledStripAnimator.component')
        .directive('effectEditor', function ($timeout) {
            return {
                restrict: 'E',
                replace: true,
                templateUrl: 'app/components/directives/effectEditor.template.html',
                scope: {
                    effect: '=',
                    effectTypes: '='
                },
                link: function (scope, element, attrs) {
                    scope.$watch('effect.effectType', function (newValue) {
                        if (scope.effect == null || scope.effectTypes == null) {
                            return;
                        }

                        var requiresParamUpdate = newValue !== scope.effect.effectType || scope.effect.params.length === 0;
                        if (requiresParamUpdate) {
                            var effectType = null;

                            for (var i = 0; i < scope.effectTypes.length; i++) {
                                if (scope.effectTypes[i].code === newValue) {
                                    effectType = scope.effectTypes[i];
                                    break;
                                }
                            }

                            if (effectType != null) {
                                scope.effect.params = [];
                                for (var j = 0; i < effectType.parameters.length; i++) {
                                    scope.effect.params.push({
                                        code: effectType.parameters[j].code,
                                        name: effectType.parameters[j].name,
                                        value: ''
                                    });
                                }
                            }
                        }
                    }, true);
                }
            }
        });
})();

