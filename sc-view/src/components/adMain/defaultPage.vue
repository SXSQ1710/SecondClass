<template>
    <div class="table-box">
        <el-divider />
        <div class="eltabs">
            <el-tabs stretch class="demo-tabs">
                <el-tab-pane label="个人信息" name="0">

                    <el-scrollbar max-height="70vh" always>
                        <div class="block">

                            <div class="userinfo">
                                <el-card class="box-card">
                                    <template #header>
                                        <div class="card-header">
                                            <span>个人信息</span>
                                            <el-button class="button" text @click="getAll()">get</el-button>
                                        </div>
                                    </template>

                                    <el-row :gutter="10">
                                        <el-col :span="8">
                                            <el-card shadow="hover">用户名：{{ uname }} </el-card>
                                        </el-col>
                                        <el-col :span="8">
                                            <el-card shadow="hover"> 学号:{{ uid }} </el-card>
                                        </el-col>
                                        <el-col :span="8">
                                            <el-card shadow="hover"> 密码:{{ upwd }} </el-card>
                                        </el-col>
                                        <el-divider></el-divider>
                                        <el-col :span="24">
                                            <el-card shadow="hover"> 所属组织:{{ oids == "{}" ? '' : oids }}</el-card>
                                        </el-col>
                                    </el-row>
                                </el-card>
                            </div>

                            <div class="userinfo wid80">
                                <el-card class="box-card">
                                    <template #header>
                                        <div class="card-header">
                                            <span>时长查询</span>
                                            <el-button class="button" text>search</el-button>
                                        </div>
                                    </template>

                                    <el-row :gutter="10">
                                        <el-col :span="8">
                                            <el-card shadow="hover" button
                                                body-style="text-align:center;color:green;text-shadow:0 0 1px"
                                                @click="getShi(shiTotal_1, shiTotal_2, 1)">1</el-card>
                                        </el-col>
                                        <el-col :span="8">
                                            <el-card shadow="hover" button
                                                body-style="text-align:center;color:green;text-shadow:0 0 1px"
                                                @click="getShi(shiTotal_1, shiTotal_2, 2, G)">2</el-card>
                                        </el-col>
                                        <el-col :span="8">
                                            <el-card shadow="hover" button
                                                body-style="text-align:center;color:green;text-shadow:0 0 1px"
                                                @click="getShi(shiTotal_1, shiTotal_2, 3, G)">3</el-card>
                                        </el-col>

                                        <el-divider></el-divider>
                                        <el-col :span="24">
                                            <el-card shadow="hover"> {{ G }}上时长总数目:{{ shiTotal_1 }}</el-card>
                                            <el-card shadow="hover">{{ G }}下时长总数目:{{ shiTotal_2 }}</el-card>
                                        </el-col>
                                    </el-row>
                                </el-card>
                            </div>

                        </div>
                    </el-scrollbar>

                </el-tab-pane>
                <el-tab-pane label="账号管理" name="1">
                    <div class="block">
                        <div class="upd">
                            修改密码
                            <el-form label-position="left" label-width="100px" style="max-width: 460px">
                                <el-form-item label="旧密码">
                                    <el-input :placeholder="upwd" readonly />
                                </el-form-item>
                                <el-form-item label="新密码">
                                    <el-input v-model="formData.newPassword" />
                                </el-form-item>
                                <el-form-item>
                                    <el-button type="primary" @click="submitForm(ruleFormRef)">修改密码</el-button>
                                    <el-button @click="resetForm(formData)">重置</el-button>
                                </el-form-item>
                            </el-form>
                        </div>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="角色权限" name="2">
                    <div class="block">
                        <div class="_box">
                            <div class="content">
                                普通学生角色权限
                                <p>活动报名、自主申报、修改个人信息</p>
                            </div>
                            <div class="content" v-if="ulevel == '1'">
                                学生组织角色权限
                                <p>活动申请、活动时长申报、签到签退管理、组织人员管理</p>
                            </div>
                        </div>
                    </div>
                </el-tab-pane>

            </el-tabs>
        </div>
    </div>
</template>
<script>
import axios from '../../server/http'
import { toRaw } from '@vue/reactivity'
import { ElMessage } from "element-plus";
export default {
    data() {
        return {
            formData: {
                newPassword: '',
                type: ''
            },
            uname: { type: String, default: "111" },
            upwd: { type: String, default: " " },
            uid: { type: String, default: " " },
            oids: { type: String },
            ulevel: { type: String, default: "" },
            shiTotal_1: 0,
            shiTotal_2: 0,
            G: "大一"
        }
    },
    methods() {
        const resetForm = (form) => {
            form.newPassword = '';
        }
        const getAll = () => {
            // 点击事件
        }

    },
    setup() {
        const getShi = (shiTotal_1, shiTotal_2, grade, G) => {
            // 统计时长数目，当前学年1-1\1-2，大一至大三[项目以大一学年为例]
            G = grade == 1 ? '大三' : grade == 2 ? '大二' : '大一'
            console.log("G =", G)
            var _shiTotal_1 = 0;
            var _shiTotal_2 = 0;
            axios.get('ShiChang/browseMyShiChang')
                .then((res) => {
                    if (res.data['code'] == '200') {
                        let _data = res.data.data
                        for (let i = 0; i < _data.length; i++) {
                            var obj = _data[i]
                            let ddd = toRaw(obj.value)
                            _shiTotal_1 += ddd[grade + '-1'].total
                            _shiTotal_2 += ddd[grade + '-2'].total
                        }
                        shiTotal_1 = _shiTotal_1
                        shiTotal_2 = _shiTotal_2
                        console.log("ending... shiTotal_1 = ", shiTotal_1, "siTotal_2 = ", shiTotal_2)
                    }
                }).catch((err) => {
                    console.log(err)
                })
        }
        return { getShi }
    },
    mounted() {
        let _this = this;
        let _uid = sessionStorage.getItem("uid")
        axios.get('manage/user/' + _uid)
            .then((res) => {
                console.log("res: ", res)
                if (res.data['code'] == '7-200') {//这个data 是接收的resolve参数--
                    _this.uname = res.data.data.uname
                    _this.upwd = res.data.data.upassword
                    _this.uid = res.data.data.uid
                    if (res.data.data.oid == undefined)
                        _this.oids = '无'
                    else
                        _this.oids = res.data.data.oid.split('[')[1].split(']')[0]
                    // 无组织用户[] 显示出来就是 ’ ‘
                    if (_this.oids.length > 0) {
                        _this.ulevel = "1"
                    }
                }
            }).catch((err) => {
                console.log(err)
            })

    }
}

</script>

<style scoped>
.eltabs {
    background-color: var(--sidebar-color);
    max-height: 80vh;
}

.userinfo:nth-child(1) {
    height: 60vh;
    width: 50%;
    font-size: 20px;
}

.userinfo:nth-child(2) {
    height: 60vh;
    width: 40%;
    font-size: 20px;
}

.card-header {
    font-size: 20pt;
    color: #9f8c7ed4;
}

.block {
    display: flex;
    height: 60vh;
    line-height: 60px;
    padding: 1%;
    gap: 30px;
    user-select: none;
    font-size: 20px;
}

.block>.upd {
    text-align: center;
    width: 100%;
}

.el-form {
    position: relative;
    transform: translate(70%, 40%);
}
</style>
