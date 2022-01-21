<script>
import { getTree, getList } from '@/api/material'
export default {
  components: {},
  props: {
    type: {
      type: String | Number,
      default: '4'
    }
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
        mediaType: '4'
      },
      list: [], // 列表
      total: 0, // 总条数
      treeData: [], // 树
      // 树props
      treeProps: {
        children: 'children',
        label: 'name'
      },
      // 分组props
      groupProps: {
        // expandTrigger: 'hover',
        checkStrictly: true,
        children: 'children',
        label: 'name',
        value: 'id',
        emitPath: false
      },
      radio: ''
    }
  },
  watch: {
    radio(val) {
      this.$emit('change', val)
    },
    type(val) {
      this.query.mediaType = this.type
      this.query.pageNum = 1
      this.list = []
      this.getList()
    }
  },
  computed: {},
  created() {
    this.query.mediaType = this.type
    this.getList()
  },
  mounted() {},
  methods: {
    // 获取素材列表
    getList(page) {
      page && (this.query.pageNum = page)
      this.loading = true
      getList(this.query)
        .then(({ rows, total }) => {
          this.list = rows.map((i) => {
            i.checked = false
            return i
          })
          this.total = +total
          this.loading = false
          this.$emit('listChange', this.list)
        })
        .catch(() => {
          this.loading = false
        })
    },
    onRowClick(row, column, event) {
      const evTarget = event.target.nodeName.toUpperCase(0)
      if (evTarget == 'INPUT') {
        return
      }

      const checked = row.checked
      if (!checked) {
        this.list.forEach((i) => (i.checked = false))
      }
      row.checked = !checked
      this.$emit('change', row.checked ? row : null)
    }
  }
}
</script>

<template>
  <div>
    <template v-if="type == 4">
      <el-input
        v-model="query.search"
        class="ml10 mr10 mb20"
        style="width: 260px"
        placeholder="请输入文本内容"
        @keydown.enter.native="getList(1)"
      ></el-input>
    </template>

    <!-- 文本 -->
    <el-table v-loading="loading" :data="list" @row-click="onRowClick">
      <el-table-column width="30">
        <template slot-scope="{ row }">
          <el-radio v-model="row.checked" :label="true"></el-radio>
        </template>
      </el-table-column>

      <!-- 文本 -->
      <el-table-column v-if="type == 4" prop="content" label="文本内容"></el-table-column>

      <!-- 图片 -->
      <el-table-column label="图片" v-if="type == 0">
        <template slot-scope="{ row }">
          <div class="ma-img flex aic" :key="row.id">
            <el-image fit class="img-li" :src="row.materialUrl" />
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="materialUrl" v-if="type == 0" label="图片地址"></el-table-column>

      <!-- 图文 -->
      <!-- <template v-if="type == 7">
        <el-table-column prop="materialName" label="图文标题"></el-table-column>
        <el-table-column prop="materialUrl" label="图文链接"></el-table-column>
      </template> -->

      <!-- 链接 -->
      <template v-if="type == 8">
        <el-table-column prop="materialName" label="图文标题"></el-table-column>
        <el-table-column prop="materialUrl" label="图文链接"></el-table-column>
      </template>

      <!-- 小程序 -->
      <template v-if="type == 9">
        <el-table-column prop="materialName" label="小程序标题"></el-table-column>
        <el-table-column prop="digest" label="小程序AppID"></el-table-column>
      </template>

      <el-table-column prop="updateTime" label="最近更新时间"></el-table-column>
    </el-table>

    <!--
    <el-radio-group
      v-if="type == 0"
      v-loading="loading"
      class="img-wrap"
      v-model="radio"
    >
      <el-radio v-for="(item, index) in list" :label="item" :key="index">
        <div class="ma-img ac mb20">
          <el-card shadow="hover" body-style="padding: 0px;">
            <img
              crossorigin="anonymous"
              class="img-li"
              :src="item.materialUrl"
              alt
            />
          </el-card>
          <div class="mt10 toe" :title="item.materialName">
            {{ item.materialName }}
          </div>
        </div>
      </el-radio>
    </el-radio-group> -->

    <el-pagination
      class="fr"
      @current-change="getList"
      :current-page="query.pageNum"
      :page-size="query.pageSize"
      layout="prev, pager, next"
      :total="total"
    ></el-pagination>
  </div>
</template>

<style lang="scss" scoped>
// .img-wrap {
//   ::v-deep.el-radio__input {
//     position: absolute;
//     left: 50%;
//     top: 50%;
//     transform: translate(-50%, -50%);
//   }
// }
.ma-img {
  width: 120px;
  height: 120px;
  border-radius: 8px;
  border: 1px solid #e6ebf5;
  justify-content: center;
}
.img-li {
  // max-width: 100%;
  // height: 160px;
}
</style>
