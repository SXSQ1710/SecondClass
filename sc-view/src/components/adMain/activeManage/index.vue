<template>
    <div class="table-box">
        <div class="title">
            列表展示
        </div>

        <!-- query查询框 -->
        <div class="query-box">
            <div class="query-btn">
                <!-- <i class="bx bx-search"></i> -->
                <el-input v-model="queryInput" placeholder="请输入XX名称查询" @input="handleQueryInput"></el-input>
                <el-button class="box_btn" type="primary" text @click="handleQueryName(queryInput)">
                    搜索
                </el-button>
            </div>
            <div class="query-btn">
                <el-button class="box_btn2" type="danger" text v-if="multipleSelection.length > 0"
                    @click="handleMultiDelete">
                    删除多行
                </el-button>
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
            <el-table border :data="tableData" ref="mutipleTableRef" style="width: 100%"
                @selection-change="handleSelectionChange">
                <!-- 多行选择器 -->
                <el-table-column type="selection" width="55" />
                <!-- fixed 属性配置，固定列-->
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

                <el-table-column fixed="right" label="操作" width="180" header-align="center">
                    <template #default="scope">
                        <el-button link type="primary" size="small" @click="handleDetail(scope.row)">详情</el-button>
                        <el-button link type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
                        <el-button link type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>

        </el-scrollbar>

        <!-- 弹窗 -->
        <el-dialog v-model="dialogFormVisible"
            :title="dialogType == 'add' ? '新增' : (dialogType == 'edit' ? '编辑' : '详情')">
            <el-form ref="ruleFormRef" :model="form" :rules="rules" label-width="120px" class="elform-input"
                size="dafault" status-icon @submit.native.prevent>
                <el-form-item class="once" label="活动ID" prop="aid">
                    <el-input @keyup.native.enter v-model="form.aid" />
                </el-form-item>

                <el-form-item class="once" label="活动名称" prop="aname">
                    <el-input @keyup.native.enter v-model="form.aname" />
                </el-form-item>
                <el-form-item class="once" label="活动状态" prop="astatus">
                    <el-select v-model="form.astatus" placeholder=" " :disabled="true">
                    </el-select>
                </el-form-item>
                <el-form-item class="once" label="举办单位" prop="oname">
                    <el-select v-model="form.oname" placeholder=" " :disabled="true">
                    </el-select>
                </el-form-item>
                <el-form-item class="once" label="申请人" prop="a_uid" placeholder="请填写用户ID">
                    <el-input @keyup.native.enter v-model="form.a_uid" />
                </el-form-item>

                <el-form-item label="活动举办地点" prop="a_address">
                    <el-select v-model="form.a_address" placeholder="校区">
                        <el-option label="龙洞校区" value="龙洞校区" />
                        <el-option label="大学城校区" value="大学城校区" />
                        <el-option label="东风路校区" value="东风路校区" />
                        <el-option label="番禺校区" value="番禺校区" />
                        <el-option label="揭阳校区" value="揭阳校区" />
                    </el-select>
                </el-form-item>
                <el-form-item class="once" label="活动限制人数" prop="a_limitted_number">
                    <el-input v-model.number="form.a_limitted_number" autocomplete="off" />
                </el-form-item>
                <el-form-item label="活动报名时间" required>
                    <el-col :span="10">
                        <el-form-item prop="a_register_open">
                            <el-date-picker v-model="form.a_register_open" type="datetime" label="请选择报名时间"
                                placeholder="报名开始时间" style="width: 100%" />
                        </el-form-item>
                    </el-col>
                    <el-col class="text-center" :span="1">
                        <span class="text-gray-500">-</span>
                    </el-col>
                    <el-col :span="10">
                        <el-form-item prop="a_register_close">
                            <el-date-picker v-model="form.a_register_close" type="datetime" label="请选择报名时间"
                                placeholder="报名结束时间" style="width: 100%" />
                        </el-form-item>
                    </el-col>
                </el-form-item>

                <el-form-item label="活动举办时间" required>
                    <el-col :span="10">
                        <el-form-item prop="a_hold_start">
                            <el-date-picker v-model="form.a_hold_start" type="datetime" label="请选择开始时间"
                                placeholder="活动开始时间" style="width: 100%" />
                        </el-form-item>
                    </el-col>
                    <el-col class="text-center" :span="1">
                        <span class="text-gray-500">-</span>
                    </el-col>
                    <el-col :span="10">
                        <el-form-item prop="a_hold_end">
                            <el-date-picker v-model="form.a_hold_end" type="datetime" label="请选择结束时间"
                                placeholder="活动结束时间" style="width: 100%" />
                        </el-form-item>
                    </el-col>
                </el-form-item>

                <el-form-item label="是否立即提交" prop="delivery">
                    <el-switch v-model="form.delivery" title="不勾选则默认保存为草稿" />
                </el-form-item>

                <el-form-item label="活动类型" prop="A_shichang_type">
                    <el-radio-group v-model="form.A_shichang_type">
                        <el-radio label="文体艺术" name="A_shichang_type" value="1" />
                        <el-radio label="双创实训" name="A_shichang_type" value="2" />
                        <el-radio label="理想信念" name="A_shichang_type" value="3" />
                        <el-radio label="实践志愿" name="A_shichang_type" value="4" />
                    </el-radio-group>
                </el-form-item>

                <el-form-item label="发放时长数目" class="once" prop="a_shichang_num">
                    <el-input v-model.number="form.a_shichang_num" autocomplete="off" />
                </el-form-item>

                <el-form-item class="once" label="活动简介" prop="adescription">
                    <el-input v-model="form.adescription" type="textarea" />
                </el-form-item>
            </el-form>
            <!-- 底部按钮 -->
            <template #footer>
                <span class="dialog-footer">
                    <!-- <el-button type="primary" @click="dialogFormVisible = false"> -->
                    <el-button text type="primary" v-if="dialogType != 'detail'" @click="handleReset">
                        重置
                    </el-button>
                    <el-button type="primary" v-if="dialogType == 'add'" v-on:submit.prevent="submitAddForm"
                        @click="handleCheckAdd">
                        确认
                    </el-button>
                    <el-button type="primary" v-else-if="dialogType == 'edit'" @click="handleCheckEdit">
                        修改
                    </el-button>
                </span>
            </template>

        </el-dialog>
    </div>
