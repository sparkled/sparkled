import config from '../config';
import gulp from 'gulp';
import browserSync from 'browser-sync';

gulp.task('stubs', () => {
    if (config.dev.stubs.enabled) {
        return gulp.src(`${config.dev.stubs.responses.src}/**/*`)
            .pipe(gulp.dest(config.dev.stubs.responses.dest))
            .pipe(browserSync.stream());
    }

    // No-op.
    return gulp.src('.');
});
