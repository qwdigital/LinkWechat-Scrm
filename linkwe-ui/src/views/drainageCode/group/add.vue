<style lang="scss" scoped>
.page {
  display: flex;
  justify-content: center;
  margin: 24px;
  border-radius: 4px;
  border: 1px solid #e6ebf5;
  background-color: #ffffff;
  .el-form {
    margin-right: 10%;
  }
}
.preview {
  width: 260px;
  background: #eee;
  border-radius: 8px;
  padding: 10px;
  height: 200px;
}
.tag-list-content {
  max-height: 400px;
  overflow: scroll;
}

.tag-list-content .list {
  display: flex;
  margin: 12px 0;
  font-size: 14px;
  font-weight: 400;
  cursor: pointer;
  color: #303133;
}

.tag-list-content .list .tag {
  padding: 5px 6px;
  margin-right: 8px;
  margin-bottom: 9px;
  font-size: 12px;
  border: 1px solid #dcdfe6;
  border-radius: 2px;
  background-color: #f6f6f9;
  color: #606266;
}

.tag-list-content .list .tag.select {
  color: #4185f4;
  border-color: rgba(65, 133, 244, 0.3);
  background-color: rgba(65, 133, 244, 0.1);
}
.welcome-input {
  display: table;
  width: 80%;
  margin: 0 auto 20px;
}
/deep/.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
<template>
  <div class="app-container page">
    <el-steps :active="active" finish-status="success">
      <el-step title="使用须知"></el-step>
      <el-step title="设置客户群活码基本信息"></el-step>
      <el-step title="添加实际群活码"></el-step>
      <el-step title="完成"></el-step>
    </el-steps>

    <div v-if="active === 1">
      <el-form :model="queryParams" ref="queryForm" v-show="showSearch" label-width="100px">
        <el-form-item label="活动头像" prop="title">
          <el-upload
            class="avatar-uploader"
            action
            :show-file-list="false"
            :on-success="uploadSuccess"
            :before-upload="beforeUpload"
          >
            <img v-if="imageUrl" :src="imageUrl" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>注：只能上传jpg/png格式图片，且不超过2M
        </el-form-item>
        <el-form-item label="活动名称" prop="operName">
          <el-input
            type="text"
            v-model="queryParams.operName"
            placeholder="请输入"
            clearable
            style="width: 240px;"
            size="small"
            maxlength="30"
            show-word-limit
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="活动场景" prop="operName">
          <el-input
            type="textarea"
            v-model="queryParams.operName"
            placeholder="请输入"
            clearable
            style="width: 240px;"
            size="small"
            maxlength="30"
            show-word-limit
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="引导语" prop="operName">
          <el-input
            type="textarea"
            v-model="queryParams.operName"
            placeholder="请输入"
            clearable
            style="width: 240px;"
            size="small"
            maxlength="30"
            show-word-limit
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="无法进群提示" prop="operName">
          <el-switch v-model="model" active-color="#13ce66" inactive-color="#ff4949"></el-switch>开启后，将在跳转页面底部显示无法进群的提示按钮
        </el-form-item>
        <template>
          <el-form-item label="提示语" prop="operName">
            <el-input
              type="text"
              v-model="queryParams.operName"
              placeholder="请输入"
              clearable
              style="width: 240px;"
              size="small"
              maxlength="30"
              show-word-limit
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="客服二维码" prop="title">
            <el-upload
              class="avatar-uploader"
              action
              :show-file-list="false"
              :on-success="uploadSuccess"
              :before-upload="beforeUpload"
            >
              <img v-if="imageUrl" :src="imageUrl" />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>注：只能上传jpg/png格式图片，且不超过2M
          </el-form-item>
        </template>
        <el-form-item label=" ">
          <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">上一步</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">下一步</el-button>
        </el-form-item>
      </el-form>
      <div>
        <div>
          <div>活动名称</div>
          <div>引导语</div>
          <el-card shadow="never">
            <el-avatar size="small" src="@/assets/image/profile.jpg"></el-avatar>实际群名称
            <!-- <img :src="require('@/assets/image/profile.jpg')" alt=""> -->
            <img src="@/assets/image/profile.jpg" alt />
          </el-card>
        </div>
        <div>二维码预览</div>
      </div>
    </div>

    <realCode v-if="active === 2"></realCode>

    <div v-if="active === 2">
      客户群活码创建成功！
      <img src="@/assets/image/profile.jpg" alt />

      <div>
        <el-button type="text">下载二维码</el-button>
        <el-button type="text">复制链接</el-button>
      </div>
      <el-button type="primary">完成</el-button>
    </div>
  </div>
</template>

<script>
import baseInfo from "./baseInfo";
import realCode from "./realCode";
export default {
  name: "Operlog",
  components: {
    baseInfo,
    realCode,
  },
  data() {
    return {
      active: 0,
      treeData: [
        {
          label: "一级 1",
          children: [
            {
              label: "二级 1-1",
              children: [
                {
                  label: "三级 1-1-1",
                },
              ],
            },
          ],
        },
        {
          label: "一级 2",
          children: [
            {
              label: "二级 2-1",
              children: [
                {
                  label: "三级 2-1-1",
                },
              ],
            },
            {
              label: "二级 2-2",
              children: [
                {
                  label: "三级 2-2-1",
                },
              ],
            },
          ],
        },
        {
          label: "一级 3",
          children: [
            {
              label: "二级 3-1",
              children: [
                {
                  label: "三级 3-1-1",
                },
              ],
            },
            {
              label: "二级 3-2",
              children: [
                {
                  label: "三级 3-2-1",
                },
              ],
            },
          ],
        },
      ],
      defaultProps: {
        children: "children",
        label: "label",
      },
      dialogVisible: false,
      dialogVisible1: false,
      dialogVisible2: false,
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
      tableData: [
        {
          date: "2016-05-02",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄",
        },
        {
          date: "2016-05-04",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1517 弄",
        },
        {
          date: "2016-05-01",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1519 弄",
        },
        {
          date: "2016-05-03",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1516 弄",
        },
      ],
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
    setCurrent(row) {
      this.$refs.singleTable.setCurrentRow(row);
    },
    handleCurrentChange(row, oldRow) {
      row.checked = true;
      this.currentRow = row;
      console.log(row);
    },
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