<template>
    <div class="table-box">
        <div class="title">
            审核列表
        </div>

        <!-- query查询框 -->
        <div class="query-box">
            <div class="query-btn">
                <el-button class="box_btn" type="primary" text @click="changeMode(0)">
                    活动申请审核
                </el-button>
                <el-button class="box_btn" type="primary" text @click="changeMode(1)">
                    自主申报审核
                </el-button>
                <el-button class="box_btn" type="primary" text @click="changeMode(2)">
                    活动时长审核
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

        <el-scrollbar v-if="mode == 0" max-height="55vh" always>

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
                <el-table-column prop="aOid" label="组织ID" sortable width="100" header-align="center" />
                <el-table-column prop="astatus" label="活动状态" sortable width="200" header-align="center" />
                <el-table-column prop="aUid" label="申请人" sortable width="120" header-align="center" />
                <el-table-column prop="aRegisterOpen" label="报名时间" sortable width="200" header-align="center" />
                <el-table-column prop="aHoldStart" label="举办时间" sortable width="200" header-align="center" />
                <el-table-column prop="aAddress" label="举办地点" width="250" header-align="center" />
                <el-table-column prop="aShichangType" label="时长类型" sortable width="120" header-align="center" />
                <el-table-column prop="aShichangNum" label="时长" sortable width="120" header-align="center" />
                <el-table-column prop="adescription" label="活动介绍" width="200" header-align="center" />

                <el-table-column fixed="right" label="操作" width="120" header-align="center">
                    <template #default="scope">
                        <el-button link type="primary" size="small" @click="handleDetail(scope.row)">详情</el-button>
                        <el-button link type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>

        </el-scrollbar>

        <el-scrollbar v-if="mode == 1" max-height="55vh" always>

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
                <el-table-column prop="aOid" label="组织ID" sortable width="100" header-align="center" />
                <el-table-column prop="astatus" label="活动状态" sortable width="200" header-align="center" />
                <el-table-column prop="aUid" label="申请人" sortable width="120" header-align="center" />
                <el-table-column prop="aRegisterOpen" label="报名时间" sortable width="200" header-align="center" />
                <el-table-column prop="aHoldStart" label="举办时间" sortable width="200" header-align="center" />
                <el-table-column prop="aAddress" label="举办地点" width="250" header-align="center" />
                <el-table-column prop="aShichangType" label="时长类型" sortable width="120" header-align="center" />
                <el-table-column prop="aShichangNum" label="时长" sortable width="120" header-align="center" />
                <el-table-column prop="adescription" label="活动介绍" width="200" header-align="center" />

                <el-table-column fixed="right" label="操作" width="120" header-align="center">
                    <template #default="scope">
                        <el-button link type="primary" size="small" @click="handleDetail(scope.row)">详情</el-button>
                        <el-button link type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>

        </el-scrollbar>

        <!-- 弹窗 -->
        <el-dialog v-model=dialogFormVisible :title="dialogType == 'add' ? '新增' : (dialogType == 'edit' ? '编辑' : '详情')">
            <el-form ref="ruleFormRef" :model="form" :rules="rules" label-width="120px" class="elform-input"
                size="dafault" status-icon @submit.native.prevent>
                <el-form-item class="once" label="活动ID" prop="app_id">
                    <el-input @keyup.native.enter v-model="form.app_id" />
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
                <el-form-item class="once" label="申请人" prop="aUid" placeholder="请填写用户ID">
                    <el-input @keyup.native.enter v-model="form.aUid" />
                </el-form-item>

                <el-form-item label="活动举办地点" prop="aAddress">
                    <el-select v-model="form.aAddress" placeholder="校区">
                        <el-option label="龙洞校区" value="龙洞校区" />
                        <el-option label="大学城校区" value="大学城校区" />
                        <el-option label="东风路校区" value="东风路校区" />
                        <el-option label="番禺校区" value="番禺校区" />
                        <el-option label="揭阳校区" value="揭阳校区" />
                    </el-select>
                </el-form-item>
                <el-form-item class="once" label="活动限制人数" prop="aLimittedNumber">
                    <el-input v-model.number="form.aLimittedNumber" autocomplete="off" />
                </el-form-item>
                <el-form-item label="活动报名时间" required>
                    <el-col :span="10">
                        <el-form-item prop="aRegisterOpen">
                            <el-date-picker v-model="form.aRegisterOpen" type="datetime" label="请选择报名时间"
                                placeholder="报名开始时间" style="width: 100%" />
                        </el-form-item>
                    </el-col>
                    <el-col class="text-center" :span="1">
                        <span class="text-gray-500">-</span>
                    </el-col>
                    <el-col :span="10">
                        <el-form-item prop="aRegisterClose">
                            <el-date-picker v-model="form.aRegisterClose" type="datetime" label="请选择报名时间"
                                placeholder="报名结束时间" style="width: 100%" />
                        </el-form-item>
                    </el-col>
                </el-form-item>

                <el-form-item label="活动举办时间" required>
                    <el-col :span="10">
                        <el-form-item prop="aHoldStart">
                            <el-date-picker v-model="form.aHoldStart" type="datetime" label="请选择开始时间"
                                placeholder="活动开始时间" style="width: 100%" />
                        </el-form-item>
                    </el-col>
                    <el-col class="text-center" :span="1">
                        <span class="text-gray-500">-</span>
                    </el-col>
                    <el-col :span="10">
                        <el-form-item prop="aHoldEnd">
                            <el-date-picker v-model="form.aHoldEnd" type="datetime" label="请选择结束时间" placeholder="活动结束时间"
                                style="width: 100%" />
                        </el-form-item>
                    </el-col>
                </el-form-item>

                <el-form-item label="是否立即提交" prop="delivery">
                    <el-switch v-model="form.delivery" title="不勾选则默认保存为草稿" />
                </el-form-item>

                <el-form-item label="活动类型" prop="aShichangType">
                    <el-radio-group v-model="form.aShichangType">
                        <el-radio label="文体艺术" name="aShichangType" value="1" />
                        <el-radio label="双创实训" name="aShichangType" value="2" />
                        <el-radio label="理想信念" name="aShichangType" value="3" />
                        <el-radio label="实践志愿" name="aShichangType" value="4" />
                    </el-radio-group>
                </el-form-item>

                <el-form-item label="发放时长数目" class="once" prop="aShichangNum">
                    <el-input v-model.number="form.aShichangNum" autocomplete="off" />
                </el-form-item>

                <el-form-item class="once" label="活动简介" prop="adescription">
                    <el-input v-model="form.adescription" type="textarea" />
                </el-form-item>
            </el-form>
            <!-- 底部按钮 -->
            <template #footer>
                <span class="dialog-footer">
                    <!-- <el-button type="primary" @click="dialogFormVisible = false"> -->

                    <span class="dialog-footer">
                        <span>UID：{{ my_uid }} {{ uname }} </span>
                        <el-button type="success" v-if="dialogType == 'detail'" @submit.native.preven
                            @click="handleCheckApp(form.app_id, 2, ' ')">
                            通过
                        </el-button>
                        <el-button type="danger" v-if="dialogType == 'detail'" @submit.native.preven
                            @click="handleCheckApp(form.app_id, 0, ' ')">
                            驳回
                        </el-button>

                        <el-button type="primary" v-if="dialogType == 'add'" v-on:submit.prevent="submitAddForm"
                            @click="handleCheckAdd">
                            确认
                        </el-button>
                    </span>

                </span>
            </template>

        </el-dialog>
    </div>
