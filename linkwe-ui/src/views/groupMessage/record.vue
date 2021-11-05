<template>
  <div>
    <div class="g-title">
      消息群发
      <img style="margin-left:10px;" :src="require('@/assets/drainageCode/icon-info.png')" alt="">
      <div class="desc">管理员下发群发任务，员工根据任务群发消息给客户</div>
    </div>
    <div class="my-divider"></div>
    <div class="g-card g-pad20" style="margin-top: 0;">
      <div>
        <el-button type="primary" style="margin-bottom: 20px;" @click="goRoute('', 'add')">新建群发</el-button>
      </div>
      <el-form :model="query" ref="queryForm" :inline="true" label-position="left" class="top-search" label-width="70px">
        <!-- <el-form-item label="创建人" prop="sender">
					<el-input v-model="query.sender" placeholder="请输入" clearable @keyup.enter.native="getList(1)" />
				</el-form-item> -->
        <el-form-item label="群发内容" prop="content">
          <el-input size="mini" v-model="query.content" placeholder="请输入群发内容" clearable @keyup.enter.native="getList(1)" />
        </el-form-item>
        <el-form-item label="群发类型" prop="pushType">
          <el-select v-model="query.pushType" placeholder="请选择群发类型" size="mini">
            <el-option v-for="(value, key, index) in pushType" :label="value" :value="key" :key="index">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="发送类型" prop="timedTask">
          <el-select v-model="query.timedTask" placeholder="请选择发送类型" size="mini">
            <el-option v-for="(value, key, index) in timedTask" :label="value" :value="key" :key="index">
            </el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="创建日期">
					<el-date-picker :picker-options="pickerOptions" v-model="dateRange" value-format="yyyy-MM-dd"
						type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期">
					</el-date-picker>
				</el-form-item> -->
        <el-form-item label-width="0">
          <el-button type="primary" size="mini" @click="getList(1)">查询</el-button>
          <el-button type="info" size="mini" plain @click="resetQuery">清空</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="g-card g-pad20">
      <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
        <!-- <el-table-column type="selection" width="55" align="center" /> -->
        <el-table-column label="群发内容" align="center" prop="content" />
        <el-table-column label="群发类型" align="center">
          <template slot-scope="scope">
            {{ pushType[scope.row.pushType] }}
          </template>
        </el-table-column>
        <el-table-column label="发送类型" align="center">
          <template slot-scope="scope">
            {{ timedTask[scope.row.timedTask] }}
          </template>
        </el-table-column>
        <!-- <el-table-column label="创建人" align="center" prop="sender" /> -->
        <el-table-column label="发送时间" align="center" prop="sendTime" width="180">
        </el-table-column>
        <!-- <el-table-column label="发送情况" align="center" prop="sendInfo">
					<template slot-scope="scope">
						{{ scope.row | sendInfo }}
					</template>
				</el-table-column> -->
        <el-table-column label="操作" align="center" width="180">
          <template slot-scope="scope">
            <!-- v-hasPermi="['enterpriseWechat:view']" -->
            <el-button size="mini" type="text" @click="goRoute(scope.row.messageId, 'detail')">详情</el-button>
            <el-divider direction="vertical"></el-divider>
            <el-button size="mini" type="text" @click="cancelSend(scope.row)">取消发送</el-button>
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
    filters: {
      sendInfo (data) {
        if (data.timedTask == 1) {
          return '定时任务 发送时间:' + data.settingTime
        } else {
          let unit = data.expectSend == 1 ? '个群' : '人'
          return `预计发送${data.expectSend}${unit}，已成功发送${data.actualSend}${unit}`
        }
      }
    },
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
          sender: undefined,
          content: undefined,
          pushType: undefined,
          beginTime: undefined,
          endTime: undefined
        },
        pushType: {
          0: '发给客户',
          1: '发给客户群'
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
        console.log(222)
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
            this.total = +total
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
