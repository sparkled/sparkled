import gulp from 'gulp';
import runSequence from 'run-sequence';

gulp.task('serveBuild', (cb) => {

    global.isProd = true;
    global.isWatching = true;

    runSequence('browserSync', cb);
});
