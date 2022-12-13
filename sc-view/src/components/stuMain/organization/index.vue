<template>
    <div class="table-box">
        <div class="title">
            列表展示
        </div>

        <!-- query查询框 -->
        <div class="query-box">
            <div class="query-btn">
                <el-input v-model="queryInput" placeholder="请输入组织ID或名称" @input="handleQueryInput"></el-input>
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

        <!-- 表格 -->
        <el-scrollbar max-height="55vh">
            <el-table border stripe :data="tableData" ref="mutipleTableRef" style="width: 100%"
                @selection-change="handleSelectionChange">
                <!-- 多行选择器 -->
                <el-table-column type="selection" width="55" />
                <!-- fixed 属性配置，固定列-->
                <el-table-column prop="oid" label="组织ID" width="120" />
                <el-table-column prop="oname" label="组织名称" width="200" />
                <el-table-column prop="uid" label="负责人ID" width="120" />
                <el-table-column prop="campus" label="所属校区" width="120" />
                <el-table-column prop="odescription" label="组织概述" width="300" />
                <el-table-column prop="superior_organization" label="上级单位" width="300" />
                <el-table-column fixed="right" label="操作" width="180">
                    <template #default="scope">
                        <el-button link type="primary" size="small" @click="handleDetail(scope.row)">详情</el-button>
                        <el-button link type="primary" size="small"
                            @click="handleCheckApply(scope.row)">申请加入</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-scrollbar>


        <!-- 弹窗 -->
        <el-dialog v-model="dialogFormVisible" :title="dialogType == 'add' ? '申请创建组织' : '详情'" draggable>
            <el-form ref="ruleFormRef" :model="form" :rules="rules" label-width="20vw" class="elform-input"
                size="dafault" status-icon @submit.native.prevent>
                <el-form-item class="once" label="组织名称" prop="oname">
                    <el-input @keyup.native.enter v-model="form.oname" />
                </el-form-item>
                <el-form-item class="once" label="负责人ID" prop="uid">
                    <el-input @keyup.native.enter v-model="form.uid" />
                </el-form-item>
                <el-form-item label="所属校区" prop="campus">
                    <el-select v-model="form.campus" placeholder="所属校区">
                        <el-option label="龙洞校区" value="龙洞校区" />
                        <el-option label="大学城校区" value="大学城校区" />
                        <el-option label="东风路校区" value="东风路校区" />
                        <el-option label="番禺校区" value="番禺校区" />
                        <el-option label="揭阳校区" value="揭阳校区" />
                    </el-select>
                </el-form-item>
                <el-form-item label="上级单位" prop="superior_organization">
                    <el-input v-model="form.superior_organization" placeholder="上级单位" width="500" />
                </el-form-item>

                <el-form-item class="once" label="组织简介" prop="odescription">
                    <el-input v-model="form.odescription" width="500" :rows="3" type="textarea" />
                </el-form-item>
            </el-form>


            <template #footer>
                <span class="dialog-footer">
                    <span>UID：{{ my_uid }} {{ uname }} </span>
                    <el-button type="primary" @submit.native.prevent>
                        申请加入组织
                    </el-button>
                </span>
            </template>

        </el-dialog>
    </div>
</template>
<script  setup>

import { onMounted } from 'vue'
import axios from 'axios'
//第一种获取target值的方式，通过vue中的响应式对象可使用toRaw()方法获取原始对象
import { toRaw } from '@vue/reactivity'
import { ElMessage, ElMessageBox } from 'element-plus';
import '../../../assets/css/common.css'

//所有的生命周期用法均为回调函数
onMounted(() => {
    all()
    //每次进入该页面自动执行该方法，即自动读取数据库数据导入到页面当中
})

// 数据
let my_uid = sessionStorage.getItem("uid")
let queryInput = $ref("")
let multipleSelection = $ref([])     // 多选
let dialogFormVisible = $ref(false)
let formLabelWidth = $ref('20vw')
let totalValue = $ref("0")
let dialogType = $ref('add')

