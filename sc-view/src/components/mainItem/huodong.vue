<template>
  <div id="loginItem">
    <el-form ref="ruleFormRef" :model="ruleForm" :rules="rules" label-width="120px" class="demo-ruleForm"
      :size="formSize" status-icon>
      <el-form-item class="once" label="活动名称" prop="name">
        <el-input v-model="ruleForm.name" />
      </el-form-item>
      <el-form-item label="活动举办地点" prop="region">
        <el-select v-model="ruleForm.region" placeholder="校区">
          <el-option label="龙洞校区" value="logndong" />
          <el-option label="大学城校区" value="daxuecheng" />
          <el-option label="东风路校区" value="dongfenglu" />
          <el-option label="番禺校区" value="panyu" />
          <el-option label="揭阳校区" value="jieyang" />
        </el-select>
      </el-form-item>
      <el-form-item class="once" label="活动限制人数" prop="count">
        <el-input v-model.number="ruleForm.count" autocomplete="off" />
      </el-form-item>
      <el-form-item label="活动报名时间" required>
        <el-col :span="4">
          <el-form-item prop="date1">
            <el-date-picker v-model="ruleForm.date1" type="date" label="请选择日期" placeholder="" style="width: 100%" />
          </el-form-item>
        </el-col>

        <el-col :span="4">
          <el-form-item prop="datetime1">
            <el-time-picker v-model="ruleForm.datetime1" label="请选择时间" placeholder="" style="width: 100%" />
          </el-form-item>
        </el-col>
        <el-col class="text-center" :span="2">
          <span class="text-gray-500">-</span>
        </el-col>
        <el-col :span="4">
          <el-form-item prop="date2">
            <el-date-picker v-model="ruleForm.date2" type="date" label="请选择日期" placeholder="" style="width: 100%" />
          </el-form-item>
        </el-col>

        <el-col :span="4">
          <el-form-item prop="datetime2">
            <el-time-picker v-model="ruleForm.datetime2" label="请选择时间" placeholder="" style="width: 100%" />
          </el-form-item>
        </el-col>
      </el-form-item>
      <el-form-item label="活动举办时间" required>
        <el-col :span="4">
          <el-form-item prop="startDate">
            <el-date-picker v-model="ruleForm.startDate" type="date" label="请选择日期" placeholder="" style="width: 100%" />
          </el-form-item>
        </el-col>
        <el-col class="text-center" :span="2">
          <span class="text-gray-500">-</span>
        </el-col>
        <el-col :span="4">
          <el-form-item prop="endDate">
            <el-date-picker v-model="ruleForm.endDate" type="date" label="请选择日期" placeholder="" style="width: 100%" />
          </el-form-item>
        </el-col>
      </el-form-item>
      <el-form-item label="是否立即提交" prop="delivery">
        <el-switch v-model="ruleForm.delivery" title="不勾选则默认保存为草稿" />
      </el-form-item>
      <el-form-item label="活动类型" prop="type">
        <el-checkbox-group v-model="ruleForm.type">
          <el-checkbox label="线上活动" name="type" />
          <el-checkbox label="线下活动" name="type" />
          <el-checkbox label="文体艺术" name="type" />
          <el-checkbox label="双创实训" name="type" />
          <el-checkbox label="理想信念" name="type" />
          <el-checkbox label="实践志愿" name="type" />
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="有无宣传用品" prop="resource">
        <el-radio-group v-model="ruleForm.resource">
          <el-radio label="有" title="如：宣传单、喷画、横幅等" />
          <el-radio label="无" />
        </el-radio-group>
      </el-form-item>
      <el-form-item class="once" label="活动简介" prop="desc">
        <el-input v-model="ruleForm.desc" type="textarea" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm(ruleFormRef)">Create</el-button>
        <el-button @click="resetForm(ruleFormRef)">Reset</el-button>
      </el-form-item>
    </el-form>
  </div>

</template>
	<!-- setup设置打开页面时的状态，show=true表示展开 -->
<script  setup lang="ts">
import { reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'

const formSize = ref('default')
const ruleFormRef = ref<FormInstance>()
const ruleForm = reactive({
  name: '',
  region: '',
  count: '',
  date1: '',
  date2: '',
  startDate: '',
  endDate: '',
  datetime1: '',
  datetime2: '',
  delivery: false,
  type: [],
  resource: '',
  desc: '',
})

const rules = reactive<FormRules>({
  name: [
    { required: true, message: '请填写活动名称', trigger: 'blur' },
    { min: 3, max: 15, message: '长度3—15个字符', trigger: 'blur' },
  ],
  region: [
    {
      required: true,
      message: '请选择活动举办所在的校区',
      trigger: 'change',
    },
  ],
  count: [
    { required: true, message: '请填写活动的限制人数', trigger: 'change', },
    { type: 'number', min: 5, max: 500, message: '请输入5-500的数字' },
  ],
  startDate: [
    {
      type: 'date',
      required: true,
      message: '请选择日期',
      trigger: 'change',
    },
  ],
  endDate: [
    {
      type: 'date',
      required: true,
      message: '请选择日期',
      trigger: 'change',
    },
  ],
  date1: [
    {
      type: 'date',
      required: true,
      message: '请选择日期',
      trigger: 'change',
    },
  ],
  datetime1: [
    {
      type: 'date',
      required: true,
      message: '请选择活动报名的时间',
      trigger: 'change',
    },
  ],
  date2: [
    {
      type: 'date',
      required: true,
      message: '请选择日期',
      trigger: 'change',
    },
  ],
  datetime2: [
    {
      type: 'date',
      required: true,
      message: '请选择活动报名的时间',
      trigger: 'change',
    },
  ],
  type: [
    {
      type: 'array',
      required: true,
      message: "请选择至少一个类型",
      trigger: 'change',
    },
  ],
  resource: [
    {
      required: true,
      message: '请选择有无宣传用品',
      trigger: 'change',
    },
  ],
  desc: [
    { required: true, message: '请填写活动简介', trigger: 'blur' },
  ],
})
// 提交表单功能实现
const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  // 验证表单
  await formEl.validate((valid, fields) => {
    if (valid) {
      console.log('submit!')
    } else {
      console.log('error submit!', fields)
    }
  })
}
// 重置表单功能实现
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}
// // 活动限制人数，idx+10 最低限制10人
// const options = Array.from({ length: 491 }).map((_, idx) => ({
//   value: `${idx + 10}`,
//   label: `${idx + 10}`,
// }))
</script>
<style scoped>
#loginItem {
  display: flex;
  align-items: center;
  flex-direction: column;
}

/* 下面是对表格form的一些css修改 */
.el-col-2 {
  transform: scale(1.5);
  padding: 0 1vw;
  flex: 0 0 0;
  user-select: none;
}

.demo-ruleForm {
  max-width: 1000px;
  width: 60%;
  height: 100%;
  margin: 20vh;
}

.once{
  width: 80%;
}
</style>
