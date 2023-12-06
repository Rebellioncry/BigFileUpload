// 1、按需引入方法
import { createRouter, createWebHashHistory } from "vue-router"
// 2、定义一些路由
const routes = [
    {
     path:"/",
     component:()=>import("../pages/upload.vue")
    }
   ]
//3、创建路由实例
const router = createRouter({
  routes,
  history:createWebHashHistory("./")
})
export default router
