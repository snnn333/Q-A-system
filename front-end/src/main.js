// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios'
import qs from 'qs'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import GL_Component from '@/components/globalComponents'


Vue.prototype.$qs=qs
Vue.prototype.$axios= axios
Vue.config.productionTip = false
Vue.use(GL_Component)
Vue.use(ElementUI)
// 允许携带cookie
axios.defaults.withCredentials=true;
import './style.css'
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router:router,
  components: { App },
  template: '<App/>'
})


