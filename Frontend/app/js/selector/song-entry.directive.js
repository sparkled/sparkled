function songEntry() {
    return {
        restrict: 'E',
        replace: true,
        templateUrl: 'selector/song-entry.template.html',
        scope: {
            song: '=',
            deleteSong: '='
        }
    }
}

module.exports = {
    name: 'songEntry',
    fn: songEntry
};
