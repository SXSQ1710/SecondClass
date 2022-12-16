<template>
    <div class="table-box">
        <div class="title">
            活动列表
        </div>

        <!-- query查询框 -->
        <div class="query-box">
            <div class="query-btn">
                <el-input v-model="queryInput" placeholder="请输入XX名称查询" @input="handleQueryInput"></el-input>
                <el-button class="box_btn" type="primary" text @click="handleQueryName(queryInput)">
                    搜索
                </el-button>
            </div>
            <div class="query-btn">
                <el-button class="box_btn" type="primary" text @click="handleAdd">
                    增加
                </el-button>
            </div>
        </div>

        <!-- 刷新数据 -->
        <div class="refresh_btn">
            <!-- 当前共有多少条数据 -->
            <el-tooltip content="更新列表数据">
                <i class='bx bx-refresh bx-flip-vertical' @click="all"></i>
            </el-tooltip>
            <span>当前共有{{ totalValue }}条数据</span>
        </div>

        <el-scrollbar max-height="55vh" always>

            <!-- 表格 -->
            <el-table border :data="tableData" height="400" style="width: 100%">
                <el-table-column prop="aid" label="活动ID" sortable width="120" align="center" header-align="center" />

                <el-table-column prop="apic" label="封面图" sortable width="120" align="center" header-align="center">
                    <!-- <template #default="scope"> -->
                    <template v-slot="scope">
                        <el-image style="width: 100%; height: 100px" :src="scope.row.apic" preview-teleported="true"
                            :preview-src-list="[scope.row.apic]" :key="scope.row.aid">
                            <div slot="error" class="image-slot">
                                <i class="el-icon-picture-outline"></i>
                            </div>
                        </el-image>
                    </template>
                </el-table-column>

                <el-table-column prop="aname" label="活动名称" sortable width="200" header-align="center" />
                <el-table-column prop="a_oid" label="组织ID" sortable width="100" header-align="center" />
                <el-table-column prop="astatus" label="活动状态" sortable width="200" header-align="center" />
                <el-table-column prop="a_uid" label="申请人" sortable width="120" header-align="center" />
                <el-table-column prop="a_register_open" label="报名时间" sortable width="200" header-align="center" />
                <el-table-column prop="a_hold_start" label="举办时间" sortable width="200" header-align="center" />
                <el-table-column prop="a_address" label="举办地点" width="250" header-align="center" />
                <el-table-column prop="A_shichang_type" label="时长类型" sortable width="120" header-align="center" />
                <el-table-column prop="a_shichang_num" label="时长" sortable width="120" header-align="center" />
                <el-table-column prop="adescription" label="活动介绍" width="200" header-align="center" />

                <el-table-column fixed="right" label="操作" width="100" header-align="center" align="center">
                    <template #default="scope">
                        <el-button link type="primary" size="small" @click="handleDetail(scope.row)">详情</el-button>
                    </template>
                </el-table-column>
            </el-table>

        </el-scrollbar>

        <!-- 弹窗 -->
        <el-dialog v-model=dialogFormVisible :title="dialogType == 'add' ? '新增' : (dialogType == 'edit' ? '编辑' : '详情')">
            <el-form :model="form" label-width="120px" size="dafault" status-icon @submit.native.prevent>
                <el-form-item class="once" label="活动ID" prop="aid">
                    <el-input @keyup.native.enter v-model="form.aid" disabled />
                </el-form-item>

                <el-form-item class="once" label="活动名称" prop="aname">
                    <el-input @keyup.native.enter v-model="form.aname" :readonly="dialogType != add" />
                </el-form-item>
                <el-form-item class="once" label="活动状态" prop="astatus">
                    <el-select v-model="form.astatus" placeholder=" " disabled>
                    </el-select>
                </el-form-item>
                <el-form-item class="once" label="举办单位" prop="oname">
                    <el-select v-model="form.oname" placeholder=" " disabled>
                    </el-select>
                </el-form-item>
                <el-form-item class="once" label="申请人" prop="a_uid" placeholder="请填写用户ID">
                    <el-input @keyup.native.enter v-model="form.a_uid" :readonly="dialogType != add" />
                </el-form-item>

                <el-form-item label="活动举办地点" prop="a_address">
                    <el-input @keyup.native.enter v-model="form.a_address" :readonly="dialogType != add" />
                </el-form-item>
                <el-form-item class="once" label="活动限制人数" prop="a_limitted_number">
                    <el-input v-model.number="form.a_limitted_number" autocomplete="off" :readonly="dialogType != add" />
                </el-form-item>
                <el-form-item label="活动报名时间" required>
                    <el-col :span="10">
                        <el-form-item prop="a_register_open">
                            <el-date-picker v-model="form.a_register_open" type="datetime" label="请选择报名时间"
                                placeholder="报名开始时间" style="width: 100%" :readonly="dialogType != add" />
                        </el-form-item>
                    </el-col>
                    <el-col class="text-center" :span="1">
                        <span class="text-gray-500">-</span>
                    </el-col>
                    <el-col :span="10">
                        <el-form-item prop="a_register_close">
                            <el-date-picker v-model="form.a_register_close" type="datetime" label="请选择报名时间"
                                placeholder="报名结束时间" style="width: 100%" :readonly="dialogType != add" />
                        </el-form-item>
                    </el-col>
                </el-form-item>

                <el-form-item label="活动举办时间" required>
                    <el-col :span="10">
                        <el-form-item prop="a_hold_start">
                            <el-date-picker v-model="form.a_hold_start" type="datetime" label="请选择开始时间"
                                placeholder="活动开始时间" style="width: 100%" :readonly="dialogType != add" />
                        </el-form-item>
                    </el-col>
                    <el-col class="text-center" :span="1">
                        <span class="text-gray-500">-</span>
                    </el-col>
                    <el-col :span="10">
                        <el-form-item prop="a_hold_end">
                            <el-date-picker v-model="form.a_hold_end" type="datetime" label="请选择结束时间"
                                placeholder="活动结束时间" style="width: 100%" :readonly="dialogType != add" />
                        </el-form-item>
                    </el-col>
                </el-form-item>

                <el-form-item label="活动类型" prop="A_shichang_type">
                    <el-radio-group v-model="form.A_shichang_type" :disabled="dialogType != add">
                        <el-radio label="文体艺术" name="A_shichang_type" value="1" />
                        <el-radio label="双创实训" name="A_shichang_type" value="2" />
                        <el-radio label="理想信念" name="A_shichang_type" value="3" />
                        <el-radio label="实践志愿" name="A_shichang_type" value="4" />
                        <el-radio label="校园建设" name="A_shichang_type" value="5" />
                    </el-radio-group>
                </el-form-item>

                <el-form-item label="发放时长数目" class="once" prop="a_shichang_num">
                    <el-input v-model.number="form.a_shichang_num" autocomplete="off" :readonly="dialogType != add" />
                </el-form-item>

                <el-form-item class="once" label="活动简介" prop="adescription">
                    <el-input v-model="form.adescription" type="textarea" :readonly="dialogType != add" />
                </el-form-item>
            </el-form>
            <!-- 底部按钮 -->
            <template #footer>
                <span class="dialog-footer">
                    <span>UID：{{ my_uid }} {{ uname }} </span>
                    <el-button text type="primary" v-if="dialogType != 'detail'" @click="handleReset">
                        重置
                    </el-button>
                    <el-button type="primary" v-if="dialogType == 'add'" v-on:submit.prevent="submitAddForm"
                        @click="handleCheckAdd">
                        提交
                    </el-button>
                    <el-button type="primary" v-if="dialogType == 'detail'" @submit.native.preven
                        @click="handleCheckApp(my_uid, form.aid)">
                        我要报名
                    </el-button>
                </span>
            </template>

        </el-dialog>
    </div>
