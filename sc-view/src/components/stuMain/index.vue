<template>
    <menu :class="dark ? 'dark' : ''">
        <nav :class="unfold ? ' sidebar unfold' : 'sidebar close'">
            <header>
                <div class="image-text">
                    <span class="image">
                        <img :src="uImage" alt="">
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
                            <a @click="open">
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
                <Commons  :uname="uname" :ulevel="ulevel" :uImage="uImage" :sideBar="sideBar" ></Commons>
            </div>
            <div class="text">
                当前时间：{{ sdf }}
            </div>
        </section>
    </menu>
</template>

<style type="text/css">
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-family: "Poppins", sans-serif;
}

/* 一些需要重复使用的样式 */
:root {
    /* 颜色 */
    --body-color: #e4e9f7;
    --sidebar-color: #fff;
    --primary-color: #695cfe;
    --primary-color-light: #f6f5ff;
    --toggle-color: #ddd;
    --text-color: #707070;

    /* 过渡效果 */
    --tran-02: all 0.2s ease;
    --tran-03: all 0.3s ease;
}

menu {
    max-height: 92vh;
    background-color: var(--body-color);
    transition: var(--tran-03);
}

::selection {
    background-color: var(--primary-color);
    color: #fff;
}

/* 当body标签拥有dark类名的时候的样式 */
menu.dark {
    --body-color: #18191a;
    --sidebar-color: #242526;
    --primary-color: #3a3b3c;
    --primary-color-light: #3a3b3c;
    --toggle-color: #fff;
    --text-color: #ccc;
}

/* sidebar上的初始化样式 */
.sidebar {
    position: absolute;
    bottom: 0;
    /* left: -88px; */
    height: 92%;
    width: 220px;
    padding: 10px 14px;
    background: var(--sidebar-color);
    transition: var(--tran-03);
    z-index: 100;
}

.sidebar.close {
    width: 88px;
}

.sidebar li {
    height: 50px;
    list-style: none;
    display: flex;
    align-items: center;
    margin-top: 10px;
}

.sidebar header .image,
.sidebar .icon {
    min-width: 60px;
    border-radius: 6px;
}

.sidebar .icon {
    min-width: 60px;
    border-radius: 6px;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 20px;
}

.sidebar .text,section,
.sidebar .icon {
    color: var(--text-color);
    transition: var(--tran-03);
}

.sidebar .text {
    font-size: 17px;
    font-weight: 500;
    white-space: nowrap;
    opacity: 1;
}

.sidebar.close .text {
    opacity: 0;

}

/* header部分样式 */

.sidebar header {
    position: relative;
}

.sidebar header .image-text {
    display: flex;
    align-items: center;
}

.sidebar header .logo-text {
    display: flex;
    flex-direction: column;
}

header .image-text .name {
    margin-top: 2px;
    font-size: 18px;
    font-weight: 600;
}

header .image-text .profession {
    font-size: 16px;
    margin-top: -2px;
    display: block;
}

.sidebar header .image {
    display: flex;
    align-items: center;
    justify-content: center;
}

.sidebar header .image img {
    width: 40px;
    border-radius: 6px;
}

.sidebar header .toggle {
    position: absolute;
    top: 50%;
    right: -25px;
    transform: translateY(-50%) rotate(180deg);
    height: 25px;
    width: 25px;
    background-color: var(--primary-color);
    color: var(--sidebar-color);
    display: flex;
    border-radius: 50%;
    align-items: center;
    justify-content: center;
    font-size: 22px;
    cursor: pointer;
    transition: var(--tran-03);
}

menu.dark .sidebar header .toggle {
    color: var(--text-color);
}

.sidebar.close .toggle {
    transform: translateY(-50%) rotate(0deg);
}

.sidebar .menu {
    margin-top: 5px;
}

.sidebar li.search-box {
    border-radius: 6px;
    background-color: var(--primary-color-light);
    cursor: pointer;
    transition: var(--tran-03);
}

