<script>
import * as api from "@/api/customer/tag";

export default {
  name: "CustomerTag",
  data() {
    return {
      query: {
        pageNum: 1,
        pageSize: "",
      },
      // 遮罩层
      loading: false,
      dialogVisible: false,
      // 表单参数
      form: {
        gourpName: "",
        weTags: [],
      },
      // 添加标签输入框
      newInput: "",
      // 添加标签显隐
      newInputVisible: false,
      // 表单验证规则
      rules: Object.freeze({
        gourpName: [{ required: true, message: "必填项", trigger: "blur" }],
      }),
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
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList(page) {
      page && (this.query.pageNum = page);
      this.loading = true;
      api
        .getList(this.query)
        .then(({ rows, total }) => {
          this.list = rows;
          this.total = +total;
          this.loading = false;
        })
        .catch(() => {
          this.loading = false;
        });
    },
    edit(data, type) {
      this.form = Object.assign({ weTags: [] }, data || {});
      this.dialogVisible = true;
    },
    closeTag(tag, index) {
      if (tag.id) {
        tag.status = 1;
      } else {
        this.form.weTags.splice(index, 1);
      }
    },

    showInput() {
      this.newInputVisible = true;
      this.$nextTick((_) => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },

    newInputConfirm() {
      let name = this.newInput;
      if (name) {
        this.form.weTags.push({ name });
      }
      this.newInputVisible = false;
      this.newInput = "";
    },
    submit() {
      this.$refs["form"].validate((valid) => {
        if (this.form.weTags.length) {
        }
        let form = JSON.parse(JSON.stringify(this.form));
        api[form.id ? "update" : "add"](form)
          .then(() => {
            this.msgSuccess("操作成功");
            this.dialogVisible = false;
            this.getList(!this.form.id && 1);
          })
          .catch(() => {
            this.dialogVisible = false;
          });
      });
    },
    sync() {},
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
      this.query.pageNum = 1;
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
      this.ids = selection.map((item) => item.id);
      this.multiple = !selection.length;
    },
    /** 详细按钮操作 */
    handleView(row) {
      this.open = true;
      this.form = row;
    },
    /** 删除按钮操作 */
    remove(id) {
      const operIds = id || this.ids + "";
      this.$confirm("是否确认删除吗?", "警告", {
        type: "warning",
      })
        .then(function () {
          return api.remove(operIds);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        });
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
      const query = this.query;
      this.$confirm("是否确认导出所有操作日志数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportOperlog(query);
        })
        .then((response) => {
          this.download(response.msg);
        })
        .catch(function () {});
    },
  },
};
</script>
<template>
  <div class="app-container">
    <div class="mid-action">
      <div class="total"></div>
      <div>
        <el-button type="primary" size="mini" icon="el-icon-plus" @click="edit()">新建标签组</el-button>
        <el-button type="primary" size="mini" icon="el-icon-refresh" @click="sync">同步标签组</el-button>
        <el-button
          v-if="ids.length"
          type="primary"
          size="mini"
          icon="el-icon-delete"
          @click="remove()"
        >批量删除</el-button>
      </div>
    </div>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="标签组" align="center" prop="gourpName" />
      <el-table-column label="标签" align="center" prop="weTags">
        <template slot-scope="scope">
          <el-tag type="info" v-for="(item, index) in scope.row.weTags" :key="index">{{item.name}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="businessType" :formatter="typeFormat" />
      <!-- <el-table-column label="创建时间" align="center" prop="operTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.operTime) }}</span>
        </template>
      </el-table-column>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="text" @click="edit(scope.row,scope.index)">编辑</el-button>
          <el-button @click="remove(scope.row.id)" type="text">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 弹窗 -->
    <el-dialog
      :title="(form.id ? '修改' : '添加') + '标签'"
      :visible.sync="dialogVisible"
      width="500px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="标签组名称" prop="gourpName">
          <el-input v-model="form.gourpName" maxlength="15" show-word-limit placeholder="请输入" />
        </el-form-item>
        <el-form-item label="标签">
          <template v-for="(item, index) in form.weTags">
            <el-tag
              v-if="item.status !== 1"
              type="primary"
              closable
              size="medium"
              :key="index"
              @close="closeTag(item, index)"
            >{{item.name}}</el-tag>
          </template>
          <el-input
            class="input-new-tag"
            v-if="newInputVisible"
            v-model="newInput"
            ref="saveTagInput"
            size="mini"
            maxlength="10"
            show-word-limit
            @keyup.enter.native="newInputConfirm"
            @blur="newInputConfirm"
          ></el-input>
          <el-button
            v-else
            type="primary"
            plain
            class="button-new-tag"
            size="mini"
            @click="showInput"
          >+ 添加标签</el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submit">确 定</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<style lang="scss" scoped>
.mid-action {
  display: flex;
  justify-content: space-between;
  margin: 10px 0;
  align-items: center;
  .num {
    color: #00f;
  }
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
.button-new-tag {
  margin-left: 10px;
}
</style>
