<template>
    <TopMenu :toptip="toptip"></TopMenu>
    <AdHome :uname="uname" :ulevel="ulevel" :uImage="uImage"></AdHome>
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
            uImage: "/src/components/pageHome/img.png",
            toptip:"后台管理系统"
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