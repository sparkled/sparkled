function animationPreview(animationService,
                          stageRestService,
                          $sce) {
    'ngInject';

    const activeLedClass = 'active-led';

    return {
        restrict: 'E',
        replace: true,
        templateUrl: 'editor/preview/animation-preview.directive.html',
        scope: {
            stageExpanded: '='
        },
        link: scope => {
            scope.stageExpanded = false;
            stageRestService.getStage(1).then(stage => scope.stageSvg = $sce.trustAsHtml(stage.svg));

            scope.$watch(() => animationService.running, (isRunning, wasRunning) => {
                if (isRunning && !wasRunning) {
                    draw();
                }
            });

            function draw() {
                const requestId = requestAnimationFrame(draw);
                const frame = animationService.getFrame();
                if (!!frame) {
                    drawFrame(frame);
                } else {
                    $(`.${activeLedClass}`).css('fill', '').removeClass(activeLedClass);
                    cancelAnimationFrame(requestId);
                }
            }

            function drawFrame(frame) {
                var ledIndex = 0;
                _(frame.leds).forEach(led => {
                    const $led = $(`#led-${ledIndex}`);
                    const alpha = _.max([led.r, led.g, led.b]) / 255;
                    $led.css('fill', `rgba(${led.r},${led.g},${led.b},${alpha})`);

                    if (led.r + led.g + led.b > 0) {
                        $led.addClass(activeLedClass);
                    } else {
                        $led.removeClass(activeLedClass);
                    }

                    ledIndex++;
                });
            }
        }
    }
}

module.exports = {
    name: 'animationPreview',
    fn: animationPreview
};
