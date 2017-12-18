import gulp from 'gulp';
import runSequence from 'run-sequence';

gulp.task('prod', ['clean'], function (cb) {

    cb = cb || function () {};

    global.isProd = true;

    runSequence(['environment', 'styles', 'images', 'favicon', 'fonts', 'views'], 'browserify', 'rev', 'gzip', 'revClean', cb);
});
