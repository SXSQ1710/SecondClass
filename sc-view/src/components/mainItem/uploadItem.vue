<template>
  <el-upload ref="upload" class="upload-demo" action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
    :limit="1" :on-exceed="handleExceed" :auto-upload="false">
    <template #trigger>
      <el-button type="primary">选择文件</el-button>
    </template>
    <div class="clear_btn">
      <el-button type="default" @click="clearFiles">
        <el-tooltip content="删除附件">
          <i class='bx bx-trash'></i>
        </el-tooltip>
      </el-button>
    </div>
    <!-- 上传附件相关说明 -->
    <template #tip>
      <div class="el-upload__tip text-red">
        最多上传1个附件，重复上传会覆盖之前的附件
      </div>
    </template>
  </el-upload>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { genFileId } from 'element-plus'
import type { UploadInstance, UploadProps, UploadRawFile } from 'element-plus'

const upload = ref<UploadInstance>()

const handleExceed: UploadProps['onExceed'] = (files) => {
  upload.value!.clearFiles()
  const file = files[0] as UploadRawFile
  file.uid = genFileId()
  upload.value!.handleStart(file)
}
const clearFiles: UploadProps['onExceed'] = (files) => {
  upload.value!.clearFiles()
}


</script>

<style>
.el-upload__tip {
  color: #60626687;
}
</style>
