<script>
import MaPage from './components/MaPage'

export default {
  name: 'MApplet',
  components: { MaPage },
  data() {
    return {
      srcList: [],
      ids: [] // 选中数组
    }
  },
  watch: {},
  created() {},
  methods: {
    listChange(data) {
      this.srcList = data.map((item) => item.materialUrl)
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id)
    }
  }
}
</script>

<template>
  <MaPage ref="page" type="9" @listChange="listChange" :selected="ids" v-slot="{ list }">
    <el-table :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="小程序标题" align="center" prop="materialName"> </el-table-column>
      <el-table-column
        label="小程序 AppID"
        align="center"
        prop="digest"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="最近更新时间" align="center" prop="updateTime"> </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="text" @click="$refs.page.edit(scope.row)">编辑</el-button>
          <el-button type="text" @click="$refs.page.remove(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </MaPage>
</template>

<style lang="scss" scoped>
.img-wrap {
  position: relative;
  height: 0;
  padding: 70% 0 0 0;
  border-bottom: 1px solid #e6ebf5;
  &:hover .actions {
    opacity: 1;
  }
}
.actions {
  position: absolute;
  width: 100%;
  height: 50px;
  left: 0;
  top: 0;
  text-align: center;
  color: #fff;
  opacity: 0;
  font-size: 20px;
  background-color: rgba(0, 0, 0, 0.5);
  transition: opacity 0.3s;
  .el-icon-edit {
    margin-right: 20px;
  }
}
.el-image {
  width: 80px;
  height: 80px;
}
</style>
