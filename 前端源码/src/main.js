import Vue from 'vue'
import App from './App.vue'
import 'element-ui/lib/theme-chalk/index.css';
import ElementUI from 'element-ui';
import VueRouter from 'vue-router';
import router from "@/router/index"
import Request from "@/api/common/Request"
Vue.config.productionTip = false;
Vue.use(VueRouter);
Vue.use(ElementUI);
import AvueUeditor from 'avue-plugin-ueditor'
Vue.use(AvueUeditor);

Vue.use(window.AVUE, {
  size: 'small',
  tableSize: 'small',
});

window.axios = Request
Vue.prototype.$axios = Request;

new Vue({
  router,
  render: h => h(App),
}).$mount('#app');
