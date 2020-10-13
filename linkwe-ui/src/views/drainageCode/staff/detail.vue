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
.top {
  align-items: flex-start;
}
</style>
<template>
  <div class="app-container">
    <el-card :body-style="{padding: '20px 0 0'}">
      <div>员工活码信息</div>
      <div class="flex top">
        <div>
          <img src alt />
          <div>
            <el-button type="text">下载二维码</el-button>
            <el-button type="text">复制链接</el-button>
          </div>
        </div>
        <el-form ref="form" label-width="100px">
          <el-form-item label="使用成员：">
            <el-tag type="success">春夏</el-tag>
          </el-form-item>
          <el-form-item label="活动场景：">55</el-form-item>
          <el-form-item label="类型：">单人</el-form-item>
          <el-form-item label="类型：">客户添加时无需经过确认自动成为好友</el-form-item>
          <el-form-item label="创建人：">wsscrm2019</el-form-item>
          <el-form-item label="创建时间：">2020-08-26 17:39:08</el-form-item>
        </el-form>

        <el-form ref="form" label-width="100px">
          <el-form-item label="扫码标签：">客户添加时无需经过确认自动成为好友</el-form-item>
          <el-form-item label="欢迎语：">wsscrm2019</el-form-item>
        </el-form>
      </div>
    </el-card>

    <el-card shadow="never">
      <div>扫码人数</div>
      <div>累计总人数：0</div>
      <div>
        <el-button-group>
          <el-button type="primary">近7日</el-button>
          <el-button type="primary">近30日</el-button>
        </el-button-group>

        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </div>
      <div class="chart" ref="chart" style="width: 600px;height:400px;"></div>
    </el-card>
  </div>
</template>

<script>
import echarts from "echarts";
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
  mounted() {
    var option = {
      xAxis: {
        type: "category",
        data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
      },
      yAxis: {
        type: "value",
      },
      series: [
        {
          data: [820, 932, 901, 934, 1290, 1330, 1320],
          type: "line",
        },
      ],
    };
    console.log('ec', echarts);
    var myChart = echarts.init(this.$refs.chart);
    myChart.setOption(option);
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