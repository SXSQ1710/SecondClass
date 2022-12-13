<template>
    <div class="table-box">
        <el-divider />
        <div class="demo-collapse">
            <el-scrollbar max-height="68vh" always>

                <div class="eltabs">
                    <el-tabs stretch class="demo-tabs">
                        <el-tab-pane label="通知我的" name="0">
                            <div class="block">
                                <div class="title2">暂无更多消息</div>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="活动概览" name="1">
                            <div class="block">
                                <!-- 表格 -->
                                <el-table border :data="tableData" style="width: 100%">
                                    <!-- fixed 属性配置，固定列-->
                                    <el-table-column prop="aid" label="活动ID" sortable width="120" align="center"
                                        header-align="center" />

                                    <el-table-column prop="apic" label="封面图" width="120" align="center"
                                        header-align="center">
                                        <!-- <template #default="scope"> -->
                                        <template v-slot="scope">
                                            <el-image style="width: 100%; height: 100px" :src="scope.row.apic"
                                                preview-teleported="true" :preview-src-list="[scope.row.apic]"
                                                :key="scope.row.aid">
                                                <div slot="error" class="image-slot">
                                                    <i class="el-icon-picture-outline"></i>
                                                </div>
                                            </el-image>
                                        </template>
                                    </el-table-column>
                                    <el-table-column label="二维码" width="120" align="center" header-align="center">
                                        <!-- <template #default="scope"> -->
                                        <template v-slot="scope">
                                            <el-image style="width: 100%; height: 100px" :src="my_qrcode.data"
                                                preview-teleported="true" :preview-src-list="[my_qrcode.data]"
                                                :key="scope.row.aid">
                                                <div slot="error" class="image-slot">
                                                    <i class="el-icon-picture-outline"></i>
                                                </div>
                                            </el-image>
                                        </template>
                                    </el-table-column>

                                    <el-table-column prop="aname" label="活动名称" sortable width="200"
                                        header-align="center" />
                                    <el-table-column prop="astatus" label="活动状态" width="200" header-align="center" />
                                    <el-table-column prop="a_register_open" label="报名时间" sortable width="200"
                                        header-align="center" />
                                    <el-table-column prop="a_hold_start" label="举办时间" sortable width="200"
                                        header-align="center" />
                                    <el-table-column prop="A_shichang_type" label="活动时长类型" width="120"
                                        header-align="center" />
                                    <el-table-column prop="a_shichang_num" label="时长" sortable width="120"
                                        header-align="center" />
                                    <el-table-column fixed="right" label="操作" width="100" align="center"
                                        header-align="center">
                                        <template #default="scope">
                                            <el-button link type="primary" size="small"
                                                @click="handleDetail(scope.row)">详情
                                            </el-button>
                                        </template>
                                    </el-table-column>
                                </el-table>
                            </div>

                            <!-- 弹窗 -->
                            <el-dialog v-model="dialogFormVisible"
                                :title="dialogType == 'add' ? '新增' : (dialogType == 'edit' ? '编辑' : '详情')">
                                <el-form ref="ruleFormRef" :model="form" :rules="rules" label-width="120px"
                                    class="elform-input" size="dafault" status-icon @submit.native.prevent
                                    readonly="true">
                                    <el-form-item class="once" label="活动ID" prop="aid">
                                        <el-input @keyup.native.enter v-model="form.aid" />
                                    </el-form-item>

                                    <el-form-item class="once" label="活动名称" prop="aname">
                                        <el-input @keyup.native.enter v-model="form.aname" />
                                    </el-form-item>
                                    <el-form-item class="once" label="活动状态" prop="astatus">
                                        <el-select v-model="form.astatus" placeholder="" readonly>
                                        </el-select>
                                    </el-form-item>
                                    <el-form-item class="once" label="举办单位" prop="oname">
                                        <el-input @keyup.native.enter v-model="form.oname" readonly />
                                    </el-form-item>

                                    <el-form-item label="活动报名时间" required>
                                        <el-col :span="10">
                                            <el-form-item prop="a_register_open">
                                                <el-date-picker v-model="form.a_register_open" type="datetime"
                                                    label="请选择报名时间" placeholder="报名开始时间" style="width: 100%" />
                                            </el-form-item>
                                        </el-col>
                                        <el-col class="text-center" :span="1">
                                            <span class="text-gray-500">-</span>
                                        </el-col>
                                        <el-col :span="10">
                                            <el-form-item prop="a_register_close">
                                                <el-date-picker v-model="form.a_register_close" type="datetime"
                                                    label="请选择报名时间" placeholder="报名结束时间" style="width: 100%" />
                                            </el-form-item>
                                        </el-col>
                                    </el-form-item>

                                    <el-form-item label="活动举办时间" required>
                                        <el-col :span="10">
                                            <el-form-item prop="a_hold_start">
                                                <el-date-picker v-model="form.a_hold_start" type="datetime"
                                                    label="请选择开始时间" placeholder="活动开始时间" style="width: 100%" />
                                            </el-form-item>
                                        </el-col>
                                        <el-col class="text-center" :span="1">
                                            <span class="text-gray-500">-</span>
                                        </el-col>
                                        <el-col :span="10">
                                            <el-form-item prop="a_hold_end">
                                                <el-date-picker v-model="form.a_hold_end" type="datetime"
                                                    label="请选择结束时间" placeholder="活动结束时间" style="width: 100%" />
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

                                    <el-form-item class="once" label="活动简介" prop="adescription">
                                        <el-input v-model="form.adescription" type="textarea" />
                                    </el-form-item>
                                </el-form>


                                <template #footer>
                                    <span class="dialog-footer">
                                        <!-- <el-button type="primary" @click="dialogFormVisible = false"> -->
                                        <el-button text type="primary" v-if="dialogType != 'detail'"
                                            @click="resetField">
                                            重置
                                        </el-button>
                                        <el-button type="primary" v-if="dialogType == 'add'"
                                            v-on:submit.prevent="submitAddForm" @click="handleCheckAdd">
                                            确认
                                        </el-button>
                                        <el-button type="primary" v-else-if="dialogType == 'edit'"
                                            @click="handleCheckEdit">
                                            修改
                                        </el-button>
                                    </span>
                                </template>

                            </el-dialog>
                        </el-tab-pane>
                        <el-tab-pane label="签到" name="2">
                            <div class="block">
                                <div class="title2" v-if="!identity">
                                    签到签退属于学生组织角色权限
                                    <p>抱歉，没有找到您申请的活动</p>
                                </div>
                                <div class="table-box" v-if="identity">
                                    <!-- 表格 -->
                                    <el-table border :data="tableData" style="width: 100%">
                                        <!-- fixed 属性配置，固定列-->
                                        <el-table-column prop="aid" label="活动ID" sortable width="120" align="center"
                                            header-align="center" />

                                        <el-table-column prop="apic" label="封面图" width="120" align="center"
                                            header-align="center">
                                            <!-- <template #default="scope"> -->
                                            <template v-slot="scope">
                                                <el-image style="width: 100%; height: 100px" :src="scope.row.apic"
                                                    preview-teleported="true" :preview-src-list="[scope.row.apic]"
                                                    :key="scope.row.aid">
                                                    <div slot="error" class="image-slot">
                                                        <i class="el-icon-picture-outline"></i>
                                                    </div>
                                                </el-image>
                                            </template>
                                        </el-table-column>

                                        <el-table-column prop="aname" label="活动名称" sortable width="200"
                                            header-align="center" />
                                        <el-table-column prop="astatus" label="活动状态" width="200"
                                            header-align="center" />
                                        <el-table-column prop="a_register_open" label="报名时间" sortable width="200"
                                            header-align="center" />
                                        <el-table-column prop="a_hold_start" label="举办时间" sortable width="200"
                                            header-align="center" />
                                        <el-table-column prop="A_shichang_type" label="活动时长类型" width="120"
                                            header-align="center" />
                                        <el-table-column prop="a_shichang_num" label="时长" sortable width="120"
                                            header-align="center" />
                                        <el-table-column fixed="right" label="操作" width="100" align="center"
                                            header-align="center">
                                            <template #default="scope">
                                                <el-button class="signin" link type="primary" size="small"
                                                    @click="openQrcode(scope.row, 1)">生成签到码
                                                </el-button>
                                                <el-button class="signin" link type="primary" size="small"
                                                    @click="openQrcode(scope.row, 0)">生成签退码
                                                </el-button>
                                            </template>
                                        </el-table-column>
                                    </el-table>

                                    <!-- 图片 -->
                                    <el-dialog v-model="openQrcodeImg" class="qrImgBox" align-center>
                                        <el-image style="width: 100%; height: 70vh" :src="getQrcode"
                                            preview-teleported="true" :preview-src-list="[getQrcode]" class="qrImg">
                                            <div slot="error" class="image-slot">
                                                <i class="el-icon-picture-outline"></i>
                                            </div>
                                        </el-image>
                                    </el-dialog>
                                </div>
                            </div>
                        </el-tab-pane>
                    </el-tabs>
                </div>
            </el-scrollbar>
        </div>

        <!-- 刷新数据 -->
        <div class="refresh_btn">
            <!-- 当前共有多少条数据 -->
            <el-tooltip content="更新列表数据">
                <i class='bx bx-refresh bx-flip-vertical' @click="get_qrcode"></i>
            </el-tooltip>
        </div>


    </div>
