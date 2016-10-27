function stageRestService(Restangular) {
    'ngInject';

    const restService = Restangular.service('stage');

    return {
        getStage: getStage
    };

    function getStage(stageId) {
        return restService.one(stageId).get();
    }
}

module.exports = {
    name: 'stageRestService',
    fn: stageRestService
};