</template>
<script  setup>
import { onMounted } from 'vue'
import { getNowTime, compareTime } from '../../../server/api/time';

import { ElMessage } from 'element-plus';
import '../../../assets/css/common.css'
import axios from '../../../server/http'


//所有的生命周期用法均为回调函数
onMounted(() => {
    tableData = []
    all()
    //每次进入该页面自动执行该方法，即自动读取数据库数据导入到页面当中
})

// 数据
let my_uid = sessionStorage.getItem("uid")
let queryInput = $ref("")
let totalValue = $ref(0)
let dialogType = $ref('add')
let dialogFormVisible = $ref(false)

let form = $ref({
    aid: '',
    aname: '',
    adescription: '',
    a_register_open: '',
    a_register_close: '',
    a_limitted_number: '',
    oname: '',
    a_uid: '',
    a_hold_start: '',
    a_hold_end: '',
    delivery: false,
    astatus: '',
    a_shichang_num: '',
    A_shichang_type: '',
    a_address: ''
})

let tableData = $ref([

])

let tableDataCopy = []

// 方法

const all = () => {
    axios.get('activity/getAll/1/10').then(res => {
        let _tableData = res.data.data.records
        let _nowTime = getNowTime()
        totalValue = _tableData.length

        for (let i = 0; i < _tableData.length; i++) {
            // 匹配活动Status
            if (_tableData[i].astatus == 2) {
                _tableData[i].astatus = '审核通过'
                if (compareTime(_nowTime, _tableData[i].a_hold_end)) {
                    _tableData[i].astatus += '[已结束]'
                } else if (compareTime(_nowTime, _tableData[i].a_register_open)) {
                    _tableData[i].astatus += '[进行中]'
                } else {
                    _tableData[i].astatus += '[待开始]'
                }
            } else {
                _tableData[i].astatus = '拒绝申请'
            }
            // 匹配活动时长Type
            if (_tableData[i].A_shichang_type == 1) {
                _tableData[i].A_shichang_type = '文体艺术'
            } else if (_tableData[i].A_shichang_type == 2) {
                _tableData[i].A_shichang_type = '双创实训'
            } else if (_tableData[i].A_shichang_type == 3) {
                _tableData[i].A_shichang_type = '理想信念'
            } else if (_tableData[i].A_shichang_type == 4) {
                _tableData[i].A_shichang_type = '实践志愿'
            } else if (_tableData[i].A_shichang_type == 5) {
                _tableData[i].A_shichang_type = '校园建设'
            }

        }
        tableData = _tableData
        tableDataCopy = _tableData

    }).catch(err => {
        console.log("获取数据失败" + err);
    })
}
//搜索，模糊查询
let handleQueryInput = (val) => {
    queryInput = val
    tableData = tableDataCopy.filter(item => (item.aid).toString().match(val) || (item.aname).match(val))
}
//搜索，模糊查询
let handleQueryName = (val) => {
    // 浅拷贝一层tableData，防止数据搜索匹配不上
    tableData = tableDataCopy.filter(item => (item.aid).toString().match(val) || (item.aname).match(val))
}

