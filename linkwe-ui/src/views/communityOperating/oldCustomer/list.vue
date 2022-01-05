<script>
import { getList, remove, getStat } from '@/api/communityOperating/oldCustomer'

export default {
  components: {},
  props: {},
  data() {
    return {
      // 老客标签建群查询参数
      query: {
        pageNum: 1,
        pageSize: 10,
        taskName: '', // 任务名称
        // sendType: '', // 发送方式
        createBy: '', // 创建人
        beginTime: '', // 创建开始时间
        endTime: '' // 创建结束时间
      },
      total: 0, // 老客标签建群总数据量
      loading: false, // 主页table加载状态
      list: [], // 老客标签建群数据
      // 可用的发送方式数据
      // sendTypeOptions: [
      //   { label: '企业群发', value: 0 },
      //   { label: '个人群发', value: 1 }
      // ],
      dateRange: [], // 创建日期[开始时间, 结束时间]
      multiSelect: [], // 多选数据
      customerSearchId: '', // 客户统计所查询的任务ID
      // 客户统计查询参数
      customerQuery: {
        pageNum: 1,
        pageSize: 10,
        customerName: '', // 客户名称
        isInGroup: '', // 是否在群
        isSent: '' // 是否送达
      },
      customerTotal: 0, // 客户统计总数据量
      customerLoading: false, // 客户统计table加载状态
      customerList: [], // 客户统计数据
      customerShowList: [], // 展示用客户统计数据
      // 是否在群选择项
      inGroupOptions: [
        { label: '在群', value: 1 },
        { label: '不在群', value: 0 }
      ],
      // 是否送达选择项
      sendStatusOptions: [
        { label: '已送达', value: 1 },
        { label: '未送达', value: 0 }
      ],
      dialogVisible: false // 客户统计会话
    }
  },
  watch: {
    // 日期选择器数据同步至查询参数
    dateRange(dateRange) {
      if (!dateRange || dateRange.length !== 2) {
        this.query.beginTime = ''
        this.query.endTime = ''
      } else {
        ;[this.query.beginTime, this.query.endTime] = dateRange
      }
    },
    dialogVisible(val) {
      if (!val) this.$refs['customerForm'].resetFields()
      this.customerList = []
      this.customerTotal = 0
    }
  },
  created() {
    this.getList(1)

    this.$store.dispatch(
      'app/setBusininessDesc',
      `
        <div>企业通过下发任务通知员工给选中的标签客户主动发送加群引导语以及群活码，客户扫码后入群。</div>
      `
    )
  },
  methods: {
    // 获取老客标签建群数据
    getList(page) {
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
    // 获取客户统计数据
    getStat(page) {
      page && (this.customerQuery.pageNum = page)
      this.customerLoading = true

      getStat(this.customerSearchId, this.customerQuery)
        .then(({ rows, total }) => {
          this.customerList = rows
          this.customerTotal = +total
          this.customerLoading = false
        })
        .catch(() => {
          this.customerLoading = false
        })
    },
    // 新增/编辑老客数据
    goRoute(id) {
      this.$router.push({
        path: 'oldCustomerAev',
        query: { id: id }
      })
    },
    // 重置查询参数
    resetQuery() {
      this.dateRange = []
      this.$refs['queryForm'].resetFields()

      this.$nextTick(() => {
        this.getList(1)
      })
    },
    // 获取显示用tag字符串
    // getDisplayTags(row) {
    //   if (!(row && row.tagList.length > 0)) return ''

    //   return row.tagList.map((d) => d.name).join(' ')
    // },
    // 批量删除
    handleBulkRemove() {
      this.$confirm('确认删除当前数据?删除操作无法撤销，请谨慎操作。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          const ids = this.multiSelect.map((t) => t.taskId)

          remove(ids + '').then((res) => {
            if (res.code === 200) {
              this.getList()
            } else {
            }
          })
        })
        .catch(() => {})
    },
    // 删除
    handleRemove(id) {
      this.$confirm('确认删除当前数据?删除操作无法撤销，请谨慎操作。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          remove(id + '').then((res) => {
            if (res.code === 200) {
              this.getList()
            } else {
            }
          })
        })
        .catch(() => {})
    },
    // 处理多选
    handleSelectionChange(val) {
      this.multiSelect = val
    },
    // 打开客户统计会话
    openCustomerDialog(id) {
      this.customerSearchId = id
      this.dialogVisible = true

      this.getStat(1)
    },
    // 过滤客户统计数据
    customerFilter() {
      const l = []
      for (let data of this.customerList) {
        if (
          this.customerQuery.customerName !== '' &&
          !this.customerQuery.customerName.includes(data.customerName)
        )
          continue
        if (this.customerQuery.isInGroup !== '' && this.customerQuery.isInGroup !== data.isInGroup)
          continue
        if (this.customerQuery.isSent !== '' && this.customerQuery.isSent !== data.isSent) continue

        l.push(data)
      }

      this.customerShowList = l.slice(
        this.customerQuery.pageNum * this.customerQuery.pageSize,
        this.customerQuery.pageSize
      )
    },
    // 客户统计查询
    customerSearch() {
      this.getStat()
      // this.customerFilter()
    },
    // 客户统计重置
    resetCustomerQuery() {
      this.$refs['customerForm'].resetFields()

      this.getStat(1)
      // this.customerFilter()
    }
  }
}
</script>

