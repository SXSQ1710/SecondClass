<template>
    <div class="box">
        <h2>登 录 中 心</h2>
        <loginitem></loginitem>
        <form action="#">
            <div class="formrow">
                <div class="inline-input-icon">
                    <span class="myicon  iconfont icon-yonghu"></span>
                </div>
                <div class="inputBox">
                    <input type="text" autocomplete="username" required="required">
                    <span>管理员账号</span>
                </div>
            </div>
            <div class="formrow">
                <div class="inline-input-icon">
                    <span class="myicon  iconfont icon-suo"></span>
                </div>
                <div class="inputBox">
                    <input type="password" autocomplete="current-password" required="required">
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
import loginitem from "./LoginItem.vue"

export default {
    name: 'StuLogin',
    props: {
        msg: String
    },
    components: { loginitem },
    data() {
        return {
            sdf: "https://jxfw.gdut.edu.cn/yzm?d=" + new Date().getTime()
        }
    },

    methods: {
        changeImage() {
            console.log("changeImage")
            this.sdf = "https://jxfw.gdut.edu.cn/yzm?d=" + new Date().getTime()
            // this.sdf.value =ref( "https://jxfw.gdut.edu.cn/yzm?d=" +new Date().getTime())
        }
    },
    // lifecycle hooks
    mounted() {
        document.title = "管理员登录"
    }
}
</script>
<style >
* {
   margin: 0;
   padding: 0;
   /* 添加内间距不扩大div */
   box-sizing: border-box;
}

.loginItem {
   position: fixed;
   left: 3%;
   top: 0%;
   display: flex;
   flex-direction: row;
   align-items: baseline;
}

form {
   transform: scale(1.5);
}

.yanzheng>img {
   height: 40px;
   position: absolute;
   right: 3px;
   top: 5px;
}

.inline-input-icon {
   line-height: 49.6px;
   color: #0d4e61;
   text-align: left;
   text-shadow: 1px 1px 2px black;
   text-indent: 4px;
}

.formrow {
   margin-top: 20px;
}

.box {
   height: 100vh;
   background: #09203f;
   display: flex;
   justify-content: center;
   align-items: center;
   flex-direction: column;
   /* 无法选中文字 */
   user-select: none;
   /* 元素之间的上下间距 */
   /* gap: 20px; */
   gap: 90px;
}

.box h2 {
   color: white;
   transform: scale(1.5);
}

.inputBox {
   position: relative;
   width: 300px;
   letter-spacing: 1px;
}

.inputBox input {
   width: 100%;
   padding: 15px;
   padding-left: 35px;
   border: 1px solid rgba(255, 255, 255, 0.5);
   background: none;
   border-radius: 5px;
   outline: none;
   color: white;
   font-size: 1vw;
   /* 过渡 */
   transition: 0.3s;
   letter-spacing: 1px;
}

.inputBox span {
   position: absolute;
   left: 0;
   padding: 15px;
   padding-left: 35px;
   pointer-events: none;
   font-size: 1vw;
   color: rgba(255, 255, 255, 0.5);
   /* 大写字母 */
   text-transform: uppercase;
   /* 过渡 */
   transition: 0.5s;
}

.inputBox input:valid~span,
.inputBox input:focus~span {
   color: #50c9c3;
   /* 变换位置 */
   transform: translateX(30px) translateY(-7px);
   font-size: 0.65em;
   padding: 0 5px;
   background: #09203f;
   letter-spacing: 0.2em;
}

.formrow:focus-within>.inline-input-icon {
   color: white;
}

.inputBox:nth-child(3) input:valid~span,
.inputBox:nth-child(3) input:focus~span {
   background: #50c9c3;
   color: white;
}

.inputBox input:valid,
.inputBox input:focus {
   border: 1px solid #50c9c3;
}

.inputBox input:focus {
   border: 1px solid #50c9c3;
}

/* 给图标写样式 */
.myicon {
   z-index: 3;
   position: absolute;
   font-size: 18px !important;
}

.myicon2 {
   font-size: 30px !important;
   text-indent: 0;
}

/* 给按钮写样式 */
.buttonBox button {
   padding: 12px 35px;
   border-radius: 6px;
   background: none;
   letter-spacing: 2px;
   font-size: 1vw;
   color: rgba(255, 255, 255, 0.5);
   border: 1px solid rgba(255, 255, 255, 0.5);
   font-weight: 600;
}

.buttonBox button:hover {
   color: #50c9c3;
   border: 1px solid #50c9c3;
}

.buttonBox button:active {
   color: #1e625f;
   font-weight: bold;
   border: 1px solid #1e625f;
}
/* 给按钮写样式 */
input[type="submit"] {
    padding: 10px 30px;
    border-radius: 5px;
    background: none;
    font-size: 1vw;
    color: rgba(255, 255, 255, 0.5);
    border: 1px solid rgba(255, 255, 255, 0.5);
    font-weight: 600;
}

input[type="submit"]:hover {
    color: #50c9c3;
    border: 1px solid #50c9c3;
}

input[type="submit"]:active {
    color: #1e625f;
    font-weight: bold;
    border: 1px solid #1e625f;
}

.buttonBox {
    text-align: center;
}
</style>