</template>
<script  setup>
import { onMounted } from 'vue'
import axios from 'axios'
import { getNowTime, timestampToTime, compareTime } from '../../../server/api/time';
import { ElMessage } from "element-plus";
import '../../../assets/css/common.css'

//所有的生命周期用法均为回调函数
onMounted(() => {
    tableData = []
    all()
    //每次进入该页面自动执行该方法，即自动读取数据库数据导入到页面当中
})

// 数据
let mode = $ref("0")
let multipleSelection = $ref([])     // 多选
let totalValue = $ref("0")
let dialogFormVisible = $ref(false)
let formLabelWidth = $ref('20vw')
let my_uid = sessionStorage.getItem("uid")

let dialogType = $ref('add')

let form = $ref({
    aid: '',
    aname: '',
    adescription: '',
    aRegisterOpen: '',
    aRegisterClose: '',
    aLimittedNumber: '',
    oname: '',
    aUid: '',
    aHoldStart: '',
    aHoldEnd: '',
    delivery: false,
    astatus: '',
    aShichangNum: '',
    aShichangType: '',
    aAddress: ''
})

let tableData = $ref([

])

let tableDataCopy = []

// 方法
const changeMode = (updMode) => {
    mode = updMode
}
// 修改table header的背景色
const tableHeaderColor = ({ row, column, rowIndex, columnIndex }) => {
    if (rowIndex === 0) {
        return 'background-color: lightblue;color: #303133;font-weight: 500;font-size: 20px;' //font-size: 20px;为字体大小设置
    }
}

