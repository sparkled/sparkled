import config from '../config';
import gulp from 'gulp';
import browserSync from 'browser-sync';

gulp.task('environment', () => {
    return gulp.src(`${config.environment.src}/**/*`)
        .pipe(gulp.dest(config.environment.dest))
        .pipe(browserSync.stream());
});
