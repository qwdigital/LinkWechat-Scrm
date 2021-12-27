<template>
  <div>
    <el-row style="margin-top: 10px;" type="flex" :gutter="10">
      <el-col :span="18">
        <div class="g-card g-pad20">
          <div class="title">
            <div class="name">
              群发统计
            </div>
            <div class="operation" v-if="isTask">
              <span v-if="data.refreshTime">最近同步时间：{{data.refreshTime}}</span>
              <el-button style="margin-left:20px;" type="primary" size="mini" @click="setFn">同步</el-button>
            </div>
          </div>
          <div class="total_list">
            <div class="item">
              <div>
                已发送{{data.chatType === 1 ? '员工':'群主'}}
              </div>
              <div style="font-size:18px;color: #000;">
                {{data.alreadySendNum ? data.alreadySendNum: 0}}
              </div>
            </div>
            <div class="item">
              <div>
                未发送{{data.chatType === 1 ? '员工':'群主'}}
              </div>
              <div style="font-size:18px;color: #000;">
                {{data.toBeSendNum ? data.toBeSendNum : 0}}
              </div>
            </div>
            <div class="item">
              <div>
                已送达{{data.chatType === 1 ? '客户':'客户群'}}
              </div>
              <div style="font-size:18px;color: #000;">
                {{data.alreadySendCustomerNum ? data.alreadySendCustomerNum: 0}}
              </div>
            </div>
            <div class="item">
              <div>
                未送达{{data.chatType === 1 ? '客户':'客户群'}}
              </div>
              <div style="font-size:18px;color: #000;">
                {{data.toBeSendCustomerNum ? data.toBeSendCustomerNum: 0}}
              </div>
            </div>
          </div>
        </div>
        <div class="g-card g-pad20">
          <div class="title">
            <div class="name">
              {{data.chatType === 1 ? '员工':'群主'}}详情
            </div>
          </div>
          <div class="search">
            <el-form :model="queryMember" ref="queryMemberForm" :inline="true" label-position="left" class="top-search" label-width="70px">
              <el-form-item :label="data.chatType === 1 ? '发送员工':'发送群主'" prop="userName">
                <el-input size="mini" v-model="queryMember.userName" style="width:150px;" placeholder="请输入" />
              </el-form-item>
              <el-form-item label="发送状态" prop="status">
                <el-select v-model="queryMember.status" placeholder="请选择发送状态" size="mini">
                  <el-option label="全部" value=''></el-option>
                  <el-option v-for="(value, key, index) in memberState" :label="value" :value="key" :key="index">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label-width="0">
                <el-button type="primary" size="mini" @click="getMemberList(1)">查询</el-button>
                <el-button type="info" size="mini" plain @click="resetMemberQuery">清空</el-button>
              </el-form-item>
            </el-form>
          </div>
          <div>
            <el-table v-loading="member.loading" :data="member.list">
              <el-table-column :label="data.chatType === 1 ? '发送员工':'发送群主'" align="center" prop="userName" />
              <el-table-column :label="data.chatType === 1 ? '预计发送客户':'预计发送客户群'" align="center" prop="total">
                <template slot-scope="scope">
                  {{ scope.row.toBeCustomerName ? scope.row.toBeCustomerName: 0}}
                </template>
              </el-table-column>
              <el-table-column :label="data.chatType === 1 ? '实际发送客户':'实际发送客户群'" align="center" prop="already">
                <template slot-scope="scope">
                  {{ scope.row.alreadyCustomerName ? scope.row.alreadyCustomerName: 0 }}
                </template>
              </el-table-column>
              <el-table-column label="发送状态" align="center" prop="status">
                <template slot-scope="scope">
                  {{ memberState[scope.row.status] }}
                </template>
              </el-table-column>
            </el-table>
            <pagination v-show="member.total > 0" :total="member.total" :page.sync="queryMember.pageNum" :limit.sync="queryMember.pageSize" @pagination="getMemberPage" />
          </div>
        </div>
        <div class="g-card g-pad20">
          <div class="title">
            <div class="name">
              {{data.chatType === 1 ? '客户':'客户群'}}详情
            </div>
          </div>
          <div class="search">
            <el-form :model="queryCustomer" ref="queryForm" :inline="true" label-position="left" class="top-search" label-width="90px">
              <el-form-item :label="data.chatType === 1 ? '发送客户':'发送客户群'" prop="customerName">
                <el-input size="mini" v-model="queryCustomer.customerName" style="width:150px;" placeholder="请输入" />
              </el-form-item>
              <el-form-item label="发送状态" prop="status">
                <el-select v-model="queryCustomer.status" placeholder="请选择发送状态" size="mini">
                  <el-option label="全部" value=''></el-option>
                  <el-option v-for="(value, key, index) in customerState" :label="value" :value="key" :key="index">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label-width="0">
                <el-button type="primary" size="mini" @click="getCustomerList(1)">查询</el-button>
                <el-button type="info" size="mini" plain @click="resetCustomerQuery">清空</el-button>
              </el-form-item>
            </el-form>
          </div>
          <div>
            <el-table v-loading="customer.loading" :data="customer.list">
              <el-table-column :label="data.chatType === 1 ? '客户':'客户群'" align="center" prop="customerName" />
              <el-table-column :label="data.chatType === 1 ? '所属员工':'所属群主'" align="center" prop="userName" />
              <el-table-column label="送达时间" align="center" prop="sendTime" width="180"></el-table-column>
              <el-table-column label="送达状态" align="center" prop="status">
                <template slot-scope="scope">
                  {{ customerState[scope.row.status] }}
                </template>
              </el-table-column>
            </el-table>
            <pagination v-show="customer.total > 0" :total="customer.total" :page.sync="queryCustomer.pageNum" :limit.sync="queryCustomer.pageSize" @pagination="getCustomerPage" />
          </div>
        </div>
      </el-col>
      <el-col style="width: 350px">
        <div class="g-card g-pad20" style="height: 100%">
          <preview-client :list="form"></preview-client>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import {
    getDetail, memberList, resultList, syncMsg
  } from '@/api/groupMessage'
  import PreviewClient from '@/components/previewInMobileClient.vue'

  export default {
    components: {
      PreviewClient
    },
    props: {},
    data () {
      return {
        customerState: {
          0: '未送达',
          1: '已送达',
          2: '非好友',
          3: '接收达上限'
        },
        memberState: {
          0: '未发送',
          1: '已发送'
        },
        member: {
          loading: false,
          total: 0,
          list: []
        },
        customer: {
          loading: false,
          total: 0,
          list: []
        },
        queryMember: {
          userName: '',
          msgTemplateId: '',
          pageNum: 1,
          pageSize: 10,
          status: ''
        },
        queryCustomer: {
          customerName: '',
          msgTemplateId: '',
          status: '',
          pageNum: 1,
          pageSize: 10
        },
        form: {
          welcomeMsg: '',
          materialMsgList: [],
        },
        data: {
          chatType: 1,
          alreadySendCustomerNum: 0,
          alreadySendNum: 0,
          toBeSendCustomerNum: 0,
          toBeSendNum: 0,
          refreshTime: ''
        },
        activeName: '0',
        total1: 0,
        total0: 0,
        msgId: '',
        isTask: 0
      }
    },
    watch: {},
    computed: {},
    created () {
      this.isTask = this.$route.query.isTask
      this.msgId = this.$route.query.id
      this.queryMember.msgTemplateId = this.msgId
      this.queryCustomer.msgTemplateId = this.msgId
      this.getDetail()
      this.getMemberList()
      this.getCustomerList()
    },
    mounted () { },
    methods: {
      setFn () {
        this.$confirm('确定同步？', '提示', {
          confirmButtonText: '确认同步',
          cancelButtonText: '放弃',
          type: 'warning'
        }).then(() => {
          syncMsg(this.msgId).then(fdfd => {
            this.getDetail()
            this.getMemberList()
            this.getCustomerList()
            this.$message({
              type: 'success',
              message: '同步成功!'
            })
          })
        }, () => {
          this.$message({
            type: 'info',
            message: '已取消同步！'
          })
        }).catch(() => {

        })
      },
      resetCustomerQuery () {
        this.queryCustomer.customerName = ''
        this.queryCustomer.status = ''
      },
      resetMemberQuery () {
        this.queryMember.userName = ''
        this.queryMember.status = ''
      },
      getMemberList (page) {
        page && (this.queryMember.pageNum = page)
        this.member.loading = true
        memberList(this.queryMember).then(res => {
          this.member.loading = false
          this.member.total = Number(res.total)
          this.member.list = res.rows
        })
      },
      getCustomerList (page) {
        page && (this.queryCustomer.pageNum = page)
        this.customer.loading = true
        resultList(this.queryCustomer).then(res => {
          this.customer.loading = false
          this.customer.total = Number(res.total)
          this.customer.list = res.rows
        })
      },
      getCustomerPage (e) {
        this.queryCustomer.pageNum = e.page
        this.getCustomerList()
      },
      getMemberPage (e) {
        this.queryMember.pageNum = e.page
        this.getMemberList()
      },
      setEditList (list) {
        let arr = []
        if (list && list.length) {
          list.forEach(dd => {
            if (dd.msgType === 'image') {
              let obj = {
                msgType: '0',
                materialUrl: dd.picUrl
              }
              arr.push(obj)
            } else if (dd.msgType === 'link') {
              let ob = {
                msgType: '8',
                materialName: dd.title,
                materialUrl: dd.linkUrl
              }
              arr.push(ob)
            } else if (dd.msgType === 'miniprogram') {
              let ff = {
                msgType: '9',
                digest: dd.appId,
                materialName: dd.title,
                coverUrl: dd.picUrl,
                materialUrl: dd.linkUrl
              }
              arr.push(ff)
            }
          })
        }
        return arr
      },
      getDetail () {
        getDetail(this.msgId).then(res => {
          if (res.code == 200) {
            this.data = res.data
            this.form.welcomeMsg = res.data.content
            // this.form.materialMsgList = res.data.attachments
            // this.form.welcomeMsg = res.data.attachments ? res.data.attachments[0].content : '',
            this.form.materialMsgList = res.data.attachments ? this.setEditList(res.data.attachments) : []
            this.$forceUpdate()
          } else {
            this.msgError(res.msg || '获取失败')
          }
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  .title {
    display: flex;
    align-items: center;
    justify-content: space-between;
    .name {
      font-size: 16px;
      font-weight: 500;
      color: #333;
    }
    .operation {
      display: flex;
      align-items: end;
      font-size: 12px;
      color: #999;
    }
  }
  .total_list {
    width: 100%;
    margin-top: 30px;
    display: flex;
    align-items: center;
    justify-content: space-between;

    .item {
      height: 50px;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      text-align: center;
    }
  }
  .search {
    margin: 20px 0;
  }
  .label {
    width: 70px;
    display: inline-block;
    text-align: right;
  }

  .crumb {
    font-size: 12px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #666666;
    display: flex;
  }

  .crumb- {
    // 一级 页面标题
    &title {
      display: flex;
      flex-direction: column;
      justify-content: center;
      height: 90px; // line-height: 90px;
      font-size: 18px;
      font-weight: 500;
      color: #333;
      padding: 0 20px;
      background: #fff;
      border-top-left-radius: 4px;
      border-top-right-radius: 4px;
    }
  }
</style>
