<script>
import * as api from "@/api/customer/dimission";
import SelectUser from "./components/SelectUser";

export default {
  name: "Dimission",
  components: { SelectUser },
  props: {},
  data() {
    return {
      // 查询参数
      query: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        beginTime: undefined,
      },
      loading: false,
      isMoreFilter: false,
      total: 0,
      form: {
        user: "",
        region: "",
      },
      list: [],
      multipleSelection: [],
      dialogVisibleSelectUser: false,
    };
  },
  watch: {},
  computed: {},
  created() {
    this.getList();
  },
  mounted() {},
  methods: {
    /** 查询 */
    getList(page) {
      page && (this.query.pageNum = page);
      this.loading = true;
      api
        .getListNo(this.query)
        .then(({ rows, total }) => {
          this.list = rows;
          this.total = +total;
          this.loading = false;
        })
        .catch(() => {
          this.loading = false;
        });
    },
    submitForm(formName) {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          alert("submit!");
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs["queryForm"].resetFields();
    },
    allocate(userlist) {
      api
        .allocate({
          handoverUserid: this.multipleSelection[0].id,
          takeoverUserid: userlist[0].id,
        })
        .then(() => {
          this.msgSuccess("操作成功");
        });
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.multipleSelection = selection;
    },
  },
};
</script>

<template>
  <div class="page">
    <el-form ref="queryForm" :inline="true" :model="query" label-width="100px" class="top-search">
      <el-form-item label="已离职员工">
        <el-input v-model="query.userName" placeholder="请输入"></el-input>
      </el-form-item>

      <el-form-item label="离职日期">
        <el-date-picker v-model="query.beginTime" type="date" placeholder="离职日期" align="right"></el-date-picker>
      </el-form-item>
      <el-form-item label>
        <el-button
          v-hasPermi="['customerManage:dimission:query']"
          type="primary"
          @click="getList(1)"
        >查询</el-button>
        <el-button
          v-hasPermi="['customerManage:dimission:query']"
          type="info"
          @click="resetForm('queryForm')"
        >重置</el-button>
      </el-form-item>
    </el-form>

    <div class="mid-action">
      <div class="total">从通讯录将离职员工删除后，可以分配他的客户及客户群给其他员工继续跟进</div>
      <div>
        <el-button
          v-hasPermi="['customerManage:dimission:filter']"
          type="primary"
          size="mini"
          @click="getList(1)"
        >已分配的离职员工</el-button>
        <el-button
          v-hasPermi="['customerManage:dimission:allocate']"
          type="primary"
          size="mini"
          @click="dialogVisibleSelectUser = true"
        >分配给其他员工</el-button>
      </div>
    </div>

    <el-table
      ref="multipleTable"
      :data="list"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="userName" label="已离职员工" width="120"></el-table-column>
      <el-table-column prop="department" label="所属部门" width="120"></el-table-column>
      <el-table-column prop="allocateCustomerNum" label="待分配客户数" show-overflow-tooltip></el-table-column>
      <el-table-column prop="allocateGroupNum" label="待分配群聊数" show-overflow-tooltip></el-table-column>
      <el-table-column prop="dimissionTime" label="离职时间" show-overflow-tooltip>
        <template slot-scope="scope">{{ scope.row.dimissionTime }}</template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <!-- <el-button
            v-hasPermi="['customerManage:dimission:edit']"
            @click="handleClick(scope.row)"
            type="text"
            size="small"
          >查看</el-button>-->
          <el-button v-hasPermi="['customerManage:dimission:edit']" type="text" size="small">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="query.pageNum"
      :limit.sync="query.pageSize"
      @pagination="getList()"
    />

    <!-- 选择添加人弹窗 -->
    <SelectUser :visible.sync="dialogVisibleSelectUser" title="选择分配人" @success="allocate"></SelectUser>
  </div>
</template>

<style lang="scss" scoped>
.page {
  padding: 24px;
}
.el-input,
.el-select {
  width: 220px;
}
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