</template>
<script  setup>
import { onMounted } from 'vue'
import axios from 'axios'

import { getNowTime } from '../../../server/api/time';



//所有的生命周期用法均为回调函数
onMounted(() => {
    tableData = []
    all()
    //每次进入该页面自动执行该方法，即自动读取数据库数据导入到页面当中
})

// 数据
let queryInput = $ref("")
let multipleSelection = $ref([])     // 多选
let totalValue = $ref("0")
let dialogFormVisible = $ref(false)
let formLabelWidth = $ref('20vw')

let dialogType = $ref('add')

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
const tableRowStyle = ({ row, rowIndex }) => {
    return 'background-color: #F7F6Fd;'
}
// 修改table header的背景色
const tableHeaderColor = ({ row, column, rowIndex, columnIndex }) => {
    if (rowIndex === 0) {
        return 'background-color: lightblue;color: #303133;font-weight: 500;font-size: 20px;' //font-size: 20px;为字体大小设置
    }
}

const all = () => {
    axios.get('http://localhost:8083/api/activity/getAllApp/1/10').then(res => {
        let _tableData = res.data.data.records
        let _nowTime = getNowTime()
        totalValue = _tableData.length

        for (let i = 0; i < _tableData.length; i++) {
            // 匹配活动Status
            if (_tableData[i].astatus == 2) {
                _tableData[i].astatus = '审核通过'
                if (_nowTime > _tableData[i].a_hold_end) {
                    _tableData[i].astatus += '[已结束]'
                } else if (_nowTime > _tableData[i].a_register_open) {
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
            }

        }
        tableData = _tableData
        tableDataCopy = _tableData

    }).catch(err => {
        console.log("获取数据失败" + err);
    })
}
// 删除按钮
let handleDelete = ({ aid }) => {
    let index = tableData.findIndex(item => item.aid == aid)
    // 从index位置开始删除tableData中的1个元素
    tableData.splice(index, 1)
}
// 删除确认
let handelCheckDelete = () => {
    console.log('clickDELETE')
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
// 修改提交
let handleCheckEdit = () => {
    dialogFormVisible = false // 关闭弹窗
    //1. 拿到数据 form = { ...row }
    //2. 添加到table
    let index = tableData.findIndex(item => item.id == form.id)
    // 从index位置开始删除tableData中的1个元素
    tableData[index] = form
}

// 新增
let handleAdd = () => {
    dialogType = 'add'
    form = []
    dialogFormVisible = true
}
// 编辑
let handleEdit = (row) => {
    dialogType = 'edit'
    form = { ...row }
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
// 多选
const handleSelectionChange = (val) => {
    multipleSelection = []
    val.forEach(element => {
        multipleSelection.push(element)
    });
    console.log(val)
}
// 多选删除
let handleMultiDelete = () => {
    multipleSelection.forEach(element => {
        handleDelete(element)
    });
}

const ruleFormRef = $ref()

const rules = $ref({
    name: [
        { required: true, message: '请填写活动名称', trigger: 'blur' },
        { min: 3, max: 15, message: '长度3—15个字符', trigger: 'blur' },
    ],
    a_address: [
        {
            required: true,
            message: '请选择活动举办所在的校区',
            trigger: 'change',
        },
    ],
    a_limitted_number: [
        { required: true, message: '请填写活动的限制人数', trigger: 'change', },
        { type: 'number', min: 5, max: 500, message: '请输入5-500的数字' },
    ],
    a_hold_start: [
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
    a_register_open: [
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
    A_shichang_type: [
        {
            type: 'int',
            required: true,
            message: "请选择一个类型",
            trigger: 'change',
        },
    ],
    a_shichang_num: [
        {
            required: true,
            message: '请正确填写时长数目',
            trigger: 'change',
        }, { type: 'number', min: 1, max: 30, message: '请输入1-30的数字' }
    ],
    desc: [
        { required: true, message: '请填写活动简介', trigger: 'blur' },
    ],
})
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


/* 标题样式 */
.title {
    font-size: 26pt;
}

.query-box {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
}

.el-input {
    width: 200px;
}

.text-center {
    margin: 0 auto
}

.elform-input {
    transform: scale(1);
    display: inline-block;
}

.el-form-item {
    /* 表单行距 */
    margin-bottom: 14px;
}

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

.once {
    width: 80%;
}

/* 顶部按钮的样式设置 */
/* .search_icon {
    color: #000000b3;
    z-index: 1;
} */

.box_btn {
    background: #c2dff5bd;
    font-weight: bold;
}

.box_btn2 {
    background: #adc2d22e;
}


.refresh_btn {
    padding: 20px 0;
    position: fixed;
    right: 3vw;
    bottom: 10vh;
    cursor: pointer;
    z-index: 1;
    display: flex;
    flex-direction: row-reverse;
}

.refresh_btn>span {
    transform: translate(-50px, 0);
    cursor: initial;
}

.bx-refresh {
    position: absolute;
    font-size: 20pt;
    display: flex;
}


/* 滚动scroll样式 */

.scrollbar-demo-item {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 50px;
    margin: 10px;
    text-align: center;
    border-radius: 4px;
    background: var(--el-color-primary-light-9);
    color: var(--el-color-primary);
}

/* 滚动scroll样式 */

.scrollbar-demo-item {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 50px;
    margin: 10px;
    text-align: center;
    border-radius: 4px;
    background: var(--el-color-primary-light-9);
    color: var(--el-color-primary);
}
</style>
