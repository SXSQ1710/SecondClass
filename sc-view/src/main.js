import App from "./App.vue";
import { createApp } from "vue";
// 引入ELementUI组件库（样式文件单独引入）
import ElementPlus from "element-plus";
import zhCn from "element-plus/es/locale/lang/zh-cn";
import "element-plus/dist/index.css"; // 引入样式文件
// 引入阿里巴巴矢量图标css配置
import "./assets/iconfonts/iconfont.css";
// 引入vuex
import store from "./store/store.js";
import Vuex from "vuex";
// // 引入windi.css清除默认样式
// import 'virtual:windi.css'

// 引入路由配置
import router from "./router";
import axios from "axios";

const Vue = createApp(App);
Vue.config.productionTip = false;
Vue.use(ElementPlus, {
  locale: zhCn,
});
Vue.use(router);
Vue.use(Vuex);
Vue.use(store);

// axios.defaults.withCredentials = true;
// // axios.defaults.headers.common["tokenValue"] = store.state.token;
// // // 添加请求拦截器
// // axios.interceptors.request.use(
// //   (config) => {
// //     // 在发送请求之前做些什么
// //     //判断是否存在token，如果存在将每个页面header都添加token
// //     if (store.state.token) {
// //       config.headers.common["tokenValue"] = store.state.token;
// //     }
// //     return config;
// //   },
// //   (error) => {
// //     // 对请求错误做些什么
// //     return Promise.reject(error);
// //   }
// // );

// // http response 拦截器
// axios.interceptors.response.use(
//   (response) => {
//     return response;
//   },
//   (error) => {
//     if (error.response) {
//       switch (error.response.status) {
//         case 401:
//           store.commit("del_token");
//           router.replace({
//             path: "/login",
//             query: { redirect: "/" }, //登录成功后跳入浏览的当前页面
//           });
//       }
//     }
//     return Promise.reject(error);
//   }
// );
Vue.mount("#app");
