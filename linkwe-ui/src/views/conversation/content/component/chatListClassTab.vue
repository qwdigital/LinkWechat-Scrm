<template>
  <div class="chatListClassTab" v-loading="loading">
    <el-date-picker
      v-model="dateRange"
      type="daterange"
      format="yyyy-MM-dd"
      value-format="yyyy-MM-dd"
      range-separator="至"
      start-placeholder="开始日期"
      end-placeholder="结束日期"
      align="right"
      @change="getList(1)"
    >
    </el-date-picker>
    <div class="list">
      <template v-if="list.length">
        <chatList v-if="'all,image,link'.includes(type)" :data="list"></chatList>

        <el-table
          v-else-if="type == 'file'"
          :data="list"
          stripe
          style="width: 100%"
          :header-cell-style="{ background: '#fff' }"
        >
          <el-table-column prop="msgtype" label="类型"> </el-table-column>
          <el-table-column label="名称">
            <template slot-scope="{ row }">
              {{ JSON.parse(row.contact).filename }}
            </template>
          </el-table-column>
          <el-table-column label="大小">
            <template slot-scope="{ row }">
              {{ filterSize(JSON.parse(row.contact).filesize) }}
            </template>
          </el-table-column>
          <el-table-column label="来源">
            <template slot-scope="{ row }">
              {{ row.name }}
            </template>
          </el-table-column>
          <el-table-column prop="action" label="操作">
            <template slot-scope="{ row }">
              <el-button type="text" size="small" @click="downloadFile(row)">下载</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-table
          v-else-if="type == 'voice'"
          :data="list"
          stripe
          :header-cell-style="{ background: '#fff' }"
        >
          <el-table-column prop="date" label="发起人">
            <template slot-scope="{ row }">
              {{ row.name }}
            </template>
          </el-table-column>
          <el-table-column prop="name" label="通话时间">
            <template slot-scope="{ row }">
              {{ row.msgTime }}
            </template>
          </el-table-column>
          <el-table-column prop="address" label="时长" min-width="50">
            <template slot-scope="{ row }"> {{ JSON.parse(row.contact).play_length }}s </template>
          </el-table-column>
          <el-table-column prop="address" min-width="150" label="操作">
            <template slot-scope="{ row }">
              <!-- <el-button
                type="text"
                size="small"
                @click="voiceLook(JSON.parse(row.contact))"
                >查看</el-button
              > -->
              <!-- <AudioPlayer
                :audio-list="[JSON.parse(row.contact).attachment]"
                ref="AudioPlayer"
              /> -->
              <voice :amrUrl="JSON.parse(row.contact)['attachment']"></voice>
              <!-- <audio controls>
                <source :src="JSON.parse(row.contact).attachment" type="audio/mpeg" />
              </audio> -->
            </template>
          </el-table-column>
        </el-table>
      </template>

      <el-empty v-else :image-size="100"></el-empty>
    </div>

    <el-pagination
      v-if="total"
      layout="prev, pager, next"
      :current-page.sync="currentPage"
      @current-change="getList"
      :total="total"
    />
  </div>
</template>

<script>
import chatList from './chatList.vue'
import * as api from '@/api/conversation/content.js'
import Voice from '@/components/Voice'
export default {
  name: '',
  components: { chatList, Voice },
  props: {
    // 消息收发者
    queryChat: {
      type: Object,
      default: () => ({})
    },
    // 消息类型
    type: {
      type: String | Number,
      default: 0
    }
  },
  data() {
    return {
      loading: false,
      currentPage: 1,
      dateRange: [],
      list: [],
      total: 0
    }
  },
  computed: {},
  watch: {
    queryChat() {
      this.getList(1)
    }
  },
  created() {
    this.getList(1)
  },
  mounted() {},
  methods: {
    getList(page) {
      // if (!this.queryChat.fromId) {
      //   return //没有选择人
      // }
      this.loading = true
      let query = {
        msgType: this.type == 'all' ? '' : this.type,
        pageSize: '10',
        orderByColumn: 't.msg_time',
        isAsc: 'desc'
      }
      if (this.dateRange) {
        query.beginTime = this.dateRange[0]
        query.endTime = this.dateRange[1]
      } else {
        query.beginTime = ''
        query.endTime = ''
      }
      page && (query.pageNum = this.currentPage = page)
      Object.assign(query, this.queryChat)
      api
        .getChatList(query)
        .then((res) => {
          this.total = ~~res.total
          this.list = res.rows
          this.loading = false
        })
        .catch((err) => {
          this.loading = false
        })
    },
    filterSize(size) {
      if (!size) return ''
      if (size < pow1024(1)) return size + ' B'
      if (size < pow1024(2)) return (size / pow1024(1)).toFixed(2) + ' KB'
      if (size < pow1024(3)) return (size / pow1024(2)).toFixed(2) + ' MB'
      if (size < pow1024(4)) return (size / pow1024(3)).toFixed(2) + ' GB'
      return (size / pow1024(4)).toFixed(2) + ' TB'

      function pow1024(num) {
        return Math.pow(1024, num)
      }
    },
    downloadFile(row) {
      this.$confirm('是否确认下载该文件?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then((response) => {
          this.download(JSON.parse(row.contact).attachment)
        })
        .catch(function() {})
    }
  }
}
</script>

<style lang="scss" scoped>
.list {
  height: calc(100vh - 370px);
  margin-top: 10px;
  background: white;
  overflow-y: scroll;
  border-bottom: 1px solid #efefef;
  color: #999;
  text-align: center;

  ::-webkit-scrollbar {
    display: none;
  }
}
.chatListClassTab {
  padding: 10px 10px 0;
  background: #fff;
}
</style>
