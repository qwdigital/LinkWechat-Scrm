<template>
  <div class="employ">
    <el-row type="flex">
      <el-col :span="6" class="borderR">
        <!-- <div class="hd_box">
          <div class="name">成员（{{employAmount}}）</div>
          <div class="paddingT10">
            <el-input placeholder="请输入内容" prefix-icon="el-icon-search" v-model="employName">
            </el-input>
          </div>
        </div> -->
        <div class="ct_box ct_boxFirst" style="height: calc(100vh - 197px)">
          <el-tree
            ref="tree"
            class="filter-tree"
            :props="defaultProps"
            :filter-node-method="filterNode"
            highlight-current
            node-key="id"
            @node-click="handleNodeClick"
            :default-expanded-keys="defaultExpandedKeys"
            :load="loadNode"
            lazy
          >
            <!--
            :data="treeData"
            :default-expand-all="true"
               :expand-on-click-node="false" -->
          </el-tree>
        </div>
      </el-col>
      <el-col :span="6" class="borderR">
        <template v-if="talkName">
          <div class="name pd15">{{ talkName }}</div>
          <el-tabs v-model="activeName" @tab-click="tabClick(true)">
            <el-tab-pane label="内部联系人" name="0">
              <userList
                v-if="activeName == 0"
                :personList="personList"
                :loading="loading"
                @chatFn="chatFn"
              >
              </userList>
            </el-tab-pane>
            <el-tab-pane label="外部联系人" name="1">
              <userList
                v-if="activeName == 1"
                :personList="personList"
                :loading="loading"
                @chatFn="chatFn"
              >
              </userList>
            </el-tab-pane>
            <el-tab-pane label="群聊" name="2">
              <grouplist
                class="ct_box"
                v-if="activeName == 2"
                :personList="personList"
                :loading="loading"
                @groupFn="groupFn"
              >
              </grouplist>
            </el-tab-pane>
          </el-tabs>
        </template>

        <el-empty v-else :image-size="100" description="请选择员工"></el-empty>
      </el-col>
      <el-col :span="12">
        <chatListClass v-show="queryChat.receiveName" :queryChat="queryChat"></chatListClass>

        <el-empty
          v-if="!queryChat.receiveName"
          description="请选择联系人"
          :image-size="100"
        ></el-empty>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import chatListClass from './component/chatListClass.vue'
import userList from './component/userList.vue'
import grouplist from './component/groupList.vue'
import * as apiOrg from '@/api/organization'
import * as api from '@/api/conversation/content.js'

export default {
  components: {
    grouplist,
    userList,
    chatListClass
  },
  data() {
    return {
      employAmount: 1,
      fromId: '',
      // employName: '',
      talkName: '',
      // treeData: [],
      defaultExpandedKeys: [],
      defaultProps: {
        label: 'name',
        children: 'children',
        isLeaf: 'isLeaf'
      },
      activeName: '0',
      chat: {},

      personList: [],
      loading: false,
      queryChat: {}
    }
  },
  watch: {
    // employName(val) {
    //   this.$refs.tree.filter(val)
    // }
  },
  mounted() {
    // this.getTree()
    //this.getAmount()
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
        // fromId: this.fromId,
        receiveName: data.name,
        roomId: data.receiver
      }
    },
    // getTree() {
    //   apiOrg.getTree().then(({ data }) => {
    //     let treeData = (this.treeData = this.handleTree(data))
    //     this.handleNodeClick(this.treeData[0], true)
    //   })
    // },
    tabClick(flag) {
      this.personList = []
      if (!this.fromId) {
        return
      }
      if (flag) {
        this.loading = true
      }
      if (this.activeName == 0) {
        api
          .getInternalChatList(this.fromId)
          .then(({ data }) => {
            this.loading = false
            this.personList = data
          })
          .catch((err) => {
            this.loading = false
          })
      } else if (this.activeName == 1) {
        api
          .getExternalChatList(this.fromId)
          .then(({ data }) => {
            this.loading = false
            this.personList = data
          })
          .catch((err) => {
            this.loading = false
          })
      } else {
        api
          .getGroupChatList(this.fromId)
          .then(({ data }) => {
            this.loading = false
            this.personList = data
          })
          .catch((err) => {
            this.loading = false
          })
      }
    },
    filterNode(value, data) {
      console.log(value, data)
      if (!value) return true
      return data.name.indexOf(value) !== -1
    },
    loadNode(node, resolve) {
      if (node.level == 0) {
        apiOrg.getTree().then(({ data }) => {
          let treeData = this.handleTree(data)
          // this.handleNodeClick(this.treeData[0], true)
          treeData[0] && (this.defaultExpandedKeys = [treeData[0].id])
          resolve(treeData)
        })
      } else {
        if (node.data.userId) {
          resolve([])
        } else {
          let querys = {
            pageNum: '1',
            pageSize: '999',
            isActivate: '',
            department: node.data.id
          }
          apiOrg.getList(querys).then(({ rows }) => {
            // if (!data.children) {
            //   this.$set(data, 'children', [])
            // }

            // data.children.push(...rows)
            let arr = node.data.children ? node.data.children.concat(rows || []) : rows || []
            arr.forEach((element) => {
              element.isLeaf = !!element.userId
            })
            resolve(arr)
          })
        }
      }
    },
    handleNodeClick(data, add) {
      if (!data.userId) {
      } else {
        this.talkName = data.name
        this.fromId = data.userId
        this.tabClick()

        this.queryChat = {
          fromId: data.userId
        }
      }
    }
  }
}
</script>
<style lang="scss" scoped>
::v-deep.el-tabs__nav-scroll {
  padding-left: 15px;
}

.borderR {
  border-right: 2px solid #ccc;
}
::v-deep .el-tabs__header {
  margin: 0;
}
.employ {
  background: #f6f6f9;

  .el-tabs {
    background: #fff;
  }

  .name {
    font-size: 18px;
    min-height: 20px;
  }
  .ct_box {
    background: white;
    height: calc(100vh - 286px);
    padding: 0 10px;
    overflow-y: auto;
    border-bottom: 1px solid #efefef;
    color: #999;
    text-align: center;

    ::-webkit-scrollbar {
      display: none;
    }
  }
}
</style>
