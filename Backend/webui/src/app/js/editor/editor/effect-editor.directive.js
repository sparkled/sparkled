function effectEditor(animationRestService,
                      editorService,
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
            scope.easingTypes = [];
            scope.effectTypes = [];
            scope.fillTypes = [];
            scope.updateEasingType = updateEasingType;
            scope.updateEffectType = updateEffectType;
            scope.updateFillType = updateFillType;

            scope.colours = [
                {name: 'Red', code: '#ff0000'},
                {name: 'Green', code: '#00ff00'},
                {name: 'Blue', code: '#0000ff'},
                {name: 'White', code: '#ffffff'},
                {name: 'Cyan', code: '#00ffff'},
                {name: 'Magenta', code: '#ff00ff'},
                {name: 'Yellow', code: '#ffff00'},
                {name: 'Black', code: '#000000'}
            ];

            animationRestService.getEasingTypes().then(easingTypes => scope.easingTypes = easingTypes);
            animationRestService.getEffectTypes().then(effectTypes => scope.effectTypes = effectTypes);
            animationRestService.getFillTypes().then(fillTypes => scope.fillTypes = fillTypes);

            scope.deleteCurrentEffect = () => {
                editorService.deleteCurrentEffect();
                scope.effect = null;
            };

            function updateEasingType() {
                if (scope.effect.easing != null && !_.isEmpty(scope.easingTypes)) {
                    updateEasingParams();
                }
            }

            function updateEasingParams() {
                const easingType = _(scope.easingTypes).find(easingType => easingType.code === scope.effect.easing.type);

                if (easingType != null) {
                    updateEasing(easingType);
                }
            }

            function updateEasing(easingType) {
                scope.effect.easing.params = [];

                for (var i = 0; i < easingType.params.length; i++) {
                    var typeParam = easingType.params[i];
                    var param = {
                        name: typeParam.name,
                        type: typeParam.type,
                        value: typeParam.value
                    };

                    scope.effect.easing.params.push(param);
                }
            }

            function updateEffectType() {
                if (scope.effect != null && !_.isEmpty(scope.effectTypes)) {
                    updateEffectParams();
                }
            }

            function updateEffectParams() {
                const effectType = _(scope.effectTypes).find(effectType => effectType.code === scope.effect.type);

                if (effectType != null) {
                    updateEffect(effectType);
                }
            }

            function updateEffect(effectType) {
                scope.effect.params = [];

                for (var i = 0; i < effectType.params.length; i++) {
                    var typeParam = effectType.params[i];
                    var param = {
                        name: typeParam.name,
                        type: typeParam.type,
                        value: typeParam.value
                    };

                    scope.effect.params.push(param);
                }
            }

            function updateFillType() {
                if (scope.effect.fill != null && !_.isEmpty(scope.fillTypes)) {
                    updateFillParams();
                }
            }

            function updateFillParams() {
                const fillType = _(scope.fillTypes).find(fillType => fillType.code === scope.effect.fill.type);

                if (fillType != null) {
                    updateFill(fillType);
                }
            }

            function updateFill(fillType) {
                scope.effect.fill.params = [];

                for (var i = 0; i < fillType.params.length; i++) {
                    var typeParam = fillType.params[i];
                    var param = {
                        name: typeParam.name,
                        type: typeParam.type,
                        value: typeParam.value
                    };

                    scope.effect.fill.params.push(param);
                }
            }
        }
    }
}

module.exports = {
    name: 'effectEditor',
    fn: effectEditor
};
