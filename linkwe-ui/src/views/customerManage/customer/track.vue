<script>
export default {
  name: '',
  props: {
    userId: {
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
    // this.getList()
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
  <ul class="infinite-list" v-infinite-scroll="getList" style="overflow:auto">
    <li v-for="i in list" class="infinite-list-item" :key="i">
      <div>2021-02-16 星期二</div>
      <el-timeline>
        <el-timeline-item
          v-for="(activity, index) in 6"
          :key="index"
          timestamp="2018/4/12"
          placement="top"
        >
          <p>更新 Github 模板</p>
          <p>王小虎 提交于 2018/4/12 20:46</p>
        </el-timeline-item>
      </el-timeline>
    </li>
  </ul>
</template>

<style lang="less" scoped></style>
