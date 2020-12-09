<script>
import * as api from '@/api/drainageCode/welcome'
export default {
  name: 'Tab',
  components: {},
  props: {
    type: {
      type: Number | String,
      default: '1',
    },
  },
  data() {
    return {
      // 查询参数
      query: {
        pageNum: 1,
        pageSize: 10,
        welcomeMsg: undefined,
        welcomeMsgTplType: 1,
      },
      loading: false,
      total: 0,
      list: [],
      wel: {
        1: '员工',
        2: '部门员工',
        3: '客户',
      },
    }
  },
  watch: {},
  computed: {},
  created() {
    this.query.welcomeMsgTplType = +this.type
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
          this.$emit('total', [this.type, this.total])
        })
        .catch(() => {
          this.loading = false
        })
    },
    /** 删除按钮操作 */
    remove(id) {
      // const operIds = id || this.ids + "";
      this.$confirm('是否确认删除吗?', '警告', {
        type: 'warning',
      })
        .then(function() {
          return api.remove(id)
        })
        .then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        })
    },
    goRoute(data) {
      let query = {}
      if (data) {
        let { id, welcomeMsgTplType, welcomeMsg, materialUrl } = data
        query = { id, welcomeMsgTplType, welcomeMsg, materialUrl }
      } else {
        query.welcomeMsgTplType = this.type
      }
      this.$router.push({ path: 'welcomeAdd', query: query })
    },
  },
}
</script>

<template>
  <div>
    <div class="mid-action">
      <div class="total"></div>
      <div>
        <el-button
          type="primary"
          size="mini"
          icon="el-icon-plus"
          @click="goRoute()"
          >新建{{ wel[type] }}欢迎语</el-button
        >
        <el-input
          placeholder="请输入内容"
          prefix-icon="el-icon-search"
          v-model="query.welcomeMsg"
          style="width: 240px; margin-left: 10px;"
          @change="getList(0)"
        ></el-input>
      </div>
    </div>
    <el-table v-loading="loading" :data="list">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="欢迎语" align="center" prop="welcomeMsg" />
      <el-table-column label="创建人" align="center" prop="createBy" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="goRoute(scope.row)"
            v-hasPermi="['monitor:operlog:query']"
            >编辑</el-button
          >
          <el-button
            size="mini"
            type="text"
            @click="remove(scope.row.id)"
            v-hasPermi="['monitor:operlog:query']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<style lang="scss" scoped>
.mid-action {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  align-items: center;
  background: #fff;
  .total {
    background-color: rgba(65, 133, 244, 0.1);
    // border: 1px solid rgba(65, 133, 244, 0.2);
    border-radius: 3px;
    font-size: 14px;
    min-height: 32px;
    line-height: 32px;
    // padding: 0 12px;
    color: #606266;
  }
  .num {
    color: #00f;
  }
}
</style>
