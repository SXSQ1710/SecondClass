<template>
    <h1>你好，我是UserMA</h1>
    <component :is="currentView" />
</template>

<script>
import defaultPage from "./defaultPage.vue";
import activeManage from "./activeManage/index.vue";
import notification from "./notification/index.vue";
import orgManage from "./orgManage/index.vue";
import todoForm from "./todoForm/index.vue";
import userManage from "./userManage/index.vue";
import wode from "./wode/index.vue";

const routes = {
    '/':defaultPage,
  '/userManage': userManage,
  '/orgManage': orgManage,
  '/todoForm': todoForm,
  '/wode': wode,
  '/activeManage': activeManage,
  '/notification': notification
}

export default {
    name: "sidetoMenu",
    data() {
        return {
            currentPath: window.location.hash
        }
    },
    computed: {
        currentView() {
            if (this.currentPath) {
                return routes[this.currentPath.slice(1)]
            }
            else {
                return routes["/"]
            }
        }
    },
    // components:{NavBar},
    mounted() {
        window.addEventListener('hashchange', () => {
            this.currentPath = window.location.hash
            console.log("this.currentPath=", this.currentPath)
        })
    }

}
</script>

<style scoped>
* {
    user-select: text;
}
</style>