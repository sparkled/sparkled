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
                    easingTypes: '=',
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

                    scope.$watch('effect.easingType', function (newValue) {
                        if (scope.previousEffect !== scope.effect) {
                            scope.previousEffect = scope.effect;
                        } else {
                            if (scope.effect != null && scope.easingTypes != null) {
                                scope.effect.easingTypes = newValue;
                                updateParams(scope, newValue);
                            }
                        }
                    }, true);

                    scope.addColourValue = function (param) {
                        param.multiValue.push({value: '#ffffff'});
                    };

                    scope.updateColoursParam = function (param) {
                        // Ignore updates until the colour picker is closed.
                        if ($('.sp-container:visible').length > 0) {
                            return;
                        }

                        param.multiValue = $.grep(param.multiValue, function (item) {
                            return !!item.value && item.value.indexOf('#') === 0;
                        });
                    };

                    scope.spectrumOptions = {
                        preferredFormat: 'hex',
                        showInitial: true,
                        showInput: true,
                        showPalette: true,
                        showSelectionPalette: false,
                        palette: [
                            ['red', 'green', 'blue', 'purple'],
                            ['yellow', 'white', 'black', 'magenta'],
                            ['transparent']
                        ]
                    };
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
        scope.coloursParamEntries = [];

        for (var i = 0; i < effectType.parameters.length; i++) {
            var paramCode = effectType.parameters[i].code,
                param = {
                    paramCode: paramCode,
                    value: '',
                    multiValue: []
                };

            if (paramCode === 'COLOUR') {
                param.value = '#ffffff';
            } else if (paramCode === 'MULTI_COLOUR') {
                param.multiValue.push({value: '#ffffff'});
            }

            scope.effect.params.push(param);
        }
    }
})();
