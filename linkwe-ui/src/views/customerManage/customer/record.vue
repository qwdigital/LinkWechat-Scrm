<script>
import { getFollowUpRecord, syncTrack } from '@/api/customer'
import { dictTrackState } from '@/utils/dictionary'
export default {
  name: '',
  props: {
    // 当前跟进人id
    userId: {
      type: String,
      default: null
    },
    // 	1:信息动态;2:社交动态;3:跟进动态;4:待办动态
    trajectoryType: {
      type: String,
      default: null
    },
    // 视图展示类型 1:表格展示，2:轨迹时间线展示
    viewType: {
      type: String,
      default: ''
    }
  },
  components: {},
  data() {
    return {
      query: {
        pageNum: 0,
        pageSize: 10,
        externalUserid: '', //	是	当前客户id
        userId: null, //		当前跟进人id,
        trajectoryType: null //		1:信息动态;2:社交动态;3:跟进动态;4:待办动态
      },
      loading: false,
      total: 0,
      list: [], // 列表
      dictTrackState
    }
  },
  computed: {},
  watch: {},
  created() {
    this.viewType && this.getList(1)
  },
  mounted() {},
  methods: {
    /** 查询 */
    getList(page) {
      Object.assign(this.query, {
        externalUserid: this.$route.query.externalUserid,
        userId: this.userId,
        trajectoryType: this.trajectoryType
      })
      if (this.viewType) {
        page && (this.query.pageNum = page)
      } else {
        this.query.pageNum++
      }
      this.loading = true
      getFollowUpRecord(this.query)
        .then(({ rows, total }) => {
          if (this.viewType) {
            this.list = rows
          } else {
            debugger
            this.list = this.list.concat(rows)
            console.log(this.list)
          }
          this.total = +total
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    },
    sync() {
      this.loading = true
      syncTrack(this.userId)
        .then(() => {
          this.getList(1)
          this.msgSuccess('后台开始同步数据，请稍后关注进度')
        })
        .catch((fail) => {
          this.loading = false
          console.error(fail)
        })
    }
  }
}
</script>

<template>
  <div v-loading="loading">
    <template v-if="viewType">
      <el-table :data="list">
        <el-table-column label="员工" align="center" prop="userName" />
        <el-table-column label="跟进记录" align="center" prop="trackContent" />
        <el-table-column prop="trackState" label="跟进状态" align="center">
          <template slot-scope="{ row }">
            <el-tag v-if="row.trackState" :type="dictTrackState[~~row.trackState + ''].color">{{
              dictTrackState[~~row.trackState + ''].name
            }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="跟进记录时间" align="center" prop="trackTime" />
      </el-table>

      <div class="ac">
        <el-pagination
          class="mt10"
          small
          @current-change="getList"
          :current-page="query.pageNum"
          :page-sizes="[10, 20, 50]"
          :page-size="query.pageSize"
          layout="total, prev, pager, next"
          :total="total"
        >
        </el-pagination>
      </div>
    </template>

    <ul v-else class="infinite-list" v-infinite-scroll="getList" style="overflow:auto">
      <!-- <li v-for="i in list" class="infinite-list-item" :key="i">
        <div>2021-02-16 星期二</div>
      </li> -->
      <el-timeline v-if="list.length">
        <el-timeline-item
          v-for="(item, index) in list"
          :key="index"
          :timestamp="item.trackTime"
          placement="top"
        >
          <p>{{ item.title }}</p>
          <p>{{ item.trackContent }}</p>
        </el-timeline-item>
      </el-timeline>
      <el-empty v-else :image-size="150"></el-empty>
    </ul>
  </div>
</template>

<style lang="less" scoped></style>
