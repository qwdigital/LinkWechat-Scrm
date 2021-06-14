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
        memberName: undefined,
      },
      joinScene: {
        1: '由成员邀请入群（直接邀请入群）',
        2: '由成员邀请入群（通过邀请链接入群）',
        3: '通过扫描群二维码入群',
      },
    }
  },
  created() {
    this.group = this.$route.query
    this.query.chatId = this.group.chatId
    this.getList()
  },
  methods: {
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
  },
}
</script>

<template>
  <div class="app-container">
    <!-- <el-button slot="append" circle icon="el-icon-back" @click="$router.back()"></el-button>返回 -->
    <div class="flex aic">
      <el-avatar
        :size="50"
        style="flex: none;"
        src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
      ></el-avatar>
      <div class="info-wrap">
        <div style="margin-bottom: 15px; font-weight: 500;">
          {{ group.groupName }}
        </div>
        <div class="info">
          <span class="key">群主：</span>{{ group.groupLeaderName }}
          <span class="line">|</span>
          <span class="key">创建时间：</span>{{ group.createTime }}
          <span class="line">|</span>
          <span class="key">群公告：</span>{{ group.notice || '未设置' }}
        </div>
      </div>
    </div>
    <el-input
      size="normal"
      placeholder="请输入群成员"
      v-model="query.memberName"
      class
    >
      <el-button type="primary" slot="append" @click="getList(1)">
        <span class="key">查询</span>
      </el-button>
    </el-input>
    <el-table
      v-loading="loading"
      :data="list"
      @selection-change="handleSelectionChange"
    >
      <el-table-column width="55" />
      <el-table-column label="群成员" prop="memberName">
        <template slot-scope="scope">
          {{ scope.row.memberName }}
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
.el-input {
  display: table;
  width: 30%;
  margin: 40px auto;
}
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
</style>
