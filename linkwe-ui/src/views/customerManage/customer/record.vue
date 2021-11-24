<script>
import { getFollowUpRecord } from '@/api/customer'
import { dictTrackState } from '@/utils/dictionary'
export default {
  name: '',
  props: {
    externalUserid: {
      type: String,
      default: ''
    },
    userId: {
      type: String,
      default: ''
    }
  },
  components: {},
  data() {
    return {
      query: {
        pageNum: 1,
        pageSize: 10,
        externalUserid: '', //	是	当前客户id
        userId: '' //	是	当前跟进人id
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
    this.externalUserid && this.getList()
  },
  mounted() {},
  methods: {
    /** 查询 */
    getList(page) {
      Object.assign(this.query, { externalUserid: this.externalUserid, userId: this.userId })
      page && (this.query.pageNum = page)
      this.loading = true
      getFollowUpRecord(this.query)
        .then(({ rows, total }) => {
          this.list = rows
          this.total = +total
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    }
  }
}
</script>

<template>
  <div>
    <el-table :data="list" v-loading="loading">
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
  </div>
</template>

<style lang="less" scoped></style>
