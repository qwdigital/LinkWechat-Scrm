import Vue from 'vue'
import config from '@/config'
import '@/assets/icon/iconfont.css'

window.CONFIG = config

import 'normalize.css/normalize.css'

import Vant from 'vant'
import 'vant/lib/index.css'

Vue.use(Vant)

import '@/styles/index.less' // global css

import App from './App.vue'
import router from './router'
import store from './store'

import VConsole from 'vconsole'
process.env.NODE_ENV === 'production' && new VConsole()

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: (h) => h(App)
}).$mount('#app')
