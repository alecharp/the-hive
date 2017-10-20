const commonConfig = require('./common.js');
const webpackConfig = require('./webpack.config.js');

const path = require('path');

module.exports = config => {
  config.set({
    basePath: '..',
    frameworks: ['mocha'],
    files: [
      commonConfig.test + '/**/*.spec.js'
    ],

    browsers: ['Chrome', 'Firefox'],

    reporters: ['progress', 'junit'],
    preprocessors: {
      'src/test/front/**/*.spec.js': ['webpack']
    },
    webpack: webpackConfig('test'),
    junitReporter: {
      outputDir: path.resolve(commonConfig.reports, 'junit')
    }
  });
};
