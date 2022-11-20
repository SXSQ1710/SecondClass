<template>
    <div class="table-box">
        <div class="title">
            列表展示
        </div>

        <!-- query查询框 -->
        <div class="query-box">
            <div class="query-btn">
                <!-- <i class="bx bx-search"></i> -->
                <el-input id="query_input" v-model="queryInput" placeholder="请输入XX名称查询" @input="handleQueryInput">
                </el-input>
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
                    导入账号
                </el-button>
            </div>

        </div>

        <!-- 刷新数据 -->
        <el-tooltip content="更新列表数据">
            <div class="refresh_btn" @click="all">
                <i class='bx bx-refresh bx-flip-vertical'></i>
            </div>
        </el-tooltip>

        <!-- 表格 -->
        <el-scrollbar max-height="55vh">
            <el-table border :data="tableData" ref="mutipleTableRef" style="width: 100%"
                @selection-change="handleSelectionChange">
                <!-- 多行选择器 -->
                <el-table-column type="selection" width="55" />
                <!-- fixed 属性配置，固定列-->
                <el-table-column property="uid" label="用户ID" width="120" />
                <el-table-column property="uname" label="用户名" width="120" />
                <el-table-column property="upassword" label="用户密码" width="120" />
                <el-table-column property="phone" label="联系方式" width="120" />
                <el-table-column property="cname" label="班级" width="200" />
                <el-table-column property="oid" label="所属组织" width="" />
                <el-table-column fixed="right" label="操作" width="180">
                    <template #default="scope">
                        <el-button link type="primary" size="small" @click="handleDetail(scope.row)">详情</el-button>
                        <el-button link type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
                        <el-button link type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>

        </el-scrollbar>

        <!-- 弹窗 -->
        <el-dialog v-model="dialogFormVisible" :title="dialogType == 'add' ? '新增' : dialogType == 'edit' ? '编辑' : '详情'"
            draggable>
            <el-form ref="ruleFormRef" :model="form" :rules="rules" label-width="180px" class="elform-input"
                size="dafault" status-icon @submit.native.prevent>
                <el-form-item class="once" label="用户ID" prop="uid" width="0px">
                    <el-input @keyup.native.enter v-model="form.uid" />
                </el-form-item>
                <el-form-item class="once" label="用户名" prop="uname">
                    <el-input @keyup.native.enter v-model="form.uname" />
                </el-form-item>
                <el-form-item class="once" label="用户密码" prop="upassword">
                    <el-input @keyup.native.enter v-model="form.upassword" />
                </el-form-item>
                <el-form-item class="once" label="联系方式" prop="phone">
                    <el-input @keyup.native.enter v-model="form.phone" />
                </el-form-item>
                <el-form-item label="班级" prop="cid">
                    <el-input @keyup.native.enter v-model="form.cid" />
                </el-form-item>
                <!-- <el-form-item label="班级ID" prop="grade">
                    <el-select v-model="form.cid" placeholder="">
                        <el-option label="1" value="1" />
                        <el-option label="2" value="2" />
                        <el-option label="3" value="3" />
                        <el-option label="4" value="4" />
                        <el-option label="5" value="5" />
                    </el-select>
                </el-form-item> 
                <el-form-item label="所属组织" prop="oid">
                    <el-select v-model="form.oid" placeholder="组织ID">
                        <el-option label="1" value="1" />
                        <el-option label="2" value="2" />
                        <el-option label="3" value="3" />
                        <el-option label="4" value="4" />
                        <el-option label="5" value="5" />
                    </el-select>
                </el-form-item>-->
                <el-form-item label="所属组织" prop="oid">
                    <el-input @keyup.native.enter v-model="form.oid" />
                </el-form-item>
            </el-form>


            <template #footer>
                <span class="dialog-footer">
                    <el-button text type="primary" v-if="dialogType != 'detail'" @click="handleReset">
                        重置
                    </el-button>
                    <el-button type="primary" v-if="dialogType == 'add'" v-on:submit.prevent="submitAddForm"
                        @click="handleCheckAdd(form)">
                        创建用户
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

//第一种获取target值的方式，通过vue中的响应式对象可使用toRaw()方法获取原始对象
import { toRaw } from '@vue/reactivity'
import { ElMessage } from 'element-plus';


