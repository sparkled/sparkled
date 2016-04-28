/*global angular*/
(function () {
    'use strict';

    // Parent Application Module - Define only feature modules here!
    angular.module('sparkled', [
                /*
                 *Required for configuring the routes in app.module.js
                 */
                'ui.router',
                /*
                 * Feature dependencies
                 */
                'sparkled.core',
                'sparkled.context',
                'sparkled.editor',
                'sparkled.rest',
                'sparkled.selector'
            ]);
}());
