<script>
  import { getList } from '@/api/customer/tag'
  import list from './list'

  export default {
    components: { list },
    props: {
      // 添加标签显隐
      visible: {
        type: Boolean,
        default: false
      },
      // title: {
      //   type: String,
      //   default: '',
      // },
      type: {
        type: String | Number,
        default: '4'
      },
      // 显示哪些素材类型标签
      showArr: {
        type: Array,
        default: () => []
      }
    },
    data () {
      return {
        text: {},
        image: {},
        file: {}
      }
    },
    watch: {},
    computed: {
      title () {
        const titleMap = {
          4: '文本',
          0: '图片',
          8: '图文',
          9: '小程序'
        }
        return titleMap[this.type] || '素材'
      },
      Pvisible: {
        get () {
          return this.visible
        },
        set (val) {
          this.$emit('update:visible', val)
        }
      },
      Ptype: {
        get () {
          return this.type
        },
        set (val) {
          this.$emit('update:type', val)
        }
      }
    },
    created () { },
    mounted () { },
    methods: {
      submit () {
        this.Pvisible = false
        this.$emit('success', this.selectedData)
      },
      onChange (data) {
        this.selectedData = data
      }
    }
  }
</script>

<template>
  <el-dialog :title="`选择${title}`" :visible.sync="Pvisible" width="680px" append-to-body destroy-on-close>
    <div>
      <list v-if="showArr.length <= 1" :type="showArr[0] || type" @change="onChange"> </list>
      <el-tabs v-else-if="showArr.length > 1" v-model="Ptype">
        <!-- <el-tab-pane name="0" v-if="showArr.includes(0)">
          <span slot="label"> <i class="el-icon-date"></i> 文本 </span>
          <list type="4" @change="onChange"> </list>
        </el-tab-pane>
        <el-tab-pane name="1" v-if="showArr.includes(1)">
          <span slot="label"> <i class="el-icon-date"></i> 图片 </span>
          <list type="0" @change="onChange"> </list>
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

  /deep/.el-dialog__body {
    padding: 5px 20px;
    height: 76vh;
    overflow: auto;
  }
</style>
