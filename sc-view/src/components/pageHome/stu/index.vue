<template>
    <TopMenu :toptip="toptip"></TopMenu>
    <StuHome :uname="uname" :ulevel="ulevel" :uImage="uImage" :sideBar="sideBar"></StuHome>
</template>
  
<script>
  import StuHome from '../../stuMain/index.vue'
  import TopMenu from '../../mainItem/topmenu.vue'
  import { ElMessage } from "element-plus";
import axios from 'axios'

  export default {
    name: 'pageHome',
    data() {
        return {
            uname : "wode",
            ulevel: "学生",
            uImage: "/src/assets/img.png",
            toptip:"学生端",
            sideBar: {
                list: [{
                    pagePath: '/wode',
                    iconPath: 'bx bx-home-alt icon',
                    text: '我的首页'
                }, {
                    pagePath: '/activity',
                    iconPath: 'bx bx-bar-chart-alt-2 icon',
                    text: '浏览活动'
                }, {
                    pagePath: '/notification',
                    iconPath: 'bx bx-bell icon',
                    text: '我的通知'
                }, {
                    pagePath: '/todoForm',
                    iconPath: 'bx bx-pie-chart-alt icon',
                    text: '自主申报'
                }, {
                    pagePath: '/userInfo',
                    iconPath: 'bx bx-heart icon',
                    text: '个人信息'
                }, {
                    pagePath: '/organization',
                    iconPath: 'bx bx-wallet icon',
                    text: '学生组织'
                }]
            }
        }
    },
    components:{StuHome,TopMenu},
    computed: {
    },
    mounted() {
        let _this = this;
        let _uid = sessionStorage.getItem("uid")
        axios.get('http://localhost:8083/api/manage/user/' + _uid)
            .then((res) => {
                if (res.data['code'] == '7-200') {//这个data 是接收的resolve参数--
                    _this.uname = res.data.data.uname
                    if (res.data.data.oid[1] == 1) {
                        _this.ulevel = "管理员"
                        _this.$router.push("/home")
                    } else if (res.data.data.oid.length > 3) {
                        _this.ulevel = "学生[组织]"
                        this.sideBar.list.push({
                            pagePath: '/',
                            iconPath: 'bx bxs-leaf icon',
                            text: '组织活动'
                        })
                        _this.$router.push("/home2")
                    }
                    else {
                        _this.ulevel = "学生";
                        _this.$router.push("/home2")
                    }
                }
            }).catch((err) => {
                console.log(err)
                ElMessage({ message: err.response.data.msg, type: 'error' })
            })
    }
  }
  </script>
  <style>
  
  </style>