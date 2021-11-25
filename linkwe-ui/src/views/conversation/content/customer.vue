<template>
  <div class="customer">
    <el-row type="flex">
      <el-col :span="6" class="borderRight">
        <div class="name pd15">客户列表（{{ customerTotal }}）</div>
        <el-input
          class="mb5"
          placeholder="搜索客户"
          prefix-icon="el-icon-search"
          v-model="customerQuery.name"
          @keyup.enter.native="getCustomerList(1)"
        >
        </el-input>
        <div v-loading="customerLoading">
          <ul v-infinite-scroll="getCustomerList" class="customer-wrap">
            <template v-if="customerList.length">
              <el-row
                v-for="(item, index) in customerList"
                :key="index"
                tag="li"
                @click.native="choiceCustomer(item, index)"
                class="customer-li"
                :class="{ active: index == personIndex }"
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
                  <span :style="{ color: item.type === 1 ? '#4bde03' : '#f9a90b' }"
                    >{{ { 1: '@微信', 2: '@企业微信' }[item.type] }}
                  </span>
                  <i :class="['el-icon-s-custom', { 1: 'man', 2: 'woman' }[item.gender]]"></i>
                </el-col>
              </el-row>

              <div class="ac mt10 mb15">
                {{ customerList.length != customerTotal ? '下拉加载更多' : '看到底了～' }}
              </div>
            </template>
            <el-empty v-else :image-size="100"></el-empty>
          </ul>
        </div>
      </el-col>
      <el-col :span="6" class="borderRight">
        <template v-if="talkName">
          <div class="name pd15">{{ talkName }}</div>
          <el-input
            class="mb5"
            placeholder="搜索联系人"
            prefix-icon="el-icon-search"
            v-model="chatContent"
            @keyup.enter.native="getChatList()"
          >
          </el-input>
          <el-tabs v-model="activeName" @tab-click="getChatList()">
            <el-tab-pane label="单聊" name="0">
              <userList
                v-if="activeName == 0"
                :personList="personList"
                :loading="loading"
                @chatFn="chatFn"
              >
              </userList>
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
        </template>

        <el-empty v-else :image-size="100" description="请选择客户"></el-empty>
      </el-col>
      <el-col :span="12">
        <chatListClass v-show="queryChat.receiveName" :queryChat="queryChat"></chatListClass>

        <el-empty
          v-if="!queryChat.receiveName"
          :image-size="100"
          description="请选择联系人"
        ></el-empty>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import chatListClass from './component/chatListClass.vue'
import userList from './component/userList.vue'
import grouplist from './component/groupList.vue'
import * as api from '@/api/conversation/content.js'
import * as apiCustomer from '@/api/customer/index'
export default {
  components: {
    userList,
    grouplist,
    chatListClass
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
      personIndex: undefined,
      activeName: '0',
      personList: [],
      loading: false,
      fromId: '',

      queryChat: {}
    }
  },
  created() {
    // this.customerList()
  },
  methods: {
    chatFn(data) {
      this.queryChat = {
        fromId: this.fromId,
        receiveName: data.name,
        toList: data.receiver
      }
    },
    groupFn(data) {
      this.queryChat = {
        fromId: this.fromId,
        receiveName: data.name,
        roomId: data.receiver
      }
    },
    choiceCustomer(data, index) {
      this.personIndex = index
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
          .catch(err => {
            this.loading = false
          })
      } else {
        api
          .getGroupChatList(this.fromId)
          .then(({ data }) => {
            this.loading = false
            this.personList = data
          })
          .catch(err => {
            this.loading = false
          })
      }
    },
    getCustomerList(page) {
      this.customerLoading = true
      if (page) {
        this.customerQuery.pageNum = page
        this.customerList = []
      }
      page && (this.customerQuery.pageNum = page)
      apiCustomer.getList(this.customerQuery).then(res => {
        this.customerList.push(...res.rows)
        this.customerTotal = ~~res.total
        this.customerQuery.pageNum++
        this.customerLoading = false
      })
    }
  }
}
</script>
<style lang="scss" scoped>
/deep/.el-tabs__nav-scroll {
  padding-left: 15px;
}

.borderRight {
  border-right: 2px solid #ccc;
}

.customer {
  background: #f6f6f9;

  .el-tabs {
    background: #fff;
  }
  /deep/ .el-tabs__header {
    margin: 0;
  }

  .name {
    font-size: 18px;
    min-height: 20px;
  }

  .customer-wrap {
    position: relative;
    height: calc(100vh - 288px);
    background: white;
    border-bottom: 1px solid #efefef;
    overflow-y: auto;
    overflow-x: hidden;
    color: #999;
    text-align: center;
    .customer-li {
      padding: 10px;
      text-align: left;
      cursor: pointer;
      border-bottom: 1px solid #efefef;

      &:hover {
        background: #efefef;
      }

      img {
        width: 40px;
      }
      &.active {
        background: #ebf4fc;
      }
    }
  }
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

.userList {
  height: calc(100vh - 328px);
}
</style>
