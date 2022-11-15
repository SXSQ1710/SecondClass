<template>
    <TopMenu :toptip="toptip"></TopMenu>
    <StuHome :uname="uname" :ulevel="ulevel" :uImage="uImage" :sideBar="sideBar"></StuHome>
</template>
  
<script>
  import StuHome from '../../stuMain/index.vue'
  import TopMenu from '../../mainItem/topmenu.vue'

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
    }
  }
  </script>
  <style>
  
  </style>