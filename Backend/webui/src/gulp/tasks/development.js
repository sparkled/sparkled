import gulp from 'gulp';
import runSequence from 'run-sequence';

gulp.task('dev', ['clean'], function (cb) {

    global.isProd = false;

    runSequence(['environment', 'stubs', 'styles', 'images', 'favicon', 'fonts', 'views'], 'browserify', 'watch', cb);
});
