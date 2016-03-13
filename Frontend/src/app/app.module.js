/*global angular*/
(function () {
    'use strict';

    // Parent Application Module - Define only feature modules here!
    angular.module('ledStripAnimator', [
                /*
                 *Required for configuring the routes in app.module.js
                 */
                'ui.router',
                /*
                 * Feature dependencies
                 */
                'ledStripAnimator.core',
                'ledStripAnimator.editor',
                'ledStripAnimator.rest',
                'ledStripAnimator.selector'
            ]);
}());
