<template>
  <div>
    <div>
      <el-form :model="query" label-position="left" ref="queryForm" :inline="true" label-width="100px" class="top-search">
        <el-form-item label="选择创建人">
          <el-input :value="name" readonly @focus="dialogVisible = true" placeholder="请选择员工" />
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
        <el-button type="primary" :disabled="disable" size="mini" @click="syncFn">同步朋友圈</el-button>
        <div class="time" v-if="lastSyncTime">最近同步：{{lastSyncTime}}</div>
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
      <el-table-column show-overflow-tooltip prop="createTime" width="170px" label="创建时间"></el-table-column>
      <el-table-column show-overflow-tooltip prop="pointNum" label="点赞数"></el-table-column>
      <el-table-column show-overflow-tooltip prop="commentNum" label="评论数"></el-table-column>
      <el-table-column label="操作" align="center" width="100">
        <template slot-scope="{ row }">
          <el-button type="text" @click="detailFn(row.momentId)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination :total="total" :page.sync="query.pageNum" :limit.sync="query.pageSize" @pagination="getList()" />
    <SelectUser :visible.sync="dialogVisible" title="组织架构" :defaultValues="userArray" @success="getSelectUser"></SelectUser>
    <el-dialog title="详情" :visible.sync="detailDialogVisible" width="40%">
      <el-form label-position="right" label-width="100px">
        <el-form-item label="内容：">
          {{detail.content}}
        </el-form-item>
        <el-form-item v-if="detail.otherContent" label=" ">
          <template v-for="(data, index) in detail.otherContent">
            <div style="display:inline-block;margin-right:10px;" v-if="data.annexType === 'image'">
              <el-image style="width: 100px; height: 100px" :src="data.annexUrl"></el-image>
            </div>
            <div style="display:inline-block;margin-right:10px;" v-else-if="data.annexType === 'video'">
              <video style="width: 100px; height: 100px" :src="data.annexUrl"></video>
            </div>
            <div style="display:inline-block;margin-right:10px;" v-else-if="data.annexType === 'link'">
              <video style="width: 100px; height: 100px" :src="data.annexUrl"></video>
            </div>
          </template>
        </el-form-item>
        <el-form-item label="点赞客户：" v-if="detail.pointCustomerName">
          <el-tag v-for="(data, key) in detail.pointCustomerName.split(',')" :key="key">{{data}}</el-tag>
        </el-form-item>
        <el-form-item label="评论客户：" v-if="detail.momentCustomerName">
          <el-tag v-for="(data, key) in detail.momentCustomerName.split(',')" :key="key">{{data}}</el-tag>
        </el-form-item>
        <span slot="footer" class="dialog-footer">
          <el-button @click="detailDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="detailDialogVisible = false">确 定</el-button>
        </span>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
  import SelectUser from '@/components/SelectUser'
  import moment from 'moment'
  import { getEnterpriceList, syncHMoments, getDetail } from '@/api/circle'
  export default {
    name: 'friend-index',
    components: {
      SelectUser
    },
    data () {
      return {
        disable: false,
        value1: [],
        dialogVisible: false,
        userArray: [],
        name: '',
        query: {
          pageSize: 10,
          pageNum: 1,
          beginTime: '',
          endTime: '',
          creator: '',
          type: 1
        },
        loading: false,
        tableData: [],
        total: 0,
        lastSyncTime: '',
        detailDialogVisible: false,
        detail: {}
      }
    },
    methods: {
      setTimeChange (e) {
        this.query.beginTime = moment(e[0]).format('YYYY-MM-DD')
        this.query.endTime = moment(e[1]).format('YYYY-MM-DD')
      },
      detailFn (id) {
        this.detailDialogVisible = true
        getDetail(id).then(dd => {
          if (dd.code === 200) {
            this.detail = dd.data
          }
        })
      },
      syncFn () {
        syncHMoments({ filterType: 1 }).then(res => {
          if (res.code === 200) {
            this.msgSuccess(res.msg)
            this.getList()
          }
        })
      },
      getSelectUser (data) {
        this.userArray = data
        this.query.creator = this.userArray.map(function (obj, index) {
          return obj.userId
        }).join(",")
        this.name = this.userArray.map(function (obj, index) {
          return obj.name
        }).join(",")
      },
      resetQuery () {
        this.name = ''
        this.query = {
          pageSize: 10,
          pageNum: 1,
          beginTime: '',
          endTime: '',
          creator: '',
          type: 1
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
            this.setTimeDiff()
            this.total = Number(res.total)
            this.loading = false
          })
          .catch(() => {
            this.loading = false
          })
      },
      setTimeDiff () {
        if (this.lastSyncTime) {
          let date1 = moment(this.lastSyncTime)
          let date2 = moment()
          let date3 =  date2.diff(date1,'minute')
          const h = Math.floor(date3/60)
          console.log(h)
          if (h >= 2) {
            this.disable = false
          } else {
            this.disable = true
          }
        }
      }
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