.sidebar li.search-box input {
    height: 100%;
    width: 100%;
    outline: none;
    border: none;
    background-color: var(--primary-color-light);
    color: var(--text-color);
    border-radius: 6px;
    font-size: 17px;
    font-weight: 500;
    transition: var(--tran-03);
}

.sidebar li a {
    list-style: none;
    background-color: transparent;
    display: flex;
    align-items: center;
    height: 100%;
    width: 100%;
    border-radius: 6px;
    text-decoration: none;
    transition: var(--tran-03);
}

.dashbord{
    padding: 0 3vw;
}

.sidebar li a:hover,
.sidebar li.selected {
    background-color: var(--primary-color);
}

/* 带上selected类的li元素，li.selected */
.sidebar li a:hover .icon,
.sidebar li a:hover .text,
.sidebar li.selected .icon,
.sidebar li.selected .text {
    color: var(--sidebar-color);
}

menu.dark .sidebar li a:hover .icon,
menu.dark .sidebar li a:hover .text,
menu.dark .sidebar li.selected .icon,
menu.dark .sidebar li.selected .text {
    color: var(--text-color);
}

.sidebar .menu-bar {
    height: calc(100% - 55px);
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    overflow-y: scroll;
}

.menu-bar::-webkit-scrollbar {
    display: none;
}

.sidebar .menu-bar .mode {
    border-radius: 6px;
    background-color: var(--primary-color-light);
    position: relative;
    transition-timing-function: var(--tran-03);
}

.menu-bar .mode .sun-moon {
    height: 50px;
    width: 60px;
}

.mode .sun-moon i {
    position: absolute;
}

.mode .sun-moon i.sun {
    opacity: 0;
}

menu.dark .mode .sun-moon i.sun {
    opacity: 1;
}

menu.dark .mode .sun-moon i.moon {
    opacity: 0;
}

.menu-bar .bottom-content .toggle-switch {
    position: absolute;
    right: 0;
    height: 100%;
    min-width: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 6px;
    cursor: pointer;
}

.toggle-switch .switch {
    position: relative;
    height: 22px;
    width: 40px;
    border-radius: 25px;
    background-color: var(--toggle-color);
    transition: var(--tran-03);
}

.switch::before {
    content: "";
    position: absolute;
    width: 15px;
    height: 15px;
    border-radius: 50%;
    top: 50%;
    left: 5px;
    transform: translateY(-50%);
    background-color: var(--sidebar-color);
    transition: var(--tran-03);
}

menu.dark .switch::before {
    left: 20px;
}

.home {
    position: absolute;
    bottom: 0;
    left: 220px;
    height: 92vh;
    width: calc(100% - 220px);
    background-color: var(--body-color);
    transition: var(--tran-03);
    user-select: none;
}

.home .text {
    font-weight: 500;
    color: var(--text-color);
    padding: 12px 60px;
    position: absolute;
    left: 0;
    bottom: 0;
}

.sidebar.close~.home {
    left: 88px;
    height: 92vh;
    width: calc(100% - 88px);
}

menu.dark .home .text {
    color: var(--text-color);
}

.unfold {
    left: 0px;
}
</style>
<script>
import { ElMessage, ElMessageBox } from 'element-plus'
import Commons from './sidetoMenu.vue'
import {getNowTime} from '../../server/api/time'

export default {
    name: 'HomePage',
    props: {
        uname: { type: String, default: "AdminName" },
        uImage: { type: String, default: "#" },
        ulevel: { type: String, default: "Administrator" },
        sideBar:{type:Object, default: ""}
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
        const open = () => {
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
                    // console.log("that.$router=" , window.getCurrentTime)
                    // that.$router.push({path:'/home ', selected:"2" })
                })
                .catch(() => {
                    ElMessage({
                        type: 'info',
                        message: '您的退出操作已撤回',
                    })
                })
        }
        return { open }
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
            var _this = this;
            this.sdf = getNowTime(new Date().getTime())     
        }
    },
    // lifecycle hooks
    mounted() {
    }
}
</script>

