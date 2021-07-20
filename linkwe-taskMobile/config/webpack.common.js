const path = require('path')
const webpack = require('webpack')
const HtmlWebpackPlugin = require('html-webpack-plugin')

const htmlTemplateProcess = new HtmlWebpackPlugin({
  template:
    'html-withimg-loader!' + path.resolve(__dirname, '../src/taskProcess.html'),
  hash: true,
  chunks: ['common', 'taskProcess'],
  inject: true,
  minify: false,
  filename: 'taskProcess.html'
})

const htmlTemplateIndex = new HtmlWebpackPlugin({
  template:
    'html-withimg-loader!' + path.resolve(__dirname, '../src/index.html'),
  hash: true,
  // chunks:['common','list'],
  chunks: ['common', 'index'],
  inject: true,
  minify: false,
  filename: 'index.html'
})

const htmlTemplateFission = new HtmlWebpackPlugin({
  template:
    'html-withimg-loader!' + path.resolve(__dirname, '../src/fission.html'),
  hash: true,
  // chunks:['common','list'],
  chunks: ['common', 'fission'],
  inject: true,
  minify: false,
  filename: 'fission.html'
})
var dev = {
  entry: {
    taskProcess: './src/js/taskProcess.js',
    fission: './src/js/fission.js',
    index: './src/js/index.js',
    common: './src/js/common.js'
  },
  resolve: {
    extensions: ['.ts', '.tsx', '.js', '.json']
  },
  module: {
    rules: [
      {
        test: /\.js$/, // 匹配文件路径的正则表达式，通常我们都是匹配文件类型后缀
        exclude: '/node_modules/',
        loader: 'babel-loader' // 指定使用的 loader
      },
      {
        test: /\.css$/,
        exclude: /node_modules/,
        // loader:'style-loader!css-loader?modules&localIdentName=[path][name]---[local]---[hash:base64:5]'
        use: ['style-loader', 'css-loader']
      },
      {
        test: /\.(png|jpg|gif)$/,
        use: [
          {
            loader: 'file-loader',
            options: {
              esModule: false,
              limit: 5 * 1024,
              outputPath: 'images'
            }
          }
        ]
      },
      {
        test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
        loader: 'url-loader',
        options: {
          limit: 10000
        }
      },
      {
        test: /\.html$/,
        // html中的img标签
        use: ['html-withimg-loader']
      }
    ]
  },
  plugins: [
    // 全局暴露统一入口
    new webpack.ProvidePlugin({
      $: 'jquery',
      jQuery: 'jquery',
      'window.jQuery': 'jquery'
    }),
    htmlTemplateIndex,
    htmlTemplateProcess,
    htmlTemplateFission,
    new webpack.HotModuleReplacementPlugin()
  ]
}
module.exports = dev
