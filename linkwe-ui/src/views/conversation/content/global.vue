<template>
  <div>
    <el-form :inline="true" :model="query" class="top-serach">
      <el-form-item label="员工名称">
        <el-input v-model="query.userName" clearable placeholder="客户名称"></el-input>
      </el-form-item>
      <el-form-item label="客户名称">
        <el-input v-model="query.customerName" clearable placeholder="客户名称"></el-input>
      </el-form-item>
      <el-form-item label="查找内容">
        <el-input v-model="query.contact" clearable placeholder="查找内容"></el-input>
      </el-form-item>
      <el-form-item label="时间范围">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          align="right"
          :picker-options="pickerOptions"
        >
        </el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="getList(1)">查询</el-button>
      </el-form-item>
      <el-form-item>
        <el-button @click="exportList()">导出列表</el-button>
      </el-form-item>
    </el-form>
    <div>
      <el-table v-loading="loading" :data="fileData" :header-cell-style="{ background: '#fff' }">
        <el-table-column prop="date" label="发送者" width="180">
          <template slot-scope="scope">
            <p v-if="scope.row">{{ scope.row.name }}</p>
          </template>
        </el-table-column>
        <el-table-column prop="name" label=" 内容">
          <template slot-scope="{ row }">
            <ChatContent :message="row"></ChatContent>
          </template>
        </el-table-column>
        <el-table-column label="消息状态" width="200">
          <template slot="header">
            {{ floorRange }}
            <el-select
              size="mini"
              v-model="floorRange"
              class="noborder"
              @change="chechName(floorRange)"
            >
              <el-option
                v-for="item in displayOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </template>
          <template slot-scope="scope">
            <div class="pers">
              <span v-if="scope.row.action == ''"> </span>
              <span v-else-if="scope.row.action == 'send'">
                <span class="green"></span>
                已发送
              </span>
              <span v-else-if="scope.row.action == 'recall'">
                <span class="red"></span>
                已撤回
              </span>
              <span v-else-if="scope.row.action == 'switch'">
                <span class="gay"></span>
                企业日志
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="address" label="发送时间" width="200">
          <template slot-scope="scope">
            {{ scope.row.msgTime }}
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        class="pagination"
        layout="prev, pager, next"
        :total="total"
        @current-change="getList"
        :current-page.sync="query.pageNum"
      >
      </el-pagination>
    </div>
  </div>
</template>
<script>
import { getChatList, exportList } from '@/api/conversation/content.js'
import ChatContent from '@/components/ChatContent'
export default {
  components: { ChatContent },
  data() {
    return {
      query: {
        userName: '',
        customerName: '',
        contact: '',
        beginTime: '',
        endTime: '',
        pageNum: 1,
        pageSize: 10,
        orderByColumn: 't.msg_time',
        isAsc: 'desc' // asc 升序 desc 降序
      },
      dateRange: [],
      total: 0,
      ac: '',
      fileData: [],
      floorRange: '全部',
      displayOptions: [
        {
          value: '',
          label: '全部'
        },
        {
          value: 'send',
          label: '已发送'
        },
        {
          value: 'recall',
          label: '已撤回'
        },
        {
          value: 'switch',
          label: '切回企业日志'
        }
      ],
      loading: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
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
      // let query = {
      //   name: this.form.userName,
      //   customerName: this.form.customerName,
      //   contact: this.form.contact,
      //   beginTime: this.form.Stime ? yearMouthDay(this.form.Stime[0]) : '',
      //   endTime: this.form.Stime ? yearMouthDay(this.form.Stime[1]) : '',
      //   pageNum: this.query.pageNum,
      //   pageSize: 10,
      //   action: this.ac,
      //   orderByColumn: 'msg_time',
      //   isAsc: 'desc'
      // }

      getChatList(this.query)
        .then((res) => {
          this.fileData = res.rows
          this.total = ~~res.total
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    },
    chechName(e) {
      if (e == '') {
        this.floorRange = '全部'
        this.ac = ''
      } else if (e == 'send') {
        this.floorRange = '已发送'
        this.ac = 'send'
      } else if (e == 'recall') {
        this.floorRange = '已撤回'
        this.ac = 'recall'
      } else {
        this.floorRange = '切回企业日志'
        this.ac = 'switch'
      }
      console.log(e, this.ac)
      this.getList()
    },
    parseMesContent(data, type) {
      let contact = JSON.parse(data)
      let typeDict = {
        text: 'content',
        image: 'attachment',
        text: 'content'
      }
      return contact[type]
    },
    exportList() {
      this.$confirm('是否确认导出所有数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          return exportList(this.query)
        })
        .then((response) => {
          this.download(response.msg)
        })
        .catch(function() {})
    }
  }
}
</script>
<style lang="scss" scoped>
.demo-form-inline {
  background: #f1f1f1;
  padding: 18px 10px 0 10px;
}

.content {
  margin-top: 15px;
  padding: 10px;
}

.pers {
  position: relative;

  .green {
    background: greenyellow;
    position: absolute;
    width: 6px;
    height: 6px;
    border-radius: 50%;
    top: 10px;
    left: -8px;
  }

  .red {
    background: red;
    position: absolute;
    width: 6px;
    height: 6px;
    border-radius: 50%;
    top: 10px;
    left: -8px;
  }

  .gay {
    background: gray;
    position: absolute;
    width: 6px;
    height: 6px;
    border-radius: 50%;
    top: 10px;
    left: -8px;
  }
}

.noborder {
  /deep/ .el-input--mini .el-input__inner {
    width: 2px;
    border: none;
  }
}

.emcode /deep/ em {
  color: #ff0000;
}
</style>
