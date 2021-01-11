import wx from '@/utils/jweixin-1.2.0.js'
import Vue from 'vue'

import Vant from 'vant'
import 'vant/lib/index.css'

Vue.use(Vant)

import App from './App.vue'
import router from './router'
import store from './store'

window.wx = wx

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount('#app')
