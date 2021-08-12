<template>
  <div class="employ">
    <el-row>
      <el-col :span="6" class="borderR">
        <div class="hd_box">
          <div class="hd_name">客户列表（{{ customerTotal }}）</div>
          <div class="paddingT10">
            <el-input
              placeholder="搜索客户"
              prefix-icon="el-icon-search"
              v-model="customerQuery.name"
              @keyup.enter.native="getCustomerList(1)"
            >
            </el-input>
          </div>
        </div>
        <div v-loading="customerLoading" class="ct_box">
          <ul v-infinite-scroll="getCustomerList" class="customer-wrap">
            <el-row
              v-for="(item, t) in customerList"
              :key="t"
              tag="li"
              @click.native="personCheck(item, t)"
              class="customer-li"
              :class="{ liActive: t == personIndex }"
              :gutter="20"
              type="flex"
              align="middle"
            >
              <el-col :span="4">
                <img v-if="item.avatar" :src="item.avatar" alt="头像" />
              </el-col>
              <el-col :span="10" v-if="item" class="toe">
                <span style="line-height:40px">{{ item.name }}</span>
              </el-col>
              <el-col :span="10" class="ar">
                <span
                  :style="{ color: item.type === 1 ? '#4bde03' : '#f9a90b' }"
                  >{{ { 1: '@微信', 2: '@企业微信' }[item.type] }}
                </span>
                <i
                  :class="[
                    'el-icon-s-custom',
                    { 1: 'man', 2: 'woman' }[item.gender]
                  ]"
                ></i>
              </el-col>
            </el-row>
          </ul>
        </div>
      </el-col>
      <el-col :span="6" class="borderR">
        <div class="hd_box">
          <div class="hd_name">{{ talkName }}</div>
          <div class="paddingT10">
            <el-input
              placeholder="搜索聊天记录"
              prefix-icon="el-icon-search"
              v-model="chatContent"
              @keyup.enter.native="chatMsgList"
            >
            </el-input>
          </div>
        </div>
        <div class="hd_tabs">
          <el-tabs v-model="activeName" @tab-click="getChatList()">
            <el-tab-pane label="单聊" name="0">
              <div class="hd_tabs_content">
                <list
                  v-if="activeName == 0"
                  :personList="personList"
                  :loading="loading"
                  @chatFn="chatFn"
                >
                </list>
              </div>
            </el-tab-pane>
            <el-tab-pane label="群聊" name="2">
              <grouplist
                v-if="activeName == 2"
                :personList="personList"
                :loading="loading"
                @groupFn="groupFn"
              >
              </grouplist>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="hd_box">
          <div class="hd_name">
            <!-- <span
              v-if="chatData&&chatData.finalChatContext">与{{chatData.finalChatContext.fromInfo.name}} 的聊天</span> -->
            <!-- 判断 activeName-->
            <span class="fr hd_nameRi">下载会话</span>
          </div>
        </div>
        <div class=" hd_tabthree">
          <el-tabs
            v-model="activeNameThree"
            @tab-click="activeNameThreeClick()"
          >
            <el-tab-pane label="全部" name="0">
              <div class="ct_box">
                <div class="hds_time">
                  <el-date-picker
                    v-model="takeTime"
                    type="datetimerange"
                    format="yyyy-MM-dd"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    align="right"
                    @change="activeNameThreeClick"
                  >
                  </el-date-picker>
                </div>
                ~
                <chats :allChat="allChat" v-if="allChat.length >= 1"></chats>
                <el-pagination
                  background
                  v-if="allChat.length >= 1"
                  layout="prev, pager, next"
                  class="pagination"
                  :current-page="currentPage"
                  @current-change="currentChange"
                  :total="total"
                >
                </el-pagination>
              </div>
            </el-tab-pane>
            <el-tab-pane label="图片及视频" name="1">
              <div class="ct_box">
                <div class="hds_time">
                  <el-date-picker
                    v-model="takeTime"
                    type="datetimerange"
                    format="yyyy-MM-dd"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    align="right"
                    @change="activeNameThreeClick"
                  >
                  </el-date-picker>
                </div>
                <!-- <chat :allChat="allChatImg" v-if="allChatImg.length>=1"></chat> -->
                <el-pagination
                  background
                  v-if="allChatImg.length >= 1"
                  class="pagination"
                  layout="prev, pager, next"
                  :total="total"
                  @current-change="currentChange"
                  :current-page="currentPage"
                >
                </el-pagination>
              </div>
            </el-tab-pane>
            <el-tab-pane label="文件" name="2">
              <div class="ct_box">
                <div class="hds_time">
                  <el-date-picker
                    v-model="takeTime"
                    type="datetimerange"
                    format="yyyy-MM-dd"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    align="right"
                    @change="activeNameThreeClick"
                  >
                  </el-date-picker>
                </div>
                <el-table
                  :data="allFile"
                  stripe
                  style="width: 100%"
                  :header-cell-style="{ background: '#fff' }"
                >
                  <el-table-column prop="msgtype" label="类型">
                  </el-table-column>
                  <el-table-column label="名称">
                    <template slot-scope="scope">
                      {{ scope.row.file.filename }}
                    </template>
                  </el-table-column>
                  <el-table-column label="大小">
                    <template slot-scope="scope">
                      {{ filterSize(scope.row.file.filesize) }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="action" label="来源">
                    <template slot-scope="scope">
                      {{ scope.row.tolist[0] }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="action" label="操作">
                    <template>
                      <el-button type="text" size="small">下载</el-button>
                      <el-button type="text" size="small">查看</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-tab-pane>
            <el-tab-pane label="链接" name="3">
              <div class="ct_box">
                <div class="hds_time">
                  <el-date-picker
                    v-model="takeTime"
                    type="datetimerange"
                    format="yyyy-MM-dd"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    align="right"
                    @change="activeNameThreeClick"
                  >
                  </el-date-picker>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="语音通话" name="4">
              <div class="ct_box">
                <div class="hds_time">
                  <el-date-picker
                    v-model="takeTime"
                    type="datetimerange"
                    format="yyyy-MM-dd"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    align="right"
                    @change="activeNameThreeClick"
                  >
                  </el-date-picker>
                </div>
                <el-table
                  :data="fileData"
                  stripe
                  style="width: 100%"
                  :header-cell-style="{ background: '#fff' }"
                >
                  <el-table-column prop="date" label="发起人">
                  </el-table-column>
                  <el-table-column prop="name" label="通话时间">
                  </el-table-column>
                  <el-table-column prop="address" label="时长">
                  </el-table-column>
                  <el-table-column prop="address" label="操作">
                  </el-table-column>
                </el-table>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import list from './component/customerList.vue'
import chats from './component/chat.vue'

import grouplist from './component/groupList.vue'
import api from '@/api/conversation/content.js'
import * as apiCustomer from '@/api/customer/index'
import { yearMouthDay } from '@/utils/common.js'
export default {
  components: {
    list,
    chats,
    grouplist
  },
  data() {
    return {
      customerLoading: false,
      customerQuery: {
        pageNum: 1,
        pageSize: 10,
        name: ''
      },
      customerList: [],
      customerTotal: 0,
      talkName: '',
      chatContent: '',
      personIndex: '-1',
      activeName: '0',
      activeNameThree: '0',
      takeTime: '',
      fileData: [],
      personList: [],
      loading: false,
      fromId: '',
      chatData: {},

      allChat: [],
      allChatImg: [],
      allFile: [],
      allLInk: [],
      allVoice: [],
      //分页
      currentPage: 1,
      total: 0
    }
  },
  created() {
    // this.customerList()
  },
  methods: {
    chatFn(data) {
      this.chatData = data
      console.log(data, '<><><><>><')
      this.activeNameThreeClick()
    },
    groupFn(data) {
      this.chatData = data
      this.activeNameThreeClick('', true)
    },
    currentChange(e) {
      this.currentPage = e
      if (this.activeName == '2') {
        return this.activeNameThreeClick(true, true)
      }
      this.activeNameThreeClick(true)
    },
    personCheck(data, e) {
      this.personIndex = e
      this.talkName = data.name
      this.fromId = data.externalUserid
      this.getChatList()
    },
    getChatList(flag) {
      if (!this.fromId) {
        return
      }
      if (flag) {
        this.loading = true
      }
      this.personList = []
      if (this.activeName == '0') {
        api
          .selectAloneChatList({
            fromId: this.fromId,
            contact: this.chatContent
          })
          .then(({ data }) => {
            this.loading = false
            this.personList = data
          })
          .catch((err) => {
            this.loading = false
          })
      } else {
        api
          .getGroupChatList({
            fromId: this.fromId
          })
          .then(({ data }) => {
            this.loading = false
            this.personList = data
          })
          .catch((err) => {
            this.loading = false
          })
      }
    },
    activeNameThreeClick(page, group) {
      if (!!!page) {
        this.currentPage = 1
      }
      let msgType = ''
      if (this.activeNameThree == 0) {
        msgType = ''
      } else if (this.activeNameThree == 1) {
        msgType = 'image'
      } else if (this.activeNameThree == 2) {
        msgType = 'file'
      } else if (this.activeNameThree == 3) {
        msgType = 'link'
      } else if (this.activeNameThree == 4) {
        msgType = 'voice'
      }

      let query = {
        msgType,
        pageSize: '10',
        pageNum: this.currentPage,
        beginTime: this.takeTime ? yearMouthDay(this.takeTime[0]) : '',
        endTime: this.takeTime ? yearMouthDay(this.takeTime[1]) : ''
      }
      if (group) {
        query.roomId = this.chatData.receiver
      } else {
        query.fromId = this.fromId
        query.toList = this.chatData.receiver
      }
      if (group) {
        api.getChatList(query).then((res) => {
          this.total = Number(res.total)
          this.resortData(res)
        })
      } else {
        api.getChatList(query).then((res) => {
          this.total = Number(res.total)
          this.resortData(res)
        })
      }
    },
    resortData(res) {
      if (this.activeNameThree == 0) {
        return (this.allChat = res.rows)
      }
      if (this.activeNameThree == 1) {
        return (this.allChatImg = res.rows)
      }
      if (this.activeNameThree == 2) {
        return (this.allFile = res.rows)
      }
      if (this.activeNameThree == 3) {
        return (this.allLInk = res.rows)
      }
      if (this.activeNameThree == 4) {
        return (this.allVoice = res.rows)
      }
    },
    getCustomerList(page) {
      this.customerLoading = true
      if (page) {
        this.customerQuery.pageNum = page
        this.customerList = []
      }
      page && (this.customerQuery.pageNum = page)
      apiCustomer.getList(this.customerQuery).then((res) => {
        this.customerList.push(...res.rows)
        this.customerTotal = ~~res.total
        this.customerQuery.pageNum++
        this.customerLoading = false
      })
    },
    chatMsgList() {
      this.getChatList()
    }
  }
}
</script>
<style lang="scss" scoped>
/deep/.el-tabs__nav-scroll {
  padding-left: 15px;
}

.borderR {
  border-right: 2px solid #ccc;
}

.paddingT10 {
  padding-top: 10px;
}

.employ {
  background: #f6f6f9;

  .hd_tabs {
    background: #fff;
  }
  /deep/ .el-tabs__header {
    margin: 0;
  }

  .hd_box {
    padding: 15px;
  }

  .hd_name {
    font-size: 18px;
    min-height: 20px;
  }

  .hd_tabs_content {
    width: 100%;
    border-bottom: 1px solid #efefef;
  }

  .hd_nameRi {
    color: #199ed8;
    text-align: right;
    font-size: 16px;
    cursor: pointer;
  }

  .customer-wrap {
    position: relative;
    height: calc(100vh - 288px);
    background: white;
    border-bottom: 1px solid #efefef;
    overflow-y: scroll;
    color: #999;
    text-align: center;
    .customer-li {
      padding: 10px;
    }
  }

  .ct_box {
    // ::-webkit-scrollbar {
    //   display: none;
    // }

    ul li {
      text-align: left;
      cursor: pointer;
      border-bottom: 1px solid #efefef;

      &:hover {
        background: #efefef;
      }

      img {
        width: 40px;
      }
    }

    .liActive {
      background: #e2e2e2 !important;
      // color: #fff;
    }

    .hds_time {
      text-align: left;
      width: 100%;
      padding: 10px 0;
    }
  }
}

.pagination {
  padding: 10px 0;
  height: 30px;
  float: left;
}

.el-icon-s-custom {
  font-size: 16px;
  margin-left: 4px;
  color: #999;
  &.man {
    color: #13a2e8;
  }
  &.woman {
    color: #f753b2;
  }
}
</style>
