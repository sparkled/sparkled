/*global angular*/
(function () {
    'use strict';
    angular.module('sparkled.component')
        .directive('animationPreview', function ($rootScope) {
            'ngInject';

            return {
                restrict: 'E',
                replace: true,
                templateUrl: 'app/components/directives/animationPreview.template.html',
                scope: {
                    stageSvg: '=',
                    stageExpanded: '='
                },
                link: function (scope, element, attrs) {
                    scope.$watch('stageSvg', function (newVal) {
                        $(element).find('#stage').append(newVal);
                    });

                    $(element).find('.stage-resize').click(function () {
                        scope.$apply(function () {
                            scope.stageExpanded = !scope.stageExpanded;
                        });
                    });

                    $rootScope.$on('animationPreview', function (event, args) {
                        var startTime = new Date().getTime(),
                            frameCount = args.renderData.length,
                            durationMs = 1000 / 60 * frameCount,
                            frameIndex = 0;

                        function draw() {
                            var requestId = requestAnimationFrame(draw),
                                now = new Date().getTime();

                            frameIndex = Math.floor((now - startTime) / durationMs * frameCount);

                            if (frameIndex < frameCount) {
                                var ledIndex = 0;
                                _.forEach(args.renderData[frameIndex].leds, function (led) {
                                    var $led = $('#led-' + ledIndex);
                                    $led.css('fill', 'rgb(' + led.r + ',' + led.g + ',' + led.b + ')');

                                    if (led.r != 0 || led.g != 0 || led.b != 0) {
                                        $led.attr('class', 'active-led');
                                    } else {
                                        $led.attr('class', '');
                                    }

                                    ledIndex++;
                                });
                            } else {
                                cancelAnimationFrame(requestId);
                                $rootScope.$broadcast('animationPreviewFinished', {
                                    startFrame: args.startFrame
                                });
                            }
                        }

                        draw();
                    });
                }
            }
        });
})();