// 新增提交
let handleCheckAdd = (_res) => {
    dialogFormVisible = false // 关闭弹窗
    //1. 拿到数据 ref(_res)
    //2. 添加到table
    tableData.push({
        id: (tableData.length + 1).toString(),
        ...form
    })
}

// 提交表单功能实现
const handleCheckApp = (formUID, formAID) => {

    dialogFormVisible = false // 关闭弹窗

    var formdata = {
        uid: formUID.toString(),
        aid: formAID.toString()
    }
    axios.post('activity/register', formdata).then((res) => {
        //处理成功后的逻辑
        if ((res.data.code) == 200) {
            ElMessage({ message: res.data.msg, type: 'success' })
        } else {
            ElMessage({ message: res.data.msg, type: 'error' })
        }
    }).catch(err => {
        ElMessage({ message: "请求失败", type: 'error' })
        console.log("请求失败" + err);
    })
}

// 新增
let handleAdd = () => {
    dialogType = 'add'
    form = []
    dialogFormVisible = true
}
// 详情
let handleDetail = (row) => {
    dialogType = 'detail'
    form = { ...row }
    dialogFormVisible = true
}

// 重置
const handleReset = () => {
    form = []
}

// 提交表单功能实现
const submitAddForm = async (formEl) => {
    console.log("FormEI", formEl)
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
</script>

<style scoped>
* {
    user-select: text;
}

.text-center {
    margin: 0 auto
}

.el-form-item {
    /* 表单行距 */
    margin-bottom: 14px;
}

.dialog-footer {
    color: rgba(86, 97, 105, 0.5);
    text-shadow: 0 0 0 black 1px;
}

.dialog-footer>span {
    padding-right: 30px;
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
</style>
