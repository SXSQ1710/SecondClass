<template>
    <div class="box">
       <h2>登 录 中 心</h2>
       <loginitem></loginitem>
       <form action="#" >
            <div class="formrow">
                <div class="inline-input-icon">
                    <span class="myicon  iconfont icon-yonghu"></span>
                </div>
            <div class="inputBox">
                <input type="text" required="required">
                <span>管理员账号</span>
            </div>
            </div>
            <div class="formrow">
                <div class="inline-input-icon">
                    <span class="myicon  iconfont icon-suo"></span>
                </div>
                <div class="inputBox">
                    <input type="password" required="required">
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
 components:{loginitem},
 data() {
   return {
     sdf: "https://jxfw.gdut.edu.cn/yzm?d=" +new Date().getTime()
   }
 },

methods: {
   changeImage(){
       console.log("changeImage")
       this.sdf = "https://jxfw.gdut.edu.cn/yzm?d=" +new Date().getTime()
       // this.sdf.value =ref( "https://jxfw.gdut.edu.cn/yzm?d=" +new Date().getTime())
   }
},
// lifecycle hooks
mounted() {
  document.title = "管理员登录"
}
}
</script>
<style scoped>
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
