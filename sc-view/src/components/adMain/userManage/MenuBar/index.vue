<template>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="用户详情" name="first">
        <defaultPage v-show="selectedIndex == 0" />
      </el-tab-pane>
      <el-tab-pane label="用户组" name="second">
        <activeManage v-show="selectedIndex == 1" />
      </el-tab-pane>
      <el-tab-pane label="用户权限" name="third">
        <notification v-show="selectedIndex == 2" />
      </el-tab-pane>
      <el-tab-pane label="用户信息" name="fourth">
        <orgManage v-show="selectedIndex == 3" />
      </el-tab-pane>
      <el-tab-pane label="部门信息" name="fifth">
        <todoForm v-show="selectedIndex == 4" />
      </el-tab-pane>
  
      <userManage v-show="selectedIndex == 5" />
      <wode v-show="selectedIndex == 6" />
    </el-tabs>
  </template>
  <script>
  import defaultPage from "./adMain/defaultPage.vue";
  import activeManage from "./adMain/activeManage/index.vue";
  import notification from "./adMain/notification/index.vue";
  import orgManage from "./adMain/orgManage/index.vue";
  import todoForm from "./adMain/todoForm/index.vue";
  import userManage from "./adMain/userManage/index.vue";
  import wode from "./adMain/wode/index.vue";
  
  export default {
    name: "MenuBar",
    data() {
      return {
        currentPath: window.location.hash,
        selectedIndex: 0
      }
    },
    methods: {
      handleClick(tab) {
        console.log("tab.index = ", tab.index)
        this.selectedIndex = tab.index
      }
    },
    components: {
      wode, orgManage, userManage, defaultPage, activeManage, notification, todoForm
    }
  
  }
  </script>
  
  <style scoped>
  * {
    user-select: text;
  }
  </style>
  