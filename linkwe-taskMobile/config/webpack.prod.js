const path = require('path');
const merge = require('webpack-merge');
const common = require('./webpack.common');
const {CleanWebpackPlugin} = require('clean-webpack-plugin')
const UglifyJSPlugin = require('uglifyjs-webpack-plugin');

module.exports = merge(common,{

    output:{
        filename: './js/[name].js',
        path:path.resolve(__dirname,'../','dist'),
        publicPath:'./',
    },
    mode:'production',
    devtool:'none',
    plugins:[
        new CleanWebpackPlugin(),
        new UglifyJSPlugin(),
    ]
})