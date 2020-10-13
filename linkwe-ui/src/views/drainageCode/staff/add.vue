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
</style>
<template>
  <div class="app-container page">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" label-width="100px">
      <el-form-item label="类型" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入"
          clearable
          style="width: 240px;"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="使用员工" prop="operName">
        <el-button icon="el-icon-plus" size="mini" @click="dialogVisible2 = true">添加</el-button>
      </el-form-item>
      <el-form-item label="添加设置" prop="operName">
        <el-checkbox v-model="checked">客户添加时无需经过确认自动成为好友</el-checkbox>
      </el-form-item>
      <el-form-item label="活动场景" prop="operName">
        <el-input
          v-model="queryParams.operName"
          placeholder="请输入"
          clearable
          style="width: 240px;"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="扫码标签" prop="operName">
        <el-tag closable>标签一</el-tag>
        <el-tag closable>标签一</el-tag>
        <el-tag closable>标签一</el-tag>
        <el-tag closable>标签一</el-tag>
        <el-button icon="el-icon-plus" size="mini" @click="dialogVisible = true">添加标签</el-button>
        <div>根据使用场景做标签记录，扫码添加的客户，可自动打上标签</div>
      </el-form-item>
      <el-form-item label="欢迎语">
        <el-card shadow="never">
          <el-input
            type="textarea"
            v-model="queryParams.operName"
            placeholder="请输入"
            clearable
            style="width: 240px;"
            size="small"
            @keyup.enter.native="handleQuery"
          />
          <el-divider></el-divider>
          <el-popover placement="top-start" width="200" trigger="hover">
            <div class="flex">
              <div>图片</div>
              <div>网页</div>
              <div>小程序</div>
            </div>
            <el-button
              slot="reference"
              icon="el-icon-plus"
              size="mini"
              @click="resetQuery"
            >添加图片/网页/小程序消息</el-button>
          </el-popover>
        </el-card>
        <el-button
          icon="el-icon-plus"
          type="primary"
          size="mini"
          @click="dialogVisible1 = true"
        >从欢迎语模板选取</el-button>
        <div>设置个性化欢迎语，扫描该员工活码添加的客户，将自动推送该欢迎语</div>
      </el-form-item>
      <el-form-item label=" ">
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">保存</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">取消</el-button>
      </el-form-item>
    </el-form>
    <div>
      <!-- <img :src="require('@/assets/image/profile.jpg')" alt=""> -->
      <img src="@/assets/image/profile.jpg" alt />
      <div>二维码预览</div>

      <el-card shadow="never">
        <el-avatar size="small" src="@/assets/image/profile.jpg"></el-avatar>63136
        <el-divider></el-divider>
        <div>性别 男</div>
        <div>设置备注和描述</div>
        <div>
          标签
          <el-tag closable>试用版1重要（1w以上）</el-tag>
          <el-tag closable>试用版1</el-tag>
        </div>
      </el-card>
      <div>扫码标签样式</div>

      <div class="preview">
        <div class="top">房管局会</div>
      </div>
      <div>欢迎语样式</div>
    </div>

    <el-dialog
      title="组织架构"
      :visible.sync="dialogVisible2"
      width="width"
      :before-close="dialogBeforeClose"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-input placeholder="请输入关键字" v-model="input3" class>
            <el-button slot="append">查询</el-button>
          </el-input>
          <el-tree :data="treeData" :props="defaultProps" accordion @node-click="handleNodeClick"></el-tree>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple"></div>
        </el-col>
      </el-row>
      <div slot="footer">
        <el-button @click="dialogVisible2 = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible2 = false">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="选择标签"
      :visible.sync="dialogVisible"
      width="width"
      :before-close="dialogBeforeClose"
    >
      <div>
        <div class="tag-list-content">
          <div class="tag-list">
            <div class="tagName">试用标签</div>
            <ul class="list">
              <li class="tag">试用版1</li>
              <!---->
              <!---->
            </ul>
          </div>
          <div class="tag-list">
            <div class="tagName">test</div>
            <ul class="list">
              <li class="tag">fg</li>
              <!---->
              <!---->
            </ul>
          </div>
          <div class="tag-list">
            <div class="tagName">客户付费级别</div>
            <ul class="list">
              <li class="tag">重要（1w以上）</li>
              <li class="tag">新用户（9.9元以下）</li>
              <li class="tag">核心（3000以上）</li>
              <!---->
              <!---->
            </ul>
          </div>
          <div class="tag-list">
            <div class="tagName">教育社群运营学员</div>
            <ul class="list">
              <li class="tag">社群运营专栏课</li>
              <li class="tag">社群运营初级4期</li>
              <li class="tag">社群运营训练营</li>
              <!---->
              <!---->
            </ul>
          </div>
          <div class="tag-list">
            <div class="tagName">教育短视频运营学员</div>
            <ul class="list">
              <li class="tag">短视频运营期</li>
              <li class="tag">短视频孵化营7期</li>
              <!---->
              <!---->
            </ul>
          </div>
          <div class="tag-list">
            <div class="tagName">客户来源</div>
            <ul class="list">
              <li class="tag">红包分享</li>
              <li class="tag">现场扫码</li>
              <li class="tag">抖音</li>
              <li class="tag">小程序</li>
              <li class="tag">公众号</li>
              <!---->
              <!---->
            </ul>
          </div>
          <div class="tag-list">
            <div class="tagName">身份</div>
            <ul class="list">
              <li class="tag">老师</li>
              <li class="tag">家长</li>
              <!---->
              <!---->
            </ul>
          </div>
          <div class="tag-list">
            <div class="tagName">客户意向</div>
            <ul class="list">
              <li class="tag">无意向</li>
              <li class="tag">意向强</li>
              <li class="tag">意向弱</li>
              <li class="tag">意向一般</li>
              <!---->
              <!---->
            </ul>
          </div>
          <div class="tag-list">
            <div class="tagName">活动营销</div>
            <ul class="list">
              <li class="tag">刮刮卡</li>
              <li class="tag">大转盘</li>
              <li class="tag">秒杀</li>
              <li class="tag">抽奖</li>
            </ul>
          </div>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="选择欢迎语"
      :visible.sync="dialogVisible1"
      width="width"
      :before-close="dialogBeforeClose"
    >
      <div>
        <el-input class="welcome-input" placeholder="请输入关键字" v-model="input3">
          <el-button slot="append">查询</el-button>
        </el-input>
        <el-table
          ref="singleTable"
          :data="tableData"
          :max-height="300"
          :show-header="false"
          highlight-current-row
          @current-change="handleCurrentChange"
          style="width: 100%"
        >
          <el-table-column property="date"></el-table-column>
          <el-table-column width="60" show-overflow-tooltip>
            <template slot-scope="{row}">
              <i
                v-if="row.checked"
                class="el-icon-check"
                style="color: rgb(65, 133, 244); font-size: 25px;"
              ></i>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <el-button @click="dialogVisible1 = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible1 = false">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Operlog",
  data() {
    return {
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