<script>
import * as api from '@/api/customer/dimission'
import SelectUser from '@/components/SelectUser'

export default {
  name: 'Dimission',
  components: { SelectUser },
  props: {},
  data() {
    return {
      // 查询参数
      query: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        beginTime: undefined,
        endTime: undefined
      },
      loading: false,
      isMoreFilter: false,
      total: 0,
      form: {
        user: '',
        region: ''
      },
      list: [],
      currentRow: {},
      dialogVisibleSelectUser: false,
      dateRange: [] // 离职日期
    }
  },
  watch: {},
  computed: {},
  created() {
    this.getList()
    this.$store.dispatch(
      'app/setBusininessDesc',
      `
        <div>将离职成员的客户和客户群分配给其他成员跟进并继续提供服务。</div>
      `
    )
  },
  mounted() {},
  methods: {
    /** 查询 */
    getList(page) {
      if (this.dateRange) {
        this.query.beginTime = this.dateRange[0]
        this.query.endTime = this.dateRange[1]
      } else {
        this.query.beginTime = ''
        this.query.endTime = ''
      }
      page && (this.query.pageNum = page)
      this.loading = true
      api
        .getListNo(this.query)
        .then(({ rows, total }) => {
          this.list = rows
          this.total = +total
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    },
    resetForm(formName) {
      this.dateRange = []
      this.$refs['queryForm'].resetFields()
    },
    showSelectDialog() {
      if (this.currentRow.userId) {
        this.dialogVisibleSelectUser = true
      } else {
        this.$message.warning('请先选择一位员工')
      }
    },
    allocate(userlist) {
      if (userlist.length > 1) {
        this.dialogVisibleSelectUser = true
        return
      }
      let loading = this.$loading()
      api
        .allocate({
          handoverUserid: this.currentRow.userId,
          takeoverUserid: userlist[0].userId
        })
        .then(() => {
          this.msgSuccess('操作成功')
          this.getList()
          loading.close()
        })
        .catch(() => {
          loading.close()
        })
    },
    // 选中数据
    handleCurrentChange(val) {
      this.currentRow = val
    }
  }
}
</script>

<template>
  <div class="page">
    <el-form ref="queryForm" :inline="true" :model="query" label-width="100px" class="top-search">
      <el-form-item label="已离职员工" prop="userName">
        <el-input v-model="query.userName" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="离职日期">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          value-format="yyyy-MM-dd"
          :picker-options="pickerOptions"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          align="right"
        ></el-date-picker>
      </el-form-item>

      <el-form-item label>
        <el-button v-hasPermi="['customerManage:dimission:query']" type="primary" @click="getList(1)">查询</el-button>
        <el-button v-hasPermi="['customerManage:dimission:query']" type="success" @click="resetForm('queryForm')"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <div class="mid-action">
      <div></div>
      <div>
        <el-button
          v-hasPermi="['customerManage:dimission:filter']"
          type="primary"
          @click="$router.push({ path: 'allocatedStaffList' })"
          >已分配的离职员工</el-button
        >
        <el-button v-hasPermi="['customerManage:dimission:allocate']" type="info" @click="showSelectDialog"
          >分配给其他员工</el-button
        >
      </div>
    </div>

    <el-table
      v-loading="loading"
      ref="multipleTable"
      :data="list"
      tooltip-effect="dark"
      style="width: 100%"
      highlight-current-row
      @current-change="handleCurrentChange"
    >
      <el-table-column type="index" label="序号" width="55"></el-table-column>
      <el-table-column prop="userName" label="已离职员工"></el-table-column>
      <el-table-column prop="department" label="所属部门"></el-table-column>
      <el-table-column prop="allocateCustomerNum" label="待分配客户数" show-overflow-tooltip></el-table-column>
      <el-table-column prop="allocateGroupNum" label="待分配群聊数" show-overflow-tooltip></el-table-column>
      <el-table-column prop="dimissionTime" label="离职时间" show-overflow-tooltip>
        <template slot-scope="scope">{{ scope.row.dimissionTime }}</template>
      </el-table-column>
      <!-- <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button v-hasPermi="['customerManage:dimission:edit']" type="text" size="small">编辑</el-button>
        </template>
      </el-table-column>-->
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="query.pageNum"
      :limit.sync="query.pageSize"
      @pagination="getList()"
    />

    <!-- 选择添加人弹窗 -->
    <SelectUser :visible.sync="dialogVisibleSelectUser" title="选择分配人" @success="allocate"></SelectUser>
  </div>
</template>

<style lang="scss" scoped>
.page {
  padding: 24px;
}
.el-input,
.el-select {
  width: 220px;
}
</style>
