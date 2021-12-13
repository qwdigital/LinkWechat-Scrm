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
    // selected: {
    //   type: Array,
    //   default: () => [],
    // },
    // "标签分组类型(1:客户标签;2:群标签)"
    type: {
      type: String,
      default: '1'
    },
    destroyOnClose: Boolean,
    // 已选中的标签，一般用于回显
    selected: {
      type: Array,
      default: () => []
    },
    // 已选中的标签，一般用于回显，(下一主版本弃用)
    defaultValues: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      list: [],
      listOneArray: [],
      selectedGroup: '', // 选择的标签分组
      selectedList: []
    }
  },
  watch: {
    selected: {
      handler(val = []) {
        this.selectedList = [...val]
      },
      immediate: true
    },
    // 下一主版本弃用
    defaultValues: {
      handler(val = []) {
        this.selectedList = [...val]
      },
      immediate: true
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
        // this.$nextTick(()=> this.selectedList = [])
      }
    },
    checkedTagMap() {
      return new Set(this.selectedList.map((i) => i.tagId || (typeof i == 'string' && i)))
    }
  },
  created() {
    this.getList()
  },
  mounted() {},
  methods: {
    // getUserTags() {
    //   getUserTags(this, this.externalUserid)
    //   .then(res => {
    //     if(res.code == 200) {
    //       console.log(res)
    //     }
    //   })
    // },
    getList() {
      getList({ groupTagType: this.type }).then(({ rows }) => {
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
      // if (!this.Pselected.length) {
      //   this.msgError('请至少选择一个标签')
      //   return
      // }
      this.$emit('success', JSON.parse(JSON.stringify(this.selectedList)))
      this.Pvisible = false
    },
    onSelectTag(tag) {
      const existIdx = this.selectedList.findIndex((i) => i.tagId === tag.tagId)
      if (existIdx > -1) {
        this.selectedList.splice(existIdx, 1)
      } else {
        this.selectedList.push(tag)
      }
    },
    toJson(data) {
      return JSON.stringify(data)
    },
    setTagSelect(data) {
      return this.checkedTagMap.has(data.tagId) || this.checkedTagMap.has(data.tagName)
    }
  }
}
</script>

<template>
  <el-dialog :title="title" :visible.sync="Pvisible" :destroy-on-close="destroyOnClose">
    <div>
      <!-- <span class="mr20">选择分组</span>
      <el-select v-model="selectedGroup" placeholder="请选择">
        <el-option label="所有标签" value></el-option>
        <el-option
          v-for="(item, index) in list"
          :key="index"
          :label="item.gourpName"
          :value="item.groupId"
        ></el-option>
      </el-select>
-->
      <section class="label-group">
        <div v-for="item in list" :key="item.groupId" class="label-group-item">
          <div class="label-group-item-title">{{ item.gourpName }}</div>
          <div v-if="item.weTags" class="label-group-item-body">
            <el-tag
              v-for="tag in item.weTags"
              :key="tag.tagId"
              :type="setTagSelect(tag) ? '' : 'info'"
              @click="onSelectTag(tag)"
              >{{ tag.name }}</el-tag
            >
          </div>
        </div>
      </section>
      <slot></slot>
    </div>
    <div slot="footer">
      <el-button @click="Pvisible = false">取 消</el-button>
      <el-button type="primary" @click="submit">确 定</el-button>
    </div>
  </el-dialog>
</template>
<style>
.el-tag.el-tag--info {
  background-color: #f4f4f5;
  border-color: #e9e9eb;
  color: #909399;
}
</style>
<style lang="scss" scoped>
.user-list {
  .el-row {
    line-height: 26px;
  }
}
.mr30 {
  margin-right: 30px;
}
.label-group {
  max-height: 400px;
  overflow-x: hidden;
  overflow-y: scroll;
  &-item {
    margin: 10px;
    border-bottom: 1px solid #f1f1f1;

    &-title {
      font-weight: 700;
      line-height: 24px;
    }
    &-body {
      padding: 10px 0;
    }

    .el-tag {
      cursor: pointer;
    }
  }
}
</style>
