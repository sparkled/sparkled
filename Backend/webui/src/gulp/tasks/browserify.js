import gulp from 'gulp';
import gulpif from 'gulp-if';
import source from 'vinyl-source-stream';
import sourcemaps from 'gulp-sourcemaps';
import buffer from 'vinyl-buffer';
import streamify from 'gulp-streamify';
import watchify from 'watchify';
import browserify from 'browserify';
import babelify from 'babelify';
import uglify from 'gulp-uglify';
import browserSync from 'browser-sync';
import ngAnnotate from 'browserify-ngannotate';
import bulkify from 'bulkify';
import envify from 'envify';
import handleErrors from '../util/handleErrors';
import bundleLogger from '../util/bundleLogger';
import config from '../config';

// Based on: http://blog.avisi.nl/2014/04/25/how-to-keep-a-fast-build-with-browserify-and-reactjs/
function buildScript(files) {
    const shouldCreateSourcemap = !global.isProd || config.browserify.prodSourcemap;

    let bundler = browserify({
        entries: files,
        debug: shouldCreateSourcemap,
        cache: {},
        packageCache: {},
        fullPaths: !global.isProd
    });

    if (!global.isProd) {
        bundler = watchify(bundler);
        bundler.on('update', rebundle);
    }

    const transforms = [
        {name: babelify, options: {}},
        {name: ngAnnotate, options: {}},
        {name: 'brfs', options: {}},
        {name: bulkify, options: {}},
        {name: envify, options: {}}
    ];

    transforms.forEach(function (transform) {
        bundler.transform(transform.name, transform.options);
    });

    function rebundle() {
        bundleLogger.start();

        const stream = bundler.bundle();
        const sourceMapLocation = global.isProd ? './' : '';

        return stream
            .on('error', handleErrors)
            .on('end', bundleLogger.end)
            .pipe(source(config.browserify.bundleName))
            .pipe(gulpif(shouldCreateSourcemap, buffer()))
            .pipe(gulpif(shouldCreateSourcemap, sourcemaps.init({loadMaps: true})))
            .pipe(gulpif(global.isProd, streamify(uglify({
                compress: {drop_console: true} // eslint-disable-line camelcase
            }))))
            .pipe(gulpif(shouldCreateSourcemap, sourcemaps.write(sourceMapLocation)))
            .pipe(gulp.dest(config.scripts.dest))
            .pipe(browserSync.stream());
    }

    return rebundle();
}

gulp.task('browserify', function () {
    var files = [];

    if (process.env.NODE_ENV === 'development') {
        files.push(`${config.sourceDir}/js/source-map-support.js`);
    }

    files.push(`${config.sourceDir}/js/main.js`);

    if (config.dev.stubs.enabled) {
        files.push(`${config.sourceDir}/js/http-backend.stubs.js`);
    }
    return buildScript(files);
});
