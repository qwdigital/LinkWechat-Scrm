import Vue from 'vue'
// require('promise.prototype.finally').shim()

import Cookies from 'js-cookie'

import 'normalize.css/normalize.css' // a modern alternative to CSS resets

import Element from 'element-ui'
import './styles/element-variables.scss'

Vue.use(Element, {
  size: Cookies.get('size') || 'small' // set element-ui default size
})

import '@/styles/common.scss' // common css
import '@/styles/index.scss' // global css
import 'video.js/dist/video-js.css'

import config from '@/config'
import App from './App'
import store from './store'
import router from './router'

import './assets/icons' // icon
import './permission' // permission control
import { getDicts } from '@/api/system/dict/data'
import { getConfigKey } from '@/api/system/config'
import {
  parseTime,
  resetForm,
  addDateRange,
  selectDictLabel,
  selectDictLabels,
  download,
  handleTree
} from '@/utils/common'

import Pagination from '@/components/Pagination'
import RightToolbar from '@/components/RightToolbar' //自定义表格工具扩展
import Upload from '@/components/Upload'
import ButtonSync from '@/components/ButtonSync'
import TagEllipsis from '@/components/TagEllipsis'
// 全局组件挂载
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)
Vue.component('Upload', Upload)
Vue.component('ButtonSync', ButtonSync)
Vue.component(TagEllipsis.name, TagEllipsis)

import directive from './directive'
Vue.use(directive)

// 全局方法挂载
Vue.prototype.getDicts = getDicts
Vue.prototype.getConfigKey = getConfigKey
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.download = download
Vue.prototype.handleTree = handleTree

Vue.prototype.msgSuccess = function (msg) {
  this.$message({ showClose: true, message: msg, type: 'success' })
}

Vue.prototype.msgError = function (msg) {
  this.$message({ showClose: true, message: msg, type: 'error' })
}

Vue.prototype.msgInfo = function (msg) {
  this.$message.info(msg)
}

import { pickerOptions } from '@/utils/index'
Vue.prototype.pickerOptions = pickerOptions
import VideoPlayer from 'vue-video-player'
Vue.use(VideoPlayer)
// import AudioPlayer from '@liripeng/vue-audio-player'
// import '@liripeng/vue-audio-player/lib/vue-audio-player.css'

// Vue.use(AudioPlayer)
import VueAMap from 'vue-amap'

Vue.use(VueAMap)

VueAMap.initAMapApiLoader({
  key: '32396af00cd726deed804cf5b63ed2d8',
  plugin: [
    'AMap.Autocomplete',
    'AMap.PlaceSearch',
    'AMap.Scale',
    'AMap.OverView',
    'AMap.ToolBar',
    'AMap.MapType',
    'AMap.PolyEditor',
    'AMap.CircleEditor'
  ],
  v: '1.4.4'
})

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: (h) => h(App)
})
