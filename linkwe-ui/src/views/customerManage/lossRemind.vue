<script>
import {
  getList,
  exportCustomer,
  lossRemind,
  getLossRemindStatus,
} from '@/api/customer'
import { getList as getListTag } from '@/api/customer/tag'
import { getList as getListOrganization } from '@/api/organization'
import SelectUser from '@/components/SelectUser'
import SelectTag from '@/components/SelectTag'

export default {
  name: 'LossRemind',
  components: { SelectUser, SelectTag },
  props: {},
  data() {
    return {
      query: {
        pageNum: 1,
        pageSize: 10,
        name: '', // "客户名称",
        userIds: '', // "添加人id",
        tagIds: '', // "标签id,多个标签，id使用逗号隔开",
        beginTime: '', // "开始时间",
        endTime: '', // "结束时间"
        status: 1,
      },
      queryTag: [], // 搜索框选择的标签
      queryUser: [], // 搜索框选择的添加人
      dateRange: [], // 添加日期
      // 日期快捷选项
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now()
        },
        shortcuts: [
          {
            text: '最近一周',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
              picker.$emit('pick', [start, end])
            },
          },
          {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
              picker.$emit('pick', [start, end])
            },
          },
          {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
              picker.$emit('pick', [start, end])
            },
          },
        ],
      },
      loading: false,
      isMoreFilter: false,
      total: 0,
      // 添加标签表单
      form: {
        gourpName: '',
        weTags: [],
      },
      list: [], // 客户列表
      listOrganization: [], // 组织架构列表
      multipleSelection: [], // 多选数组
      dialogVisible: false, // 选择标签弹窗显隐
      dialogVisibleSelectUser: false, // 选择添加人弹窗显隐
      selectedGroup: '', // 选择的标签分组
      selectedTag: [], // 选择的标签
      removeTag: [], // 可移除的标签
      tagDialogType: {
        title: '', // 选择标签弹窗标题
        type: '', // 弹窗类型
      },
      isNotice: '0',
    }
  },
  watch: {},
  computed: {},
  created() {
    this.getList()
    this.getListTag()
    this.getListOrganization()
    this.getLossRemindStatus()
  },
  mounted() {},
  methods: {
    getList(page) {
      // console.log(this.dateRange);
      if (this.dateRange[0]) {
        this.query.beginTime = this.dateRange[0]
        this.query.endTime = this.dateRange[1]
      } else {
        this.query.beginTime = ''
        this.query.endTime = ''
      }
      page && (this.query.pageNum = page)
      this.loading = true
      getList(this.query)
        .then(({ rows, total }) => {
          this.list = rows
          this.total = +total
          this.loading = false
          this.multipleSelection = []
        })
        .catch(() => {
          this.loading = false
        })
    },
    getListTag() {
      getListTag().then(({ rows }) => {
        this.listTagOneArray = []
        rows.forEach((element) => {
          element.weTags.forEach((d) => {
            this.listTagOneArray.push(d)
          })
        })
      })
    },
    getListOrganization() {
      getListOrganization().then(({ rows }) => {
        this.listOrganization = Object.freeze(rows)
      })
    },
    getLossRemindStatus() {
      getLossRemindStatus().then(({ data }) => {
        this.isNotice = data
      })
    },
    showTagDialog() {
      this.selectedTag = this.queryTag
      this.tagDialogType = {
        title: '选择标签',
        type: 'query',
      }
      this.dialogVisible = true
    },
    /** 导出按钮操作 */
    exportCustomer() {
      const queryParams = this.query
      this.$confirm('是否确认导出所有客户数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(function() {
          return exportCustomer(queryParams)
        })
        .then((response) => {
          this.download(response.msg)
        })
        .catch(function() {})
    },
    selectedUser(list) {
      this.queryUser = list
      this.query.userIds = list.map((d) => d.userId) + ''
    },
    submitSelectTag(selected) {
      if (this.tagDialogType.type === 'query') {
        this.query.tagIds = selected.map((d) => d.tagId) + ''
        // debugger;
        this.queryTag = selected
        this.dialogVisible = false
      }
    },
    resetForm(formName) {
      this.dateRange = []
      this.queryTag = []
      this.queryUser = []
      this.$refs['queryForm'].resetFields()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },
    // 流失提醒开关事件
    remindSwitch(val) {
      lossRemind(val).then(() => {
        this.msgSuccess('操作成功')
      })
    },
  },
}
</script>

