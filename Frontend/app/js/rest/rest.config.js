function restConfig(environmentConstants,
                    RestangularProvider) {
    'ngInject';

    const ec = environmentConstants.get();
    const baseUrl = `${ec.protocol}://${ec.host}:${ec.port}/${ec.restPath}`;
    RestangularProvider.setBaseUrl(baseUrl);
}

module.exports = restConfig;
