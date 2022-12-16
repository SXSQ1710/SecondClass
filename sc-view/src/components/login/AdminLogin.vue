<template>
    <div class="box">
        <h2>登 录 中 心</h2>
        <loginitem></loginitem>
        <form @submit.prevent="submit">
            <div class="formrow">
                <div class="inline-input-icon">
                    <span class="myicon  iconfont icon-yonghu"></span>
                </div>
                <div class="inputBox">
                    <input type="text" autocomplete="username" required="required" v-model="username"
                        @keydown="trimLR($event)" />
                    <span>管理员账号</span>
                </div>
            </div>
            <div class="formrow">
                <div class="inline-input-icon">
                    <span class="myicon  iconfont icon-suo"></span>
                </div>
                <div class="inputBox">
                    <input type="password" autocomplete="current-password" required="required" v-model="password"
                        @keydown="trimLR($event)" />
                    <span>密码</span>
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
import axios from "../../server/http"
import store from "../../store/store"
import '../../assets/css/login.css'

export default {
    name: 'StuLogin',
    components: { loginitem },
    data() {
        return {
            username: "",
            password: "",
        }
    },

    methods: {
        // 禁止输入空格
        trimLR(event) {
            if (event.keyCode == 32) {
                event.returnValue = false
            }
        },
        submit() {
            var _this = this
            var formdata = {
                uid: this.username,
                upassword: this.password,
                role: 1
            };
            axios.post('manage/login', formdata).then((res) => {
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
    },
    // lifecycle hooks
    mounted() {
        document.title = "管理员登录"
    }
}
</script>
