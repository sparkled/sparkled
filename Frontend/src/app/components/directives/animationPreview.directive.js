/*global angular*/
(function () {
    'use strict';
    angular.module('sparkled.component')
        .directive('animationPreview', function ($log, $rootScope, $timeout) {
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

                    var timer = null;
                    var unregisterAnimationPreviewCancelled = $rootScope.$on('animationPreviewCancelled', function (event, args) {
                        scope.animationCancelled = true;

                        if (timer != null) {
                            $log.info('Removing pending timer.');
                            $timeout.cancel(timer);
                        }

                        $rootScope.$broadcast('animationPreviewFinished', {});
                    });

                    var unregisterAnimationPreview = $rootScope.$on('animationPreview', function (event, args) {
                        scope.animationCancelled = false;

                        var startTime = new Date().getTime(),
                            frameCount = args.renderData.length,
                            durationMs = 1000 / 60 * frameCount,
                            frameIndex = 0;

                        $log.info('Animation preview started. Duration: ' + durationMs + 'ms.');

                        timer = $timeout(function () {
                            timer = null;
                            if (!scope.animationCancelled) {
                                $log.info('Animation preview completed.');
                                $rootScope.$broadcast('animationPreviewFinished', {});
                            }
                        }, durationMs);

                        function draw() {
                            var requestId = requestAnimationFrame(draw),
                                now = new Date().getTime();

                            frameIndex = Math.floor((now - startTime) / durationMs * frameCount);

                            if (!scope.animationCancelled && frameIndex < frameCount) {
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
                                clearLastFrame();
                            }
                        }

                        draw();
                    });

                    function clearLastFrame() {
                        $('.active-led').css('fill', '')
                            .attr('class', '');
                    }

                    scope.$on('$destroy', function () {
                        $log.info('Cleaning up animation preview directive.');
                        if (timer != null) {
                            $log.info('Removing pending timer.');
                            $timeout.cancel(timer);
                        }

                        unregisterAnimationPreview();
                        unregisterAnimationPreviewCancelled();
                    })
                }
            }
        });
})();

