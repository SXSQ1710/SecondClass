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

		<div class="StuForm">
			 <el-form
				ref="ruleFormRef"
				:model="ruleForm"
				:rules="rules"
				label-width="120px"
				class="demo-ruleForm"
				:size="formSize"
				status-icon
			>
    <el-form-item style="width:80%" label="活动名称" prop="name">
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
    <el-form-item label="活动限制人数" prop="count">
      <el-select-v2
        v-model="ruleForm.count"
        placeholder="100"
        :options="options"
      />
    </el-form-item>
    <el-form-item label="活动报名时间" required>
		<el-col :span="4">
        <el-form-item prop="date1">
          <el-date-picker
            v-model="ruleForm.date1"
            type="date"
            label="请选择日期"
            placeholder=""
            style="width: 100%"
          />
        </el-form-item>
      </el-col>
 
      <el-col :span="4">
        <el-form-item prop="datetime1">
          <el-time-picker
            v-model="ruleForm.datetime1"
            label="请选择时间"
            placeholder=""
            style="width: 100%"
          />
        </el-form-item>
      </el-col> 
	  <el-col class="text-center" :span="2">
        <span class="text-gray-500">-</span>
      </el-col>
	  <el-col :span="4">
        <el-form-item prop="date2">
          <el-date-picker
            v-model="ruleForm.date2"
            type="date"
            label="请选择日期"
            placeholder=""
            style="width: 100%"
          />
        </el-form-item>
      </el-col>
    
      <el-col :span="4">
        <el-form-item prop="datetime2">
          <el-time-picker
            v-model="ruleForm.datetime2"
            label="请选择时间"
            placeholder=""
            style="width: 100%"
          />
        </el-form-item>
      </el-col>
    </el-form-item>
	<el-form-item label="活动举办时间" required>
		<el-col :span="4">
        <el-form-item prop="startDate">
          <el-date-picker
            v-model="ruleForm.startDate"
            type="date"
            label="请选择日期"
            placeholder=""
            style="width: 100%"
          />
        </el-form-item>
      </el-col>
	  <el-col class="text-center" :span="2">
        <span class="text-gray-500">-</span>
      </el-col>
	  <el-col :span="4">
        <el-form-item prop="endDate">
          <el-date-picker
            v-model="ruleForm.endDate"
            type="date"
            label="请选择日期"
            placeholder=""
            style="width: 100%"
          />
        </el-form-item>
      </el-col>
    </el-form-item>
    <el-form-item label="是否立即提交" prop="delivery">
      <el-switch v-model="ruleForm.delivery"  title="不勾选则默认保存为草稿"/>
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
        <el-radio label="有"  title="如：宣传单、喷画、横幅等"/>
        <el-radio label="无" />
      </el-radio-group>
    </el-form-item>
    <el-form-item style="width:80%" label="活动简介" prop="desc">
      <el-input v-model="ruleForm.desc" type="textarea" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm(ruleFormRef)"
        >Create</el-button
      >
      <el-button @click="resetForm(ruleFormRef)">Reset</el-button>
    </el-form-item>
  </el-form>
		</div>
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
    { min: 3, max: 5, message: '长度3—5个字符', trigger: 'blur' },
  ],
  region: [
    {
      required: true,
      message: '请选择活动举办所在的校区',
      trigger: 'change',
    },
  ],
  count: [
    {
      required: true,
      message: '请填写活动的限制人数',
      trigger: 'change',
    },
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

const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      console.log('submit!')
    } else {
      console.log('error submit!', fields)
    }
  })
}

const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}
// 活动限制人数，idx+10 最低限制10人
const options = Array.from({ length: 491 }).map((_, idx) => ({
  value: `${idx + 10}`,
  label: `${idx + 10}`,
}))
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
  .folder{
	margin-top: 20px; 
	height: 200px;
	position: relative;
  }
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

/* 下面是对表格form的一些css修改 */
.el-col-2{
	transform: scale(1.5);
    padding: 0 1vw;
	flex: 0 0 0;
}
</style>
