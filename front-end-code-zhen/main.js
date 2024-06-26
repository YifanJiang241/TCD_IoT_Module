import App from './App'

// #ifndef VUE3
import Vue from 'vue'
//使用vuex
import store from './store'
Vue.prototype.$store=store

Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
    ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  return {
    app
  }
}
// #endif