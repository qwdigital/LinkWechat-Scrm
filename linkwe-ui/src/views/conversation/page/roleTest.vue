<template>
  <div>
    <el-form :inline="true" :model="form" class="demo-form-inline">
      <el-row>
        <el-form-item label="员工名称">
          <el-input v-model="form.Ename" placeholder="客户名称" style="width:300px"></el-input>

        </el-form-item>
        <el-form-item label="客户名称">
          <el-input v-model="form.Cname" placeholder="客户名称" style="width:300px"></el-input>
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label="查找内容">
          <el-input v-model="form.Scontent" placeholder="查找内容" style="width:300px"></el-input>
        </el-form-item>
        <el-form-item label="时间范围">

          <el-date-picker v-model="form.Stime" type="datetimerange" format='yyyy-MM-dd' range-separator="至"
            start-placeholder="开始日期" end-placeholder="结束日期" align="right">
          </el-date-picker>
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item>
          <el-button type="primary" @click="init">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button>导出列表</el-button>
        </el-form-item>
      </el-row>
    </el-form>
    <div class="content">
      <el-table :data="fileData" stripe style="width: 100%" :header-cell-style="{background:'#fff'}">
        <el-table-column prop="date" label="发送者" width="180">
          <template slot-scope="scope">
            <p v-if="scope.row.fromInfo">{{scope.row.fromInfo.name}}</p>
          </template>
        </el-table-column>
        <el-table-column prop="name" label=" 内容">
          <template slot-scope="scope">
            <p v-if="!!scope.row.content" class="emcode" v-html="scope.row.content"></p>
            <p v-else-if="!!!scope.row.content&&scope.row.text">{{scope.row.text.content}}</p>
          </template>
        </el-table-column>
        <el-table-column label="消息状态" width="200">
          <template slot="header">
            {{floorRange}}
            <el-select size="mini" v-model="floorRange" class="noborder" @change="chechName(floorRange)">
              <el-option v-for="item in displayOptions" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </template>
          <template slot-scope="scope">
            <div class="pers">
              <span v-if="scope.row.action==''">
              </span>
              <span v-else-if="scope.row.action=='send'">
                <span class="green"></span>
                已发送
              </span>
              <span v-else-if="scope.row.action=='recall'">
                <span class="red"></span>
                已撤回
              </span>
              <span v-else-if="scope.row.action=='switch'">
                <span class="gay"></span>
                企业日志
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="address" label="发送时间" width="200">
          <template slot-scope="scope" v-if="scope.row.text">
            {{parseTime(scope.row.msgtime)}}
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background class="pagination" layout="prev, pager, next" :total="total"
        @current-change="currentChange" :current-page="currentPage">
      </el-pagination>
    </div>
  </div>
</template>
<script>
  import {
    content
  } from '@/api/content.js'
  import {
    yearMouthDay,
    parseTime
  } from '@/utils/common.js'
  export default {
    data() {
      return {
        form: {
          Ename: "",
          Cname: '',
          Scontent: '',
          Stime: ''
        },
        currentPage: 1,
        total: 0,
        ac: '',
        fileData: [],
        floorRange: '全部',
        displayOptions: [{
            value: "",
            label: "全部"
          },
          {
            value: "send",
            label: "已发送"
          },
          {
            value: "recall",
            label: "已撤回"
          },
          {
            value: "switch",
            label: "切回企业日志"
          }
        ]
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      currentChange(e) {
        this.currentPage = e
        this.init(true)
      },
      init(flag) {
        if (!!!flag) {
          this.currentPage = 1
        }
        let query = {
          userName: this.form.Ename,
          customerName: this.form.Cname,
          keyWord: this.form.Scontent,
          beginTime: this.form.Stime ? yearMouthDay(this.form.Stime[0]) : "",
          endTime: this.form.Stime ? yearMouthDay(this.form.Stime[1]) : "",
          pageNum: this.currentPage,
          action: this.ac
        }
        content.getChatAllList(query).then(res => {
          console.log(res)
          this.fileData = res.rows;
          this.total = Number(res.total)
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
        console.log(e,this.ac)
     this.init()
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
      border: none
    }
  }

  .emcode /deep/ em {
    color: #ff0000;
  }

</style>
