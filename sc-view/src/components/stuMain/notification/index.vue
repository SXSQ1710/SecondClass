<template>
    <div class="table-box">
        <el-divider />
        <div class="eltabs">
            <el-tabs stretch class="demo-tabs">
                <el-tab-pane label="审核列表" name="2">
                    <div class="block">
                        <el-scrollbar max-height="55vh" always>
                            <!-- 表格-->
                            <el-table border :data="tableData2" height="400" style="width: 100%">
                                <el-table-column prop="appOid" label="组织ID" sortable width="120" align="center"
                                    header-align="center" />
                                <el-table-column prop="appOname" label="组织名称" sortable width="120" align="center"
                                    header-align="center" />
                                <el-table-column prop="campus" label="所属校区" width="100" header-align="center" />

                                <el-table-column prop="uname" label="申请人" sortable width="100" header-align="center" />
                                <el-table-column prop="uid" label="学号" sortable width="100" header-align="center" />
                                <el-table-column prop="grade" label="年级" sortable width="80" header-align="center" />
                                <el-table-column prop="college" label="学院" sortable width="120" header-align="center" />
                                <el-table-column prop="cname" label="班级" sortable width="200" header-align="center" />
                                <el-table-column prop="major" label="专业" sortable width="200" header-align="center" />
                                <el-table-column prop="phone" label="联系方式" sortable width="150" header-align="center" />

                                <el-table-column fixed="right" label="操作" width="120" header-align="center">
                                    <template #default="scope">
                                        <el-button link type="primary" size="small"
                                            @click="handleDetail2(scope.row)">审核</el-button>
                                        <el-button link type="danger" size="small"
                                            @click="handleDelete2(scope.row)">驳回</el-button>
                                    </template>
                                </el-table-column>
                            </el-table>

                        </el-scrollbar>
                        <!-- 弹窗 2 -->
                        <el-dialog v-model="dialogFormVisible2" title="详情">
                            <el-form ref="ruleFormRef" :model="form" :rules="rules" label-width="120px" size="dafault"
                                status-icon @submit.native.prevent>
                                <el-form-item class="once" label="申请组织ID" prop="appOid">
                                    <el-input @keyup.native.enter v-model="form.appOid"
                                        :readonly="dialogType2 == 'detail'" />
                                </el-form-item>
                                <el-form-item class="once" label="组织名称" prop="appOname">
                                    <el-input @keyup.native.enter v-model="form.appOname"
                                        :readonly="dialogType2 == 'detail'" />
                                </el-form-item>
                                <el-form-item class="once" label="所属校区" prop="campus">
                                    <el-input @keyup.native.enter v-model="form.campus"
                                        :readonly="dialogType2 == 'detail'" />
                                </el-form-item>
                                <el-form-item class="once" label="申请人" prop="uname">
                                    <el-input v-model="form.uname" :readonly="dialogType2 == 'detail'" />
                                </el-form-item>
                                <el-form-item class="once" label="学号" prop="uid">
                                    <el-input v-model="form.uid" :readonly="dialogType2 == 'detail'" />
                                </el-form-item>
                                <el-form-item class="once" label="年级" prop="grade">
                                    <el-input v-model="form.grade" :readonly="dialogType2 == 'detail'" />
                                </el-form-item>
                                <el-form-item class="once" label="学院" prop="college">
                                    <el-input v-model="form.college" :readonly="dialogType2 == 'detail'" />
                                </el-form-item>
                                <el-form-item class="once" label="班级" prop="cname">
                                    <el-input v-model="form.cname" :readonly="dialogType2 == 'detail'" />
                                </el-form-item>
                                <el-form-item class="once" label="专业" prop="major">
                                    <el-input v-model="form.major" :readonly="dialogType2 == 'detail'" />
                                </el-form-item>
                                <el-form-item class="once" label="联系方式" prop="phone">
                                    <el-input v-model="form.phone" :readonly="dialogType2 == 'detail'" />
                                </el-form-item>
                            </el-form>
                            <!-- 底部按钮 -->
                            <template #footer>
                                <span class="dialog-footer">
                                    <span class="dialog-footer">
                                        <span>UID：{{ my_uid }} {{ uname }} </span>
                                        <el-button type="success" v-if="dialogType == 'detail'" @submit.native.prevent
                                            @click="handleCheckOrg(form.appOid, 2)">
                                            通过
                                        </el-button>
                                        <el-button type="danger" v-if="dialogType == 'detail'" @submit.native.prevent
                                            @click="handleCheckOrg(form.appOid, 1)">
                                            驳回
                                        </el-button>
                                    </span>
                                </span>
                            </template>
                        </el-dialog>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="我参与的" name="0">
                    <div class="block">
                        <!-- 表格 -->
                        <el-table border :data="tableData" height="400px" style="width: 100%">
                            <el-table-column prop="aid" label="活动ID" sortable width="120" align="center"
                                header-align="center" />
                            <el-table-column prop="apic" label="封面图" width="120" align="center" header-align="center">
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

                            <el-table-column prop="aname" label="活动名称" sortable width="200" header-align="center" />
                            <el-table-column prop="astatus" label="活动状态" width="200" header-align="center" />
                            <el-table-column prop="a_register_open" label="报名时间" sortable width="200"
                                header-align="center" />
                            <el-table-column prop="a_hold_start" label="举办时间" sortable width="200"
                                header-align="center" />
                            <el-table-column prop="A_shichang_type" label="活动时长类型" width="120" header-align="center" />
                            <el-table-column prop="a_shichang_num" label="时长" sortable width="120"
                                header-align="center" />
                            <el-table-column fixed="right" label="操作" width="100" align="center" header-align="center">
                                <template #default="scope">
                                    <el-button link type="primary" size="small" @click="handleDetail(scope.row)">详情
                                    </el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>

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
                                <el-select v-model="form.astatus" placeholder="" readonly>
                                </el-select>
                            </el-form-item>
                            <el-form-item class="once" label="举办单位" prop="oname">
                                <el-input @keyup.native.enter v-model="form.oname" readonly />
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
                                    <el-radio label="校园建设" name="A_shichang_type" />
                                </el-radio-group>
                            </el-form-item>

                            <el-form-item class="once" label="活动简介" prop="adescription">
                                <el-input v-model="form.adescription" type="textarea" />
                            </el-form-item>
                        </el-form>

                        <template #footer>
                            <span class="dialog-footer">
                                <el-button type="primary" v-if="dialogType == 'add'" v-on:submit.prevent="submitAddForm"
                                    @click="handleCheckAdd">
                                    确认
                                </el-button>
                            </span>
                        </template>

                    </el-dialog>
                </el-tab-pane>
                <el-tab-pane label="签到" name="1">
                    <div class="block">
                        <div class="title2" v-if="!identity">
                            签到签退属于学生组织角色权限
                            <p>抱歉，没有找到您申请的活动</p>
                        </div>
                        <div class="table-box" v-if="identity">
                            <!-- 表格 -->
                            <el-table border :data="tableData" height="400px" style="width: 100%">
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

                                <el-table-column prop="aname" label="活动名称" sortable width="200" header-align="center" />
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
                                <el-image style="width: 100%; height: 70vh" :src="getQrcode" preview-teleported="true"
                                    :preview-src-list="[getQrcode]" class="qrImg">
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
    </div>
