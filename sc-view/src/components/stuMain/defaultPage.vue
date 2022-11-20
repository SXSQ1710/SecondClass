<template>

    <div class="_box" v-if="!haveOrg">
        <h1>to be continued ...</h1>
    </div>

    <div class="table-box" v-if="haveOrg">
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
        </div>
        <!-- 介绍 -->
        <el-tooltip placement="top" content="查看详情点击报名">
            <div class="intro_btn">
                <span class="myicon  iconfont icon-9"></span>
            </div>
        </el-tooltip>
        <!-- 刷新数据 -->
        <el-tooltip content="更新列表数据">
            <div class="refresh_btn" @click="all">
                <i class='bx bx-refresh bx-flip-vertical'></i>
            </div>
        </el-tooltip>

        <el-scrollbar max-height="55vh" always>

            <!-- 表格 -->
            <el-table border :data="tableData" ref="mutipleTableRef" style="width: 100%"
                @selection-change="handleSelectionChange">
                <!-- 多行选择器
            <el-table-column type="selection" width="55" /> -->
                <!-- fixed 属性配置，固定列-->
                <el-table-column prop="aid" label="活动ID" sortable width="120" align="center" header-align="center" />

                <el-table-column prop="apic" label="封面图" width="120" align="center" header-align="center">
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
                <el-table-column prop="a_oid" label="举办单位" width="100" header-align="center" />
                <el-table-column prop="astatus" label="活动状态" sortable width="200" header-align="center" />
                <el-table-column prop="a_uid" label="申请人" sortable width="120" header-align="center" />
                <el-table-column prop="a_register_open" label="报名时间" sortable width="200" header-align="center" />
                <el-table-column prop="a_hold_start" label="举办时间" sortable width="200" header-align="center" />
                <el-table-column prop="a_address" label="举办地点" width="250" header-align="center" />
                <el-table-column prop="A_shichang_type" label="活动时长类型" width="120" header-align="center" />
                <el-table-column prop="a_shichang_num" label="时长" sortable width="120" header-align="center" />
                <el-table-column fixed="right" label="操作" width="100" align="center" header-align="center">
                    <template #default="scope">
                        <el-button link type="primary" size="small" @click="handleDetail(scope.row)">详情</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-scrollbar>
        <!-- 弹窗 -->
        <el-dialog v-model="dialogFormVisible"
            :title="dialogType == 'add' ? '新增' : (dialogType == 'edit' ? '编辑' : '详情')">
            <el-form ref="ruleFormRef" :model="form" :rules="rules" label-width="120px" class="elform-input"
                size="dafault" status-icon @submit.native.prevent readonly="true">
                <el-form-item class="once" label="活动ID" prop="aid">
                    <el-input @keyup.native.enter v-model="form.aid" />
                </el-form-item>

                <el-form-item class="once" label="活动名称" prop="aname">
                    <el-input @keyup.native.enter v-model="form.aname" />
                </el-form-item>
                <el-form-item class="once" label="活动状态" prop="astatus">
                    <el-select v-model="form.astatus" placeholder="">
                    </el-select>
                </el-form-item>
                <el-form-item class="once" label="举办单位" prop="oname">
                    <el-input @keyup.native.enter v-model="form.oname" />
                </el-form-item>
                <el-form-item class="once" label="申请人" prop="a_uid" placeholder="请填写用户ID">
                    <el-input @keyup.native.enter v-model="form.a_uid" />
                </el-form-item>

                <el-form-item label="活动举办地点" prop="a_address">
                    <el-select v-model="form.a_address" placeholder="校区">
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

                <el-form-item label="活动类型" prop="A_shichang_type">
                    <el-radio-group v-model="form.A_shichang_type">
                        <el-radio label="文体艺术" name="A_shichang_type" />
                        <el-radio label="双创实训" name="A_shichang_type" />
                        <el-radio label="理想信念" name="A_shichang_type" />
                        <el-radio label="实践志愿" name="A_shichang_type" />
                    </el-radio-group>
                </el-form-item>
                <el-form-item class="once" label="活动限制人数" prop="a_limitted_number">
                    <el-input v-model.number="form.a_limitted_number" autocomplete="off" />
                </el-form-item>

                <el-form-item class="once" label="活动简介" prop="adescription">
                    <el-input v-model="form.adescription" type="textarea" />
                </el-form-item>
            </el-form>


            <template #footer>
                <span class="dialog-footer">
                    <span>UID：{{ my_uid }} {{ uname }} </span>
                    <el-button type="primary" @submit.native.preven @click="handleCheckApp(my_uid, form.aid)">
                        我要报名
                    </el-button>
                </span>
            </template>

        </el-dialog>
    </div>
</template>
<script  setup>
import { onMounted } from 'vue'
import axios from 'axios'

import { getNowTime } from '../../server/api/time';
import { ElMessage } from 'element-plus';



//所有的生命周期用法均为回调函数
onMounted(() => {
    tableData = []
    all()
    //每次进入该页面自动执行该方法，即自动读取数据库数据导入到页面当中
})

// 数据
let my_uid = sessionStorage.getItem("uid")
let queryInput = $ref("")
let haveOrg = $ref(true)     // 多选
let totalValue = $ref("0")

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
    axios.get('http://localhost:8083/api/activity/getAll/1/10').then(res => {

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
        tableData = _tableData.filter(item => (item.a_uid.toString()).match(new RegExp([0-9])) )

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

// 详情
let handleDetail = (row) => {
    dialogType = 'detail'
    form = { ...row }
    dialogFormVisible = true
}

// 重置(使用element-plus的resetField方法)

// 提交表单功能实现
const handleCheckApp = (formUID, formAID) => {

    dialogFormVisible = false // 关闭弹窗

    var formdata = {
        uid: formUID.toString(),
        aid: formAID.toString()
    }
    axios.post('http://localhost:8083/api/activity/register', formdata).then((res) => {
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

.box_btn {
    background: #c2dff5bd;
    font-weight: bold;
}

.box_btn2 {
    background: #adc2d22e;
}

.intro_btn {
    padding: 0;
    position: fixed;
    z-index: 3;
    right: 70px;
    top: 200px;
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

.dialog-footer {
    color: rgba(86, 97, 105, 0.5);
    text-shadow: 0 0 0 black 1px;

}

.dialog-footer>span {
    padding-right: 30px;
}
</style>
