import { createRouter, createWebHistory } from "vue-router";
import TestItem from "../components/Test.vue";
import StuLogin from "../components/login/StuLogin.vue";
import AdminLogin from "../components/login/AdminLogin.vue";
import pageHome from "../components/pageHome/index.vue";
import sidetoMenu from "../components/adMain/sidetoMenu.vue";
const routes = [
  {
    path: "/",
    name: "default",
    component: StuLogin,
  },
  {
    path: "/userNew/userShow",
    components: {
      default: sidetoMenu,
    },
  },
  {
    path: "/about",
    name: "TestItem",
    component: TestItem,
    children:[
      {
        name:'userManage',
        path:'/userManage',
        components:sidetoMenu,
      }
    ]
  },
  {
    path: "/admin",
    name: "AdminLogin",
    component: AdminLogin,
  },
  {
    path: "/user",
    name: "StuLogin",
    component: StuLogin,
  },
  {
    path: "/home",
    name: "pageHome",
    component: pageHome
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// router.beforeEach((to,from,new)=>{
// // form -  to，只有执行完next()才从from跳转到to
  
// })











export default router;

// // // 提供一个重置路由的方法
//     // export const resetRouter = () => {
//     //   router.matcher = new VueRouter({
//     //     mode: 'history',
//     //     base: process.env.BASE_URL,
//     //     routes
//     //   })
//     // }

//     // // 注意：刷新页面会导致页面路由重置
//     // export const setRoutes = () => {
//     //   const storeMenus = localStorage.getItem("menus");
//     //   if (storeMenus) {

//     //     // 获取当前的路由对象名称数组
//     //     const currentRouteNames = router.getRoutes().map(v => v.name)
//     //     if (!currentRouteNames.includes('Manage')) {
//     //       // 拼装动态路由
//     //       const manageRoute = { path: '/', name: 'Manage', component: () => import('../views/Manage.vue'), redirect: "/home", children: [
//     //           { path: 'person', name: '个人信息', component: () => import('../views/Person.vue')},
//     //           { path: 'password', name: '修改密码', component: () => import('../views/Password.vue')}
//     //         ] }
//     //       const menus = JSON.parse(storeMenus)
//     //       menus.forEach(item => {
//     //         if (item.path) {  // 当且仅当path不为空的时候才去设置路由
//     //           let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue')}
//     //           manageRoute.children.push(itemMenu)
//     //         } else if(item.children.length) {
//     //           item.children.forEach(item => {
//     //             if (item.path) {
//     //               let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue')}
//     //               manageRoute.children.push(itemMenu)
//     //             }
//     //           })
//     //         }
//     //       })
//     //       // 动态添加到现在的路由对象中去
//     //       router.addRoute(manageRoute)
//     //     }

//     //   }
//     // }

//     // // 重置我就再set一次路由
//     // setRoutes()

//     // router.beforeEach((to, from, next) => {
//     //   localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称
//     //   store.commit("setPath")

//     //   // 未找到路由的情况
//     //   if (!to.matched.length) {
//     //     const storeMenus = localStorage.getItem("menus")
//     //     if (storeMenus) {
//     //       next("/404")
//     //     } else {
//     //       // 跳回登录页面
//     //       next("/login")
//     //     }
//     //   }
//     //   // 其他的情况都放行
//     //   next()

// // })
