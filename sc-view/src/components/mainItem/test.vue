<template>
  <div class="main">
    <h3>Vue3表单验证</h3>

    <div class="form-box">
      <div class="form-group">
        <label class="label">账号</label>
        <input
         :class="{ checkusername: isActiveUsername }"
          type="text"
          v-model="loginForm.username"
          class="input"
          placeholder="请输入账号"
        />
        <span class="check-message">{{ errorMessage.username }}</span>
      </div>
      <div class="form-group">
        <label class="label">密码</label>
        <input
          :class="{ checkpassword: isActivePassword }"
          tyep="password"
          v-model="loginForm.password"
          type="text"
          placeholder="请输入密码"
          class="input"
        />
        <span class="check-message">{{ errorMessage.password }}</span>
      </div>

      <div class="form-group">
        <button class="btn" @click="submit()">保存</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import Schema from "async-validator";
import { reactive,ref } from "vue";
//控制输入框变红
const isActiveUsername = ref(false)
const isActivePassword = ref(false)
// 表单对象
const loginForm = reactive({
  username: "",
  password: "",
});
// 校验规则
const descriptor = reactive({
  username: {
    required: true,
    message: "姓名不能为空",
  },
  password: [
    {
      required: true,
      message: "密码不能为空",
    },
  ],
});
// 错误提示
const errorMessage = reactive({
  username: "",
  password: "",
});
const validator = reactive(new Schema(descriptor));
const submit = () => {
   if (loginForm.username === '') {
    isActiveUsername.value = true
  }
  if (loginForm.password === '') {
    isActivePassword.value = true
  }
  if (loginForm.username != '') {
    isActiveUsername.value = false
  }
  if (loginForm.password != '') {
    isActivePassword.value = false
  }
  clearMessage();
  validator
    .validate(loginForm, {
      firstFields: true,
    })
    .then(() => {
      // 校验通过
      console.log(" 校验通过,可以发起请求");
    })
    .catch(({ errors }) => {
      // 校验未通过
      // 显示错误信息
      for (let { field, message } of errors) errorMessage[field] = message;
    });
};
// 清空校验错误提示
const clearMessage = () => {
  for (let key in errorMessage) {
    errorMessage[key] = "";
  }
};
</script>

<style scoped>
.main {
  text-align: center;
}
.btn {
  margin: 0;
  line-height: 1;
  padding: 15px;
  height: 30px;
  width: 60px;
  font-size: 14px;
  border-radius: 4px;
  color: #fff;
  background-color: #2080f0;
  white-space: nowrap;
  outline: none;
  position: relative;
  border: none;
  display: inline-flex;
  flex-wrap: nowrap;
  flex-shrink: 0;
  align-items: center;
  justify-content: center;
  user-select: none;
  text-align: center;
  cursor: pointer;
  text-decoration: none;
}
.form-box {
  width: 500px;
  max-width: 100%;
  margin: 0 auto;
  padding: 10px;
  border: 5px solid rgb(171 174 186);
}
.form-group {
  height: 30px;
  margin: 10px;
  padding: 10px 15px 10px 0;
}
.label {
  padding-right: 10px;
  padding-left: 10px;
  display: inline-block;
  box-sizing: border-box;
  width: 110px;
  text-align: right;
}

.input {
  width: calc(100% - 120px);
  height: 28px;
}
.check-message {
  color: #d03050;
  position: relative;
  left: -70px;
}
.checkpassword,
.checkusername {
  border: 2px solid #d03050 !important ;
}
</style>