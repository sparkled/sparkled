const toastr = require('toastr');

function toastrConfig() {
    toastr.options = {
        closeButton: true,
        preventDuplicates: true,
        showDuration: 100,
        hideDuration: 100,
        timeOut: 2000,
        extendedTimeOut: 2000,
        showMethod: 'fadeIn',
        hideMethod: 'fadeOut',
        positionClass: 'toast-bottom-right'
    };
}

module.exports = toastrConfig;
