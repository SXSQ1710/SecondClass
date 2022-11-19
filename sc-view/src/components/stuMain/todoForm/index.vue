<template>

    <div class="title">
        时长自主申报
    </div>
    <div class="demo-collapse">
        <el-scrollbar max-height="72vh" always>

            <el-collapse style="font-weight: 600;" v-model="activeNames" >
                <!-- 申报列表1 -->
                <el-collapse-item title="校外实践时长申报" name="1">
                    <div style="margin-left:10px">
                        <el-button type="primary" :icon="Edit" round>申报时长</el-button>
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
                        <el-button type="primary" :icon="Edit" round>申报时长</el-button>
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
   <el-dialog v-model="dialogFormVisible" title="申报时长"
            draggable>
            <el-form ref="ruleFormRef" :model="form" :rules="rules" label-width="20vw" class="elform-input"
                size="dafault" status-icon @submit.native.prevent>
                <el-form-item class="once" label="学号" prop="uid">
                    <el-input @keyup.native.enter :max="8" v-model="form.uid" label-width="10vw" />
                </el-form-item>
           
                <el-form-item label="时长类型" prop="shichang_type">
                    <el-select v-model="form.shichang_type" placeholder="时长类型">
                        <el-option label="实践志愿" value="实践志愿" />
                        <el-option label="双创实训" value="双创实训" />
                        <el-option label="文体艺术" value="文体艺术" />
                        <el-option label="理想信念" value="理想信念" />
                    </el-select>
                </el-form-item>

                <el-form-item class="once" label="组织简介" prop="odescription">
                    <el-input v-model="form.odescription" width="500" :rows="3" type="textarea" />
                </el-form-item>
            </el-form>


            <template #footer>
                <span class="dialog-footer">
                    <!-- <el-button type="primary" @click="dialogFormVisible = false"> -->
                    <el-button text type="primary" v-if="dialogType != 'detail'" @click="handleReset">
                        清除附件
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

        </el-scrollbar>
    </div>
</template>
    
<script>
export default {
    name: "todoForm"
}
</script>
<script setup>
import { ref } from 'vue'
import { Edit } from '@element-plus/icons-vue'
import { getMySelfApplication } from '@/server/api/shiChang'

const activeNames = ref(['1'])


const Data = () => {
    getMySelfApplication(uid, pageNo, pageSize).then(function (response) {
        return response;
    });
}

// const Data = [
//         uid,
//         selfAppType,
//         selfAppShiNum,
//         selfAppDescription,
//         selfAppAttachment,
//         selfAppStatu
// ]
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
</script>
<style>
.title {
    font-size: 26pt;
}

div[role="button"] {
    padding:1% 0 1% 1%;
    font-size: 15pt;
}

/* .el-collapse {
    background-color: var(--sidebar-color); 
}*/
</style>