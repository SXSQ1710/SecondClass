<template>

    <div class="title">
        时长自主申报
    </div>
    <div class="demo-collapse">
        <el-scrollbar max-height="72vh" always>

            <el-collapse style="font-weight: 600;" v-model="activeNames" @change="handleChange">
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
const handleChange = (val) => {
    console.log(val)
}
const handleClick = () => {
    console.log('click')
}

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
    font-size: 15pt;
}

/* .el-collapse {
    background-color: var(--sidebar-color); 
}*/
</style>