function effectEditor(animationRestService,
                      _) {
    'ngInject';

    return {
        restrict: 'E',
        replace: true,
        templateUrl: 'editor/editor/effect-editor.directive.html',
        scope: {
            channel: '=',
            effect: '='
        },
        link: scope => {
            scope.previousEffect = undefined;
            scope.easingTypes = [];
            scope.effectTypes = [];
            scope.colours = [
                {name: 'Red', code: '#ff0000'},
                {name: 'Green', code: '#00ff00'},
                {name: 'Blue', code: '#0000ff'}
            ];

            animationRestService.getEasingTypes().then(easingTypes => scope.easingTypes = easingTypes);
            animationRestService.getEffectTypes().then(effectTypes => scope.effectTypes = effectTypes);

            scope.deleteCurrentEffect = () => {
                _.remove(scope.channel.effects, effect => {
                    return effect === scope.effect;
                });
                scope.effect = null;
            };

            scope.getParamName = (effectTypeCode, paramCode) => {
                const effectType = _.find(scope.effectTypes, item => item.code === effectTypeCode);
                const param = _.find(effectType.parameters, item => item.code === paramCode);
                return param ? param.name : '';
            };

            scope.$watch('effect.effectType', updateEffectType, true);

            function updateEffectType(effectTypeCode) {
                if (scope.previousEffect !== scope.effect) {
                    scope.previousEffect = scope.effect;
                } else {
                    if (scope.effect != null && !_.isEmpty(scope.effectTypes)) {
                        scope.effect.effectType = effectTypeCode;
                        updateParams(effectTypeCode);
                    }
                }
            }

            function updateParams(effectTypeCode) {
                const effectType = _(scope.effectTypes).find(effectType => effectType.code === effectTypeCode);

                if (effectType != null) {
                    updateEffect(effectType);
                }
            }

            function updateEffect(effectType) {
                scope.effect.params = [];
                scope.coloursParamEntries = [];

                for (var i = 0; i < effectType.parameters.length; i++) {
                    var paramCode = effectType.parameters[i].code,
                        param = {
                            paramCode: paramCode,
                            value: undefined,
                            multiValue: []
                        };

                    scope.effect.params.push(param);
                }
            }
        }
    }
}

module.exports = {
    name: 'effectEditor',
    fn: effectEditor
};
