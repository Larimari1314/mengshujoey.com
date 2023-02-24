import Vue from 'vue'
import App from './App'
import store from './store'
import router from './router'
import './plugins'
import '@/layouts/export'
import VueI18n from 'vue-i18n'
Vue.use(VueI18n)
const i18n = new VueI18n({
  locale: (function () {
    if (localStorage.getItem('lang')) {
      return localStorage.getItem('lang')
    }
    return 'zh'
  })(),
  messages: {
    en: require('../src/assets/language/en'), //英文语言包
    portugal: require('../src/assets/language/portugal'), //葡萄牙语言包
    zh: require('../src/assets/language/zh'), //中文语言包
    zhc: require('../src/assets/language/zhc'), //中文繁体语言包
  },
})
Vue.config.productionTip = false

new Vue({
  el: '#mengshujoey',
  i18n,
  router,
  store,
  render: (h) => h(App),
})
