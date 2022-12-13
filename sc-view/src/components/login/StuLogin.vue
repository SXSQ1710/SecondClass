<template>
   <div class="box">
      <h2>登 录 中 心</h2>
      <div class="loginItem">
         <loginitem></loginitem>
      </div>
      <form @submit.prevent="submit">
         <div class="formrow">
            <div class="inline-input-icon">
               <span class="myicon iconfont icon-yonghu"></span>
            </div>
            <div class="inputBox">
               <input type="text" autocomplete="username" required="required" v-model="username">
               <span>学号</span>
            </div>
         </div>
         <div class="formrow">
            <div class="inline-input-icon">
               <span class="myicon  iconfont icon-suo"></span>
            </div>

            <div class="inputBox">
               <input type="password" autocomplete="current-password" required="required" v-model="password">
               <span>密码</span>
            </div>
         </div>
         <div class="formrow">
            <div class="inline-input-icon">
               <span class="myicon myicon2 iconfont icon-9"></span>
            </div>
            <div class="inputBox yanzheng">
               <input type="text" maxlength="4">
               <span>验证码</span>
               <img :src="sdf" @click="changeImage()" alt="点我更换图片" title="刷新图片" />
               <!-- https://jxfw.gdut.edu.cn/yzm?d=1666938986151 -->
            </div>
         </div>
         <div class="buttonBox formrow">
            <input type="submit" value="登录">
         </div>
      </form>
   </div>
</template>

<script>
import { ElMessage } from "element-plus";
import { h } from 'vue'
import { setUser } from '../../server/api/login'
import loginitem from "./LoginItem.vue"
import axios from "axios"
import store from "../../store/store"
import '../../assets/css/login.css'

export default {
   name: 'StuLogin',
   props: {
      msg: String
   },
   components: { loginitem },
   data() {
      return {
         username: "",
         password: "",
         sdf: "https://jxfw.gdut.edu.cn/yzm?d=" + new Date().getTime()
      }
   },
   methods: {
      changeImage() {
         console.log("changeImage")
         this.sdf = "https://jxfw.gdut.edu.cn/yzm?d=" + new Date().getTime()
         // this.sdf.value =ref( "https://jxfw.gdut.edu.cn/yzm?d=" +new Date().getTime())
      },
      submit() {
         console.log("submit!")
         var _this = this
         var formdata = {
            uid: this.username,
            upassword: this.password,
            role: 0
         };
         axios.post('http://localhost:8083/api/manage/login', formdata).then((res) => {
            //处理成功后的逻辑
            if (res.data['code'] == '1-200') {//这个data 是接收的resolve参数--
               ElMessage({
                  message: h('p', { style: 'color:green' }, [
                     h('span', null, res.data.msg),
                  ]), type: 'success'
               })
               //根据store中set_token方法将token保存至localStorage/sessionStorage中，data["tokenValue"]，获取token的value值  
               store.commit('set_token', res.data.data["tokenValue"]);

               setUser(formdata)
               if (store.state.token) {
                  _this.$router.push("/home2#/wode");
               } else {
                  _this.$router.replace('/user');
               }

            } else {
               ElMessage({ message: res.data.msg, type: 'error' })
            }
         }).catch((err) => {
            ElMessage('请求错误！')
            console.log(err)
         })
      }
   }
}
</script>