const minimist = require('minimist');
const istanbul = require('browserify-istanbul');
const isparta = require('isparta');

const args = minimist(process.argv.slice(2));
const coverageEnabled = args.coverage === 'true';

const reporters = ['progress'];
const transforms = [
    'babelify',
    'browserify-ngannotate',
    'bulkify'
];

if (coverageEnabled) {
    console.log('Coverage reporting is enabled.');

    reporters.push('coverage');
    transforms.push(
        istanbul({
            instrumenter: isparta,
            ignore: ['**/node_modules/**', '**/test/**']
        })
    );
} else {
    console.log('Coverage reporting is not enabled.');
}

const karmaBaseConfig = {

    basePath: '../',

    singleRun: true,

    frameworks: ['jasmine', 'browserify'],

    preprocessors: {
        'app/js/**/!(*.spec|*.dev|*.module|*.constants).js': ['browserify', 'coverage'],
        'app/js/**/(*.spec|*.dev|*.module|*.constants).js': ['browserify']
    },

    browsers: ['Chrome'],

    reporters: reporters,

    autoWatch: false,

    browserify: {
        debug: true,
        extensions: ['.js'],
        transform: transforms
    },

    files: [
        // app-specific code
        'app/js/main.js',

        // 3rd-party resources
        'node_modules/angular-mocks/angular-mocks.js',

        // test files
        'app/js/**/*.spec.js'
    ]

};

module.exports = function (config) {
    config.set(karmaBaseConfig);
};
