<script>
import { getList } from '@/api/customer/tag'
import list from './list'

export default {
  components: { list },
  props: {
    // 添加标签显隐
    visible: {
      type: Boolean,
      default: false,
    },
    title: {
      type: String,
      default: '',
    },
    type: {
      type: String,
      default: '0',
    },
  },
  data() {
    return {
      typeText: ['文本', '图片'],
    }
  },
  watch: {
    type(val) {
      val === 'remove' && (this.removeTag = this.selected.slice())
    },
  },
  computed: {
    Pvisible: {
      get() {
        // this.getTree();
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      },
    },
    Ptype: {
      get() {
        // this.getTree();
        return this.type
      },
      set(val) {
        this.$emit('update:type', val)
      },
    },
  },
  created() {},
  mounted() {},
  methods: {
    submit() {
      this.Pvisible = false
      this.$emit('success', this.Pselected)
    },
  },
}
</script>

<template>
  <el-dialog
    :title="`选择${typeText[Ptype]}素材`"
    :visible.sync="Pvisible"
    width="650px"
  >
    <div>
      <el-tabs v-model="Ptype">
        <el-tab-pane name="0">
          <span slot="label"> <i class="el-icon-date"></i> 文本 </span>
          <list type="4"> </list>
        </el-tab-pane>
        <el-tab-pane name="1">
          <span slot="label"> <i class="el-icon-date"></i> 图片 </span>
          <list type="0"> </list>
        </el-tab-pane>
        <!-- <el-tab-pane name="2">
          <span slot="label"> <i class="el-icon-date"></i> 文件 </span>
          <list>
            <el-table :data="list" style="width: 100%">
              <el-table-column width="50">
                <template slot-scope="scope">
                  <el-radio v-model="radio" :label="scope.row.id"></el-radio>
                </template>
              </el-table-column>
              <el-table-column prop="content"> </el-table-column>
            </el-table>
          </list>
        </el-tab-pane> -->
      </el-tabs>
    </div>
    <div slot="footer">
      <el-button @click="Pvisible = false">取 消</el-button>
      <el-button type="primary" @click="submit">确 定</el-button>
    </div>
  </el-dialog>
</template>

<style lang="scss" scoped>
.user-list {
  .el-row {
    line-height: 26px;
  }
}
.mr30 {
  margin-right: 30px;
}
</style>
