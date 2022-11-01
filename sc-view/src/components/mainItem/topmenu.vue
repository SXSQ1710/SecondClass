<template>
  <div class="topmenu">
    <span>{{ toptip }}</span>
    <span class="rightBtn">
      <p class="exit" @click="open" >退出登录</p>
      <p>当前登录时间为：{{ nowtime }}</p>
    </span>
  </div>
</template>
<script>
import { ElMessage, ElMessageBox } from 'element-plus'

export default {

  name: 'topMenu',
  props: {
    toptip: { type: String, default: "干活啦干活啦" },
    selected: 0
  },
  data() {
    return {
      nowtime: Date
    }
  },
  setup(){
    var that = this
    const goToForm = (path) => {
      router.push({
        path: path,
      })
    }
  const open = () => {
    ElMessageBox.confirm(
      '确定要退出登录吗?',
      '退出登录',
      {
        confirmButtonText: '退出',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
      .then(() => {
        ElMessage({
          type: 'success',
          message: '您已退出系统，请重新登录'
        })
        // console.log("that.$router=" , window.getCurrentTime)
        // that.$router.push({path:'/home ', selected:"2" })
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '您的退出操作已撤回',
        })
      })
      }
      // 如果没有’{}‘中括号包围open，会默认在页面打开时调用一次该方法
      return {open,goToForm}
  }
  ,
  methods: {
    getCurrentTime() {
      var _this = this;
      let yy = new Date().getFullYear();
      let mm = new Date().getMonth() + 1;
      let dd = new Date().getDate();
      let hh = new Date().getHours();
      let mf = new Date().getMinutes() < 10 ? '0' + new Date().getMinutes() : new Date().getMinutes();
      let ss = new Date().getSeconds() < 10 ? '0' + new Date().getSeconds() : new Date().getSeconds();
      _this.gettime = yy + '/' + mm + '/' + dd + ' ' + hh + ':' + mf + ':' + ss;
      _this.nowtime = _this.gettime
    }
  },
  created() {
    this.getCurrentTime()
  }
}
</script>
<style scoped>
.topmenu {
  width: 100vw;
  height: 8vh;
  font-size: 3vw;
  text-align: left;
  line-height: 8vh;
  position: relative;
  background: linear-gradient(to top, rgb(192, 194, 217), rgb(248, 229, 229));
  /* background: var(--sidebar-color);
    transition: var(--tran-03); */
  border-bottom: 0.5px solid rgba(228, 233, 247, 0.6);
}

.rightBtn {
  float: right;
  margin-right: 2%;
  font-size: 16pt;
  display: flex;
  flex-direction: row-reverse;
  gap: 20px;
  user-select: none;
}

#topMenu {
  display: flex;
  align-items: center;
  flex-direction: column;
}

.exit {
  color: red;
  cursor: pointer;
}

.exit:hover {
  opacity: .6;
}

.exit:active {
  color: rgba(80, 17, 17, 0.6);
}
</style>
  