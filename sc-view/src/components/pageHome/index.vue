<template>
    <TopMenu :toptip="toptip"></TopMenu>
    <AdHome :uname="uname" :ulevel="ulevel" :uImage="uImage" :sideBar="sideBar"></AdHome>
</template>
  
<script>
import AdHome from '../adMain/index.vue'
import TopMenu from '../mainItem/topmenu.vue'
import { ElMessage } from "element-plus";

import axios from 'axios'
export default {
    name: 'pageHome',
    data() {
        return {
            uname: "",
            ulevel: "",
            uImage: "/src/assets/img.png",
            toptip: "后台管理系统",


            sideBar: {
                list: [{
                    pagePath: '/wode',
                    iconPath: 'bx bx-home-alt icon',
                    text: '我的首页'
                }, {
                    pagePath: '/activeManage',
                    iconPath: 'bx bx-bar-chart-alt-2 icon',
                    text: '活动管理'
                }, {
                    pagePath: '/notification',
                    iconPath: 'bx bx-bell icon',
                    text: '我的通知'
                }, {
                    pagePath: '/todoForm',
                    iconPath: 'bx bx-pie-chart-alt icon',
                    text: '待办事项'
                }, {
                    pagePath: '/userManage',
                    iconPath: 'bx bx-heart icon',
                    text: '用户管理'
                }, {
                    pagePath: '/orgManage',
                    iconPath: 'bx bx-wallet icon',
                    text: '学生组织'
                }]
            }
        }
    },
    components: { AdHome, TopMenu },
    computed: {
    },
    mounted() {
        let _this = this;
        let _uid = sessionStorage.getItem("uid")

        console.log("_uid: ", _uid)

        axios.get('http://localhost:8083/api/manage/user/' + _uid)
            .then((res) => {
                console.log("_uid: ", _uid)
                console.log("_uid: ", res.data)

                if (res.data['code'] == '7-200') {//这个data 是接收的resolve参数--
                    _this.uname = res.data.data.uname
                    if (res.data.data.oid == undefined || res.data.data.oid.length <= 2) {
                        _this.ulevel = "学生";
                        _this.$router.push("/home2")
                    } else if (res.data.data.oid.split(",")[0].split("[")[1] == 1) {
                        _this.ulevel = "管理员"
                        _this.$router.push("/home")
                    } else {
                        _this.ulevel = "学生[组织]"
                        this.sideBar.list.push({
                            pagePath: '/',
                            iconPath: 'bx bxs-leaf icon',
                            text: '组织活动'
                        })
                        _this.$router.push("/home2")
                    }
                }
            }).catch((err) => {
                console.log(err)
            })
    }
}
</script>