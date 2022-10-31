<template>
  <div class="table-box">
    <div class="title">
      <h2>简单的CRUD DEMO</h2>
    </div>
    <!-- query查询框 -->
    <div class="query-box">
      <el-input v-model="queryInput" placeholder="queryID"></el-input>
      <el-button type="primary">增加</el-button>
    </div>
    <!-- 表格 -->
    <el-table 
    :data="tableData" 
    ref = "mutipleTableRef"
    style="width: 100%"
    @selection-change="handleSelectionChange"
    >
      <!-- 多行选择器 -->
      <el-table-column type="selection" width="55" />

      <el-table-column fixed prop="date" label="Date" width="150" />
      <el-table-column prop="name" label="Name" width="120" />
      <el-table-column prop="state" label="State" width="120" />
      <el-table-column prop="city" label="City" width="120" />
      <el-table-column prop="address" label="Address" width="600" />
      <el-table-column prop="zip" label="Zip" width="120" />
      <el-table-column fixed="right" label="Operations" width="120">
        <template #default>
          <el-button link type="primary" size="small" @click="handleClick">Detail</el-button>
          <el-button link type="primary" size="small">Edit</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script  setup>
import { ref } from "vue"
// 数据
let queryInput = ref("")


let tableData = ref([
  {
    date: '2016-05-03',
    name: 'Tom',
    state: 'California',
    city: 'Los Angeles',
    address: 'No. 189, Grove St, Los Angeles',
    zip: 'CA 90036',
    tag: 'Home',
  },
  {
    date: '2016-05-02',
    name: 'Tom',
    state: 'California',
    city: 'Los Angeles',
    address: 'No. 189, Grove St, Los Angeles',
    zip: 'CA 90036',
    tag: 'Office',
  },
  {
    date: '2016-05-04',
    name: 'Tom',
    state: 'California',
    city: 'Los Angeles',
    address: 'No. 189, Grove St, Los Angeles',
    zip: 'CA 90036',
    tag: 'Home',
  },
  {
    date: '2016-05-01',
    name: 'Tom',
    state: 'California',
    city: 'Los Angeles',
    address: 'No. 189, Grove St, Los Angeles',
    zip: 'CA 90036',
    tag: 'Office',
  },
])
// 方法
let handleClick = () => {
  console.log('click')
}

let multipleSelection = ref([])

const handleSelectionChange = (val) => {
  conosle.log(val)
  multipleSelection.value = val
}

</script>

<style scoped>
* {
  user-select: text;
}

.table-box {
  width: 800px;
  /*水平垂直方向居中 */
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.title {
  text-align: center;
  padding: 20px 0;
}

.query-box {
  display: flex;
  justify-content: space-between;
}

.el-input {
  width: 200px;
}
</style>
