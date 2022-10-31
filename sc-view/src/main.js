import App from './App.vue'
import { createApp } from 'vue'
// 引入ELementUI组件库（样式文件单独引入）
import ElementPlus from 'element-plus'
import  'element-plus/dist/index.css'       // 引入样式文件
// 引入阿里巴巴矢量图标css配置
import "./assets/iconfonts/iconfont.css"

import router from './router'

const Vue = createApp(App)

Vue.config.productionTip = false
Vue.use(ElementPlus)
Vue.use(router)

Vue.mount('#app')
