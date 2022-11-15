<template>
    <TopMenu :toptip="toptip"></TopMenu>
    <AdHome :uname="uname" :ulevel="ulevel" :uImage="uImage" :sideBar="sideBar"></AdHome>
</template>
  
<script>
  import AdHome from '../adMain/index.vue'
  import TopMenu from '../mainItem/topmenu.vue'

  export default {
    name: 'pageHome',
    data() {
        return {
            uname : "小张",
            ulevel: "管理员",
            uImage: "/src/assets/img.png",
            toptip:"后台管理系统",
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
    components:{AdHome,TopMenu},
    computed: {
    },
    mounted() {
    }
  }
  </script>
  <style>
  
  </style>