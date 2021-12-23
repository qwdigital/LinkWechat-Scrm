<script>
import { getList } from '@/api/customer/dimission'

export default {
  name: 'AllocatedStaffList',
  components: {},
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
      total: 0,
      list: [],
      dialogVisibleSelectUser: false,
      dateRange: [] // 离职日期
    }
  },
  watch: {},
  computed: {},
  created() {
    this.getList()
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
      getList(this.query)
        .then(({ rows, total }) => {
          this.list = rows
          this.total = +total
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    },
    resetForm() {
      this.dateRange = []
      this.$refs['queryForm'].resetFields()
      this.getList(1)
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
          value-format="yyyy-MM-dd"
          type="daterange"
          :picker-options="pickerOptions"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          align="right"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label>
        <el-button
          v-hasPermi="['customerManage:dimission:query']"
          type="primary"
          @click="getList(1)"
          >查询</el-button
        >
        <el-button
          v-hasPermi="['customerManage:dimission:query']"
          type="success"
          @click="resetForm('queryForm')"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-table ref="multipleTable" :data="list" tooltip-effect="dark" style="width: 100%">
      <el-table-column type="index" label="序号" width="55"></el-table-column>
      <el-table-column prop="userName" label="已离职员工"></el-table-column>
      <el-table-column prop="department" label="所属部门"></el-table-column>
      <el-table-column
        prop="allocateCustomerNum"
        label="已分配客户数"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="allocateGroupNum"
        label="已分配群聊数"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column prop="dimissionTime" label="离职时间" show-overflow-tooltip>
        <template slot-scope="scope">{{ scope.row.dimissionTime }}</template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['customerManage:dimission:edit']"
            @click="
              $router.push({
                path: 'allocatedStaffDetail',
                query: { userId: scope.row.userId }
              })
            "
            type="text"
            size="small"
            >查看</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="query.pageNum"
      :limit.sync="query.pageSize"
      @pagination="getList()"
    />
  </div>
</template>

<style lang="scss" scoped></style>