let form = $ref({
    uid: '',
    uname: '',
    phone: '',
    oid: '',
    oname: '',
    campus: '',
    odescription: '',
    superior_organization: '',
})

let tableData = $ref([
    {
        uid: '',
        uname: '',
        phone: '',
        oid: '',
        oname: '篮球俱乐部',
        campus: '龙洞校区',
        odescription: 'welcome to join us！',
        superior_organization: '体育部',
    }
])

let tableDataCopy = Object.assign(tableData)

// 方法
const all = () => {
    axios.get('http://localhost:8083/api/manage/getAllOrg/1').then(res => {
        tableData = res.data.data;//数据传递到页面数组
        totalValue = tableData.length

        tableDataCopy = Object.assign(tableData)
    }).catch(err => {
        console.log("获取数据失败" + err);
    })
}

// 申请加入组织
let handleCheckApply = (row) => {
    // console.log(row.oid)
    ElMessageBox.confirm(
        '确定申请加入该组织吗?',
        '申请加入',
        {
            confirmButtonText: '是',
            cancelButtonText: '否',
            type: 'warning',
        }
    )
        .then(() => {

            // 创建组织账号
            axios.post('http://localhost:8083/api/manage/applyOrg', { oid: row.oid, uid: my_uid }).then((res) => {
                console.log(res)
                if (res.data["code"] == "8-200")
                    ElMessage({ message: res.data.msg, type: "success" })
                else
                    ElMessage({ message: res.data.msg, type: "error" })
            }).catch(err => {
                console.log("请求失败" + err);
                ElMessage({ message: "失败了", type: "error" })
            })

            // ElMessage({
            //     type: 'success',
            //     message: '成功提交申请至学生组织，请耐心等待审核通知'
            // })
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '您的申请已撤回',
            })
        })
}
//搜索，模糊查询
let handleQueryInput = (val) => {
    queryInput = val
    tableData = tableDataCopy.filter(item => (item.oname).toString().match(val.toLowerCase()) || (item.oid).toString().match(val.toLowerCase()))
}
//搜索，模糊查询
let handleQueryName = () => {
    // 浅拷贝一层tableData，防止数据搜索匹配不上
    tableData = tableDataCopy.filter(item => (item.oname).toString().match(val.toLowerCase()) || (item.oid).toString().match(val.toLowerCase()))
}
// 新增提交
let handleCheckAdd = (formData) => {
    dialogFormVisible = false // 关闭弹窗
    var formDataList = toRaw(formData)

    // 创建组织账号
    axios.post('http://localhost:8083/api/manage/createOrg', { oname: formDataList.oname, uid: formDataList.uid, campus: formDataList.campus, odescription: formDataList.odescription, superior_organization: formDataList.superior_organization }).then(res => {
        if (res.data["code"] == "8-200")
            ElMessage({ message: res.data.msg, type: "success" })
        else
            ElMessage({ message: res.data.msg, type: "error" })
    }).catch(err => {
        console.log("请求失败" + err);
        ElMessage({ message: "失败了", type: "error" })
    })

    all()
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

// 多选
const handleSelectionChange = (val) => {
    multipleSelection = []
    val.forEach(element => {
        multipleSelection.push(element)
    });
}
// 多选删除
let handleMultiDelete = () => {
    multipleSelection.forEach(element => {
        handleDelete(element)
    });
}

const ruleFormRef = $ref()

const rules = $ref({
    uid: [
        { required: true, message: '请填写用户ID', trigger: 'blur' },
        { min: 10, max: 10, message: '请正确填写10位学号' }
        // 限制学号位数为10位
    ],

    oname: [
        { required: true, message: '请填写组织名称', trigger: 'blur' }
    ],
    campus: [
        {
            message: '请选择您的所属校区',
            trigger: 'change',
        },
    ],
    odescription: [
        {
            message: '请填写组织描述信息',
            trigger: 'change',
        },
    ],
    superior_organization: [
        {
            message: '请填写上级单位',
            trigger: 'change',
        },
    ]
})
</script>

<style scoped>
* {
    user-select: text;
}

.el-input {
    width: 222px;
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
</style>
