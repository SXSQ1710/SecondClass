<template>
  <div class="box">
    <div class="banner">
      <el-carousel trigger="click" height="100vh">
        <el-carousel-item v-for="(item) in banner">
          <img :src="item.path" alt="" />
        </el-carousel-item>
      </el-carousel>
    </div>
    <div class="text">
      <div class="title">
        <p>欢迎登录第二课堂系统</p>
      </div>
      <div class="log_in">
        <div class="iconBtn" @click="to(1)">
          <div class="myicon">
            <i class='bx bx-smile'></i>
          </div>
          <div class="icontxt">
            学生登录
          </div>
        </div>
        <div class="iconBtn" @click="to(2)">
          <div class="myicon">
            <i class='bx bx-cool'></i>
          </div>
          <div class="icontxt">
            管理员登录
          </div>
        </div>

      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      banner: [
        {
          path: '../src/assets/apicture/01.jpg',
        }, {
          path: '../src/assets/apicture/02.jpg',
        }, {
          path: '../src/assets/apicture/03.jpg',
        },
      ],
    }
  },
  methods: {
    to(type) {
      if (type == 1)
        this.$router.push("/user");
      if (type == 2)
        this.$router.push("/admin");
    }
  }
}


</script>
<style scoped>
* {
  margin: 0;
  padding: 0;
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

.el-carousel img {
  height: 100%;
  width: 100%;
  opacity: 0.6;
}

.box {
  height: 100vh;
  display: grid;
  background: #09203f;
  color: white;
  text-decoration: none;
  margin: -8px;
  grid-template-columns: 70% 30%;
  align-items: center;
}
.text{
  height: 80vh;
  user-select: none;
}

.title {
  height: 200px;
  line-height: 200px;
  margin-bottom: 100px;
}

.title>p {
  font-size: 30pt;
  text-align: center;
}

/* 下面是对登录按钮的一些设置 */
.log_in {
  display: flex;
  column-gap: 1vw;
  padding-left: 0.5vw;
}

.iconBtn {
  width: 14vw;
  height: 25vh;
  line-height: 10vh;
  font-size: 22pt;
  text-align: center;
  /* justify-content: center; */
  background: #688493;
  cursor: pointer;
}

.iconBtn:nth-child(1):hover {
  opacity: 0.6;
  transform: rotate(5deg);
}

.iconBtn:nth-child(2):hover {
  opacity: 0.6;
  transform: rotate(-5deg);
}

.iconBtn:active {
  opacity: 1;
  color: var(--text-color);
}

.iconBtn>.myicon {
  color: #a2dda2;
  font-size: 40pt;
  padding-top: 1vh;
}
</style>