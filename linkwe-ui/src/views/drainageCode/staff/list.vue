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
<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="100px"
    >
      <el-form-item label="使用员工" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入"
          clearable
          style="width: 240px;"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="姓名" prop="operName">
        <el-input
          v-model="queryParams.operName"
          placeholder="请输入"
          clearable
          style="width: 240px;"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="operName">
        <el-input
          v-model="queryParams.operName"
          placeholder="请输入"
          clearable
          style="width: 240px;"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="活动场景" prop="operName">
        <el-input
          v-model="queryParams.operName"
          placeholder="请输入"
          clearable
          style="width: 240px;"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建人" prop="operName">
        <el-input
          v-model="queryParams.operName"
          placeholder="请输入"
          clearable
          style="width: 240px;"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建日期">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label=" ">
        <el-button type="cyan" @click="handleQuery">查询</el-button>
        <el-button @click="resetQuery">重置</el-button>
        <el-button @click="resetQuery">导出</el-button>
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
        <el-button type="primary" size="mini">新建员工活码</el-button>
        <el-button type="primary" size="mini" @click="dialogVisible = true"
          >批量新建</el-button
        >
        <el-button type="primary" size="mini">删除</el-button>
        <el-button type="primary" size="mini">下载</el-button>
      </div>
    </div>

    <el-table
      v-loading="loading"
      :data="list"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="样式" align="center" prop="operId" />
      <el-table-column label="使用员工" align="center" prop="title" />
      <el-table-column label="姓名" align="center" prop="businessType" />
      <el-table-column label="手机号" align="center" prop="businessType" />
      <el-table-column label="活动场景" align="center" prop="businessType" />
      <el-table-column label="创建人" align="center" prop="businessType" />
      <el-table-column
        label="创建时间"
        align="center"
        prop="operTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.operTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row, scope.index)"
            v-hasPermi="['monitor:operlog:query']"
            >下载</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row, scope.index)"
            v-hasPermi="['monitor:operlog:query']"
            >复制链接</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row, scope.index)"
            v-hasPermi="['monitor:operlog:query']"
            >查看详情</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row, scope.index)"
            v-hasPermi="['monitor:operlog:query']"
            >编辑</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row, scope.index)"
            v-hasPermi="['monitor:operlog:query']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 批量新建弹窗 -->
    <el-dialog
      title="组织架构"
      :visible.sync="dialogVisible"
      width="width"
      :before-close="dialogBeforeClose"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-input placeholder="请输入关键字" v-model="input3" class>
            <el-button slot="append">查询</el-button>
          </el-input>
          <el-tree
            :data="treeData"
            :props="defaultProps"
            accordion
            @node-click="handleNodeClick"
          ></el-tree>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple"></div>
        </el-col>
      </el-row>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false"
          >确 定</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Operlog',
  data() {
    return {
      treeData: [
        {
          label: '一级 1',
          children: [
            {
              label: '二级 1-1',
              children: [
                {
                  label: '三级 1-1-1',
                },
              ],
            },
          ],
        },
        {
          label: '一级 2',
          children: [
            {
              label: '二级 2-1',
              children: [
                {
                  label: '三级 2-1-1',
                },
              ],
            },
            {
              label: '二级 2-2',
              children: [
                {
                  label: '三级 2-2-1',
                },
              ],
            },
          ],
        },
        {
          label: '一级 3',
          children: [
            {
              label: '二级 3-1',
              children: [
                {
                  label: '三级 3-1-1',
                },
              ],
            },
            {
              label: '二级 3-2',
              children: [
                {
                  label: '三级 3-2-1',
                },
              ],
            },
          ],
        },
      ],
      defaultProps: {
        children: 'children',
        label: 'label',
      },
      dialogVisible: false,
      // 遮罩层
      loading: false,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 是否显示弹出层
      open: false,
      // 类型数据字典
      typeOptions: [],
      // 类型数据字典
      statusOptions: [],
      // 日期范围
      dateRange: [],
      // 表单参数
      form: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        operName: undefined,
        businessType: undefined,
        status: undefined,
      },
    }
  },
  created() {
    this.getList()
    this.getDicts('sys_oper_type').then((response) => {
      this.typeOptions = response.data
    })
    this.getDicts('sys_common_status').then((response) => {
      this.statusOptions = response.data
    })
  },
  methods: {
    /** 查询登录日志 */
    getList() {
      this.loading = false
      list(this.addDateRange(this.queryParams, this.dateRange)).then(
        (response) => {
          this.list = response.rows
          this.total = response.total
          this.loading = false
        }
      )
    },
    // 操作日志状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 操作日志类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.businessType)
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.operId)
      this.multiple = !selection.length
    },
    /** 详细按钮操作 */
    handleView(row) {
      this.open = true
      this.form = row
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const operIds = row.operId || this.ids
      this.$confirm(
        '是否确认删除日志编号为"' + operIds + '"的数据项?',
        '警告',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
      )
        .then(function() {
          return delOperlog(operIds)
        })
        .then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        })
        .catch(function() {})
    },
    /** 清空按钮操作 */
    handleClean() {
      this.$confirm('是否确认清空所有操作日志数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(function() {
          return cleanOperlog()
        })
        .then(() => {
          this.getList()
          this.msgSuccess('清空成功')
        })
        .catch(function() {})
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm('是否确认导出所有操作日志数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(function() {
          return exportOperlog(queryParams)
        })
        .then((response) => {
          this.download(response.msg)
        })
        .catch(function() {})
    },
  },
}
</script>
