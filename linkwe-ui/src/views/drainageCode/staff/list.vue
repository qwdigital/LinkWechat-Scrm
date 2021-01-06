<script>
import {
  getList,
  remove,
  batchAdd,
  downloadBatch,
  download,
} from '@/api/drainageCode/staff'
import SelectUser from '@/components/SelectUser'
import ClipboardJS from 'clipboard'
export default {
  name: 'CodeStaff',
  components: { SelectUser },
  data() {
    return {
      // 查询参数
      query: {
        pageNum: 1,
        pageSize: 10,
        useUserName: undefined,
        mobile: undefined,
        activityScene: undefined,
        createBy: undefined,
        beginTime: undefined,
        endTime: undefined,
      },
      // 日期范围
      dateRange: [],
      dialogVisible: false,
      // 遮罩层
      loading: false,
      // 选中数组
      ids: [],
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 表单参数
      form: {
        codeType: 3,
        qrcode: '',
        isJoinConfirmFriends: 0,
        weEmpleCodeTags: [],
        weEmpleCodeUseScops: [],
      },
    }
  },
  created() {
    this.getList()
  },
  mounted() {
    var clipboard = new ClipboardJS('.copy-btn')
    clipboard.on('success', (e) => {
      this.$notify({
        title: '成功',
        message: '链接已复制到剪切板，可粘贴。',
        type: 'success',
      })
    })
    clipboard.on('error', (e) => {
      this.$message.error('链接复制失败')
    })
  },
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
          this.ids = []
        })
        .catch(() => {
          this.loading = false
        })
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.$refs['queryForm'].resetFields()
      this.getList(1)
    },
    goRoute(path, id) {
      this.$router.push({ path: '/drainageCode/' + path, query: { id } })
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
    // 选择人员变化事件
    selectedUser(users) {
      this.form.weEmpleCodeUseScops = users.map((d) => {
        return {
          businessId: d.id || d.userId,
          businessName: d.name,
          businessIdType: d.userId ? 2 : 1,
        }
      })
      batchAdd(this.form).then(({ data }) => {
        this.msgSuccess('操作成功')
        this.getList(1)
      })
    },
    download(id, userName, activityScene) {
      let name = userName + '-' + activityScene + '.png'
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
    /** 下载 */
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
      :model="query"
      ref="queryForm"
      :inline="true"
      label-width="100px"
      class="top-search"
    >
      <el-form-item label="使用员工" prop="useUserName">
        <el-input
          v-model="query.useUserName"
          placeholder="请输入"
          clearable
          @keyup.enter.native="getList(1)"
        />
      </el-form-item>
      <!-- <el-form-item label="姓名">
        <el-input
          v-model="query.operName"
          placeholder="请输入"
          clearable
          @keyup.enter.native="getList(1)"
        />
      </el-form-item> -->
      <el-form-item label="手机号" prop="mobile">
        <el-input
          v-model="query.mobile"
          placeholder="请输入"
          clearable
          @keyup.enter.native="getList(1)"
        />
      </el-form-item>
      <el-form-item label="活动场景" prop="activityScene">
        <el-input
          v-model="query.activityScene"
          placeholder="请输入"
          clearable
          @keyup.enter.native="getList(1)"
        />
      </el-form-item>
      <el-form-item label="创建人" prop="createBy">
        <el-input
          v-model="query.createBy"
          placeholder="请输入"
          clearable
          @keyup.enter.native="getList(1)"
        />
      </el-form-item>
      <el-form-item label="创建日期">
        <el-date-picker
          v-model="dateRange"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label=" ">
        <el-button type="cyan" @click="getList(1)">查询</el-button>
        <el-button @click="resetQuery">重置</el-button>
        <!-- <el-button @click="resetQuery">导出</el-button> -->
      </el-form-item>
    </el-form>

    <div class="mid-action">
      <div class="total">
        己选
        <span class="num">{{ total }}</span> 条，当前已经配置
        <span class="num">{{ total }}</span> 个 最多配置
        <span class="num">{{ total }}</span> 个
      </div>
      <div>
        <el-button type="primary" size="mini" @click="goRoute('staffAdd')"
          >新建员工活码</el-button
        >
        <el-button type="primary" size="mini" @click="dialogVisible = true"
          >批量新建</el-button
        >
        <el-button type="primary" size="mini" @click="remove">删除</el-button>
        <el-button type="primary" size="mini" @click="downloadBatch()"
          >批量下载</el-button
        >
      </div>
    </div>

    <el-table
      v-loading="loading"
      :data="list"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="样式" align="center" prop="qrCode">
        <template slot-scope="{ row }">
          <el-image
            :src="row.qrCode"
            fit="fit"
            :preview-src-list="[row.qrCode]"
            style="width: 100px; height: 100px"
          ></el-image>
        </template>
      </el-table-column>
      <!-- <el-table-column label="使用员工" align="center" prop="useUserName" /> -->
      <el-table-column
        label="姓名"
        align="center"
        prop="useUserName"
        show-overflow-tooltip
      />
      <el-table-column
        label="手机号"
        align="center"
        prop="mobile"
        show-overflow-tooltip
      />
      <el-table-column
        label="活动场景"
        align="center"
        prop="activityScene"
        show-overflow-tooltip
      />
      <el-table-column label="创建人" align="center" prop="createBy" />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
      >
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="{ row }">
          <el-button
            type="text"
            @click="download(row.id, row.useUserName, row.activityScene)"
            v-hasPermi="['monitor:operlog:query']"
            >下载</el-button
          >
          <el-button
            type="text"
            class="copy-btn"
            :data-clipboard-text="row.qrCode"
            v-hasPermi="['monitor:operlog:query']"
            >复制链接</el-button
          >
          <el-button
            type="text"
            @click="goRoute('staffDetail', row.id)"
            v-hasPermi="['monitor:operlog:query']"
            >查看详情</el-button
          >
          <el-button
            type="text"
            @click="goRoute('staffAdd', row.id)"
            v-hasPermi="['monitor:operlog:query']"
            >编辑</el-button
          >
          <el-button
            type="text"
            @click="remove(row.id)"
            v-hasPermi="['monitor:operlog:query']"
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

    <!-- 批量新建弹窗 -->
    <SelectUser
      :visible.sync="dialogVisible"
      title="组织架构"
      @success="selectedUser"
    ></SelectUser>
  </div>
</template>

<style lang="scss" scoped>
.mid-action {
  display: flex;
  justify-content: space-between;
  margin: 10px 0;
  align-items: center;
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
</style>
