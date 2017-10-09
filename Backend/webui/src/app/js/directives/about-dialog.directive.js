const $ = require('jquery');
const sweetAlert = require('sweetalert2');

function aboutDialog() {
    return {
        restrict: 'A',
        link: (scope, element) => {
            $(element).click(() => {
                const body = $('body');
                const dialogClass = 'showing-about-dialog';
                body.addClass(dialogClass);

                sweetAlert({
                    html: `
                        <img src="images/logos/logo-large.svg"/>
                        <h4>Sparkled: The web-based LED animation sequencer</h4>
                        <a href="https://github.com/chrisparton1991/sparkled" class="github-link" title="View GitHub Project" target="_blank">
                            <i class="fa fa-github"></i>
                        </a>`
                })
                    .then(() => body.removeClass(dialogClass))
                    .catch(sweetAlert.noop);
            });
        }
    }
}

module.exports = {
    name: 'aboutDialog',
    fn: aboutDialog
};