<template>
  <div>
    <div class="top-search">
      <el-form inline label-position="right" :model="query" label-width="100px" ref="queryForm">
        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="query.taskName" placeholder="请输入"></el-input>
        </el-form-item>
        <!-- <el-form-item label="发送方式" prop="sendType">
          <el-select v-model="query.sendType" placeholder="请选择" size="small">
            <el-option
              v-for="(sendType, index) in sendTypeOptions"
              :label="sendType.label"
              :value="sendType.value"
              :key="index"
            ></el-option>
          </el-select>
        </el-form-item> -->
        <el-form-item label="创建人" prop="createBy">
          <el-input v-model="query.createBy" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="dateRange"
            value-format="yyyy-MM-dd"
            type="daterange"
            :picker-options="pickerOptions"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            align="right"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label=" ">
          <el-button type="primary" @click="getList(1)">查询</el-button>
          <el-button type="success" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="mid-action">
      <div>
        <el-button type="primary" v-hasPermi="['enterpriseWechat:add']" @click="goRoute()">
          新建任务
        </el-button>
      </div>
      <div>
        <el-button :disabled="multiSelect.length === 0" @click="handleBulkRemove" type="cyan">
          批量删除
        </el-button>
      </div>
    </div>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center"></el-table-column>
      <el-table-column
        label="任务名称"
        align="center"
        prop="taskName"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <!-- <el-table-column prop="sendType" label="发送方式" align="center">
        <template #default="{ row }">
          {{ parseInt(row.sendType) === 0 ? '企业群发' : '个人群发' }}
        </template>
      </el-table-column> -->
      <el-table-column label="当前群人数" align="center" width="100">
        <template #default="{ row }">
          <el-button type="text" @click="openCustomerDialog(row.taskId)">
            {{ row.totalMember }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="客户标签" align="center">
        <div v-if="row.tagList" slot-scope="{ row }">
          <el-popover placement="bottom" trigger="hover" :disabled="row.tagList.length < 3">
            <div>
              <el-tag v-for="(unit, unique) in row.tagList" :key="unique">
                {{ unit.name }}
              </el-tag>
            </div>
            <div slot="reference">
              <el-tag v-for="(unit, unique) in row.tagList.slice(0, 2)" :key="unique">
                {{ unit.name }}
              </el-tag>
              <el-tag key="a" v-if="row.tagList.length > 2">...</el-tag>
            </div>
          </el-popover>
        </div>
        <span v-else>无标签</span>

        <!-- <template #default="{ row }">
          <el-popover placement="bottom" width="200" trigger="hover" :content="getDisplayTags(row)">
            <div slot="reference" class="table-desc overflow-ellipsis">
              {{ getDisplayTags(row) }}
            </div>
          </el-popover>
        </template> -->
      </el-table-column>

      <el-table-column prop="createBy" label="创建人" align="center"></el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="160"
      ></el-table-column>

      <el-table-column
        label="操作"
        align="center"
        width="180"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['enterpriseWechat:view']"
            size="mini"
            type="text"
            @click="handleRemove(scope.row.taskId)"
            >删除</el-button
          >
          <el-button
            v-hasPermi="['enterpriseWechat:edit']"
            size="mini"
            type="text"
            @click="goRoute(scope.row.taskId)"
            >编辑</el-button
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

    <el-dialog title="客户统计" :visible.sync="dialogVisible" :close-on-click-modal="false">
      <div>
        <div class="top-search">
          <el-form
            inline
            label-position="right"
            :model="customerQuery"
            label-width="80px"
            ref="customerForm"
          >
            <el-form-item prop="customerName">
              <el-input
                v-model="customerQuery.customerName"
                placeholder="请输入客户名称"
              ></el-input>
            </el-form-item>
            <el-form-item prop="isInGroup">
              <el-select v-model="customerQuery.isInGroup" placeholder="全部" size="small">
                <el-option
                  v-for="(inGroup, index) in inGroupOptions"
                  :label="inGroup.label"
                  :value="inGroup.value"
                  :key="index"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="isSent">
              <el-select v-model="customerQuery.isSent" placeholder="全部状态" size="small">
                <el-option
                  v-for="(status, index) in sendStatusOptions"
                  :label="status.label"
                  :value="status.value"
                  :key="index"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label>
              <el-button type="primary" @click="customerSearch">查询</el-button>
              <el-button @click="resetCustomerQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
        <el-table v-loading="customerLoading" :data="customerList">
          <el-table-column label="客户名" align="center" prop="customerName"></el-table-column>
          <el-table-column prop="sent" label="送达状态" align="center">
            <template #default="{ row }">
              <template v-if="row.sent">
                已送达
              </template>
              <template v-else>
                未送达
              </template>
            </template>
          </el-table-column>
          <el-table-column prop="inGroup" label="是否在群" align="center">
            <template #default="{ row }">
              <template v-if="row.inGroup">
                在群
              </template>
              <template v-else>
                不在群
              </template>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="customerTotal > 0"
          :total="customerTotal"
          :page.sync="customerQuery.pageNum"
          :limit.sync="customerQuery.pageSize"
          @pagination="customerSearch"
        />
      </div>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
// .overflow-ellipsis {
//   white-space: nowrap;
//   overflow: hidden;
//   text-overflow: ellipsis;
// }

// .table-desc {
//   max-width: 120px;
// }
</style>
