<script>
import * as api from '@/api/customer'
import { getList as getListTag } from '@/api/customer/tag'
import { getList as getListOrganization } from '@/api/organization'
import AddTag from '@/components/AddTag'
import SelectUser from '@/components/SelectUser'
import SelectTag from '@/components/SelectTag'

export default {
  name: 'Customer',
  components: { AddTag, SelectUser, SelectTag },
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
        endTime: '' // "结束时间"
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
      dialogVisibleAddTag: false, // 添加标签弹窗显隐
      selectedTag: [], // 选择的标签
      tagDialogType: {
        title: '', // 选择标签弹窗标题
        type: '' // 弹窗类型
      },
      dictCustomerType: { 1: '微信客户', 2: '企业客户' },
      dictAddType: { 1: '员工活码', 2: '新客拉群', 2: '活动裂变', 2: '手机号' },
      dictStatus: [
        { name: '待跟进', code: 0, color: '' },
        { name: '跟进中', code: 0, color: '' },
        { name: '已成交', code: 0, color: '' },
        { name: '无意向', code: 0, color: '' },
        { name: '已流失', code: 0, color: '' }
      ]
    }
  },
  watch: {},
  computed: {},
  created() {
    this.getList()
    this.getListTag()
    this.getListOrganization()

    this.$store.dispatch(
      'app/setBusininessDesc',
      `
        <div>用于查看当前企业所有的客户列表及详细信息，支持对客户进行打标签。</div>
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
      api
        .getListNew(this.query)
        .then(({ rows, total }) => {
          rows.forEach((element) => {
            if (element.tagIds && element.tagNames) {
              element.tagIds = element.tagIds.split(',')
              element.tagNames = element.tagNames.split(',')
              element.tags = []
              element.tagIds.forEach((unit, index) => {
                element.tags.push({
                  tagId: unit,
                  tagName: element.tagNames[index]
                })
              })
            }
          })
          this.list = rows
          this.total = +total
          this.loading = false
          this.multipleSelection = []
        })
        .catch(() => {
          this.loading = false
        })
    },
    getListTag(refresh) {
      getListTag().then(({ rows }) => {
        this.listTagOneArray = []
        rows.forEach((element) => {
          element.weTags.forEach((d) => {
            this.listTagOneArray.push(d)
          })
        })
        if (refresh) {
          this.$refs.selectTag.getList()
          this.form = {
            gourpName: '',
            weTags: []
          }
        }
      })
    },
    getListOrganization() {
      getListOrganization().then(({ rows }) => {
        this.listOrganization = Object.freeze(rows)
      })
    },
    showTagDialog() {
      this.selectedTag = this.queryTag
      this.tagDialogType = {
        title: '选择标签',
        type: 'query'
      }
      this.dialogVisible = true
      // this.$refs.selectTag.$forceUpdate()
    },
    makeTag(row) {
      // if (!this.multipleSelection.length) {
      //   this.msgInfo('请选择一位客户')
      //   return
      // }
      // if (this.multipleSelection.length > 1) {
      //   this.msgInfo('同时只能选择一位客户')
      //   return
      // }
      this.selectedTag = []
      let hasErrorTag = []
      let repeat = []
      row.tags &&
        row.tags.forEach((unit) => {
          // 判断是否有重复标签
          let isRepeat = this.selectedTag.some((d) => {
            return d.tagId === unit.tagId
          })
          // 去重
          if (isRepeat) {
            repeat.push(unit.tagName)
            return
          }
          let filter = this.listTagOneArray.find((d) => {
            return d.tagId === unit.tagId
          })
          // 如果没有匹配到，则说明该便签处于异常状态，可能已被删除或破坏
          if (!filter) {
            hasErrorTag.push(unit.tagName)
            return
          }

          this.selectedTag.push(filter)
        })
      // this.multipleSelection.forEach((element) => {
      // })
      if (hasErrorTag.length > 0) {
        this.msgError('已有标签[' + hasErrorTag + ']不在标签库中，或存在异常')
        return
      }

      this.tagDialogType = {
        title: '标签设置' + (repeat.length ? '（重复的标签已去重显示）' : '')
      }
      this.dialogVisible = true
      // this.$refs.selectTag.$forceUpdate()
    },
    sync() {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      api
        .sync()
        .then(() => {
          loading.close()
          this.msgSuccess('后台开始同步数据，请稍后关注进度')
        })
        .catch((fail) => {
          loading.close()
          console.log(fail)
        })
    },
    /** 导出按钮操作 */
    exportCustomer() {
      const queryParams = this.query
      this.$confirm('是否确认导出所有客户数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function() {
          return api.exportCustomer(queryParams)
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
      } else {
        let data = {
          externalUserid: this.multipleSelection[0].externalUserid,
          addTag: selected,
          userId: this.multipleSelection[0].firstUserId
        }
        // let apiType = {
        //   add: 'makeLabel',
        //   remove: 'removeLabel'
        // }
        api.makeLabel(data).then(() => {
          this.msgSuccess('操作成功')
          this.dialogVisible = false
          this.getList()
        })
      }
    },
    resetForm(formName) {
      this.dateRange = []
      this.queryTag = []
      this.queryUser = []
      this.$refs['queryForm'].resetFields()
      this.getList(1)
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },
    // 用于多选变单选
    handleSelection(selection, row) {
      this.$nextTick(() => {
        this.$refs.table.clearSelection()
        this.$refs.table.toggleRowSelection(row)
      })
    },
    goRoute(row) {
      this.$router.push({
        path: 'customerDetail',
        query: { data: JSON.stringify(row) }
      })
    }
  }
}
</script>

<template>
  <div>
    <el-form ref="queryForm" :inline="true" :model="query" label-width="100px" class="top-search" size="small">
      <el-form-item label="客户名称" prop="name">
        <el-input v-model="query.name" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="客户类型" prop="name">
        <el-select v-model="model" placeholder="请选择">
          <el-option v-for="(item, index) in dictCustomerType" :key="index" label="item" value="index"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="客户标签">
        <div class="tag-input" @click="showTagDialog">
          <span class="tag-place" v-if="!queryTag.length">请选择</span>
          <template v-else>
            <el-tag type="info" v-for="(unit, unique) in queryTag" :key="unique">{{ unit.name }}</el-tag>
          </template>
        </div>
      </el-form-item>
      <el-form-item label="跟进员工">
        <div class="tag-input" @click="dialogVisibleSelectUser = true">
          <span class="tag-place" v-if="!queryUser.length">请选择</span>
          <template v-else>
            <el-tag type="info" v-for="(unit, unique) in queryUser" :key="unique">{{ unit.name }}</el-tag>
          </template>
        </div>
      </el-form-item>
      <el-form-item label="跟进状态" prop="name">
        <el-select v-model="model" placeholder="请选择">
          <el-option v-for="(item, index) in dictStatus" :key="index" :label="item.name" :value="item.code"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="添加方式">
        <el-select v-model="model" placeholder="请选择">
          <el-option v-for="(item, index) in dictAddType" :key="index" :label="item" :value="index"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="添加日期">
        <el-date-picker
          v-model="dateRange"
          value-format="yyyy-MM-dd"
          type="daterange"
          :picker-options="pickerOptions"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          align="right"
        ></el-date-picker>
      </el-form-item>

      <el-form-item label=" ">
        <el-button v-hasPermi="['customerManage:customer:query']" type="primary" @click="getList(1)">查询</el-button>
        <el-button v-hasPermi="['customerManage:customer:query']" type="success" @click="resetForm()">重置</el-button>
        <el-button v-hasPermi="['customerManage:customer:export']" type="info" @click="exportCustomer"
          >导出列表</el-button
        >
      </el-form-item>
    </el-form>

    <div class="mid-action">
      <div>
        <!-- 共
        <span class="num">{{ total }}</span> 位客户，实际客户
        <span class="num">{{ total }}</span> 位。 -->
        <el-button v-hasPermi="['customerManage:customer:sync']" v-preventReClick type="primary" @click="sync"
          >同步客户</el-button
        >
        <!-- <el-button v-hasPermi="['customerManage:customer:checkRepeat']" type="primary">查看重复客户</el-button> -->
        <span>
          最近同步：2021-05-17 15:05:43
        </span>
      </div>
    </div>

    <div>客户总数(去重)：234</div>

    <el-table
      ref="table"
      :data="list"
      tooltip-effect="dark"
      highlight-current-row
      @select="handleSelection"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" align="center" width="55"> </el-table-column>
      <el-table-column label="客户" prop="name" align="center">
        <template slot-scope="{ row }">
          <div class="cp flex aic" @click="goRoute(row)">
            <el-image style="width: 50px; height: 50px;" :src="url" fit="fit"></el-image>
            <div class="ml10">
              {{ row.customerName }}
              <!-- <span :style="{ color: row.type === 1 ? '#4bde03' : '#f9a90b' }">{{
              { 1: '@微信', 2: '@企业微信' }[row.type]
            }}</span>
            <i :class="['el-icon-s-custom', { 1: 'man', 2: 'woman' }[row.gender]]"></i> -->
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="tagNames" label="客户标签" align="center">
        <div v-if="row.tagNames" slot-scope="{ row }">
          <el-tag type="info" v-for="(unit, unique) in row.tagNames" :key="unique">{{ unit }}</el-tag>
          <!-- <div v-for="(item, index) in row.weFlowerCustomerRels" :key="index">
            <el-tag type="info" v-for="(unit, unique) in item.weFlowerCustomerTagRels" :key="unique">{{
              unit.tagName
            }}</el-tag>
          </div> -->
        </div>
        <span v-else>无标签</span>
      </el-table-column>
      <!-- <el-table-column prop="corpName" label="公司名称" align="center"></el-table-column> -->
      <el-table-column prop="userName" label="跟进员工" align="center">
        <!-- <template slot-scope="{ row }">{{
          row.weFlowerCustomerRels[0] ? row.weFlowerCustomerRels[0].userName : ''
        }}</template> -->
      </el-table-column>
      <el-table-column prop="firstAddTime" label="跟进状态" align="center">
        <template slot-scope="{ row }">
          <el-tag type="success">{{}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="firstAddTime" label="添加方式" align="center">
        <!-- <template slot-scope="{ row }">{{
          row.weFlowerCustomerRels[0] ? row.weFlowerCustomerRels[0].createTime : ''
        }}</template> -->
      </el-table-column>
      <el-table-column prop="firstAddTime" label="添加时间" align="center">
        <!-- <template slot-scope="{ row }">{{
          row.weFlowerCustomerRels[0] ? row.weFlowerCustomerRels[0].createTime : ''
        }}</template> -->
      </el-table-column>

      <el-table-column label="操作" width="160" align="center">
        <template slot-scope="{ row }">
          <el-button v-hasPermi="['customerManage:customer:view']" @click="goRoute(row)" type="text" size="small"
            >查看</el-button
          >
          <el-button v-hasPermi="['customerManage/customer:makeTag']" type="text" @click="makeTag(row)"
            >标签管理</el-button
          >
          <el-button type="text" size="small">在职继承 </el-button>
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
      :visible.sync="dialogVisible"
      :title="tagDialogType.title"
      :selected="selectedTag"
      @success="submitSelectTag"
    >
      <el-button class="ml20" type="primary" @click="dialogVisibleAddTag = true">添加标签</el-button>
    </SelectTag>

    <!-- 选择添加人弹窗 -->
    <SelectUser :visible.sync="dialogVisibleSelectUser" title="选择添加人" @success="selectedUser"></SelectUser>

    <!-- 添加标签弹窗 -->
    <AddTag :visible.sync="dialogVisibleAddTag" :form="form" @success="getListTag(true)" />
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
