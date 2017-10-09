import config from '../config';
import gulp from 'gulp';
import browserSync from 'browser-sync';

gulp.task('images', function () {

    return gulp.src(config.images.src)
        .pipe(gulp.dest(config.images.dest))
        .pipe(browserSync.stream());
});
