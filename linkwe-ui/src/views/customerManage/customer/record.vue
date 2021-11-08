<script>
export default {
  name: '',
  props: {
    user: {
      type: String,
      default: ''
    }
  },
  components: {},
  data() {
    return {
      query: {
        pageNum: 1,
        pageSize: 10,
        name: '' // "客户名称",
      },
      loading: false,
      total: 0,
      list: [] // 列表
    }
  },
  computed: {},
  watch: {},
  created() {
    this.getList()
  },
  mounted() {},
  methods: {
    /** 查询 */
    getList(page) {
      page && (this.query.pageNum = page)
      this.loading = true
      api
        .getList(this.query)
        .then(({ rows, total }) => {
          this.list = rows
          this.total = +total
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    }
  }
}
</script>

<template>
  <div>
    <el-table :data="list" v-loading="loading">
      <el-table-column label="员工" align="center" prop="welcomeMsg" />
      <el-table-column label="跟进记录" align="center" prop="createBy" />
      <el-table-column label="跟进状态" align="center" prop="createTime" />
      <el-table-column label="跟进记录时间" align="center" prop="createTime" />
    </el-table>

    <el-pagination
      @current-change="getList"
      :current-page="query.pageNum"
      :page-sizes="[10, 20, 50]"
      :page-size="query.pageSize"
      layout="total, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>
  </div>
</template>

<style lang="less" scoped></style>
