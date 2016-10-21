require('./third-party-dependencies');
const $ = require('jquery');

const appModule = require('./app.module');

$(document).ready(loadEnvironmentConfig);

function loadEnvironmentConfig() {
    loadFile('./environment/environment-config.json', response => {
        window.environmentConfig = response;
        startApp()
    });
}

function loadFile(url, successCallback) {
    $.ajax({
        url: url,
        type: 'get',
        success: successCallback
    });
}

function startApp() {
    angular.bootstrap(document, [appModule.name], {
        strictDi: true
    });
}
