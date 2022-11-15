<template>
    <h1 style="height:30px;width:100%"></h1>
    <!-- 导航栏？ -->
    <component :is="currentView" />
</template>

<script>
import defaultPage from "./defaultPage.vue";
import activity from "./activity/index.vue";
import notification from "./notification/index.vue";
import organization from "./organization/index.vue";
import todoForm from "./todoForm/index.vue";
import userManage from "./userManage/index.vue";
import wode from "./wode/index.vue";

const routes = {
    '/': defaultPage,
    '/userInfo': userManage,
    '/organization': organization,
    '/todoForm': todoForm,
    '/wode': wode,
    '/activity': activity,
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