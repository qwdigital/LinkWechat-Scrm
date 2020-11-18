<script>
import { getList } from '@/api/customer/tag'

export default {
  name: 'SelectTag',
  components: {},
  props: {
    // 添加标签显隐
    visible: {
      type: Boolean,
      default: false,
    },
    title: {
      type: String,
      default: '选择标签',
    },
    selected: {
      type: Array,
      default: () => [],
    },
    type: {
      type: String,
      default: 'add',
    },
  },
  data() {
    return {
      list: [],
      listOneArray: [],
      selectedGroup: '', // 选择的标签分组
      removeTag: [],
      Pselected: this.selected,
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
  },
  created() {
    // this.type === 'remove' && (this.removeTag = this.Pselected.slice())
    this.getList()
  },
  mounted() {},
  methods: {
    getList() {
      getList().then(({ rows }) => {
        this.list = Object.freeze(rows)
        this.listOneArray = []
        this.list.forEach((element) => {
          element.weTags.forEach((d) => {
            this.listOneArray.push(d)
          })
        })
      })
    },
    submit() {
      this.Pvisible = false
      this.$emit('success', this.Pselected)
    },
  },
}
</script>

<template>
  <el-dialog :title="title" :visible.sync="Pvisible">
    <div>
      <span class="mr20">选择分组</span>
      <el-select v-model="selectedGroup" placeholder="请选择">
        <el-option label="所有标签" value></el-option>
        <el-option
          v-for="(item, index) in list"
          :key="index"
          :label="item.gourpName"
          :value="item.groupId"
        ></el-option>
      </el-select>
      <div class="mt20">
        <el-checkbox-group v-model="Pselected" v-if="type !== 'remove'">
          <template v-for="(item, index) in list">
            <div
              class="bfc-d mr30"
              v-show="item.groupId === selectedGroup || !selectedGroup"
              :key="index"
            >
              <el-checkbox
                :label="unit"
                v-for="(unit, unique) in item.weTags"
                :key="index + unique"
                >{{ unit.name }}</el-checkbox
              >
            </div>
          </template>
        </el-checkbox-group>
        <el-checkbox-group v-else v-model="Pselected">
          <template v-for="(item, index) in removeTag">
            <el-checkbox
              v-if="item.groupId === selectedGroup || !selectedGroup"
              :label="item"
              :key="index"
              >{{ item.name }}</el-checkbox
            >
          </template>
        </el-checkbox-group>
      </div>
      <slot></slot>
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
