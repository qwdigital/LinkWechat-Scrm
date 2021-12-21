<script>
  import * as api from '@/api/task'

  export default {
    name: 'Group',
    data () {
      return {
        query: {
          pageNum: 1,
          pageSize: 10,
          taskName: '',
          startTime: '',
          overTime: '',
          fissionType: 1
        },
        dateRange: [],
        tableData: [],
        total: 0,
        loading: false
      }
    },
    created () {
      this.getList()

      this.$store.dispatch(
        'app/setBusininessDesc',
        `
        <div>是通过管理员统一下发群发消息的任务，将消息推送给员工，员工点击任务按钮进行发送，将任务裂变海报以 H5 网页链接的形式发送给客户。此方式也会消耗一月四次客户群发触达的次数限制。</div>
      `
      )
    },
    methods: {
      setChange (e) {
        if (e) {
          this.query.startTime = e[0]
          this.query.overTime = e[1]
        } else {
          this.query.startTime = ''
          this.query.overTime = ''
        }
      },
      resetFn () {
        this.query = {
          pageNum: 1,
          pageSize: 10,
          taskName: '',
          startTime: '',
          overTime: '',
          fissionType: 1
        }
        this.dateRange = []
        this.getList()
      },
      getList (data) {
        this.loading = true
        let params = Object.assign({}, this.query, data)
        api.getList(params).then(({ rows, total }) => {
          this.tableData = rows
          this.total = +total
          this.loading = false
        })
      },
      resetForm () { },
      toDetail (row) {
        this.$router.push({
          path: `taskDetail?id=${row.id}`
        })
      },
      newAdd () {
        this.$router.push({
          path: 'addTask'
        })
      },
      toEdit (row) {
        this.$router.push({
          path: `addTask?id=${row.id}`
        })
      }
    }
  }
</script>

<template>
  <div class="app-container">
    <el-form ref="queryForm" :inline="true" :model="query" class="top-search" size="small">
      <el-form-item label="任务名" prop="taskName">
        <el-input v-model="query.taskName" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="添加日期">
        <el-date-picker v-model="dateRange" @change="setChange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" align="right"></el-date-picker>
      </el-form-item>
      <el-form-item label=" ">
        <el-button v-hasPermi="['customerManage:customer:query']" type="primary" :loading="loading" @click="getList({ pageNum: 1 })">查询</el-button>
        <el-button @click="resetFn">清空</el-button>

        <el-button v-hasPermi="['customerManage:customer:query']" type="primary" @click="newAdd()" style="background: #FA7216;color: #FFFFFF;border-color:#FA7216">新增任务</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="tableData">
      <el-table-column prop="taskName" label="任务活动名称"> </el-table-column>
      <el-table-column prop="fissNum" label="裂变客户数量"> </el-table-column>
      <el-table-column prop="fissStatus" label="活动状态">
        <template slot-scope="scope">
          <p>{{ scope.row.fissStatus === 1 ? '进行中' : '已结束' }}</p>
        </template>
      </el-table-column>
      <el-table-column prop="startTime" label="活动时间">
        <template slot-scope="scope">
          <p>{{ scope.row.startTime }}-{{ scope.row.overTime }}</p>
        </template>
      </el-table-column>
      <el-table-column prop="operation" label="操作" width="120">
        <template slot-scope="scope">
          <el-button @click="toDetail(scope.row)" v-hasPermi="['enterpriseWechat:view']" size="medium" type="text" icon="el-icon-view"></el-button>
          <el-button v-if="scope.row.fissStatus != 2" @click="toEdit(scope.row)" v-hasPermi="['enterpriseWechat:edit']" size="medium" type="text" icon="el-icon-edit-outline" style="color:#E74E59"></el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="query.pageNum" :limit.sync="query.pageSize" @pagination="getList()" />
  </div>
</template>

<style lang="scss" scoped></style>
