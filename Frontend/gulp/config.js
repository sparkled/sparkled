import minimist from 'minimist';

const sourceDir = './app';
const buildDir = './build';
const environmentDest = `${buildDir}/environment`;

const args = minimist(process.argv.slice(2));
const environmentName = args.environment || 'development';

export default {

    browserPort: 3000,
    UIPort: 3001,
    testPort: 3002,

    sourceDir: sourceDir,
    buildDir: buildDir,

    dev: {
        stubs: {
            enabled: args.stubs === 'true',
            files: [
                `${sourceDir}/js/`
            ],
            responses: {
                src: `${sourceDir}/dev/stubResponses`,
                dest: `${buildDir}/dev/stubResponses`
            }
        }
    },

    environment: {
        src: `${sourceDir}/environment/${environmentName}`,
        dest: environmentDest
    },

    styles: {
        src: 'app/styles/**/*.scss',
        dest: 'build/css',
        prodSourcemap: false,
        sassIncludePaths: []
    },

    scripts: {
        src: 'app/js/**/*.js',
        dest: 'build/js',
        test: 'test/**/*.js',
        gulp: 'gulp/**/*.js'
    },

    images: {
        src: 'app/images/**/*',
        dest: 'build/images'
    },

    favicon: {
        src: 'app/favicon.ico',
        dest: 'build'
    },

    fonts: {
        src: [
            'node_modules/font-awesome/fonts/**/*',
            'app/fonts/**/*'
        ],
        dest: 'build/fonts'
    },

    assetExtensions: [
        'js',
        'css',
        'png',
        'jpe?g',
        'gif',
        'svg',
        'ico',
        'eot',
        'otf',
        'ttc',
        'ttf',
        'woff2?',
        'json',
        'html'
    ],

    views: {
        index: 'app/index.html',
        src: ['app/layout/**/*.html', 'app/js/**/*.html'],
        dest: 'app/js'
    },

    gzip: {
        src: ['build/**/*.{html,xml,json,css,js,js.map,css.map}', `!${environmentDest}/**/*`],
        dest: 'build/',
        options: {}
    },

    browserify: {
        bundleName: 'main.js',
        prodSourcemap: false
    },

    test: {
        karma: 'test/karma.conf.js'
    },

    init: function () {
        this.views.watch = [
            this.views.index,
            this.views.src
        ];

        return this;
    }

}.init();
