<script>
import { getList, syncMsg } from '@/api/groupMessage'
export default {
  name: 'Operlog',
  filters: {
    sendInfo(data) {
      if (data.timedTask == 1) {
        return '定时任务 发送时间:' + data.settingTime
      } else {
        let unit = data.expectSend == 1 ? '个群' : '人'
        return `预计发送${data.expectSend}${unit}，已成功发送${data.actualSend}${unit}`
      }
    },
  },
  data() {
    return {
      // 遮罩层
      loading: false,
      // 选中数组
      ids: [],
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 日期范围
      dateRange: [],
      // 查询参数
      query: {
        pageNum: 1,
        pageSize: 10,
        sender: undefined,
        content: undefined,
        pushType: undefined,
        beginTime: undefined,
        endTime: undefined,
      },
      pushType: {
        0: '发给客户',
        1: '发给客户群',
      },
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now() // 选当前时间之前的时间
        },
      },
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList(page) {
      if (this.dateRange[0]) {
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
          this.ids = []
        })
        .catch(() => {
          this.loading = false
        })
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.$refs['queryForm'].resetFields()
      this.getList(1)
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id)
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const operIds = row.operId || this.ids
      this.$confirm(
        '是否确认删除日志编号为"' + operIds + '"的数据项?',
        '警告',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
      )
        .then(function() {})
        .then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        })
        .catch(function() {})
    },
    goRoute(id, path) {
      this.$router.push({ path: '/groupMessage/' + path, query: { id } })
    },
    syncMsg(data) {
      let { msgid, messageId } = data
      syncMsg({ msgid, messageId })
        .then(({ data }) => {
          this.msgSuccess('同步成功')
          this.getList()
          // this.list = rows
          // this.total = +total
          // this.loading = false
          // this.ids = []
        })
        .catch(() => {
          // this.loading = false
        })
    },
  },
}
</script>
<template>
  <div>
    <el-form
      :model="query"
      ref="queryForm"
      :inline="true"
      class="top-search"
      label-width="100px"
    >
      <el-form-item label="创建人" prop="sender">
        <el-input
          v-model="query.sender"
          placeholder="请输入"
          clearable
          @keyup.enter.native="getList(1)"
        />
      </el-form-item>
      <el-form-item label="内容消息" prop="content">
        <el-input
          v-model="query.content"
          placeholder="请输入"
          clearable
          @keyup.enter.native="getList(1)"
        />
      </el-form-item>
      <el-form-item label="群发类型" prop="pushType">
        <el-select v-model="query.pushType" placeholder="请选择" size="small">
          <el-option
            v-for="(value, key, index) in pushType"
            :label="value"
            :value="key"
            :key="index"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="创建日期">
        <el-date-picker
          :picker-options="pickerOptions"
          v-model="dateRange"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label=" ">
        <el-button type="primary" @click="getList(1)">查询</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="list"
      @selection-change="handleSelectionChange"
    >
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="消息内容" align="center" prop="content" />
      <el-table-column label="群发类型" align="center">
        <template slot-scope="scope">
          {{ pushType[scope.row.pushType] }}
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="sender" />
      <el-table-column
        label="创建时间"
        align="center"
        prop="sendTime"
        width="180"
      >
      </el-table-column>
      <el-table-column label="发送情况" align="center" prop="sendInfo">
        <template slot-scope="scope">
          {{ scope.row | sendInfo }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['enterpriseWechat:view']"
            size="mini"
            type="text"
            @click="goRoute(scope.row.messageId, 'detail')"
            >查看</el-button
          >
          <el-button
            v-hasPermi="['enterpriseWechat:edit']"
            size="mini"
            type="text"
            disabled=""
            @click="goRoute(scope.row, 1)"
            >编辑</el-button
          >
          <el-button
            v-hasPermi="['enterpriseWechat:edit']"
            size="mini"
            type="text"
            @click="syncMsg(scope.row)"
            >同步</el-button
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

<style lang="scss" scoped>
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
