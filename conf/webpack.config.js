const commonConfig = require('./common.js');

const path = require('path');
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const merge = require('webpack-merge');

const common = {
  entry: {
    app: path.resolve(commonConfig.src, 'index.js'),
    // vendors: []
  },
  output: {
    path: path.resolve(commonConfig.output),
    filename: '[name]-[chunkhash].js'
  },
  plugins: [
    new ExtractTextPlugin({
      filename: "[name]-[contenthash].css"
    }),
    new HtmlWebpackPlugin({
      template: path.resolve(commonConfig.src, 'index.html'),
      inject: 'body'
    })
  ],
  resolve: {
    extensions: ['.js', '.jsx'],
    modules: [
      path.resolve('node_modules'),
      path.resolve(commonConfig.src)
    ]
  },
  module: {
    rules: [{
      test: /\.js$/,
      exclude: /node_modules/,
      use: [{
        loader: 'babel-loader'
      }]
    }, {
      test: /\.less$/,
      exclude: /node_modules/,
      loader: ExtractTextPlugin.extract({
        use: ['css-loader', 'less-loader'],
        fallback: 'style-loader'
      })
    }]
  }
};
const production = {
  performance: {
    hints: 'warning'
  },
  plugins: [
    new webpack.optimize.CommonsChunkPlugin({
      name: 'vendors'
    }),
    new webpack.DefinePlugin({
      'process.env': {
        NODE_ENV: JSON.stringify('production')
      }
    })
  ]
};
const development = {
  output: {
    path: path.resolve(commonConfig.tmp),
    devtoolModuleFilenameTemplate: 'webpack:///[absolute-resource-path]'
  },
  devtool: 'eval-source-map',
  devServer: {
    inline: true,
    compress: true,
    port: commonConfig.port,
    proxy: {
      '/api': commonConfig.backendServer
    }
  }
};

module.exports = (env = 'prod') => {
  switch(env) {
    case 'dev':
      return merge.smart(common, development);
    case 'test':
      const config = merge.smart(common, production)
      const chunkPluginIndex = config.plugins.findIndex(p => p.chunkNames)
      config.plugins.splice(chunkPluginIndex, 1)
      return config
    case 'prod':
    default:
      return merge.smart(common, production);
  }
};
