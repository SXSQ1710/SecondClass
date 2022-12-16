<template>
    <menu :class="dark ? 'dark' : ''">
        <nav :class="unfold ? ' sidebar unfold' : 'sidebar close'">
            <header>
                <div class="image-text">
                    <span class="image">
                        <img :src="uImage" alt="头像">
                    </span>
                    <div class="text logo-text">
                        <span class="name">{{ uname }}</span>
                        <span class="profession">{{ ulevel }}</span>
                    </div>
                </div>
                <i class="bx bx-chevron-right toggle" @click="changeMode(0)"></i>
            </header>
            <div class="menu-bar">
                <div class="menu">

                    <ul class="menu-links">
                        <li class="search-box">
                            <i class="bx bx-search icon"></i>
                            <input type="text" placeholder="search...">
                        </li>
                        <!-- 采用循环v-for -->
                        <li :class='selected != index ? "nav-link" : "nav-link selected"'
                            v-for="(item, index) in sideBar.list" @click="nav(index)">
                            <a :href="'#' + item.pagePath">
                                <i :class="item.iconPath"></i>
                                <span class="text nav-text">{{ item.text }}</span>
                            </a>
                        </li>
                    </ul>
                </div>
                <div class="bottom-content">
                    <ul>
                        <li>
                            <a @click="exitCheck">
                                <i class="bx bx-log-out icon"></i>
                                <span class="text nav-text">注销</span>
                            </a>
                        </li>
                        <li class="mode">
                            <div class="sun-moon">
                                <i class="bx bx-moon icon moon"></i>
                                <i class="bx bx-sun icon sun"></i>
                            </div>
                            <span class=" text mode-text">{{ mode }}</span>
                            <div class="toggle-switch" @click="changeMode(1)">
                                <span class="switch"></span>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>

        <section class="home">
            <div class="dashbord">
                <Commons :selected=selected></Commons>
            </div>
            <div class="text">
                当前时间：{{ sdf }}
            </div>
        </section>
    </menu>
</template>
<script>
import { ElMessage, ElMessageBox } from 'element-plus'
import Commons from './sidetoMenu.vue'
import { getNowTime } from '../../server/api/time'
import '../../assets/css/pageHome.css'
import store from '../../store/store.js'


export default {
    name: 'HomePage',
    props: {
        uname: { type: String, default: "AdminName" },
        uImage: { type: String, default: "#" },
        ulevel: { type: String, default: "Administrator" },
        sideBar: { type: Object, default: "" }
    },
    components: { Commons },
    data() {
        return {
            mode: '夜间模式',
            dark: false,
            unfold: true,
            selected: 0,
            sdf: Date,

        }
    },
    setup() {

        const exitCheck = () => {
            ElMessageBox.confirm(
                '确定要退出登录吗?',
                '退出登录',
                {
                    confirmButtonText: '退出',
                    cancelButtonText: '取消',
                    type: 'warning',
                }
            )
                .then(() => {
                    ElMessage({
                        type: 'success',
                        message: '您已退出系统，请重新登录'
                    })
                    store.commit("del_token"); //清空token
                    location.reload();   
                })
                .catch((error) => {
                    console.log(error)
                })
        }
        return { exitCheck }
    },
    created() {
        this.getTimes();
        this.mode = this.dark ? ('日间模式') : ('夜间模式')
    },
    // 在Vue实例销毁前，清除我们的定时器
    beforeDestroy() {
        if (this.sdf) {
            clearInterval(this.getCurrentTime);
        }
    },
    methods: {
        getTimes() {
            setInterval(this.getCurrentTime, 1000);
        },
        nav(index) {
            console.log(index)
            this.selected = index
        }
        ,
        changeMode(index) {
            // 0=false ， 折叠；1=true，日夜
            if (index) {
                this.dark = !this.dark
                this.mode = this.dark ? ('日间模式') : ('夜间模式')
            } else {
                this.unfold = !this.unfold
            }
        },
        getCurrentTime() {
            this.sdf = getNowTime(new Date().getTime())
        }
    },
    // lifecycle hooks
    mounted() {
    }
}
</script>