function animationRestService(Restangular) {
    'ngInject';

    const easingTypeRestService = Restangular.service('easing-type');
    const effectTypeRestService = Restangular.service('effect-type');
    const fillTypeRestService = Restangular.service('fill-type');

    return {
        getEasingTypes: getEasingTypes,
        getEffectTypes: getEffectTypes,
        getFillTypes: getFillTypes
    };

    function getEasingTypes() {
        return easingTypeRestService.getList();
    }

    function getEffectTypes() {
        return effectTypeRestService.getList();
    }

    function getFillTypes() {
        return fillTypeRestService.getList();
    }
}

module.exports = {
    name: 'animationRestService',
    fn: animationRestService
};
