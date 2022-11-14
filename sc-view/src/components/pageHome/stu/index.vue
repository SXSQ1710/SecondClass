<template>
    <TopMenu :toptip="toptip"></TopMenu>
    <AdHome :uname="uname" :ulevel="ulevel" :uImage="uImage"></AdHome>
</template>
  
<script>
  import AdHome from '../../adMain/index.vue'
  import TopMenu from '../../mainItem/topmenu.vue'

  export default {
    name: 'pageHome',
    data() {
        return {
            uname : "wode",
            ulevel: "学生",
            uImage: "/src/components/pageHome/img.png",
            toptip:"学生端"
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