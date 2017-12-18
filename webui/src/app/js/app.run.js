function appRun($templateCache) {
    'ngInject';

    const templateName = 'bootstrap/match.tpl.html';
    var template = $templateCache.get(templateName);
    template = template.replace('glyphicon glyphicon-remove', 'fa fa-remove ui-select-match-remove');
    $templateCache.put(templateName, template);
}

module.exports = appRun;
