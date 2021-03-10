<script>
import {
  getList,
  remove,
  download,
  downloadBatch,
} from '@/api/communityOperating/newCustomer'

export default {
  components: {},
  props: {},
  data() {
    return {
      query: {
        pageNum: 1,
        pageSize: 10,
        empleCodeName: '',
        createBy: '',
        beginTime: '', // "开始时间",
        endTime: '', // "结束时间"
      },
      dateRange: [], // 添加日期
      total: 0,
      form: {},
      list: [],
      dialogVisible: false,
      disabled: false,
      loading: false,
      status: ['正常', '停用'],
      pushType: {
        0: '发给客户',
        1: '发给客户群',
      },
      queryUser: [], // 搜索框选择的添加人
      ids: [],
    }
  },
  watch: {},
  computed: {},
  created() {
    this.getList()
  },
  mounted() {
    // new clipboard(".copy-btn");
  },
  methods: {
    getList(page) {
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
        })
        .catch(() => {
          this.loading = false
        })
    },
    edit(data, type) {
      this.form = Object.assign({}, data || {})
      this.dialogVisible = true
      type || !data ? (this.disabled = false) : (this.disabled = true)
    },
    goRoute(id) {
      this.$router.push({
        path: '/communityOperating/newCustomerAev',
        query: { id },
      })
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id)
    },
    /** 删除按钮操作 */
    remove(id) {
      const ids = id || this.ids
      this.$confirm('是否确认删除?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(function() {
          return remove(ids)
        })
        .then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        })
        .catch(function() {})
    },
    download(data) {
      let name = data.activityScene + '-' + data.newGroupId + '.png'
      download(id).then((res) => {
        if (res != null) {
          let blob = new Blob([res], { type: 'application/zip' })
          let url = window.URL.createObjectURL(blob)
          const link = document.createElement('a') // 创建a标签
          link.href = url
          link.download = name // 重命名文件
          link.click()
          URL.revokeObjectURL(url) // 释放内存
        }
      })
    },
    /** 批量下载 */
    downloadBatch(qrCode) {
      this.$confirm('是否确认下载所有图片吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          return downloadBatch(this.ids + '')
          // window.open(downloadBatch(this.ids))
        })
        .then((res) => {
          if (res != null) {
            let blob = new Blob([res], { type: 'application/zip' })
            let url = window.URL.createObjectURL(blob)
            const link = document.createElement('a') // 创建a标签
            link.href = url
            link.download = '批量员工活码.zip' // 重命名文件
            link.click()
            URL.revokeObjectURL(url) // 释放内存
          }
        })
        .catch(function() {})
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
      label-width="80px"
      class="top-search"
      size="small"
    >
      <el-form-item label="活码名称">
        <el-input v-model="query.empleCodeName" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="创建人">
        <el-input v-model="query.createBy" placeholder="请输入"></el-input>
        <!-- <div class="tag-input" @click="dialogVisibleSelectUser = true">
          <span class="tag-place" v-if="!queryUser.length">请选择</span>
          <template v-else>
            <el-tag
              type="info"
              v-for="(unit, unique) in queryUser"
              :key="unique"
              >{{ unit.name }}</el-tag
            >
          </template>
        </div> -->
      </el-form-item>
      <el-form-item label="创建时间">
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
      <el-form-item label="">
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
      </el-form-item>
    </el-form>

    <div class="fxbw mb10 aic">
      <div class="total">
        <el-button type="primary" @click="goRoute()">新建自动拉群</el-button>
        <!-- 新客自动拉群 -->
        <el-tooltip
          effect="light"
          content="客户通过员工活码添加员工，自动发送入群引导语、群活码，客户扫码入群。"
          placement="top-start"
        >
          <i
            class="el-icon-question"
            style="font-size: 26px;vertical-align: middle; margin-left: 10px;"
          ></i>
        </el-tooltip>
      </div>
      <div>
        <el-button type="primary" @click="download()">批量下载</el-button>
        <el-button
          v-hasPermi="['customerManage:customer:export']"
          type="cyan"
          @click="remove()"
          >批量删除</el-button
        >
      </div>
    </div>
    <!-- <el-card shadow="never" :body-style="{padding: '20px 0 0'}">
    </el-card>-->

    <el-table
      v-loading="loading"
      :data="list"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column prop="activityScene" label="活码名称" align="center">
      </el-table-column>
      <el-table-column label="员工活码" align="center" prop="qrCode">
        <template slot-scope="{ row }">
          <el-image v-if="row.qrCode" :src="row.qrCode" class="code-image">
          </el-image>
        </template>
      </el-table-column>
      <el-table-column
        prop="weEmpleCodeUseScops"
        label="使用员工"
        align="center"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="{ row }">
          <el-tag
            size="medium"
            type="info"
            v-for="(item, index) in row.weEmpleCodeUseScops"
            :key="index"
            >{{ item.businessName }}</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column
        label="客户标签"
        align="center"
        width="160"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="{ row }">
          <el-tag
            size="medium"
            type="info"
            v-for="(item, index) in row.weEmpleCodeTags"
            :key="index"
            >{{ item.tagName }}</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column
        label="实际群聊"
        align="center"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="{ row }">
          <el-tag
            size="medium"
            v-for="(item, index) in row.weGroupUserScops"
            :key="index"
            >{{ item.chatGroupName }}</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column
        label="添加好友数"
        align="center"
        prop="joinFriendNums"
      ></el-table-column>
      <el-table-column
        label="创建人"
        align="center"
        prop="createBy"
      ></el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="160"
      ></el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="180"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="{ row }">
          <el-button
            v-hasPermi="['enterpriseWechat:edit']"
            type="text"
            @click="goRoute(row.newGroupId)"
            >编辑</el-button
          >
          <el-button
            v-hasPermi="['enterpriseWechat:view']"
            type="text"
            @click="download(row)"
            >下载</el-button
          >
          <el-button
            v-hasPermi="['enterpriseWechat:edit']"
            type="text"
            @click="remove(row.newGroupId)"
            >删除</el-button
          >
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
