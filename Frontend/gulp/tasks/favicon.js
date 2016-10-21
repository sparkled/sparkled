import config from '../config';
import gulp from 'gulp';
import browserSync from 'browser-sync';

gulp.task('favicon', function () {

    return gulp.src(config.favicon.src)
        .pipe(gulp.dest(config.favicon.dest))
        .pipe(browserSync.stream());
});
