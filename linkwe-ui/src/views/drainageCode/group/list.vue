<style lang="scss" scoped>
.mid-action {
  display: flex;
  justify-content: space-between;
  margin: 10px 0;
  align-items: center;
  .total {
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
  <div class="app-container page">
    <el-input placeholder="请输入关键字" style="width: 240px;" class="mr10" v-model="input3">
      <el-button slot="append" icon="el-icon-search"></el-button>
    </el-input>
    <el-button type="primary" icon="el-icon-plus">新建客户群活码</el-button>
    <el-button type="primary">批量下载</el-button>

    <div class="mid-action">
      <div class="total">
        客户群活码是一个可变化的群二维码，相当于一个跳转页面，当一个群快满时，会自动在跳转页面中显示另一个群的二维码，突破客户入群的限制。
        <el-popover placement="right" width="400" trigger="hover">
          <div class="notic-message">
            <p>1. 群活码并未改变微信原有规则，只是提供了一个固定不变的入口二维码、根据人数自动换群及扫码数据统计分析等功能</p>
            <p>2. 如果微信群7天没有加满200人，系统会提前一天通过服务号发送群二维码即将到期提醒，请及时更新群二维码，避免因群二维码过期无法进群</p>
            <p>3. 微信群超过200人无法扫码只能邀请进群，想实现所有人都能扫码进群，需要拆分为多个200人群，准督多个空群，每扫码200人自动换过</p>
            <p>4. 支持将活码二维码以图片方式分享朋友圈(活码以文字链接方式分享不影响)，如有需要可以绑定自有域名，或开启防护加速仅支持企业版已认证用户功能</p>
            <p>5. 根据《微信外链管理规范》，违规内容/用途、用户投诉等可能导致活码被封，且申诉有可能无法解封，具体以官方处理结果为准，无法更换活码的(例如用于印刷)，建议绑定自有域名或开启防护加速功能</p>
            <p>6. 禁止任何违法、违规用途，所有活码都有专人审核，一经发现即关闭访问、停止分配域名，情节严重将封停账户《微信外链管理规范》</p>
          </div>
          <span class="num" slot="reference">使用须知</span>
        </el-popover>
      </div>
    </div>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="样式" align="center" prop="operId" />
      <el-table-column label="活动名称" align="center" prop="title" />
      <el-table-column label="活动场景" align="center" prop="businessType" />
      <el-table-column label="实际群码数" align="center" prop="businessType" />
      <el-table-column label="扫码次数" align="center" prop="businessType" />
      <el-table-column label="创建人" align="center" prop="businessType" />
      <el-table-column label="创建时间" align="center" prop="operTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.operTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row,scope.index)"
            v-hasPermi="['monitor:operlog:query']"
          >下载</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row,scope.index)"
            v-hasPermi="['monitor:operlog:query']"
          >复制链接</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row,scope.index)"
            v-hasPermi="['monitor:operlog:query']"
          >查看详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row,scope.index)"
            v-hasPermi="['monitor:operlog:query']"
          >编辑</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row,scope.index)"
            v-hasPermi="['monitor:operlog:query']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
export default {
  name: "Operlog",
  data() {
    return {
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
    };
  },
  created() {
    this.getList();
    this.getDicts("sys_oper_type").then((response) => {
      this.typeOptions = response.data;
    });
    this.getDicts("sys_common_status").then((response) => {
      this.statusOptions = response.data;
    });
  },
  methods: {
    /** 查询登录日志 */
    getList() {
      this.loading = false;
      list(this.addDateRange(this.queryParams, this.dateRange)).then(
        (response) => {
          this.list = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    // 操作日志状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 操作日志类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.businessType);
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.operId);
      this.multiple = !selection.length;
    },
    /** 详细按钮操作 */
    handleView(row) {
      this.open = true;
      this.form = row;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const operIds = row.operId || this.ids;
      this.$confirm(
        '是否确认删除日志编号为"' + operIds + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delOperlog(operIds);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function () {});
    },
    /** 清空按钮操作 */
    handleClean() {
      this.$confirm("是否确认清空所有操作日志数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return cleanOperlog();
        })
        .then(() => {
          this.getList();
          this.msgSuccess("清空成功");
        })
        .catch(function () {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有操作日志数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportOperlog(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        })
        .catch(function () {});
    },
  },
};
</script>