</template>
<script setup>
import axios from '../../../server/http'
import { ElMessage } from "element-plus";
import { onMounted } from 'vue'
import { getNowTime, compareTime } from '../../../server/api/time';
import { toRaw } from '@vue/reactivity'

// 数据
let totalValue = $ref("0")
let dialogType = $ref('detail')
let dialogType2 = $ref('detail')
let dialogFormVisible = $ref(false)
let dialogFormVisible2 = $ref(false)
let openQrcodeImg = $ref(false)
let identity = $ref(false)

let my_uid = sessionStorage.getItem("uid")
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
let tableData2 = $ref([

])

// 方法
const all = () => {

    axios.get('activity/getParticipationByUid/' + (sessionStorage.getItem("uid")) + '/1/10').then(res => {
        let _tableData = res.data.data.records
        let _nowTime = getNowTime()
        totalValue = _tableData.length

        for (let i = 0; i < _tableData.length; i++) {
            // 匹配活动Status
            if (compareTime(_nowTime, _tableData[i].a_hold_end)) {
                _tableData[i].astatus = '[已结束]'
            } else if (compareTime(_nowTime, _tableData[i].a_register_open)) {
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
            } else if (_tableData[i].A_shichang_type == 4) {
                _tableData[i].A_shichang_type = '校园建设'
            }
        }
        tableData = _tableData

    }).catch(err => {
        console.log("获取数据失败" + err);
    })

    axios.get('manage/getApplyOrg/1/10').then(res => {
        let _tableData = res.data.data.records
        for (let i = 0; i < _tableData.length; i++) {
            console.log(_tableData[i])
        }
        tableData2 = _tableData

    }).catch(err => {
        console.log("获取数据失败" + err);
    })

}

// 详情
let handleDetail = (row) => {
    dialogType = 'detail'
    form = { ...row }
    dialogFormVisible = true
}// 详情
let handleDetail2 = (row) => {
    dialogType2 = 'detail'
    form = { ...row }
    dialogFormVisible2 = true
}
let handleCheckOrg = (oAppId, oAppStatus) => {
    dialogFormVisible = false
    axios.get('manage/auditOrgApp/' + oAppId + '/' + oAppStatus).then(res => {
        console.log(res)
        if (res.data['code'] != '8-200')
            ElMessage({ msg: res.data.msg, type: "info" })
        else
            ElMessage({ msg: res.data.msg, type: "success" })
    }).catch(err => {
        console.log("操作无效" + err);
        ElMessage({ message: res.data.msg, type: "info" })
    })
}
let checkID = () => {
    axios.get('manage/user/' + my_uid).then(res => {
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
    axios.get('activity/signIn/' + activityData.aid + '/' + activityData.uid + '/' + typeCount).then(res => {
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

.title2 {
    font-size: 1.8vw
}

.title2>p {
    font-size: 1.6vw;
    color: rgba(86, 97, 105, 0.5);
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
