<template>
  <div class="employ">
    <el-row>
      <el-col :span="6" class="borderR">
        <div class="hd_box">
          <div class="hd_name">成员（{{employAmount}}）</div>
          <div class="paddingT10">
            <el-input placeholder="请输入内容" prefix-icon="el-icon-search" v-model="employName">
            </el-input>
          </div>
        </div>
        <div class="ct_box ct_boxFirst">
          <el-tree class="filter-tree" :data="treeData" :props="defaultProps" :filter-node-method="filterNode"
            ref="tree" @node-click="handleNodeClick" :default-expand-all="true">
          </el-tree>
        </div>
      </el-col>
      <el-col :span="6" class="borderR">
        <div class="hd_box">
          <div class="hd_name">{{talkName}}</div>
        </div>
        <div class="hd_tabs">
          <el-tabs v-model="activeName" @tab-click="tabClick(true)">
            <el-tab-pane label="内部联系人" name="0">
              <div class="ct_box">
                <list v-if="activeName==0" :personList="personList" :loading="loading" @chatFn='chatFn'>
                </list>
              </div>
            </el-tab-pane>
            <el-tab-pane label="外部联系人" name="1">
              <list v-if="activeName==1" :personList="personList" :loading="loading" @chatFn='chatFn'>
              </list>
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
          <div class="hd_name"><span v-if="chat.receiveWeCustomer"> 与{{chat.receiveWeCustomer.name}}的聊天</span>
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
                </div>
                <chat :allChat="allChat" v-if="allChat.length>=1"></chat>
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
                <chat :allChat="allChatImg" v-if="allChatImg.length>=1"></chat>
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
  import list from '../component/list.vue'
  import chat from '../component/chat.vue'
  import grouplist from '../component/groupList.vue'
  import * as api from '@/api/organization'
  import {
    content
  } from '@/api/content.js'
  import {
    yearMouthDay
  } from '@/utils/common.js'
  export default {
    components: {
      list,
      grouplist,
      chat
    },
    data() {
      return {
        employAmount: 1,
        employId: '',
        employName: '',
        talkName: '',
        treeData: [],
        defaultProps: {
          label: 'name',
          children: 'children',
        },
        activeName: "1",
        activeNameThree: '0',
        takeTime: '',
        fileData: [],
        chat: {},

        personList: [],
        loading: false,
        allChat: [], //全部聊天记录
        allChatImg: [], //图片，
        allVoice: [],
        allFile: [],
        allLInk: [],
        //分页
        total: '',
        currentPage: ''
      };
    },
    watch: {
      employName(val) {
        this.$refs.tree.filter(val);
      }
    },
    mounted() {
      this.getTree()
    },
    methods: {
      filterSize(size) {
        if (!size) return '';
        if (size < this.pow1024(1)) return size + ' B';
        if (size < this.pow1024(2)) return (size / this.pow1024(1)).toFixed(2) + ' KB';
        if (size < this.pow1024(3)) return (size / this.pow1024(2)).toFixed(2) + ' MB';
        if (size < this.pow1024(4)) return (size / this.pow1024(3)).toFixed(2) + ' GB';
        return (size / pow1024(4)).toFixed(2) + ' TB'
      },
      pow1024(num) {
        return Math.pow(1024, num)
      },
      currentChange(e) {
        this.currentPage = e
        if (this.activeName == '2') {
          return this.activeNameThreeClick(true, true)
        }
        this.activeNameThreeClick(true)
      },
      activeNameThreeClick(page, group) {
        if (!!!page) {
          this.currentPage = 1
        }
        if (this.chat && this.chat.receiveWeCustomer) {
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
            fromId: this.employId,
            msgType,
            pageSize: '10',
            pageNum: this.currentPage,
            beginTime: this.takeTime ? yearMouthDay(this.takeTime[0]) : "",
            endTime: this.takeTime ? yearMouthDay(this.takeTime[1]) : '',
          }
          if (group) {
            query.roomId = this.chat.roomId
          } else {
            query.receiveId = this.chat.receiveId
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
      chatFn(data) {
        this.chat = data;
        if (data.receiveWeCustomer) {
          this.activeNameThreeClick()
        }
      },
      groupFn(data) {
        this.chat = data;
        this.chat.receiveWeCustomer = 'a'
        this.activeNameThreeClick('', true)
      },
      getTree() {
        api.getTree().then(({
          data
        }) => {
          this.treeData = this.handleTree(data)
          this.handleNodeClick(this.treeData[0], true)
        })
      },
      tabClick(flag) {
        this.personList = []
        if (!this.employId) {
          return
        }
        if (flag) {
          this.loading = true
        }
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
      filterNode(value, data) {
        console.log(value, data)
        if (!value) return true;
        return data.name.indexOf(value) !== -1;
      },
      handleNodeClick(data, add) {
        if (!data.gender) {
          let querys = {
            pageNum: '1',
            pageSize: '999',
            isActivate: '4',
            department: data.id
          }
          api.getList(querys).then(({
            rows
          }) => {
            if (add == true) {
              console.log(this.treeData, data, rows)
              this.treeData[0].children = data.children.concat(rows)
              return
            } else {
              const newChild = rows;
              this.$set(data, 'children', []);
              data.children = rows
            }
          })
        } else {
          this.talkName = data.name;
          this.employId = data.userId;
          this.tabClick()
        }
      }
    }

  }

</script>
<style lang="scss" scoped>
  * {
    margin: 0;
    padding: 0;
  }

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

      /deep/ .el-tabs__header {
        margin: 0;
      }
    }

    .hd_tabthree {
      /deep/ .el-tabs__header {
        margin: 0;
      }
    }

    .hd_box {
      padding: 15px;
    }

    .hd_name {
      font-size: 18px;
      min-height: 20px;
    }

    .hd_nameRi {
      color: #199ed8;
      text-align: right;
      font-size: 16px;
      cursor: pointer;
    }

    .ct_boxFirst {
      height: 707px !important;
    }

    .ct_box {
      background: white;
      height: 710px;
      padding: 10px;
      overflow-y: scroll;
      border-bottom: 1px solid #efefef;
      color: #999;
      text-align: center;

      ::-webkit-scrollbar {
        display: none;
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
