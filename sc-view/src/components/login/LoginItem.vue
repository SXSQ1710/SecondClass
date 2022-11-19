<template>
	<div class="loginItem">
		<div class="shousuo">
			<el-button @click="show = !show">
				<span class="myicon myicon3  iconfont icon-menu"></span>
			</el-button>
	    </div>
			<el-collapse-transition class="folder">
				<div v-show="show">
					<div class="transition-box"> 
                        <router-link to="/user"> 学生登录</router-link>
					</div>
                    <div class="transition-box">   
                        <router-link to="/admin"> 管理员登录</router-link>
					</div>
				</div>
			</el-collapse-transition>
	</div>

  </template>
	<!-- setup设置打开页面时的状态，show=true表示展开 -->
	<script  setup>
		import { ref } from 'vue'
		let show = ref(true)
	</script>
  <style scoped>
  .folder{
        position: relative;
        display: inline-flex;
    }
    .loginItem, .folder{
        gap: 10px;
    }

    .myicon3{
        position: relative;
    }
    .shousuo{
        left: 3%;
        line-height: 60px;
    }
    .shousuo:hover{
        box-shadow: 0.1px 0.1px 1px black;
    }


    .transition-box {
        margin-bottom: 10px;
        width: 100px;
        height: 40px;
        border-radius: 10px;
        background-color: #3b836d;;
        padding: 10px;
        box-sizing: border-box;
        text-align: center;
    }
    .transition-box >a {
        text-decoration: none;
        color: #fff;
    }
    .transition-box >a:hover {
        color: #50c9c3;
    }
    .transition-box >a:active {
    color: #1e625f;
    font-weight: bold;
    }
</style>
