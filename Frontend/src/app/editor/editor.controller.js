/*global angular*/
(function () {
    'use strict';

    angular.module('ledStripAnimator.editor')
        .controller('EditorController', EditorController);

    function EditorController($log,
                                 applicationContextService) {
        var self = this;


/*        angular.element(document).ready(function () {
            $('#wrapper').css('margin', '0 auto ' + -$('#footer-container').height() + 'px');
            $('#footer, #push').css('height', $('#footer-container').height() + 'px');
        });*/
        /*
         (function () {
         'use strict';

         angular.module('ledStripAnimator.editor', []);

         $(document).ready(function() {
         $("#footer").

         .wrapper {
         min-height: 100%;
         margin: 0 auto -350px;
         }

         #footer {
         background: #ddd;
         padding: 3px;
         font-size: 0;
         #footer-container {
         overflow: auto;
         }
         &, #push {
         height: 350px;
         }
         }
         })
         }());

         */

    }
}());
