<script>
import { getTree, getList } from '@/api/material'
export default {
  components: {},
  props: {
    type: {
      type: String,
      default: '4',
    },
  },
  data() {
    return {
      loading: true, // 遮罩层
      // 查询条件
      query: {
        pageNum: 1,
        pageSize: 10,
        categoryId: '',
        search: '',
        mediaType: '4',
      },
      list: [], // 列表
      total: 0, // 总条数
      treeData: [], // 树
      // 树props
      treeProps: {
        children: 'children',
        label: 'name',
      },
      // 分组props
      groupProps: {
        // expandTrigger: 'hover',
        checkStrictly: true,
        children: 'children',
        label: 'name',
        value: 'id',
        emitPath: false,
      },
      radio: '',
    }
  },
  watch: {
    radio(val) {
      this.$emit('change', val)
    },
  },
  computed: {},
  created() {
    this.query.mediaType = this.type
    this.getTree()
    this.getList()
  },
  mounted() {},
  methods: {
    // 获取类目树
    getTree() {
      getTree(this.type).then(({ data }) => {
        this.treeData = data
      })
    },
    // 获取素材列表
    getList(page) {
      page && (this.query.pageNum = page)
      this.loading = true
      getList(this.query)
        .then(({ rows, total }) => {
          this.list = rows
          this.total = +total
          this.loading = false
          this.$emit('listChange', this.list)
        })
        .catch(() => {
          this.loading = false
        })
    },
  },
}
</script>

<template>
  <div>
    <el-form ref="form" :model="query" label-width="80px">
      <el-form-item label="选择分组">
        <el-cascader
          v-model="query.categoryId"
          :options="treeData"
          :props="groupProps"
        ></el-cascader>
        <el-input
          v-model="query.search"
          class="ml10 mr10"
          style="width: 150px;"
          @keydown.enter="getList(1)"
        ></el-input>
        <el-button
          v-hasPermi="['contacts:organization:query']"
          icon="el-icon-search"
          circle
          @click="getList(1)"
        ></el-button>

        <el-pagination
          class="fr"
          @current-change="getList"
          :current-page="query.pageNum"
          :page-size="query.pageSize"
          layout="prev, pager, next"
          :total="total"
        ></el-pagination>
      </el-form-item>
    </el-form>

    <!-- <slot :list="list"></slot> -->
    <!-- 文本 -->
    <el-table
      v-if="type == 4"
      v-loading="loading"
      :data="list"
      :show-header="false"
    >
      <el-table-column width="30">
        <template slot-scope="scope">
          <el-radio v-model="radio" :label="scope.row">'</el-radio>
        </template>
      </el-table-column>
      <el-table-column prop="content"> </el-table-column>
    </el-table>

    <!-- 图片 -->
    <el-radio-group
      v-if="type == 0"
      v-loading="loading"
      class="img-wrap"
      v-model="radio"
    >
      <el-radio v-for="(item, index) in list" :label="item" :key="index">
        <img class="img-li" :src="item.materialUrl" alt />
        <div class="ac mt5">{{ item.materialName }}</div>
      </el-radio>
    </el-radio-group>
  </div>
</template>

<style lang="scss" scoped>
// .img-wrap {
//   /deep/.el-radio__input {
//     position: absolute;
//     left: 50%;
//     top: 50%;
//     transform: translate(-50%, -50%);
//   }
// }
.img-li {
  width: 160px;
  height: 160px;
  border-radius: 8px;
  border: 1px solid #e6ebf5;
}
</style>
