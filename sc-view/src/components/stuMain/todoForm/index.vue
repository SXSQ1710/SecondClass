<template>

    <div class="title">
        时长自主申报
    </div>
    <div class="demo-collapse">
        <el-scrollbar max-height="72vh" always>

            <el-collapse style="font-weight: 600;" v-model="activeNames">
                <!-- 申报列表1 -->
                <el-collapse-item title="校外实践时长申报" name="1">
                    <div style="margin-left:10px">
                        <el-button type="primary" :icon="Edit" round @click="handleAdd">申报时长</el-button>
                    </div>
                    <div>
                        <el-table :data="tableData" style="min-width: 100%;" height="250">
                            <el-table-column prop="uid" label="学号" width="150" />
                            <el-table-column prop="selfAppType" label="时长类型" width="150" />
                            <el-table-column prop="selfAppShiNum" label="时长数" width="150" />
                            <el-table-column prop="selfAppDescription" label="时长描述" width="200" />
                            <el-table-column prop="selfAppAttachment" label="查看附件" width="120" />
                            <el-table-column fixed="right" prop="selfAppStatu" label="申报状态" width="120">
                            </el-table-column>
                        </el-table>
                    </div>
                </el-collapse-item>
                <!-- 申报列表2 -->
                <el-collapse-item title="其他类型时长申报" name="2">
                    <div style="margin-left:10px">
                        <el-button type="primary" :icon="Edit" round @click="handleAdd">申报时长</el-button>
                    </div>
                    <div>
                        <el-table :data="tableData" style="min-width: 100%;" height="250">
                            <el-table-column prop="uid" label="学号" width="150" />
                            <el-table-column prop="selfAppType" label="时长类型" width="150" />
                            <el-table-column prop="selfAppShiNum" label="时长数" width="150" />
                            <el-table-column prop="selfAppDescription" label="时长描述" width="200" />
                            <el-table-column prop="selfAppAttachment" label="查看附件" width="120" />
                            <el-table-column fixed="right" prop="selfAppStatu" label="申报状态" width="120">
                            </el-table-column>
                        </el-table>
                    </div>
                </el-collapse-item>
            </el-collapse>

            <!-- 弹窗 -->
            <el-dialog v-model="dialogFormVisible" title="申报时长" draggable>
                <el-form ref="ruleFormRef" :model="form" :rules="rules" label-width="15vw" class="elform-input"
                    size="dafault" status-icon @submit.native.prevent>
                    <el-form-item class="once" label="学号" prop="uid">
                        <el-input @keyup.native.enter :max="8" v-model="form.uid" />
                    </el-form-item>

                    <el-form-item label="时长类型" prop="selfAppType">
                        <el-select v-model="form.selfAppType" placeholder="时长类型">
                            <el-option label="实践志愿" value="0" />
                            <el-option label="双创实训" value="1" />
                            <el-option label="文体艺术" value="2" />
                            <el-option label="理想信念" value="3" />
                        </el-select>
                    </el-form-item>


                    <el-form-item class="once" label="时长数量" prop="selfAppShiNum">
                        <el-input v-model="form.selfAppShiNum" width="500" type="text" />
                    </el-form-item>
                    <el-form-item class="once" label="申请理由" prop="selfAppDescription">
                        <el-input v-model="form.selfAppDescription" width="500" :rows="3" type="textarea" />
                    </el-form-item>
                    <el-form-item class="once" label="上传附件" prop="selfAppDescription">
                        <upload_demo></upload_demo>
                    </el-form-item>
                </el-form>


                <template #footer>
                    <span class="dialog-footer">
                        <span>UID：{{ my_uid }} {{ uname }} </span>

                        <el-button type="primary" v-if="dialogType == 'add'" v-on:submit.prevent="submitAddForm"
                            @click="handleCheckAdd">
                            申报
                        </el-button>
                        <el-button type="primary" v-else-if="dialogType == 'edit'" @click="handleCheckEdit">
                            修改
                        </el-button>

                    </span>
                </template>
            </el-dialog>

        </el-scrollbar>
    </div>
</template>
    
<script>
import upload_demo from '../../mainItem/uploadItem.vue'

export default {
    name: "todoForm",
    components: [upload_demo]
}
</script>
<script setup>
import { ref } from 'vue'
import { Edit } from '@element-plus/icons-vue'
import { getMySelfApplication } from '@/server/api/shiChang'

// 数据
let my_uid = sessionStorage.getItem("uid")
const activeNames = $ref(['1'])
const dialogFormVisible = $ref('false')
const dialogType = $ref('add')


let form = $ref({
    uid: '',
    selfAppType: '',
    selfAppShiNum: '',
    selfAppDescription: '',
    selfAppAttachment: '',
    selfAppStatu: ''
})



const tableData = [
    {
        uid: '3220001111',
        selfAppType: '志愿时长',
        selfAppShiNum: '10',
        selfAppDescription: '献血',
        selfAppAttachment: '附件',
        selfAppStatu: '待审核'
    },
    {
        uid: '3220001111',
        selfAppType: '双创时长',
        selfAppShiNum: '40',
        selfAppDescription: '实习',
        selfAppAttachment: '附件',
        selfAppStatu: '已通过'
    },
]

// 新增（自主申报）
let handleAdd = () => {
    dialogType = 'add'
    form = []
    dialogFormVisible = true
}

</script>
<style>
.title {
    font-size: 26pt;
}

div[role="button"] {
    padding: 1% 0 1% 1%;
    font-size: 15pt;
}

.el-input,
.el-textarea {
    width: 70%;
}

.dialog-footer {
    color: rgba(86, 97, 105, 0.5);
    text-shadow: 0 0 0 black 1px;
}

.dialog-footer>span {
    padding-right: 30px;
}

form {
    transform: scale(1.0);
}

/* 定义删除附件图标样式 */
.clear_btn {
    display: contents !important;
    float: right;
}

.clear_btn>button {
    margin-left: 5vw;
}
</style>