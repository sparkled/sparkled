import gulp from 'gulp';
import config from '../config';
import clean from 'gulp-clean';
import rev from 'gulp-rev';
import revReplace from 'gulp-rev-replace';

gulp.task('revFiles', function () {
    return gulp.src([
        `${config.buildDir}/**/*.{css,js}`,

        // Ignore environment files.
        `!${config.environment.dest}/**/*`
    ], {base: config.buildDir})
        .pipe(rev())
        .pipe(gulp.dest(config.buildDir))
        .pipe(rev.manifest({merge: true}))
        .pipe(gulp.dest(config.buildDir));
});

gulp.task('revReplace', ['revFiles'], function () {
    var manifest = gulp.src(`./${config.buildDir}/rev-manifest.json`);

    return gulp.src(`${config.buildDir}/index.html`)
        .pipe(revReplace({manifest: manifest}))
        .pipe(gulp.dest(config.buildDir));
});

gulp.task('rev', ['revFiles', 'revReplace']);

// This task is a workaround. The proper way to remove the unhashed files is to use a tool like gulp rev-napkin.
// However, an exception is thrown in the gzip task when that is used.
gulp.task('revClean', function () {
    return gulp.src([
        `./${config.buildDir}/rev-manifest.{json,json.gz}`,
        `./${config.buildDir}/css/main.{css,css.gz}`,
        `./${config.buildDir}/js/main.{js,js.gz}`
    ]).pipe(clean());
});
