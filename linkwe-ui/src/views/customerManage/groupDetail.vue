<script>
import { dictJoinGroupType } from '@/utils/dictionary'
import * as api from '@/api/customer/group'
import SelectTag from '@/components/SelectTag'

export default {
  // name: 'GroupDetail',
  components: { SelectTag },
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
      dictJoinGroupType,
      dialogVisible: false,
      selectedTag: [],
      tagDialogType: {}
    }
  },
  created() {
    this.group = this.$route.query
    this.query.chatId = this.group.chatId
    // this.getDetail()
    this.getList()
  },
  methods: {
    getDetail() {
      api.getDetail(this.query.chatId).then((res) => {
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
    makeTag() {
      let data = this.group
      this.tagDialogType.type = ''
      let curTags = []
      if (data.tags && data.tagIds) {
        curTags = data.tags.map((i, idx) => ({
          name: i,
          tagName: i,
          tagId: data.tagIds[idx]
        }))
      }
      this.selectedTag = curTags
      this.dialogVisible = true
    },
    submitSelectTag(data) {
      this.loading = true
      api
        .makeGroupTag(this, {
          chatId: this.query.chatId,
          weeGroupTagRel: data.map((row) => ({
            chatId: this.query.chatId,
            tagId: row.tagId
          }))
        })
        .then((res) => {
          if (res.code == 200) {
            this.loading = false
            // this.getGroupDetail()
            this.msgSuccess('操作成功')
          }
        })
        .finally(() => {
          this.loading = false
          this.dialogVisible = false
        })
    }
  }
}
</script>

<template>
  <div v-loading="loading">
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
            <div class="info" style="flex: 1;">
              <span class="">客群标签：</span>
              <div style="display:inline-flex;">
                <template v-if="group.tags">
                  <el-tag class="" v-for="(tag, index) in group.tags" :key="index">{{
                    tag
                  }}</el-tag>
                </template>
              </div>
              <el-button
                type="text"
                v-hasPermi="['customerManage/customer:makeTag']"
                @click="makeTag()"
                >编辑标签</el-button
              >
            </div>
          </div>
        </div>

        <div class="flex aic ac mt10 overview">
          <div>
            <div class="value">
              {{ group.groupLeaderName }}
            </div>
            <div class="key">群主</div>
          </div>
          <span class="line"></span>
          <div>
            <div class="value">
              {{ group.memberNum || 0 }}
            </div>
            <div class="key">群总人数</div>
          </div>
          <span class="line"></span>
          <div>
            <div class="value">
              {{ group.customerNum || 0 }}
            </div>
            <div class="key">客户总数</div>
          </div>
          <span class="line"></span>
          <div>
            <div class="value">
              {{ group.joinGroupMemberNum || 0 }}
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
          <!-- 为更好的服务广大客户，我司将于2021年5月9日晚22:00至22:30进行系统升级维护。敬请您妥善安排业务办理时间。由此给您带来的不便，我们深表歉意，并将尽快恢复对您的服务，衷心感谢您对我行的理解和支持！ -->
        </div>
      </div>
    </div>

    <el-input class="mt20" placeholder="请输入群成员" v-model="query.name" clearable>
      <el-button type="primary" slot="append" @click="getList(1)">
        <span>查询</span>
      </el-button>
    </el-input>
    <el-table :data="list" @selection-change="handleSelectionChange">
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
          <span>{{ dictJoinGroupType[scope.row.joinScene] }}</span>
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

    <!-- 选择标签弹窗 -->
    <SelectTag
      ref="selectTag"
      type="2"
      :visible.sync="dialogVisible"
      :title="tagDialogType.title"
      :defaultValues="selectedTag"
      @success="submitSelectTag"
    >
    </SelectTag>
  </div>
</template>

<style lang="scss" scoped>
.info-wrap {
  margin-left: 20px;
  .info {
    color: #666;
  }
}
.overview {
  justify-content: space-around;
  line-height: 30px;
  .key {
    color: #999;
  }
  .value {
    color: #333;
    font-weight: 600;
    font-size: 16px;
  }
  .line {
    width: 1px;
    height: 30px;
    background: #ddd;
  }
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
</style>
