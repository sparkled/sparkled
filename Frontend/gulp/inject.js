'use strict';

var gulp = require('gulp');
var paths = gulp.paths;

var $ = require('gulp-load-plugins')({
    pattern: ['gulp-*', 'minimist']
});

var wiredep = require('wiredep').stream;

gulp.task('inject', ['styles'], function () {

    var injectStyles = gulp.src([
        paths.tmp + '/serve/{app,components}/**/*.css',
        '/bower_components/bootswatch-dist/css/bootstrap.css'
    ], {read: false});

    var injectScripts = gulp.src([
        paths.src + '/{app,components}/**/*.js',
        '/bower_components/bootswatch-dist/js/bootstrap.min.js'
    ]).pipe($.angularFilesort());

    var injectOptions = {
        ignorePath: [paths.src, paths.tmp + '/serve'],
        addRootSlash: false
    };

    var wiredepOptions = {
        directory: 'bower_components'
    };

    return gulp.src(paths.src + '/*.html')
        .pipe($.inject(injectStyles, injectOptions))
        .pipe($.inject(injectScripts, injectOptions))
        .pipe(wiredep(wiredepOptions))
        .pipe(gulp.dest(paths.tmp + '/serve'));
});
