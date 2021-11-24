<template>
  <div>
    <div>
      <el-form :model="query" label-position="left" ref="queryForm" :inline="true" label-width="100px" class="top-search">
        <el-form-item label="选择创建人" prop="creator">
          <el-input :value="query.creator" readonly @focus="dialogVisible = true" placeholder="请选择员工" />
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker v-model="value1" format="yyyy-MM-dd" @change="setTimeChange" type="daterange" range-separator="——" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
        </el-form-item>
        <el-form-item label-width="0">
          <!-- v-hasPermi="['wecom:code:list']" -->
          <el-button type="primary" @click="getList(1)">查询
          </el-button>
          <el-button @click="resetQuery">清空</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div>
      <div style="margin:10px 0;display:flex;">
        <el-button type="primary" size="mini">发表动态</el-button>
        <el-button type="primary" size="mini" @click="syncFn">同步朋友圈</el-button>
        <div class="time" v-if="lastSyncTime">{{lastSyncTime}}</div>
        <div class="time" v-else>暂无记录，请手动点击同步</div>
      </div>
    </div>
    <el-table ref="table" v-loading="loading" :data="tableData" style="width: 100%">
      <el-table-column show-overflow-tooltip prop="content" label="朋友圈内容">
        <template slot-scope="{ row }">
          <el-image v-if="row.contentType === 'image'" :src="row.content" fit="fit" :preview-src-list="[row.content]" style="width: 100px; height: 100px"></el-image>
          <div v-else>{{row.content}}</div>
        </template>
      </el-table-column>
      <el-table-column show-overflow-tooltip prop="contentType" label="类型">
        <template slot-scope="{ row }">
          <!-- image:图片；text:文本;video:视频；link:图文 -->
          <div>
            {{row.contentType === 'image'?'图片':row.contentType === 'text'?'文本':row.contentType === 'video'?'视频':'图文'}}
          </div>
        </template>
      </el-table-column>
      <el-table-column show-overflow-tooltip prop="creator" label="创建人"></el-table-column>
      <el-table-column show-overflow-tooltip prop="addUserName" min-width='140px' label="已发送员工">
        <template slot-scope="{ row }">
          <div v-if="row.addUserName">
            <template v-for="(data, key) in row.addUserName.split(',')">
              <el-tag v-if="key < 2 && data" :key="key" size="mini">{{data}}</el-tag>
            </template>
            <el-popover trigger="hover" width="200">
              <template v-for="(unit, index) in row.addUserName.split(',')">
                <el-tag :key="index" v-if="index > 1 && unit" size="mini">
                  {{unit}}</el-tag>
              </template>
              <div style="display:inline;" slot="reference">
                <el-tag v-if="row.addUserName.split(',').length > 2" size="mini">...</el-tag>
              </div>
            </el-popover>
          </div>
        </template>
      </el-table-column>
      <el-table-column show-overflow-tooltip prop="noAddUserName" min-width='140px' label="未发送员工">
        <template slot-scope="{ row }">
          <div v-if="row.noAddUserName">
            <template v-for="(data, key) in row.noAddUserName.split(',')">
              <el-tag v-if="key < 2 && data" :key="key" size="mini">{{data}}</el-tag>
            </template>
            <el-popover trigger="hover" width="200">
              <template v-for="(unit, index) in row.noAddUserName.split(',')">
                <el-tag :key="index" v-if="index > 1 && unit" size="mini">
                  {{unit}}</el-tag>
              </template>
              <div style="display:inline;" slot="reference">
                <el-tag v-if="row.noAddUserName.split(',').length > 2" size="mini">...</el-tag>
              </div>
            </el-popover>
          </div>
        </template>
      </el-table-column>
      <el-table-column show-overflow-tooltip prop="createTime" width="170px" label="创建时间"></el-table-column>
      <el-table-column label="操作" align="center" width="100">
        <template slot-scope="{ row }">
          <el-button type="text" @click="detailFn(row.momentId)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination :total="total" :page.sync="query.pageNum" :limit.sync="query.pageSize" @pagination="getList()" />
    <SelectUser :visible.sync="dialogVisible" title="组织架构" :defaultValues="userArray" @success="getSelectUser"></SelectUser>
  </div>
</template>
<script>
  import SelectUser from '@/components/SelectUser'
  import moment from 'moment'
  import { getEnterpriceList, syncHMoments, getDetail } from '@/api/circle'
  export default {
    name: 'enterprise',
    components: {
      SelectUser
    },
    data () {
      return {
        value1: [],
        dialogVisible: false,
        userArray: [],
        query: {
          pageSize: 10,
          pageNum: 1,
          beginTime: '',
          endTime: '',
          creator: '',
          type: 0
        },
        loading: false,
        tableData: [],
        total: 0,
        lastSyncTime: ''
      }
    },
    methods: {
      setTimeChange (e) {
        this.query.beginTime = moment(e[0]).format('YYYY-MM-DD')
        this.query.endTime = moment(e[1]).format('YYYY-MM-DD')
      },
      detailFn (id) {
        getDetail(id)
      },
      syncFn () {
        syncHMoments({ filterType: 0 }).then(res => {
          if (res.code === 200) {
            this.msgSuccess(res.msg)
            this.getList()
          }
        })
      },
      getSelectUser (data) {
        this.userArray = data
        this.query.creator = this.userArray.map(function (obj, index) {
          return obj.name
        }).join(",")
      },
      resetQuery () {
        this.query = {
          pageSize: 10,
          pageNum: 1,
          beginTime: '',
          endTime: '',
          creator: '',
          type: 0
        },
          this.userArray = []
        this.value1 = []
        this.getList()
      },
      getList (type) {
        type && (this.query.pageNum = type)
        this.loading = true
        getEnterpriceList(this.query)
          .then(res => {
            this.tableData = res.rows
            this.lastSyncTime = res.lastSyncTime
            this.total = Number(res.total)
            this.loading = false
          })
          .catch(() => {
            this.loading = false
          })
      },
    },
    mounted () {
      this.query.beginTime = moment(this.value1[0]).format('YYYY-MM-DD')
      this.query.endTime = moment(this.value1[1]).format('YYYY-MM-DD')
    },
    created () {
      this.value1[1] = moment(new Date()).format('YYYY-MM-DD')
      this.value1[0] = moment(new Date()).subtract(1, 'months').format('YYYY-MM-DD')
      this.getList()
    }
  }
</script>
<style lang="scss" scoped>
  .time {
    margin-left: 20px;
    color: #999;
    font-size: 12px;
    align-self: flex-end;
  }
</style>
