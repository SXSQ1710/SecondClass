<template>
  <header>
    <div class="wrapper">
      <router-view v-if="isRouteAlive"></router-view>
    </div>
  </header>
</template>

<script>
export default {
  provide() {
    return {
      reload: this.reload
    }
  },
  name: 'App',
  data() {
    return { isRouteAlive: true }
  },
  mounted() {
  },
  methods() {
    reload = () => {
      // 从 DOM 中删除 my-component 组件
      this.isRouteAlive = false;

      this.$nextTick().then(() => {
        this.isRouteAlive = true;
      });
    }
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
</style>