//所有的生命周期用法均为回调函数
onMounted(() => {
    all()
    //每次进入该页面自动执行该方法，即自动读取数据库数据导入到页面当中
})

// 数据
let queryInput = $ref("")
let multipleSelection = $ref([])     // 多选
let dialogFormVisible = $ref(false)
let formLabelWidth = $ref('20vw')
let dialogType = $ref('add')

let form = $ref({
    uid: '',
    uname: '',
    upassword: '',
    phone: '',
    cname: '',
    oid: '',
})


let tableData = $ref([
    {
        id: '1',
        uid: '1',
        uname: '小明',
        upassword: '123456',
        phone: '123456',
        cname: '信管1班',
        oid: '[1]'
    }, {
        id: '2',
        uid: '2',
        uname: '小红',
        upassword: '123456',
        phone: '123456',
        cname: '信管1班',
        oid: '[2,4]'
    }, {
        id: '3',
        uid: '3220001111',
        uname: '张三',
        upassword: '123456',
        phone: '19854181623',
        cname: '信管1班',
        oid: '[4]'
    },
])

let tableDataCopy = Object.assign(tableData)
// 方法

const all = () => {
    // axios.get('http://localhost/select_user').then(res => {
    //     tableData = res.data;//数据传递到页面数组
    //     tableDataCopy = Object.assign(tableData)
    //     console.log("数据查询成功" + res.data)
    // }).catch(err => {
    //     console.log("获取数据失败" + err);
    // })
}

// 删除按钮
let handleDelete = ({ id }) => {
    let index = tableData.findIndex(item => item.id == id)
    // 从index位置开始删除tableData中的1个元素
    tableData.splice(index, 1)
}
//搜索，模糊查询
let handleQueryInput = (val) => {
    queryInput = val
    tableData = tableDataCopy.filter(item => (item.uid).toLowerCase().match(val.toLowerCase()) || (item.uname).toLowerCase().match(val.toLowerCase()))
}
//搜索，模糊查询
let handleQueryName = (val) => {
    // 浅拷贝一层tableData，防止数据搜索匹配不上
    tableData = tableDataCopy.filter(item => (item.uid).toLowerCase().match(val.toLowerCase()) || (item.uname).toLowerCase().match(val.toLowerCase()))
}

// 新增提交
let handleCheckAdd = (formData) => {
    dialogFormVisible = false // 关闭弹窗
    var formDataList = toRaw(formData)
  
    var receiveData = {
        uid: formDataList.uid,
        upassword: formDataList.upassword,
        cid: formDataList.cid,
        uname: formDataList.uname,
        oid: formDataList.oid,
        phone: formDataList.phone
    }

    console.log("receiveData", receiveData)
    // 创建组织账号
    axios.post('http://localhost:8083/api/manage/addAccount', receiveData).then(res => {
        console.log(res)
        if (res.data["code"] == "8-200") {
            form.cname = "信管" + form.cid +"班"

            tableData.push({
                id: (tableData.length + 1).toString(),
                ...form
            })
            ElMessage({ message: res.data.msg, type: "success" })
        } else
            ElMessage({ message: res.data.msg, type: "error" })
    }).catch(err => {
        console.log("请求失败" + err);
        ElMessage({ message: "请求失败", type: "error" })
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
    uid: [
        { required: true, message: '请填写用户ID', trigger: 'blur' },
        { min: 10, max: 10, message: '请正确填写10位学号' }
    ],
    uname: [
        { required: true, message: '请填写用户名称', trigger: 'blur' }
    ],
    phone: [
        { required: true, message: '请填写用户的联系方式', trigger: 'blur' },
        { min: 11, max: 11, message: '请正确填写11位手机号' }
    ],
    cname: [
        {
            message: '请选择您的所属班级',
            trigger: 'change',
        },
    ],
    oid: [
        {
            message: '请选择您的所属组织',
            trigger: 'change',
        },
    ]
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

/* 顶部标题 */
.title {
    font-size: 26pt;
}

.query-box {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
}

/* input文本框的宽度 */
.el-input {
    width: 300px;
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
</style>
