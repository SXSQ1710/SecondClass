import { ElMessage } from "element-plus";
import store from '../store/store.js'
import jscookie from 'js-cookie'
import { createRouter, createWebHistory } from "vue-router";
import NotFound from "@/components/404.vue";
const routes = [
  {
    path: "/",
    name: "default",
    component: () => import("../App.vue"),
    redirect: "/welcome",
    meta: {
      title: "登录页",
    },
    children: [
      {
        path: "/user",
        name: "StuLogin",
        component: () => import("../components/login/StuLogin.vue"),
        meta: {
          title: "学生登录",
        },
      },
      {
        path: "/admin",
        name: "AdminLogin",
        component: () => import("../components/login/AdminLogin.vue"),
        meta: {
          title: "后台登录",
        },
      },
      {
        path: "/welcome",
        name: "Welcome",
        component: () => import("../components/Welcome.vue"),
        meta: {
          title: "首页",
        },
      },
      {
        path: "/about",
        name: "test",
        component: () => import("../components/mainItem/test.vue"),
        meta: {
          title: "测试",
        },
      },
      {
        path: "/home",
        name: "pageHome",
        component: () => import("../components/pageHome/index.vue"),
        meta: {
          title: "后台管理",
          requireAuth: true,
        },
      },
      {
        path: "/home2",
        name: "stu_pageHome",
        component: () => import("../components/pageHome/stu/index.vue"),
        meta: {
          title: "学生端",
          requireAuth: true,
        },
      },
    ],
  },
  {
    path: "/:pathMatch(.*)*",
    name: "NotFound",
    component: NotFound,
  },
];
// 页面刷新时，重新赋值token
if (sessionStorage.getItem("token")) {
  store.commit("set_token", sessionStorage.getItem("token"));
}
const router = createRouter({
  history: createWebHistory(),
  routes,
});


router.beforeEach(async (to, from, next) => {
  const title = to.meta.title ? to.meta.title : "default";
  document.title = title;

  // 判断该路由是否需要登录权限
  if (to.meta.requireAuth) {
    //如果token不存在，就回到首页
    if (jscookie.get('satoken')) next();
    else {
      next("/");
      ElMessage("您未登录！");
    }
  } else {
    // 登录过一次后无需再次登录，直至用户退出登录
    if (jscookie.get('satoken')) {
      if (to.path == "/admin") {
        next("/home");
      }
      if (to.path == "/user") {
        next("/home2");
      }
    } else next();
  }
});

export default router;
