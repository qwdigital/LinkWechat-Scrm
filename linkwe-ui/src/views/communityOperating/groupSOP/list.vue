<script>
import { getList, remove } from '@/api/communityOperating/groupSOP'

export default {
  data() {
    return {
      query: {
        pageNum: 1,
        pageSize: 10,
        ruleName: '', // 规则名称
        createBy: '', // 创建人
        beginTime: '', // 创建开始时间
        endTime: '' // 创建结束时间
      },
      dateRange: [], // 添加日期
      total: 0, // 群SOP数据总量
      list: [], // 群SOP数据
      multiSelect: [], // 多选数据
      loading: false
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
    }
  },
  created() {
    this.getList()

    this.$store.dispatch(
      'app/setBusininessDesc',
      `
        <div>管理员在创建群 SOP 规则后，员工通过推送的消息通知，在规定时间内给规定群聊发送规定内容。</div>
      `
    )
  },
  methods: {
    // 获取群SOP数据
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
    // 新增/编辑群SOP
    goRoute(id) {
      this.$router.push({
        path: 'groupSOPAev',
        query: { id: id }
      })
    },
    // 重置查询参数
    resetQuery() {
      this.dateRange = []
      this.$refs['queryForm'].resetFields()
      this.getList(1)
    },
    // 批量删除
    handleBulkRemove() {
      this.$confirm('确认删除当前数据?删除操作无法撤销，请谨慎操作。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          const ids = this.multiSelect.map((r) => r.ruleId)

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
    // 获取显示用实际群聊
    getDisplayGroups(row) {
      if (row.groupList) {
        const groups = row.groupList.map((g) => g.groupName)
        return groups.join('，')
      }
    }
  }
}
</script>

<template>
  <div>
    <div class="top-search">
      <el-form inline label-position="right" :model="query" label-width="100px" ref="queryForm">
        <el-form-item label="规则名称" prop="ruleName">
          <el-input v-model="query.ruleName" placeholder="请输入"></el-input>
        </el-form-item>
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
          <el-button
            v-hasPermi="['customerManage:customer:query']"
            type="primary"
            @click="getList(1)"
            >查询</el-button
          >
          <el-button
            v-hasPermi="['customerManage:customer:query']"
            type="success"
            @click="resetQuery()"
            >重置</el-button
          >
        </el-form-item>
      </el-form>
    </div>

    <div class="mid-action">
      <div>
        <el-button type="primary" @click="goRoute()">新建规则</el-button>
      </div>
      <div>
        <el-button
          v-hasPermi="['customerManage:customer:export']"
          :disabled="multiSelect.length === 0"
          @click="handleBulkRemove"
          type="cyan"
          >批量删除</el-button
        >
      </div>
    </div>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center"></el-table-column>
      <el-table-column
        label="规则名称"
        align="center"
        prop="ruleName"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <el-table-column label="执行群聊" align="center" width="120">
        <template #default="{ row }">
          <el-popover
            placement="bottom"
            width="200"
            trigger="hover"
            :content="getDisplayGroups(row)"
          >
            <div slot="reference" class="table-desc overflow-ellipsis">
              {{ getDisplayGroups(row) }}
            </div>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column label="创建人" align="center" prop="createBy"></el-table-column>

      <el-table-column label="创建人" align="center" prop="createBy"></el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime"></el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['enterpriseWechat:view']"
            size="mini"
            type="text"
            @click="handleRemove(scope.row.ruleId)"
            >删除</el-button
          >
          <el-button
            v-hasPermi="['enterpriseWechat:edit']"
            size="mini"
            type="text"
            @click="goRoute(scope.row.ruleId)"
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
  </div>
</template>

<style scoped lang="scss">
.overflow-ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.table-desc {
  max-width: 120px;
}
</style>
