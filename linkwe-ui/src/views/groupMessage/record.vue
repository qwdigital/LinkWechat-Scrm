<template>
  <div>
    <div>
      <div>
        <el-button type="primary" style="margin-bottom: 20px;" @click="goRoute('', 'add')">新建群发</el-button>
      </div>
      <el-form :model="query" ref="queryForm" :inline="true" label-position="left" class="top-search" label-width="70px">
        <el-form-item label="群发内容" prop="content">
          <el-input size="mini" v-model="query.content" placeholder="请输入群发内容" clearable @keyup.enter.native="getList(1)" />
        </el-form-item>
        <el-form-item label="群发类型" prop="chatType">
          <el-select v-model="query.chatType" placeholder="请选择群发类型" size="mini">
            <el-option v-for="(value, key, index) in pushType" :label="value" :value="key" :key="index">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="发送类型" prop="isTask">
          <el-select v-model="query.isTask" placeholder="请选择发送类型" size="mini">
            <el-option v-for="(value, key, index) in timedTask" :label="value" :value="key" :key="index">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label-width="0">
          <el-button type="primary" size="mini" @click="getList(1)">查询</el-button>
          <el-button type="info" size="mini" plain @click="resetQuery">清空</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div>
      <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
        <el-table-column label="群发内容" align="center" prop="content" />
        <el-table-column label="群发类型" align="center">
          <template slot-scope="scope">
            {{ pushType[scope.row.chatType] }}
          </template>
        </el-table-column>
        <el-table-column label="发送类型" align="center">
          <template slot-scope="scope">
            {{ timedTask[scope.row.isTask] }}
          </template>
        </el-table-column>
        <el-table-column label="发送时间" align="center" prop="sendTime" width="180">
        </el-table-column>
        <el-table-column label="操作" align="center" width="180">
          <template slot-scope="scope">
            <!-- v-hasPermi="['enterpriseWechat:view']" -->
            <el-button size="mini" type="text" @click="goRoute(scope.row.id, 'detail')">详情</el-button>
            <el-divider v-if="scope.row.isTask === 1 && scope.row.status === 0" direction="vertical"></el-divider>
            <el-button size="mini" v-if="scope.row.isTask === 1 && scope.row.status === 0" type="text" @click="cancelSend(scope.row)">取消发送</el-button>
            <!-- <el-button v-hasPermi="['enterpriseWechat:edit']" size="mini" type="text" disabled=""
							@click="goRoute(scope.row, 1)">编辑</el-button>
						<el-button v-hasPermi="['enterpriseWechat:edit']" size="mini" type="text"
							@click="syncMsg(scope.row)">同步</el-button> -->
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" :page.sync="query.pageNum" :limit.sync="query.pageSize" @pagination="getList()" />
    </div>

  </div>
</template>


<script>
  import {
    getList,
    syncMsg,
    cancelSend
  } from '@/api/groupMessage'
  export default {
    name: 'Operlog',
    data () {
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
          content: undefined,
          chatType: undefined,
          isTask: undefined,
          orderByColumn: 'create_time',
          isAsc: 'desc'
        },
        pushType: {
          1: '发给客户',
          2: '发给客户群'
        },
        timedTask: {
          0: '立即发送',
          1: '定时发送'
        },
        pickerOptions: {
          disabledDate (time) {
            return time.getTime() > Date.now() // 选当前时间之前的时间
          }
        }
      }
    },
    created () {
      this.getList()
    },
    methods: {
      getList (page) {
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
          .then(({
            rows,
            total
          }) => {
            this.list = rows
            this.total = Number(total)
            this.loading = false
            this.ids = []
          })
          .catch(() => {
            this.loading = false
          })
      },
      /** 重置按钮操作 */
      resetQuery () {
        this.dateRange = []
        this.$refs['queryForm'].resetFields()
        this.getList(1)
      },
      // 多选框选中数据
      handleSelectionChange (selection) {
        this.ids = selection.map((item) => item.id)
      },
      /** 删除按钮操作 */
      handleDelete (row) {
        const operIds = row.operId || this.ids
        this.$confirm(
          '是否确认删除日志编号为"' + operIds + '"的数据项?',
          '警告', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
          .then(function () { })
          .then(() => {
            this.getList()
            this.msgSuccess('删除成功')
          })
          .catch(function () { })
      },
      goRoute (id, path) {
        const query = {}
        if (id) {
          query.id = id
        }
        this.$router.push({
          path: '/customerMaintain/groupMessage/' + path,
          query
        })
      },
      syncMsg (data) {
        let {
          msgid,
          messageId
        } = data
        syncMsg(this, {
          msgids: [msgid],
          messageId
        })
          .then(({
            data
          }) => {
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
      cancelSend (data) {
        cancelSend(data.id).then(res => {
          if (res.code == 200) {
            this.getList()
            this.msgSuccess('操作成功')
          } else {
            this.msgError(res.msg || '操作失败')
          }
        })
      },
    }
  }
</script>


<style lang="scss" scoped>
  .my-divider {
    display: block;
    height: 1px;
    width: 100%;
    background-color: #dcdfe6;
  }
</style>
