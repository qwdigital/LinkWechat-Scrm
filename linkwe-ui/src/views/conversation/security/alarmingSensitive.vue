<!-- 敏感行为警告页面 -->
<template>
  <div class="alarming">
    <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="敏感记录" name="1">
          <el-table :data="recordSensitive" stripe style="width: 100%"
            :header-cell-style="{background:'#fff'}">
            <el-table-column prop="createBy" label="操作员工">
            </el-table-column>
            <el-table-column prop="auditUserName" label="操作对象">
            </el-table-column>
            <el-table-column prop="patternWords" label="敏感行为">
            </el-table-column>
            <el-table-column prop="updateTime" label="操作时间">
            </el-table-column>
          </el-table>
          <pagination
            v-show="recordPageTotal > 0"
            :total="recordPageTotal"
            :page.sync="recordPageConfig.pageNum"
            :limit.sync="recordPageConfig.pageSize"
            @pagination="getSensiveRecordList()"
          />
        </el-tab-pane>
        <el-tab-pane label="敏感行为管理" name="2">
          <el-table :data="tabData" stripe style="width: 100%"
            :header-cell-style="{background:'#fff'}">
            <el-table-column prop="actName" label="敏感操作">
            </el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-switch v-model="scope.row.enableFlag" :active-value="1" :inactive-value="2" @change="handleStatusChange(scope.row)"></el-switch>
              </template>
            </el-table-column>
          </el-table>
          <pagination
            v-show="total > 0"
            :total="total"
            :page.sync="query.pageNum"
            :limit.sync="query.pageSize"
            @pagination="getSensitiveManagementList()"
          />
        </el-tab-pane>
      </el-tabs>
  </div>
</template>
<script>
import * as sensitiveApis from '@/api/conversation/security'
export default {
  data() {
    return {
      activeName: '1',
      recordSensitive: [],
      tabData: [],
      total: 0,
      query: {
        pageNum: 1,
        pageSize: 10
      },
      recordPageTotal: 0,
      recordPageConfig: {
        pageNum: 1,
        pageSize: 10
      }
    }
  },
  mounted() {
    this.getSensiveRecordList()
  },
  methods: {
    getSensiveRecordList() {
      sensitiveApis.getSensitiveRecord(this.recordPageConfig).then(res => {
        if (res.code === 200) {
          this.recordSensitive = res.rows
          this.recordPageTotal = Number(res.total)
        }
      })
    },
    getSensitiveManagementList() {
      sensitiveApis.getSensitiveManagement(this.query).then(res => {
        if (res.code === 200) {
          this.tabData = res.rows
          this.total = Number(res.total)
        }
      })
    },
    handleStatusChange(e) {
      console.log(e)
    },
    handleClick(tab, event) {
      console.log(this.activeName==='2')
      if(this.activeName==='2'){
       this.getSensitiveManagementList()
      }else{
        this.getSensiveRecordList()
      }
    }
  }
}
</script>
