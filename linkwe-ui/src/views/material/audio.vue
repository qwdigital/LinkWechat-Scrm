<script>
import MaPage from '@/views/material/components/MaPage'

export default {
  components: { MaPage },
  data() {
    return {
      list: [], // 列表
      ids: [], // 选中数组
    }
  },
  watch: {},
  created() {},
  methods: {
    listChange(data) {
      this.list = data
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.userId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
  },
}
</script>

<template>
  <MaPage ref="page" type="1" @listChange="listChange">
    <el-table
      v-loading="loading"
      :data="userList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="语音" align="center" prop="userName" />
      <el-table-column
        label="时长"
        align="center"
        prop="userName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="160"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="180"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-key"
            @click="handleResetPwd(scope.row)"
            v-hasPermi="['system:user:resetPwd']"
            >下载</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:user:edit']"
            >修改</el-button
          >
          <el-button
            v-if="scope.row.userId !== 1"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:user:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
  </MaPage>
</template>
