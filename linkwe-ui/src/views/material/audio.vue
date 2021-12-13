<script>
import MaPage from '@/views/material/components/MaPage'
import Voice from '@/components/Voice'

export default {
  name: 'Audio',
  components: { MaPage, Voice },
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
  <MaPage ref="page" type="1" :selected="ids" v-slot="{ list }">
    <el-table :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="语音" align="center" prop="materialUrl">
        <template slot-scope="{ row }">
          <Voice :amrUrl="row.materialUrl"></Voice>
        </template>
      </el-table-column>
      <el-table-column label="时长" align="center" prop="audioTime" />
      <el-table-column label="最近更新时间" align="center" prop="updateTime" width="160">
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="180"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button type="text" @click="$refs.page.download(scope.row)">下载</el-button>
          <el-button
            type="text"
            @click="$refs.page.edit(scope.row)"
            v-hasPermi="['wechat:material:edit']"
            >修改</el-button
          >
          <el-button
            type="text"
            @click="$refs.page.remove(scope.row.id)"
            v-hasPermi="['wechat:material:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
  </MaPage>
</template>
