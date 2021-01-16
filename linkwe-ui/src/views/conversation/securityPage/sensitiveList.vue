<!-- 敏感词触发页面 -->
<template>
  <div>
    <el-form :inline="true" :model="form" class="demo-form-inline">
      <el-form-item label="添加人">
        <div class="tag-input" @click="dialogVisibleSelectUser = true">
          <span class="tag-place" v-if="!queryUser.length">请选择</span>
          <template v-else>
            <el-tag
              type="info"
              v-for="(unit, unique) in queryUser"
              :key="unique"
              >{{ unit.name }}</el-tag
            >
          </template>
        </div>
      </el-form-item>
      <el-form-item>
        <el-input v-model="form.keyword" placeholder="搜索关键词" style="width:300px">
          <el-button slot="prepend" icon="el-icon-search"></el-button>
        </el-input>
      </el-form-item>
      <el-form-item>
          <el-button type="primary" @click="getSensitiveList">查询</el-button>
      </el-form-item>
    </el-form>
    <div class="search-content">
      <el-row>
        <el-col :span="8">
          <div class="left-title">触发记录</div>
        </el-col>
        <el-col :span="16" style="text-align: right;">
          <el-radio-group v-model="selectDate" size="small">
            <el-radio-button label="昨日"></el-radio-button>
            <el-radio-button label="7日"></el-radio-button>
            <el-radio-button label="30日"></el-radio-button>
          </el-radio-group>
          <el-date-picker
            v-model="dateRangeValue"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            class="date-range">
          </el-date-picker>
        </el-col>
      </el-row>
    </div>
    <el-table :data="tableData" stripe style="width: 100%"
      :header-cell-style="{background:'#fff'}">
      <el-table-column prop="pattern_words" label="敏感词">
      </el-table-column>
      <el-table-column prop="content" label="内容">
      </el-table-column>
      <el-table-column prop="from" label="触发者">
      </el-table-column>
      <el-table-column prop="status" label="消息状态">
        <template slot="header" >
          {{floorRange}} 
          <el-select size="mini" v-model="floorRange" class="noborder" @change="chechName(floorRange)">
            <el-option v-for="item in displayOptions" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column prop="msgtime" label="发送时间">
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="query.pageNum"
      :limit.sync="query.pageSize"
      @pagination="getSensitiveList()"
    />
    <!-- 选择添加人弹窗 -->
    <SelectUser
      :visible.sync="dialogVisibleSelectUser"
      title="选择添加人"
      :isSigleSelect="true"
      @success="selectedUser"
    ></SelectUser>
  </div>
</template>
<script>
import * as sensitiveApis from '@/api/conversation/security'
import SelectUser from "@/components/SelectUser"
export default {
  components: {
    SelectUser
  },
  data() {
    return {
      form: {
        pageSize: 10,
        pageNum: 1,
        scopeType: '',
        auditScopeId: '',
        keyword: '' // 关键词
      },
      selectDate: '',
      dateRangeValue: '', // 时间选择
      tableData: [],
      queryUser: [], // 搜索框选择的添加人
      query: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      dialogVisibleSelectUser: false, // 选择添加人弹窗显隐
      floorRange:'全部',
        displayOptions: [{
          value: "0",
          label: "全部"
        },
        {
          value: "1",
          label: "已发送"
        },
        {
          value: "2",
          label: "已撤回"
        },
        {
          value: "3",
          label: "已删除"
        }
      ]  
    }
  },
  methods: {
    getSensitiveList() {
      if (this.queryUser.length > 0) {
        this.form.pageSize = this.query.pageSize
        this.form.pageNum = this.query.pageNum
        sensitiveApis.getSecurityList(this.form).then(res => {
          if (res.code === 200) {
            this.tableData = res.rows
            this.total = Number(res.total)
          }
        })
      } else {
        this.$message.warning('请选择人员！')
      }
    },
    chechName(e) {
      if (e == 0) {
        this.floorRange = '全部'
      } else if (e == 1) {
        this.floorRange = '已发送'
      } else if (e == 2) {
        this.floorRange = '已撤回'
      } else {
        this.floorRange = '切回企业日志'
      }
    },
    selectedUser(list) {
      // console.log(list)
      this.queryUser = list
      this.form.scopeType = list.map(d => d.department) + ""
      this.form.auditScopeId = list.map(d => d.userId) + "" 
      this.getSensitiveList()
    }
  }
}
</script>
<style lang="scss" scoped>
  .demo-form-inline {
    background: #efefef;
    padding: 18px 10px 0 10px;
  }
  .tag-input {
    width: 240px;
    display: flex;
    border-radius: 4px;
    border: 1px solid #dcdfe6;
    align-items: center;
    padding: 0 15px;
    overflow: hidden;
    height: 32px;
    background: #fff;
    .tag-place {
      color: #bbb;
      font-size: 14px;
    }
  }
  .search-content {
    height: 40px;
    margin-top: 15px;
    padding: 10px;
    .left-title {
      height: 40px;
      line-height: 40px;
    }
    .date-range {
      margin-left: 10px;
      width: 300px;
    }
  }
  .noborder {
    /deep/ .el-input--mini .el-input__inner{
      width: 2px;
      border: none;
    }
  }
  .content {
    margin-top: 15px;
    padding: 10px;
  }
</style>