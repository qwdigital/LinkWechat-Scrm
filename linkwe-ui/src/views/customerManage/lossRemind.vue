<script>
import { getListNew, exportCustomer, lossRemind, getLossRemindStatus } from '@/api/customer'
// import { getList as getListTag } from '@/api/customer/tag'
// import { getList as getListOrganization } from '@/api/organization'
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
        delFlag: 1
      },
      queryTag: [], // 搜索框选择的标签
      queryUser: [], // 搜索框选择的添加人
      dateRange: [], // 添加日期
      loading: false,
      isMoreFilter: false,
      total: 0,
      // 添加标签表单
      form: {
        gourpName: '',
        weTags: []
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
        type: '' // 弹窗类型
      },
      isNotice: '0'
    }
  },
  watch: {},
  computed: {},
  created() {
    this.getList()
    // this.getListTag()
    // this.getListOrganization()
    this.getLossRemindStatus()

    this.$store.dispatch(
      'app/setBusininessDesc',
      `
        <div>当企业成员被客户删除时，会在流失列表中产生一条记录，开启删除通知后，被删除的成员会收到一条推送</div>
      `
    )
  },
  mounted() {},
  methods: {
    getList(page) {
      // console.log(this.dateRange);
      if (this.dateRange) {
        this.query.beginTime = this.dateRange[0]
        this.query.endTime = this.dateRange[1]
      } else {
        this.query.beginTime = ''
        this.query.endTime = ''
      }
      page && (this.query.pageNum = page)
      this.loading = true
      getListNew(this.query)
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
    // getListTag() {
    //   getListTag().then(({ rows }) => {
    //     this.listTagOneArray = []
    //     rows.forEach((element) => {
    //       element.weTags.forEach((d) => {
    //         this.listTagOneArray.push(d)
    //       })
    //     })
    //   })
    // },
    // getListOrganization() {
    //   getListOrganization().then(({ rows }) => {
    //     this.listOrganization = Object.freeze(rows)
    //   })
    // },
    getLossRemindStatus() {
      getLossRemindStatus().then(({ data }) => {
        this.isNotice = data
      })
    },
    showTagDialog() {
      this.selectedTag = this.queryTag
      this.tagDialogType = {
        title: '选择标签',
        type: 'query'
      }
      this.dialogVisible = true
    },
    // /** 导出按钮操作 */
    // exportCustomer() {
    //   const queryParams = this.query
    //   this.$confirm('是否确认导出所有客户数据项?', '警告', {
    //     confirmButtonText: '确定',
    //     cancelButtonText: '取消',
    //     type: 'warning'
    //   })
    //     .then(function() {
    //       return exportCustomer(queryParams)
    //     })
    //     .then((response) => {
    //       this.download(response.msg)
    //     })
    //     .catch(function() {})
    // },
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
    }
  }
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
      <el-form-item label="客户名称" prop="name">
        <el-input v-model="query.name" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="添加人">
        <div class="tag-input" @click="dialogVisibleSelectUser = true">
          <span class="tag-place" v-if="!queryUser.length">请选择</span>
          <template v-else>
            <el-tag type="info" v-for="(unit, unique) in queryUser" :key="unique">{{
              unit.name
            }}</el-tag>
          </template>
        </div>
      </el-form-item>
      <el-form-item label="添加日期">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          value-format="yyyy-MM-dd"
          :picker-options="pickerOptions"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          align="right"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="标签">
        <div class="tag-input" @click="showTagDialog">
          <span class="tag-place" v-if="!queryTag.length">请选择</span>
          <template v-else>
            <el-tag type="info" v-for="(unit, unique) in queryTag" :key="unique">{{
              unit.name
            }}</el-tag>
          </template>
        </div>
      </el-form-item>
      <el-form-item label=" ">
        <el-button v-hasPermi="['customerManage:customer:query']" type="primary" @click="getList(1)"
          >查询</el-button
        >
        <el-button
          v-hasPermi="['customerManage:customer:query']"
          type="success"
          @click="resetForm()"
          >重置</el-button
        >
        <!-- <el-button
          v-hasPermi="['customerManage:customer:export']"
          type="info"
          @click="exportCustomer"
          >导出列表</el-button
        > -->
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
      </div>
    </div>

    <el-table
      v-loading="loading"
      ref="multipleTable"
      :data="list"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" align="center" width="55"></el-table-column>
      <el-table-column label="客户" prop="name" align="center">
        <template slot-scope="scope">
          {{ scope.row.customerName }}
          <!-- <span :style="{ color: scope.row.type === 1 ? '#4bde03' : '#f9a90b' }">{{
            { 1: '@微信', 2: '@企业微信' }[scope.row.type]
          }}</span>
          <i :class="['el-icon-s-custom', { 1: 'man', 2: 'woman' }[scope.row.gender]]"></i> -->
        </template>
      </el-table-column>
      <!-- <el-table-column prop="corpName" label="公司名称" align="center"></el-table-column> -->
      <el-table-column prop="userName" label="添加人（首位）" align="center">
        <!-- <template slot-scope="scope">{{ scope.row.weFlowerCustomerRels[0].userName }}</template> -->
      </el-table-column>
      <el-table-column prop="firstAddTime" label="添加时间" align="center">
        <!-- <template slot-scope="scope">{{ scope.row.weFlowerCustomerRels[0].createTime }}</template> -->
      </el-table-column>
      <el-table-column prop="address" label="标签" align="center">
        <template slot-scope="{ row }" v-if="row.tagNames">
          <el-tag type="info" v-for="(unit, unique) in row.tagNames.split(',')" :key="unique">{{
            unit
          }}</el-tag>

          <!-- <div v-for="(item, index) in row.weFlowerCustomerRels" :key="index">
            <el-tag type="info" v-for="(unit, unique) in item.weFlowerCustomerTagRels" :key="unique">{{
              unit.tagName
            }}</el-tag>
          </div> -->
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="{ row }">
          <el-button
            v-hasPermi="['customerManage:customer:view']"
            @click="
              $router.push({
                path: 'customerDetail',
                query: {
                  externalUserid: row.externalUserid,
                  userId: row.firstUserId,
                  delFlag: row.delFlag
                }
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
