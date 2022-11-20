<template>
    <div class="table-box">
        <el-divider />
        <div class="eltabs">
            <el-tabs stretch class="demo-tabs">
                <el-tab-pane label="个人信息" name="0">
                    <div class="block">
                        <div class="userimg">
                            <el-image :src="url" :preview-src-list="[url]" preview-teleported fit="cover" />
                        </div>

                        <div class="userinfo">
                            <el-row :gutter="10" justify="center">
                                <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1">
                                    <div class="grid-content ep-bg-purple">用户名</div>
                                </el-col>
                                <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1">
                                    <div class="grid-content ep-bg-purple-light">{{ uname }}</div>
                                </el-col>
                            </el-row>
                            <el-row :gutter="10" justify="center">
                                <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1">
                                    <div class="grid-content ep-bg-purple">学号</div>
                                </el-col>
                                <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1">
                                    <div class="grid-content ep-bg-purple-light">{{ uid }}</div>
                                </el-col>
                            </el-row>
                            <el-row :gutter="10" justify="center">
                                <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1">
                                    <div class="grid-content ep-bg-purple">密码</div>
                                </el-col>
                                <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1">
                                    <div class="grid-content ep-bg-purple-light">{{ upwd }}</div>
                                </el-col>
                            </el-row>
                            <el-row :gutter="10" justify="center">
                                <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1">
                                    <div class="grid-content ep-bg-purple">所属组织</div>
                                </el-col>
                                <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1">
                                    <div class="grid-content ep-bg-purple-light">{{ oids }}</div>
                                </el-col>
                            </el-row>
                        </div>
                    </div>
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
                            <div class="content" v-if="ulevel=='1' ">
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
import axios from 'axios'
import { ElMessage } from "element-plus";


export default {
    props: {
        url: { type: String, default: "https://api2.mubu.com/v3/document_image/883c3014-d556-4a2f-9fa3-1f0816d73ace-10633218.jpg" }
    },
    data() {
        return {
            formData: {
                newPassword: '',
                type: ''
            },
            uname: { type: String, default: " " },
            upwd: { type: String, default: " " },
            uid: { type: String, default: " " },
            oids: { type: String, default: "" },
            ulevel:{ type: String, default: "" },
        }
    },
    setup() {
        const resetForm = (form) => {
            form.newPassword = '';
        }
        return { resetForm }
    }, mounted() {
        let _this = this;
        let _uid = sessionStorage.getItem("uid")
        axios.get('http://localhost:8083/api/manage/user/' + _uid)
            .then((res) => {
                if (res.data['code'] == '7-200') {//这个data 是接收的resolve参数--
                    _this.uname = res.data.data.uname
                    _this.upwd = res.data.data.upassword
                    _this.uid = res.data.data.uid
                    _this.oids = res.data.data.oid
                    // 无组织用户[] 显示出来就是 ’ ‘
                    if(_this.oids.length >3){
                        _this.ulevel = "1"
                    }
                }
            }).catch((err) => {
                console.log(err)
                ElMessage({ message: err.response.data.msg, type: 'error' })
            })
    }
}

</script>

<style scoped>
.eltabs {
    background-color: var(--sidebar-color);
    ;
    max-height: 80vh;
}

.userinfo {
    transform: translate(0, 16vh);
    height: 60vh;
    width: 60%;
    font-size: 20px;
}

.userimg {
    width: 30vw;
    height: 30vw;
    border-radius: 30px;
    transform: translate(10vw, 12vh)
}

.userimg>.el-image {
    width: 200px;
    height: 200px;
    border-radius: 50%;
}

.block {
    display: flex;
    height: 60vh;
    line-height: 60px;
    padding: 1%;
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
