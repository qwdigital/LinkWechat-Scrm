<script>
import * as api from '@/api/customer/group'

export default {
  // name: 'GroupDetail',
  data() {
    return {
      // 遮罩层
      loading: false,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 群信息
      group: {},
      // 查询参数
      query: {
        pageNum: 1,
        pageSize: 10,
        groupId: undefined,
        name: undefined
      },
      joinScene: {
        1: '由成员邀请入群（直接邀请入群）',
        2: '由成员邀请入群（通过邀请链接入群）',
        3: '通过扫描群二维码入群'
      },
      dialogVisible: false,
      selectedTag: []
    }
  },
  created() {
    this.group = this.$route.query
    this.query.chatId = this.group.chatId
    this.getDetail()
    this.getList()
  },
  methods: {
    getDetail() {
      api.getDetail(this, this.query.chatId).then((res) => {
        if (res.code == 200) {
          this.group = Object.assign({}, this.group, res.data)
          // console.log(this.group)
          this.group.createTime = moment(this.group.createTime).format('YYYY-MM-DD HH:mm:SS')
          if (this.group.tagIds) {
            let arr = this.group.tagIds.split(',')
            this.selectedTag = arr.map((dd) => {
              return { tagId: dd }
            })
          }
        }
      })
    },
    getList(page) {
      page && (this.query.pageNum = page)
      this.loading = false
      api.getMembers(this.query).then((response) => {
        this.list = response.rows
        this.total = +response.total
        this.loading = false
      })
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.operId)
      this.multiple = !selection.length
    },
    onEditTag() {
      this.dialogVisible = true
    },
    submitSelectTag(data) {
      api
        .makeGroupTag(this, {
          chatId: this.query.chatId,
          weeGroupTagRel: data
        })
        .then((res) => {
          if (res.code == 200) {
            this.getGroupDetail()
            this.msgSuccess('操作成功')
          }
        })
    }
  }
}
</script>

<template>
  <div class="app-container">
    <!-- <el-button slot="append" circle icon="el-icon-back" @click="$router.back()"></el-button>返回 -->
    <div class="flex mt10">
      <div class="left g-card g-pad20">
        <div class="flex aic">
          <el-avatar
            :size="50"
            style="flex: none;"
            src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
          >
          </el-avatar>
          <div class="info-wrap">
            <div style="margin-bottom: 15px; font-weight: 500;">
              {{ group.groupName }}
            </div>
            <div class="info"><span class="">创建时间：</span>{{ group.createTime }}</div>
            <div class="info">
              <span class="">客群标签：</span>
              <template v-if="group.tags">
                <el-tag style="margin-bottom: 10px;" v-for="(tag, index) in group.tags.split(',')" :key="index">{{
                  tag
                }}</el-tag>
              </template>
              <el-button type="text">编辑标签</el-button>
            </div>
          </div>
        </div>

        <div class="flex aic">
          <div>
            <div>
              {{ group.groupLeaderName }}
            </div>
            <div class="key">群主</div>
          </div>
          <span class="line">|</span>
          <div>
            <div>
              {{ group.memberNum }}
            </div>
            <div class="key">群总人数</div>
          </div>
          <span class="line">|</span>
          <div>
            <div>
              {{ group.customerNum }}
            </div>
            <div class="key">客户总数</div>
          </div>
          <span class="line">|</span>
          <div>
            <div>
              {{ group.joinGroupMemberNum }}
            </div>
            <div class="key">今日进群数</div>
          </div>
        </div>
      </div>

      <div class="g-card right" style="margin:0 0 0 10px;">
        <div class="g-title g-title-sub">
          群公告
        </div>
        <div style="padding:0 20px 20px;">
          {{ group.notice || '未设置' }}
          <!-- 为更好的服务广大客户，我行将于2021年5月9日晚22:00至22:30进行系统升级维护。敬请您妥善安排业务办理时间。由此给您带来的不便，我们深表歉意，并将尽快恢复对您的服务，衷心感谢您对我行的理解和支持！ -->
        </div>
      </div>
    </div>

    <el-input class="mt20" placeholder="请输入群成员" v-model="query.name" clearable>
      <el-button type="primary" slot="append" @click="getList(1)">
        <span class="key">查询</span>
      </el-button>
    </el-input>
    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column width="55" />
      <el-table-column label="群成员" prop="name">
        <template slot-scope="scope">
          {{ scope.row.name }}
          <!-- <span
            :style="{color: scope.row.joinType == 1 ? '#4bde03' : '#f9a90b'}"
          >{{ ({1: '@微信', 2: '@企业微信'})[scope.row.joinType] }}</span>-->
          <!-- <i :class="['el-icon-s-custom', ({1: 'man', 2: 'woman'})[scope.row.gender]]"></i> -->
        </template>
      </el-table-column>
      <el-table-column label="进群时间" prop="joinTime"></el-table-column>
      <el-table-column label="进群方式" prop="joinScene">
        <template slot-scope="scope">
          <span>{{ joinScene[scope.row.joinScene] }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="query.pageNum"
      :limit.sync="query.pageSize"
      @pagination="getList()"
    />
  </div>
</template>

<style lang="scss" scoped>
.info-wrap {
  margin-left: 20px;
  .info {
    color: #666;
  }

  .line {
    padding: 15px;
    color: #dbdbdb;
  }
}
.key {
  color: $blue;
}
.left {
  width: 70%;
}
.right {
  width: 30%;
}
.el-input {
  width: 30%;
}

.card-blue {
  flex: 1;
  width: unset;
  max-width: 200px;
}
</style>
