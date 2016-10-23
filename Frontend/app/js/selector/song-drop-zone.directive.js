const $ = require('jquery');
const jsMediaTags = require('jsmediatags');
const sweetAlert = require('sweetalert2');

const maxMp3SizeMb = 10;
const draggedCssClass = 'dragged';

function songDropZone($timeout) {
    'ngInject';

    const config = {
        restrict: 'E',
        replace: true,
        templateUrl: 'selector/song-drop-zone.template.html',
        scope: {
            onSongDrop: '='
        },
        link: (scope, element) => {
            var dropZone = $(element).find('.dropzone');
            dropZone.bind('dragover', onDragEvent);
            dropZone.bind('dragenter', onDragEvent);
            dropZone.bind('dragleave', onDragLeaveEvent);
            return dropZone.bind('drop', event => onDropEvent(event, element, scope));
        }
    };

    var onDragEvent = event => {
        event.preventDefault();
        $(event.target).addClass(draggedCssClass);
        event.originalEvent.dataTransfer.effectAllowed = 'copy';
        return false;
    };

    var onDragLeaveEvent = event => $(event.target).removeClass(draggedCssClass);

    var onDropEvent = (event, element, scope) => {
        var file, name, reader, size, type;
        event.preventDefault();

        $(event.target).removeClass(draggedCssClass);

        reader = new FileReader();
        reader.onload = evt => {
            if (!isTypeValid(type)) {
                showSongError('Audio file must be an MP3.');
            } else if (!isAcceptableSize(size)) {
                showSongError(`File must be no larger than ${maxMp3SizeMb}MB.`);
            } else {
                return $timeout(() => {
                    scope.file = evt.target.result;
                    if (angular.isString(scope.fileName)) {
                        return scope.fileName = name;
                    }
                });
            }
        };

        file = event.originalEvent.dataTransfer.files[0];

        jsMediaTags.read(file, {
            onSuccess: tag => {
                addSong(scope, file, tag.tags, element)
            },
            onError: error => showSongError(error.info)
        });

        return false;
    };

    var addSong = (scope, file, tags, element) => {
        var objectUrl = URL.createObjectURL(file);
        $('<audio style="display: none"></audio>').appendTo(element)
            .on('canplaythrough', e => {
                var songData = {
                    name: tags.title,
                    artist: tags.artist,
                    album: tags.album,
                    durationSeconds: Math.ceil(e.currentTarget.duration)
                };

                scope.onSongDrop(file, songData);

                $(element).find('audio').remove();
                URL.revokeObjectURL(objectUrl);
            })
            .attr('src', objectUrl);
    };

    var isTypeValid = type => type === 'audio/mp3';
    var isAcceptableSize = size => size / 1048576 <= maxSizeMb;

    var showSongError = errorMessage => {
        sweetAlert({
            title: 'Error',
            text: `Failed to add song: ${errorMessage}.`,
            type: 'error'
        });
    };

    return config;
}

module.exports = {
    name: 'songDropZone',
    fn: songDropZone
};
