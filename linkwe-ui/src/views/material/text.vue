<script>
import MaPage from '@/views/material/components/MaPage'
import {} from '@/api/material'

export default {
  components: { MaPage },
  data() {
    return {
      list: [], // 列表
      ids: [], // 选中数组
    }
  },
  watch: {},
  computed: {},
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
  <MaPage ref="page" type="4" @listChange="listChange">
    <el-table
      class="mt20"
      :data="list"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column
        label="文本内容"
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
        <!-- <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template> -->
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="180"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            type="text"
            @click="$refs.page.edit(scope.row)"
            v-hasPermi="['system:user:edit']"
            >修改</el-button
          >
          <el-button
            type="text"
            @click="$refs.page.remove(scope.row.id)"
            v-hasPermi="['system:user:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
  </MaPage>
</template>
