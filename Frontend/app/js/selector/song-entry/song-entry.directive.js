function songEntry() {
    return {
        restrict: 'E',
        replace: true,
        templateUrl: 'selector/song-entry/song-entry.directive.html',
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
