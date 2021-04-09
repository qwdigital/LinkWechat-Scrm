const path = require('path')
const merge = require('webpack-merge');
const common = require('./webpack.common');

var dev = {

    output:{
        filename: '[name].bundle.js',
        path:path.resolve(__dirname,'../','dist'),
        // publicPath:'/',
    },
    devtool:'inline-source-map',
      mode:"development",
    devServer:{
        // contentBase: path.join(__dirname, "../src"),
        // publicPath:'/',
        // host: "192.168.1.3",
        host: "localhost",
        port: "3333",
        // overlay: true, // 浏览器页面上显示错误
        // open: true, // 开启自动打开浏览器
        // stats: "errors-only", //stats: "errors-only"表示只打印错误：
        hot: true, // 开启热更新
        historyApiFallback: true,
        disableHostCheck:true,
        // host:'0.0.0.0',
        proxy: {
            '/': {
                // target: 'http://119.45.28.29:8090',
                target: 'http://146.56.222.200:8090',
                // target:'http://47.112.117.15:40001',
                changeOrigin: true,
                // ws: true,
                pathRewrite: {
                    // "^/api": "/"
                }
            }
        }
    },
    plugins:[

    ]
}
module.exports = merge(common, dev);