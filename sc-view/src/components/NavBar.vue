<template>
    <header>
        <h1>你好，路由在下面</h1>
        <nav>
            <router-link to="/"> 首页</router-link>
            <router-link to="/home"> 后台</router-link>
            <router-link to="/admin"> 管理员登录</router-link>
            <router-link to="/user"> 学生登录</router-link>
            <router-link to="/404"> test跳转</router-link>
        </nav>
    </header>
    <hr/>
    <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/home2#/wode' }">我的首页</el-breadcrumb-item>
        <el-breadcrumb-item>promotion list</el-breadcrumb-item>
        <el-breadcrumb-item>promotion detail</el-breadcrumb-item>
    </el-breadcrumb>
</template>
<script setup>

</script>
<style scoped>

</style>