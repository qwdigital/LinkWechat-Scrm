<script>
import { getList } from '@/api/customer/tag'

export default {
  name: 'SelectTag',
  components: {},
  props: {
    // 添加标签显隐
    visible: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: '选择标签'
    },
    selected: {
      type: Array,
      default: () => []
    },
    // add: 打标签，remove: 移除标签
    type: {
      type: String,
      default: 'add'
    }
  },
  data() {
    return {
      list: [],
      listOneArray: [],
      selectedGroup: '', // 选择的标签分组
      removeTag: [],
      Pselected: []
    }
  },
  watch: {
    // type(val) {
    //   val === 'remove' && (this.removeTag = this.selected.slice())
    // },
    selected(val) {
      if (this.type === 'add') {
        this.Pselected = []
        val.forEach((element) => {
          let find = this.listOneArray.find((d) => {
            return element.tagId === d.tagId
          })
          this.Pselected.push(find)
        })
      } else if (this.type === 'remove') {
        this.removeTag = val.slice()
        this.Pselected = val.slice()
      }
      // this.list = JSON.parse(JSON.stringify(this.list))
    },
    list(val) {
      if (this.type === 'add') {
        this.Pselected = []
        this.selected.forEach((element) => {
          let find = this.listOneArray.find((d) => {
            return element.tagId === d.tagId
          })
          this.Pselected.push(find)
        })
      } else if (this.type === 'remove') {
        this.removeTag = this.selected.slice()
        this.Pselected = this.selected.slice()
      }
    }
  },
  computed: {
    Pvisible: {
      get() {
        // this.getTree();
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  created() {
    this.getList()
  },
  mounted() {},
  methods: {
    getList() {
      getList().then(({ rows }) => {
        // this.list = Object.freeze(rows)
        this.list = rows
        this.listOneArray = []
        this.list.forEach((element) => {
          element.weTags.forEach((d) => {
            this.listOneArray.push(d)
          })
        })
      })
    },
    submit() {
      if (!this.Pselected.length) {
        this.msgError('请至少选择一个标签')
        return
      }
      this.Pvisible = false
      this.$emit('success', this.Pselected)
    },
    isChecked(unit) {
      // debugger
      return this.Pselected.some((el) => {
        return unit.tagId === el.tagId
      })
    },
    toJson(data) {
      return JSON.stringify(data)
    }
  }
}
</script>

<template>
  <el-dialog :title="title" :visible.sync="Pvisible">
    <div class="dialog-content">
      <span class="mr20">选择分组</span>
      <el-select v-model="selectedGroup" placeholder="请选择">
        <el-option label="所有标签" value></el-option>
        <el-option v-for="(item, index) in list" :key="index" :label="item.gourpName" :value="item.groupId"></el-option>
      </el-select>
      <slot></slot>
      <div v-if="Pvisible">
        <el-checkbox-group v-if="type !== 'remove'" v-model="Pselected">
          <template v-for="(item, index) in list">
            <div class="checkbox-li" v-if="item.groupId === selectedGroup || !selectedGroup" :key="index">
              <div class="checkbox-group-title">{{ item.gourpName }}</div>
              <template v-for="(unit, unique) in item.weTags">
                <el-checkbox v-if="unit.name.trim()" :label="unit" :key="index + '' + unique">{{
                  unit.name
                }}</el-checkbox>
              </template>
            </div>
          </template>
        </el-checkbox-group>
        <el-checkbox-group class="mt20" v-else v-model="Pselected">
          <template v-for="(item, index) in removeTag">
            <el-checkbox v-if="item.groupId === selectedGroup || !selectedGroup" :label="item" :key="index">{{
              item.name.trim() || '(空的无效标签，请移除)'
            }}</el-checkbox>
          </template>
        </el-checkbox-group>
      </div>
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
.dialog-content {
  overflow: auto;
  max-height: calc(85vh - 150px);
}

.checkbox-li {
  .checkbox-group-title {
    font-size: 16px;
    margin-bottom: 15px;
    color: #aaa;
  }
  padding: 20px 0 15px 0;
  & + .checkbox-li {
    border-top: 1px solid #eee;
  }
}
</style>
