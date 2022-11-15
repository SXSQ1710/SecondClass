import { createRouter, createWebHistory } from "vue-router";

const routes = [
  {
    path: "/",
    name: "default",
    component: () => import("../components/login/Welcome.vue"),
    redirect:'/',
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
        meta:{
          title: "测试"
        }
      },
      {
        path: "/home",
        name: "pageHome",
        component: () => import("../components/pageHome/index.vue"), 
        meta:{
          title: "后台管理"
        }
      },
      {
        path: "/home2",
        name: "stu_pageHome",
        component: () => import("../components/pageHome/stu/index.vue"),
        meta:{
          title: "学生端"
        }
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
