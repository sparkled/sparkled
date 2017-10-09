function animationRestService(Restangular) {
    'ngInject';

    const easingTypeRestService = Restangular.service('animation-easing-type');
    const effectTypeRestService = Restangular.service('animation-effect-type');

    return {
        getEasingTypes: getEasingTypes,
        getEffectTypes: getEffectTypes
    };

    function getEasingTypes() {
        return easingTypeRestService.getList();
    }

    function getEffectTypes() {
        return effectTypeRestService.getList();
    }
}

module.exports = {
    name: 'animationRestService',
    fn: animationRestService
};
