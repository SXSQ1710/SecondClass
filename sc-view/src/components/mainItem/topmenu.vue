<template>
  <div class="topmenu">
    <span class="leftTip">{{ toptip }}</span>
    <span class="rightBtn">
      <p class="exit" @click="exitCheck">退出登录</p>
      <p>本次登录时间：{{ sdf }}</p>
    </span>
  </div>
</template>
<script>
import { ElMessage, ElMessageBox } from 'element-plus'
import { getNowTime } from '../../server/api/time'
import store from '../../store/store.js'

export default {

  name: 'topMenu',
  props: {
    toptip: { type: String, default: "干活啦干活啦" },
    selected: 0
  },
  data() {
    return {
      sdf: Date
    }
  },
  setup() {
    var that = this
    const goToForm = (path) => {
      router.push({
        path: path,
      })
    }
    const exitCheck = () => {
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
          store.commit("del_token"); //清空token
          location.reload();   
        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: '您的退出操作已撤回',
          })
        })
    }
    // 如果没有’{}‘中括号包围open，会默认在页面打开时调用一次该方法
    return { exitCheck, goToForm }
  }
  ,
  methods: {
    getCurrentTime() {
      this.sdf = getNowTime(new Date().getTime())
    }
  },
  created() {
    this.getCurrentTime()
  }
}
</script>
<style scoped>
.topmenu {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100vw;
  height: 8vh;
  font-size: 3vw;
  text-align: left;
  line-height: 8vh;
  background: linear-gradient(to right, rgba(192, 194, 217, 0.751), rgb(248, 229, 229, 0.751));
  border-bottom: 0.1px solid rgba(84, 124, 235, 0.6);
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

.leftTip {
  font-size: 2vw;
  margin-left: 2%;
}
</style>
  