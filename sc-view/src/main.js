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

import axios from 'axios'
axios.defaults.withCredentials=true //让ajax携带cookie


const Vue = createApp(App);
Vue.config.productionTip = false;
Vue.use(ElementPlus, {
  locale: zhCn,
});
Vue.use(router);
Vue.use(Vuex);
Vue.use(store);


Vue.mount("#app");
