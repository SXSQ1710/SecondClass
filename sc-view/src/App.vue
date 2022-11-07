<template>
  <header>
    <div class="wrapper">
      <router-view></router-view>
    </div>
  </header>
</template>

<script>
// import NavBar from './components/NavBar.vue'

export default {
  name: 'App',
  data() {
    return {}
  },
  // components:{NavBar},
  mounted() {
  }
}
</script>

<style scoped>
header {
  line-height: 1.5;
}

#app {
  margin: 0;
}

/*定义一个区别于手机的尺寸，宽小于1024px*/
@media (max-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
}
</style>
