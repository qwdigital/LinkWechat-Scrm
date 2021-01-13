import Vue from 'vue'
import config from '@/config'

window.CONFIG = config

import 'normalize.css/normalize.css'

import Vant from 'vant'
import 'vant/lib/index.css'

Vue.use(Vant)

import '@/styles/index.less' // global css

import App from './App.vue'
import router from './router'
import store from './store'

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount('#app')
