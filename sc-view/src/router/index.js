import { ElMessage } from "element-plus";
import { createRouter, createWebHistory } from "vue-router";

const routes = [
  {
    path: "/",
    name: "default",
    component: () => import("../App.vue"),
    redirect: "/user",
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
        path: "/users",
        name: "users",
        component: () =>
          import("../components/adMain/userManage/MenuBar/index.vue"),
      },
      {
        path: "/about",
        name: "TestItem",
        component: () => import("../components/Test.vue"),
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
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  // 判断该路由是否需要登录权限
  if (to.meta.requireAuth) {
    //如果token不存在，就跳到首页
    if (sessionStorage.getItem("access_token")) next();
    else {
      next("/user");
      ElMessage("您未登录！");
    }
  } else {
    // 登录过一次后无需再次登录，直至用户退出登录
    if (sessionStorage.getItem("access_token")) {
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