const all = () => {
    axios.get('http://localhost:8083/api/activity/getAllApp/1/10').then(res => {
        var _tata = res.data.data.records
        var _tableData = []
        for (let i = 0; i < _tata.length; i++) {
            var _tata2 = _tata[i]
            _tableData.push(JSON.parse(_tata2.aappDescription))
        }
        let _nowTime = getNowTime()

        for (let i = 0; i < _tableData.length; i++) {
            _tableData[i].app_id = _tata[i].a_app_id

            // 时间戳转换
            _tableData[i].aRegisterOpen = timestampToTime(_tableData[i].aRegisterOpen)
            _tableData[i].aHoldStart = timestampToTime(_tableData[i].aHoldStart)
            _tableData[i].aHoldEnd = timestampToTime(_tableData[i].aHoldEnd)
            // 匹配活动Status
            if (_tableData[i].astatus == 2) {
                _tableData[i].astatus = '审核通过'
                if (compareTime(_nowTime, _tableData[i].aHoldEnd)) {
                    _tableData[i].astatus += '[已结束]'
                } else if (compareTime(_nowTime, _tableData[i].aRegisterOpen)) {
                    _tableData[i].astatus += '[进行中]'
                } else {
                    _tableData[i].astatus += '[待开始]'
                }
            } else if (_tableData[i].astatus == null) {
                _tableData[i].astatus = '待审核'
            } else if (_tableData[i].astatus == 0) {
                _tableData[i].astatus = '拒绝申请'
            }
            // 匹配活动时长Type
            if (_tableData[i].aShichangType == 1) {
                _tableData[i].aShichangType = '文体艺术'
            } else if (_tableData[i].aShichangType == 2) {
                _tableData[i].aShichangType = '双创实训'
            } else if (_tableData[i].aShichangType == 3) {
                _tableData[i].aShichangType = '理想信念'
            } else if (_tableData[i].aShichangType == 4) {
                _tableData[i].aShichangType = '实践志愿'
            }

        }

        totalValue = _tableData.length

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
// 驳回
let handleCheckApp = (aAppId, updStatus, explain) => {
    dialogFormVisible = false
    console.log(aAppId, updStatus, explain)
    axios.get('http://localhost:8083/api/activity/auditActivity/' + aAppId + '/' + updStatus + '/' + explain).then(res => {
        console.log(res)
        if (res.data['code'] == "3-200") {
            ElMessage({ message: res.data.msg, type: 'success' })
        } else {
            ElMessage({ message: res.data.msg, type: 'error' })
        }
    }).catch(err => {
        console.log("获取数据失败" + err);
        ElMessage({ message: err, type: 'error' })
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


const ruleFormRef = $ref()

const rules = $ref({
    name: [
        { required: true, message: '请填写活动名称', trigger: 'blur' },
        { min: 3, max: 15, message: '长度3—15个字符', trigger: 'blur' },
    ],
    aAddress: [
        {
            required: true,
            message: '请选择活动举办所在的校区',
            trigger: 'change',
        },
    ],
    aLimittedNumber: [
        { required: true, message: '请填写活动的限制人数', trigger: 'change', },
        { type: 'number', min: 5, max: 500, message: '请输入5-500的数字' },
    ],
    aHoldStart: [
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
    aRegisterOpen: [
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
    aShichangType: [
        {
            type: 'int',
            required: true,
            message: "请选择一个类型",
            trigger: 'change',
        },
    ],
    aShichangNum: [
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
</script>

<style scoped>
* {
    user-select: text;
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

.el-dialog__body {
    padding: 0
}

.dialog-footer {
    color: rgba(86, 97, 105, 0.5);
    text-shadow: 0 0 0 black 1px;

}

.dialog-footer>span {
    padding-right: 30px;
}
</style>