</template>
<script setup>
import axios from 'axios'
import { ElMessage } from "element-plus";
import { onMounted } from 'vue'
import { getNowTime } from '../../../server/api/time';
import { toRaw } from '@vue/reactivity'

// 数据
let totalValue = $ref("0")
let dialogType = $ref('detail')
let dialogFormVisible = $ref(false)
let openQrcodeImg = $ref(false)
let identity = $ref(false)
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
    astatus: '',
    a_shichang_num: '',
    A_shichang_type: '',
    a_address: ''
})
let getQrcode = $ref()
let tableData = $ref([

])
let tableDataCopy = []

// # 遍历所有的活动信息，获取其签到签退码
let my_qrcode = $ref({
    data:
        "data:image/png;base64,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"
})

// 方法
const all = () => {

    axios.get('http://localhost:8083/api/activity/getAll/1/10').then(res => {
        let _tableData = res.data.data.records
        let _nowTime = getNowTime()

        for (let i = 0; i < _tableData.length; i++) {
            // 匹配活动Status
            if (_nowTime > _tableData[i].a_hold_end) {
                _tableData[i].astatus = '[已结束]'
            } else if (_nowTime > _tableData[i].a_register_open) {
                _tableData[i].astatus = '[进行中]'
            } else {
                _tableData[i].astatus = '[待开始]'
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
        tableDataCopy = _tableData
        tableData = _tableData.filter(item => (item.astatus).match("[进行中]") || (item.astatus).match("[待开始]"))
        totalValue = tableData.length

    }).catch(err => {
        console.log("获取数据失败" + err);
    })
}


const get_qrcode = () => {
    for (var i = 0; i < tableData.length; i++) {
        var activityData = toRaw(tableData[i])
        console.log("获取正在进行的活动信息", activityData.aid, activityData.a_oid, 0)
        // 1为获取签到码；0为获取签退码
        axios.get('http://localhost:8083/api/activity/signIn/' + activityData.aid + '/' + activityData.a_oid + '/0').then(res => {
            ElMessage({ message: "刷新成功", type: "success" })
            my_qrcode.push({ id: i, data: res.data.data })
            console.log(my_qrcode)
        }).catch(err => {
            console.log("获取数据失败" + err);
        })
    }
}


// 详情
let handleDetail = (row) => {
    dialogType = 'detail'
    form = { ...row }
    dialogFormVisible = true
}


let checkID = () => {
    var _uid = sessionStorage.getItem("uid")
    axios.get('http://localhost:8083/api/manage/user/' + _uid).then(res => {
        var my_oids = res.data.data.oid
        if (my_oids.length >= 3) {
            identity = true
        }
    }).catch(err => {
        console.log("获取数据失败" + err);
        ElMessage({ message: res.data.msg, type: "info" })
    })
}
let openQrcode = (row, typeCount) => {
    openQrcodeImg = true

    var activityData = toRaw(row)
    console.log(activityData)
    // 1为获取签到码；0为获取签退码
    axios.get('http://localhost:8083/api/activity/signIn/' + activityData.aid + '/' + activityData.a_uid + '/' + typeCount).then(res => {
        console.log(res.data.data)
        getQrcode = res.data.data
        ElMessage({ message: res.data.msg, type: "success" })
    }).catch(err => {
        console.log("获取数据失败" + err);
    })
}


//所有的生命周期用法均为回调函数
onMounted(() => {
    tableData = []
    all()
    checkID()
    //每次进入该页面自动执行该方法，即自动读取数据库数据导入到页面当中
})



</script>

<style scoped>
.eltabs {
    background-color: var(--sidebar-color);
    max-height: 80vh;
}

.userinfo {
    transform: translate(0, 16vh);
    height: 60vh;
    width: 60%;
    font-size: 20px;
}


.block {
    height: 60vh;
    line-height: 60px;
    padding: 1%;
    user-select: none;
    font-size: 20px;
}

.el-form {
    position: relative;
    transform: scale(1.0);
}

/* 内容文字 */
.title2 {
    font-size: 1.8vw
}

.title2>p {
    font-size: 1.6vw;
    color: rgba(86, 97, 105, 0.5);
}

/* 更新按钮 */
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

/* 签到签退 */
.signin {
    margin-bottom: 10px;
    float: right;
}

._box {
    height: 50vh;
}
</style>
