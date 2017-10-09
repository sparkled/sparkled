function restConfig(environmentConstants,
                    RestangularProvider) {
    'ngInject';

    RestangularProvider.setBaseUrl(environmentConstants.get().baseUrl);
}

module.exports = restConfig;
