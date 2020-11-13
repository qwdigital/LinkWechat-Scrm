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
        endTime: undefined,
      },
      loading: false,
      isMoreFilter: false,
      total: 0,
      form: {
        user: '',
        region: '',
      },
      list: [],
      currentRow: {},
      dialogVisibleSelectUser: false,
      dateRange: [], // 离职日期
      // 日期快捷选项
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now()
        },
        shortcuts: [
          {
            text: '最近一周',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
              picker.$emit('pick', [start, end])
            },
          },
          {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
              picker.$emit('pick', [start, end])
            },
          },
          {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
              picker.$emit('pick', [start, end])
            },
          },
        ],
      },
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
      if (this.dateRange[0]) {
        this.query.beginTime = this.dateRange[0]
        this.query.endTime = this.dateRange[1]
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
      api
        .allocate({
          handoverUserid: this.currentRow.userId,
          takeoverUserid: userlist[0].userId,
        })
        .then(() => {
          this.msgSuccess('操作成功')
        })
    },
    // 选中数据
    handleCurrentChange(val) {
      this.currentRow = val
    },
  },
}
</script>

<template>
  <div class="page">
    <el-form
      ref="queryForm"
      :inline="true"
      :model="query"
      label-width="100px"
      class="top-search"
    >
      <el-form-item label="已离职员工">
        <el-input v-model="query.userName" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="离职日期">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          :picker-options="pickerOptions"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          align="right"
        ></el-date-picker>
      </el-form-item>

      <el-form-item label="离职日期">
        <el-date-picker
          v-model="query.beginTime"
          type="date"
          placeholder="离职日期"
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
          type="info"
          @click="resetForm('queryForm')"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <div class="mid-action">
      <div class="total">
        从通讯录将离职员工删除后，可以分配他的客户及客户群给其他员工继续跟进
      </div>
      <div>
        <el-button
          v-hasPermi="['customerManage:dimission:filter']"
          type="primary"
          size="mini"
          @click="$router.push({ path: '/customerManage/allocatedStaffList' })"
          >已分配的离职员工</el-button
        >
        <el-button
          v-hasPermi="['customerManage:dimission:allocate']"
          type="primary"
          size="mini"
          @click="showSelectDialog"
          >分配给其他员工</el-button
        >
      </div>
    </div>

    <el-table
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
      <el-table-column
        prop="allocateCustomerNum"
        label="待分配客户数"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="allocateGroupNum"
        label="待分配群聊数"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="dimissionTime"
        label="离职时间"
        show-overflow-tooltip
      >
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
    <SelectUser
      :visible.sync="dialogVisibleSelectUser"
      title="选择分配人"
      @success="allocate"
    ></SelectUser>
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
.mid-action {
  display: flex;
  justify-content: space-between;
  margin: 10px 0;
  align-items: center;
  .total {
    background-color: rgba(65, 133, 244, 0.1);
    border: 1px solid rgba(65, 133, 244, 0.2);
    border-radius: 3px;
    font-size: 14px;
    min-height: 32px;
    line-height: 32px;
    padding: 0 12px;
    color: #606266;
  }
  .num {
    color: #00f;
  }
}
</style>
