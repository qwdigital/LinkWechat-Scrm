<script>
import { getTree, getList } from '@/api/material'
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
      default: '选择海报'
    },
    selected: {
      type: Array,
      default: () => []
    },
    type: {
      type: String,
      default: 'add'
    }
  },
  data() {
    return {
      list: [],
      total: 0,
      listOneArray: [],
      selectedGroup: '',
      removeTag: [],
      Pselected: this.selected,
      treeData: [],
      attrProps: { value: 'id', label: 'name' },
      posterRequestParams: {
        mediaType: 5,
        pageNum: 1,
        pageSize: 10,
        categoryId: '',
        search: ''
      },
      selectPoster: {}
    }
  },
  watch: {
    type(val) {
      val === 'remove' && (this.removeTag = this.selected.slice())
    },
    selectGroup(value) {
      console.log(value)
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
    },
    page() {
      return Math.ceil(this.total / 10)
    }
  },
  created() {
    // this.type === 'remove' && (this.removeTag = this.Pselected.slice())
    this.getList(this.posterRequestParams)
    this.getTree()
  },
  mounted() {},
  methods: {
    // 获取类目树
    getTree() {
      getTree(5).then(({ data }) => {
        this.treeData = data
      })
    },
    // 获取素材列表
    getList(params) {
      this.loading = true
      getList(params)
        .then(({ rows, total }) => {
          this.list = rows
          this.total = +total
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    },
    selectGroup(value) {
      this.posterRequestParams.categoryId = value[value.length - 1]
      this.getList(this.posterRequestParams)
    },
    selctPoster(value) {
      this.selectPoster = value
    },
    decrease() {
      if (this.posterRequestParams.pageNum > 1) {
        this.posterRequestParams.pageNum -= 1
        this.getList(this.posterRequestParams)
      }
    },
    Increase() {
      if (Math.ceil(this.total / 10) > this.posterRequestParams.pageNum) {
        this.posterRequestParams.pageNum += 1
        this.getList(this.posterRequestParams)
      }
    },
    submit() {
      this.Pvisible = false
      this.$emit('success', this.selectPoster)
    }
  }
}
</script>

<template>
  <el-dialog :title="title" :visible.sync="Pvisible" :close-on-click-modal="false">
    <div>
      <div class="selectPosterGroup">
        <div>
          <span class="mr20">选择分组</span>
          <el-cascader :options="treeData" :props="attrProps" @change="selectGroup" clearable>
          </el-cascader>
        </div>
        <div class="selectPage">
          <span
            ><i class="el-icon-arrow-left" @click="decrease"></i>1/{{ page
            }}<i class="el-icon-arrow-right" @click="Increase"></i
          ></span>
        </div>
      </div>
      <div class="mt20">
        <div class="posterBody" v-for="item in list" :key="item.id" @click="selctPoster(item)">
          <img :src="item.materialUrl" />
          <div class="materialName">{{ item.materialName }}</div>
          <div class="materialMask" v-if="item.id === selectPoster.id">
            <i class="el-icon-success"></i>
          </div>
        </div>
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
.selectPage {
  i {
    cursor: pointer;
  }
}
.mt20 {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  .posterBody {
    width: 30%;
    border: 1px solid #000;
    margin-bottom: 10px;
    cursor: pointer;
    position: relative;
  }
  img {
    width: 100%;
  }
  .materialName {
    height: 40px;
    font-size: 14px;
    line-height: 40px;
    padding-left: 20px;
    border-top: 1px solid #000;
  }
  .materialMask {
    position: absolute;
    left: 0;
    top: 0;
    height: 100%;
    width: 100%;
    z-index: 99;
    background: rgba(0, 0, 0, 0.6);
    display: flex;
    align-items: center;
    justify-content: center;
    i {
      font-size: 24px;
      color: #4185f4;
      z-index: 888;
    }
  }
}

.selectPosterGroup {
  display: flex;
  justify-content: space-between;
}
</style>
