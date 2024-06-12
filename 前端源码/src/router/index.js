import VueRouter from 'vue-router'  //将createRouter、createWebHistory引入vue

const routes = [
  {
    path: '/',  //配置默认路由
    name: 'home', //路由名
    component: () => import("@/views/ReportView.vue"), //引入该路由使用的组件
  },
];
const router = new VueRouter({ //设置为history模式
  routes
});

export default router