<template>
  <div>
    <el-form
      ref="queryForm"
      :inline="true"
      :model="query"
      label-width="100px"
      class="top-search"
      size="small"
    >
      <el-form-item label="客户名称">
        <el-input v-model="query.name" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="添加人">
        <div class="tag-input" @click="dialogVisibleSelectUser = true">
          <span class="tag-place" v-if="!queryUser.length">请选择</span>
          <template v-else>
            <el-tag
              type="info"
              v-for="(unit, unique) in queryUser"
              :key="unique"
              >{{ unit.name }}</el-tag
            >
          </template>
        </div>
      </el-form-item>
      <el-form-item label="添加日期">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          :picker-options="pickerOptions"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          align="right"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="标签">
        <div class="tag-input" @click="showTagDialog">
          <span class="tag-place" v-if="!queryTag.length">请选择</span>
          <template v-else>
            <el-tag
              type="info"
              v-for="(unit, unique) in queryTag"
              :key="unique"
              >{{ unit.name }}</el-tag
            >
          </template>
        </div>
      </el-form-item>
      <el-form-item label=" ">
        <el-button
          v-hasPermi="['customerManage:customer:query']"
          type="primary"
          @click="getList(1)"
          >查询</el-button
        >
        <el-button
          v-hasPermi="['customerManage:customer:query']"
          type="info"
          @click="resetForm()"
          >重置</el-button
        >
        <el-button
          v-hasPermi="['customerManage:customer:export']"
          type="cyan"
          @click="exportCustomer"
          >导出列表</el-button
        >
      </el-form-item>
    </el-form>

    <div class="mid-action">
      <div class=""></div>
      <div>
        通知提醒<el-switch
          class="ml10 mr10"
          v-hasPermi="['customerManage/customer:makeTag']"
          v-model="isNotice"
          active-value="1"
          inactive-value="0"
          active-color="#1890ff"
          inactive-color="#ff4949"
          @change="remindSwitch"
        ></el-switch>
        <el-tooltip
          class="item"
          effect="dark"
          content="开启后，当员工被客户删除时，被删除的员工将收到一条消息提醒"
          placement="top-end"
        >
          <i class="el-icon-warning-outline"></i>
        </el-tooltip>
      </div>
    </div>

    <el-table
      ref="multipleTable"
      :data="list"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        type="selection"
        align="center"
        width="55"
      ></el-table-column>
      <el-table-column label="客户" prop="name" align="center">
        <template slot-scope="scope">
          {{ scope.row.name }}
          <span
            :style="{ color: scope.row.type === 1 ? '#4bde03' : '#f9a90b' }"
            >{{ { 1: '@微信', 2: '@企业微信' }[scope.row.type] }}</span
          >
          <i
            :class="[
              'el-icon-s-custom',
              { 1: 'man', 2: 'woman' }[scope.row.gender],
            ]"
          ></i>
        </template>
      </el-table-column>
      <el-table-column
        prop="corpName"
        label="公司名称"
        align="center"
      ></el-table-column>
      <el-table-column prop="userName" label="添加人（首位）" align="center">
        <template slot-scope="scope">{{
          scope.row.weFlowerCustomerRels[0].userName
        }}</template>
      </el-table-column>
      <el-table-column prop="createTime" label="添加时间" align="center">
        <template slot-scope="scope">{{
          scope.row.weFlowerCustomerRels[0].createTime
        }}</template>
      </el-table-column>
      <el-table-column prop="address" label="标签" align="center">
        <template slot-scope="scope">
          <div
            v-for="(item, index) in scope.row.weFlowerCustomerRels"
            :key="index"
          >
            <el-tag
              type="info"
              v-for="(unit, unique) in item.weFlowerCustomerTagRels"
              :key="unique"
              >{{ unit.tagName }}</el-tag
            >
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['customerManage:customer:view']"
            @click="
              $router.push({
                path: '/customerManage/customerDetail',
                query: { id: scope.row.externalUserid },
              })
            "
            type="text"
            size="small"
            >查看</el-button
          >
          <!-- <el-button type="text" size="small">编辑</el-button> -->
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
      :visible.sync="dialogVisible"
      :title="tagDialogType.title"
      :selected="selectedTag"
      :type="tagDialogType.type"
      @success="submitSelectTag"
    >
    </SelectTag>

    <!-- 选择添加人弹窗 -->
    <SelectUser
      :visible.sync="dialogVisibleSelectUser"
      title="选择添加人"
      @success="selectedUser"
    ></SelectUser>
  </div>
</template>

<style lang="scss" scoped>
.tag-input {
  width: 240px;
  display: flex;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  align-items: center;
  padding: 0 15px;
  overflow: hidden;
  height: 32px;
  .tag-place {
    color: #bbb;
    font-size: 14px;
  }
}
.mid-action {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  align-items: center;
  background: #fff;
  .total {
    background-color: rgba(65, 133, 244, 0.1);
    border: 1px solid rgba(65, 133, 244, 0.2);
    border-radius: 3px;
    font-size: 14px;
    min-height: 32px;
    line-height: 32px;
    padding: 0 12px;
    color: #606266;
  }
  .num {
    color: #00f;
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
.bfc-d + .bfc-d .el-checkbox:first-child {
  margin-left: 10px;
}
</style>
