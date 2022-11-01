<template>
	<div id="loginItem">
		<h2>Test</h2>
		<div id="banner">
			<el-carousel indicator-position="outside">
				<el-carousel-item v-for="item in imglist" :key="item">
					<img :src="item.path"  alt="123">
					<h3 justify="center">{{ item.path }}</h3>
				</el-carousel-item>
			</el-carousel>
		</div>
	</div>

  </template>
	<!-- setup设置打开页面时的状态，show=true表示展开 -->
	<script  setup lang="ts">
		import { ref } from 'vue'
  		const imglist = [
        {
            path: '../src/assets/apicture/01.jpg',
        },
		{
			path: '../src/assets/apicture/02.jpg',

        },
		{
			path: '../src/assets/apicture/03.jpg',
        }
    ]
	</script>
  <style scoped>
 
  #banner{
	width: 448px;
	height: 300px;
	margin: auto;
	margin-top: 20px;
  }
  .el-carousel__item{
	width:448px;
	height: 300px;
  }
  .el-carousel__item h3 {
	display: flex;
	color: #475669;
	opacity: 0.75;
	line-height: 300px;
	margin: 0;
	justify-content: center;
  }
  
  .el-carousel__item:nth-child(2n) {
	background-color: #99a9bf;
  }
  
  .el-carousel__item:nth-child(2n + 1) {
	background-color: #d3dce6;
  }

img{
	height: 300px;
	width: 448px;
}

#loginItem{
	display: flex;
	align-items: center;
	flex-direction: column;
}

</style>
