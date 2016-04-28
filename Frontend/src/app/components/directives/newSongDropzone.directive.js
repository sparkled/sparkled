/*global angular*/
(function () {
    'use strict';
    angular.module('sparkled.component')
        .directive('newSongDropZone', function () {
            return {
                restrict: 'E',
                replace: true,
                templateUrl: 'app/components/directives/newSongDropZone.template.html',
                scope: {
                    addSong: '='
                },
                link: function (scope, element, attrs) {
                    var dropZone = $(element).find('.dropzone');
                    dropZone.bind('dragover', onDragEvent);
                    dropZone.bind('dragenter', onDragEvent);
                    dropZone.bind('dragleave', onDragLeaveEvent);
                    return dropZone.bind('drop', function (event) {
                        onDropEvent(event, element, scope);
                    });
                }
            }
        });

    var maxSizeMb = 10;

    var onDragEvent = function (event) {
        if (event != null) {
            event.preventDefault();
        }

        $(event.target).addClass('dragged');
        event.originalEvent.dataTransfer.effectAllowed = 'copy';
        return false;
    };

    var onDragLeaveEvent = function (event) {
        $(event.target).removeClass('dragged');
    };

    var onDropEvent = function (event, element, scope) {
        var file, name, reader, size, type;
        if (event != null) {
            event.preventDefault();
        }

        $(event.target).removeClass('dragged');

        reader = new FileReader();
        reader.onload = function (evt) {
            if (!isTypeValid(type)) {
                showSongError('Audio file must be an MP3.');
            } else if (!isAcceptableSize(size)) {
                showSongError('File must be no larger than ' + maxSizeMb + 'MB.');
            } else {
                return scope.$apply(function () {
                    scope.file = evt.target.result;
                    if (angular.isString(scope.fileName)) {
                        return scope.fileName = name;
                    }
                });
            }
        };

        file = event.originalEvent.dataTransfer.files[0];

        jsmediatags.read(file, {
            onSuccess: function (tag) {
                addSong(scope, file, tag.tags, element);
            },
            onError: function (error) {
                showSongError(error.info);
            }
        });

        return false;
    };

    var addSong = function (scope, file, tags, element) {
        var objectUrl = URL.createObjectURL(file);
        $('<audio style="display: none"></audio>').appendTo(element)
            .on('canplaythrough', function (e) {
                var songData = {
                    name: tags.title,
                    artist: tags.artist,
                    album: tags.album,
                    durationSeconds: Math.ceil(e.currentTarget.duration)
                };

                scope.addSong(file, songData);

                $(element).find('audio').remove();
                URL.revokeObjectURL(objectUrl);
            })
            .attr('src', objectUrl);
    };

    var isTypeValid = function (type) {
        return type === 'audio/mp3';
    };

    var isAcceptableSize = function (size) {
        return size / 1048576 <= maxSizeMb
    };

    var showSongError = function (errorMessage) {
        sweetAlert({
            title: 'Error',
            text: 'Failed to add song: ' + errorMessage + '.',
            type: 'error'
        });
    }
})();

