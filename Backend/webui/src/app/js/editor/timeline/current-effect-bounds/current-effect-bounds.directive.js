const $ = require('jquery');

function currentEffectBounds($interval) {
    'ngInject';

    return {
        restrict: 'E',
        replace: true,
        template: '<div id="current-effect-bounds"></div>',
        link: (scope, element) => {
            var stop = $interval(updateBounds, 10);
            scope.$on('$destroy', () => $interval.cancel(stop));

            function updateBounds() {
                const effect = $('.animation-effect.selected');
                const leftOffset = parseInt(effect.css('left'));
                element.css('width', effect.width() + 'px')
                    .css('left', leftOffset + 'px');
            }
        }
    };
}

module.exports = {
    name: 'currentEffectBounds',
    fn: currentEffectBounds
};
