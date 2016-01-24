'use strict';

var gulp = require('gulp');

gulp.paths = {
    src: 'src',
    dist: 'dist',
    tmp: '.tmp',
    lib: 'lib'
};

require('require-dir')('./gulp');

gulp.task('default', ['clean'], function () {
    gulp.start('build');
});
