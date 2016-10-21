function songEntry() {
    return {
        restrict: 'E',
        scope: {
            song: '=',
            edit: '&',
            remove: '&'
        },
        replace: true,
        templateUrl: 'selector/song-entry.template.html'
    }
}

module.exports = {
    name: 'songEntry',
    fn: songEntry
};
