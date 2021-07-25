import Vue from 'vue'
import './plugins/axios'
import App from './App.vue'
import store from './store'
import vuetify from './plugins/vuetify'
import swal from './plugins/swal'
import router from './router'

Vue.config.productionTip = false

new Vue({
  router,
  store,
  vuetify,
  swal,
  beforeCreate() {
    this.$store.dispatch('auth/getUserDetails')
  },
  render: h => h(App)
}).$mount('#app')
