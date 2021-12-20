<script>
import { getFollowUpRecord, syncTrack } from '@/api/customer'
import { dictTrackState } from '@/utils/dictionary'
import { dateFormat } from '@/utils'
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
    },
    lastSyncTime: String
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
      dictTrackState,
      dateFormat
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
        .then(({ rows, total, lastSyncTime }) => {
          if (this.viewType) {
            this.list = rows
          } else {
            let list = []
            let dayList = []

            // 提取天
            rows.forEach((ele) => {
              let date = this.dateFormat(ele.createTime, 'yyyyMMdd')
              dayList.includes(date) || dayList.push(date)
            })
            dayList.sort((a, b) => b - a)

            // 提取每天的时间点
            for (let i = 0; i < dayList.length; i++) {
              let timeList = []
              for (let j = 0; j < rows.length; j++) {
                // console.log(rows[j].createTime);
                if (dayList[i] == this.dateFormat(rows[j].createTime, 'yyyyMMdd')) {
                  timeList.push(rows[j])
                }
              }
              list.push(timeList)
            }

            this.list = this.list.concat(list)
          }
          this.total = +total
          this.$emit('update:lastSyncTime', lastSyncTime)
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

    <div v-else class="timeline-time-wrap">
      <ul class="infinite-list" v-infinite-scroll="getList" style="overflow:auto">
        <template v-if="list.length">
          <li v-for="(unit, i) in list" class="infinite-list-item" :key="i">
            <div class="timeline-time">{{ dateFormat(unit[0].createTime, 'yyyy-MM-dd w') }}</div>
            <el-timeline class="timeline-box">
              <el-timeline-item
                v-for="(item, index) in unit"
                :key="index"
                class="timeline-box-item"
                :timestamp="item.trackTime"
                placement="top"
                type="primary"
              >
                <p class="timeline-box-item-title">{{ item.title || '无数据' }}</p>
                <p>{{ item.trackContent || '无数据' }}</p>
              </el-timeline-item>
            </el-timeline>
          </li>
        </template>
        <el-empty v-else :image-size="150"></el-empty>
      </ul>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.timeline-time-wrap {
  display: flex;
  justify-content: center;
}
.timeline-time {
  font-size: 14px;
  color: #999999;
  line-height: 48px;
}
.el-timeline > .timeline-box-item:first-child /deep/ .el-timeline-item__node {
  border-radius: 0 !important;
}
.timeline-box {
  padding-left: 70px;
  &-item {
    /deep/ .el-timeline-item__timestamp {
      position: absolute;
      top: 4px;
      left: -45px;
      margin: 0;
    }

    &-title {
      font-weight: 700;
      line-height: 25px;
    }
  }
}

/deep/ .el-timeline-item__tail {
  border-left-color: #3c88f0;
}
</style>
