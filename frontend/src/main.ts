import Vue from 'vue';
import App from './App.vue';
import router from './router/router';
import store from './store';
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.use(BootstrapVue)

import 'devextreme/dist/css/dx.common.css';
import 'devextreme/dist/css/dx.light.css';



Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount('#app');
