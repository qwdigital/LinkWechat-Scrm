<template>
  <div class="employ">
    <el-row>
      <el-col :span="6" class="borderR">
        <div class="hd_box">
          <div class="hd_name">客户列表（{{employAmount}}）</div>
          <div class="paddingT10">
            <el-input placeholder="搜索客户" prefix-icon="el-icon-search" v-model="employName">
            </el-input>
          </div>
        </div>
        <div class="ct_box ct_boxFirst">
          <ul>
            <li v-for="(i,t) in CList" :key="t" @click="personCheck(i,t)" :class="{'liActive':t==personIndex}">
              <el-row :gutter="20" style="   padding: 10px 0;margin:0">
                <el-col :span="4">
                  <img v-if="i.avatar" :src="i.avatar" alt="头像">
                </el-col>
                <el-col :span="16" v-if="i"><span style="line-height:40px">{{i.name}}</span></el-col>
              </el-row>
            </li>
          </ul>
        </div>
      </el-col>
      <el-col :span="6" class="borderR">
        <div class="hd_box">
          <div class="hd_name">{{talkName}}</div>
          <div class="paddingT10">
            <el-input placeholder="搜索聊天记录" prefix-icon="el-icon-search" v-model="employName">
            </el-input>
          </div>
        </div>
        <div class="hd_tabs">
          <el-tabs v-model="activeName" @tab-click="getChatList()">
            <el-tab-pane label="单聊" name="0">
              <div class="hd_tabs_content">
                <list v-if="activeName==0" :personList="personList" :loading="loading" @chatFn="chatFn">
                </list>
              </div>
            </el-tab-pane>
            <el-tab-pane label="群聊" name="2">
              <grouplist v-if="activeName==2" :personList="personList" :loading="loading" @groupFn='groupFn'>
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
            <span class="fr hd_nameRi">下载会话</span></div>
        </div>
        <div class=" hd_tabthree">
          <el-tabs v-model="activeNameThree" @tab-click="activeNameThreeClick()">
            <el-tab-pane label="全部" name="0">
              <div class="ct_box">
                <div class="hds_time">
                  <el-date-picker v-model="takeTime" type="datetimerange" format='yyyy-MM-dd' range-separator="至"
                    start-placeholder="开始日期" end-placeholder="结束日期" align="right" @change="activeNameThreeClick">
                  </el-date-picker>
                </div>~
                <chats :allChat="allChat" v-if="allChat.length>=1"></chats>
                <el-pagination background v-if="allChat.length>=1" layout="prev, pager, next" class="pagination"
                  :current-page="currentPage" @current-change="currentChange" :total="total">
                </el-pagination>
              </div>
            </el-tab-pane>
            <el-tab-pane label="图片及视频" name="1">
              <div class="ct_box">
                <div class="hds_time">
                  <el-date-picker v-model="takeTime" type="datetimerange" format='yyyy-MM-dd' range-separator="至"
                    start-placeholder="开始日期" end-placeholder="结束日期" align="right" @change="activeNameThreeClick">
                  </el-date-picker>
                </div>
                <!-- <chat :allChat="allChatImg" v-if="allChatImg.length>=1"></chat> -->
                <el-pagination background v-if="allChatImg.length>=1" class="pagination" layout="prev, pager, next"
                  :total="total" @current-change="currentChange" :current-page="currentPage">
                </el-pagination>
              </div>
            </el-tab-pane>
            <el-tab-pane label="文件" name="2">
              <div class="ct_box">
                <div class="hds_time">
                  <el-date-picker v-model="takeTime" type="datetimerange" format='yyyy-MM-dd' range-separator="至"
                    start-placeholder="开始日期" end-placeholder="结束日期" align="right" @change="activeNameThreeClick">
                  </el-date-picker>
                </div>
                <el-table :data="allFile" stripe style="width: 100%" :header-cell-style="{background:'#fff'}">
                  <el-table-column prop="msgtype" label="类型">
                  </el-table-column>
                  <el-table-column label="名称">
                    <template slot-scope="scope">
                      {{scope.row.file.filename}}
                    </template>
                  </el-table-column>
                  <el-table-column label="大小">
                    <template slot-scope="scope">
                      {{filterSize(scope.row.file.filesize)}}
                    </template>
                  </el-table-column>
                  <el-table-column prop="action" label="来源">
                    <template slot-scope="scope">
                      {{scope.row.tolist[0]}}
                    </template>
                  </el-table-column>
                  <el-table-column prop="action" label="操作">
                    <template slot-scope="scope">
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
                  <el-date-picker v-model="takeTime" type="datetimerange" format='yyyy-MM-dd' range-separator="至"
                    start-placeholder="开始日期" end-placeholder="结束日期" align="right" @change="activeNameThreeClick">
                  </el-date-picker>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="语音通话" name="4">
              <div class="ct_box">
                <div class="hds_time">
                  <el-date-picker v-model="takeTime" type="datetimerange" format='yyyy-MM-dd' range-separator="至"
                    start-placeholder="开始日期" end-placeholder="结束日期" align="right" @change="activeNameThreeClick">
                  </el-date-picker>
                </div>
                <el-table :data="fileData" stripe style="width: 100%" :header-cell-style="{background:'#fff'}">
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
  import list from '../component/customerList.vue'
  import chats from '../component/chat.vue'

  import grouplist from '../component/groupList.vue'
  import {
    content
  } from '@/api/content.js'
  import {
    yearMouthDay
  } from '@/utils/common.js'
  export default {
    components: {
      list,
      chats,
      grouplist
    },
    data() {
      return {
        employAmount: 1,
        employName: '',
        talkName: '',
        personIndex: '-1',
        activeName: "0",
        activeNameThree: '0',
        takeTime: '',
        fileData: [],
        CList: [],
        personList: [],
        loading: false,
        employId: '',
        chatData: {},

        allChat: [],
        allChatImg: [],
        allFile: [],
        allLInk: [],
        allVoice: [],
        //分页
        currentPage: 1,
        total: 0

      };
    },

    methods: {
      chatFn(data) {
        this.chatData = data
        console.log(data)
        this.activeNameThreeClick()
      },
      groupFn(data){
         this.chatData = data;
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
        this.talkName = data.name;
        this.employId = data.externalUserid
        this.getChatList()
      },
      getChatList(flag) {
        if (!this.employId) {
          return
        }
        if (flag) {
          this.loading = true
        }
        this.personList = []
        content.getTree({
          fromId: this.employId,
          searchType: this.activeName
        }).then(({
          rows
        }) => {
          this.loading = false
          this.personList = rows
        }).catch(err => {
          this.loading = false
        })
      },
      activeNameThreeClick(page,group) {
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
          fromId: this.chatData.fromId,
          msgType,
          pageSize: '10',
          pageNum: this.currentPage,
          beginTime: this.takeTime ? yearMouthDay(this.takeTime[0]) : "",
          endTime: this.takeTime ? yearMouthDay(this.takeTime[1]) : '',
        }
        if (group) {
          query.roomId = this.chatData.roomId
        } else {
          query.receiveId = this.chatData.receiveId
        }
          if (group) {
            content.chatGrounpList(query).then(res => {
              this.total = Number(res.total)
             this.resortData(res)
            })
          } else {
            content.chatList(query).then(res => {
              this.total = Number(res.total)
             this.resortData(res)
            })
          }   
      },
          resortData(res){
         if (this.activeNameThree == 0) {
                return this.allChat = res.rows
              }
              if (this.activeNameThree == 1) {
                return this.allChatImg = res.rows
              }
              if (this.activeNameThree == 2) {
                return this.allFile = res.rows
              }
              if (this.activeNameThree == 3) {
                return this.allLInk = res.rows
              }
              if (this.activeNameThree == 4) {
                return this.allVoice = res.rows
              }
      },
      customerList() {
        let querys = {
          pageNum: 1,
          pageSize: 999,
          name: this.employName,
          userId: '',
          tagIds: '',
          beginTime: '',
          endTime: '',
          status: '',
          isOpenChat: '1'
        }
        content.listByCustomer(querys).then(res => {
          console.log(res)
          res.rows = res.rows.filter((a, b) => {
            return a && a.name
          })
          this.CList = res.rows
          this.employAmount = res.total;
        })
      }
    },
    mounted() {
      this.customerList()
    }

  }

</script>
<style lang="scss" scoped>
  /deep/.el-tabs__nav-scroll {
    padding-left: 15px;
  }

  .fr {
    float: right;
  }

  .borderR {
    border-right: 2px solid #ccc;
  }

  .paddingT10 {
    padding-top: 10px
  }

  .employ {
    background: #f6f6f9;
    min-height: 800px;

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
      min-height: 653px;
      border-bottom: 1px solid #efefef;
    }

    .hd_nameRi {
      color: #199ed8;
      text-align: right;
      font-size: 16px;
      cursor: pointer;
    }

    .ct_boxFirst {
      height: 700px;
    }

    .ct_box {
      background: white;
      border-bottom: 1px solid #efefef;
      min-height: 709px;
      padding: 10px;
      overflow-y: scroll;
      color: #999;
      text-align: center;

      ::-webkit-scrollbar {
        display: none;
      }

      ul {
        margin: 0;
        padding: 0;
      }

      ul li {

        text-align: left;
        cursor: pointer;
        border-bottom: 1px solid #efefef;

        :hover {
          background: #efefef;
        }

        img {
          width: 40px;
        }

        ;
      }

      .liActive {
        background: lightblue !important;
        color: #199ed8;
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

</style>
