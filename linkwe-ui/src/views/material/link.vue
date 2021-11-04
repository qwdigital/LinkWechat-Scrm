<script>
import MaPage from '@/views/material/components/MaPage'

export default {
  name: 'File',
  components: { MaPage },
  data() {
    return {
      ids: [] // 选中数组
    }
  },
  watch: {},
  created() {},
  methods: {
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id)
    }
  }
}
</script>

<template>
  <MaPage ref="page" type="9" :selected="ids" v-slot="{ list }">
    <el-table :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="图文标题" align="center" prop="materialName" :show-overflow-tooltip="true" />
      <el-table-column label="图文链接" align="center" prop="userName" />

      <el-table-column label="最近更新时间" align="center" prop="createTime"> </el-table-column>
      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="text" @click="$refs.page.edit(scope.row)" v-hasPermi="['wechat:material:edit']"
            >修改</el-button
          >
          <el-button type="text" @click="$refs.page.remove(scope.row.id)" v-hasPermi="['wechat:material:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
  </MaPage>
</template>
