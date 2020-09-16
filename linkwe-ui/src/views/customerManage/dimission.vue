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
    font-size: 14px;
  }
  .num {
    color: #00f;
  }
}
</style>

<template>
  <div class="page">
    <el-card shadow="never" :body-style="{padding: '20px 0 0'}">
      <el-form ref="form" :inline="true" :model="form" label-width="100px" class="top-search">
        <el-form-item label="已离职员工">
          <el-input v-model="form.user" placeholder="请输入"></el-input>
        </el-form-item>

        <el-form-item label="离职日期">
          <el-date-picker
            v-model="value2"
            type="daterange"
            :picker-options="pickerOptions"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            align="right"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label=" ">
          <el-button type="primary" @click="submitForm">查询</el-button>
          <el-button type="info" @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <div class="mid-action">
      <div class="total">从通讯录将离职员工删除后，可以分配他的客户及客户群给其他员工继续跟进</div>
      <div>
        <el-button type="primary" size="mini">已分配的离职员工</el-button>
        <el-button type="primary" size="mini">分配给其他员工</el-button>
      </div>
    </div>

    <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="已离职员工" width="120">
        <template slot-scope="scope">{{ scope.row.date }}</template>
      </el-table-column>
      <el-table-column prop="name" label="所属部门" width="120"></el-table-column>
      <el-table-column prop="address" label="待分配客户数" show-overflow-tooltip></el-table-column>
      <el-table-column prop="address" label="待分配群聊数" show-overflow-tooltip></el-table-column>
      <el-table-column prop="address" label="离职时间" show-overflow-tooltip>
        <template slot-scope="scope">{{ scope.row.date }}</template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>
          <el-button type="text" size="small">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      background
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage4"
      :page-sizes="[100, 200, 300, 400]"
      :page-size="100"
      layout="total, sizes, prev, pager, next, jumper"
      :total="400"
    ></el-pagination>
  </div>
</template>

<script>
export default {
  name: "Dimission",
  components: {},
  props: {},
  data() {
    return {
      isMoreFilter: false,
      total: 0,
      form: {
        user: "",
        region: "",
      },
      tableData: [
        {
          date: "2016-05-03",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄",
        },
        {
          date: "2016-05-02",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄",
        },
        {
          date: "2016-05-04",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄",
        },
        {
          date: "2016-05-01",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄",
        },
        {
          date: "2016-05-08",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄",
        },
        {
          date: "2016-05-06",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄",
        },
        {
          date: "2016-05-07",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄",
        },
      ],
      multipleSelection: [],
    };
  },
  watch: {},
  computed: {},
  created() {},
  mounted() {},
  methods: {
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
      this.$refs["form"].resetFields();
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
    },
  },
};
</script>
