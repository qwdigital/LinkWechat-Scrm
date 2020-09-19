<style lang="scss" scoped>
.mid-action {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  align-items: center;
  background: #fff;
  .total {
    font-size: 14px;
  }
  .num {
    color: #00f;
  }
}
</style>

<template>
  <div>
    <el-form
      ref="form"
      :inline="true"
      :model="form"
      label-width="100px"
      class="top-search"
      size="small"
    >
      <el-form-item label="客户名称">
        <el-input v-model="form.user" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="添加人">
        <el-select v-model="form.region" placeholder="请选择">
          <el-option label="区域一" value="shanghai"></el-option>
          <el-option label="区域二" value="beijing"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="添加日期">
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
      <el-form-item label="标签">
        <el-input v-model="form.user" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label=" ">
        <el-button type="primary" @click="submitForm">查询</el-button>
        <el-button type="info" @click="resetForm('ruleForm')">重置</el-button>
        <el-button type="cyan" @click="isMoreFilter = !isMoreFilter">导出列表</el-button>
      </el-form-item>
    </el-form>

    <div class="mid-action">
      <div class="total">
        共
        <span class="num">{{total}}</span> 位客户，实际客户
        <span class="num">{{total}}</span> 位。
      </div>
      <div>
        <el-button
          type="primary"
          size="mini"
          icon="el-icon-s-flag"
          @click="dialogVisible = true"
        >打标签</el-button>
        <el-button type="primary" size="mini" icon="el-icon-brush">移除标签</el-button>
        <el-button type="primary" size="mini" icon="el-icon-refresh" @click="sync">同步客户</el-button>
        <el-button type="primary" size="mini" icon="el-icon-view">查看重复客户</el-button>
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
      <el-table-column label="客户" width>
        <template slot-scope="scope">{{ scope.row.date }}</template>
      </el-table-column>
      <el-table-column prop="name" label="公司名称"></el-table-column>
      <el-table-column prop="address" label="添加人" show-overflow-tooltip></el-table-column>
      <el-table-column prop="address" label="添加时间" show-overflow-tooltip></el-table-column>
      <el-table-column prop="address" label="标签" show-overflow-tooltip></el-table-column>
      <!-- <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>
          <el-button type="text" size="small">编辑</el-button>
        </template>
      </el-table-column>-->
    </el-table>

    <el-pagination
      background
      hide-on-single-page
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage4"
      :page-sizes="[10, 20, 30, 40]"
      :page-size="10"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    ></el-pagination>

    <el-dialog
      title="为客户增加标签"
      :visible.sync="dialogVisible"
      width="width"
      :before-close="dialogBeforeClose"
    >
      <div>
        <span class="mr20">选择分组</span>
        <el-select v-model="group" placeholder="请选择">
          <el-option label="区域一" value="shanghai"></el-option>
          <el-option label="区域二" value="beijing"></el-option>
          <el-option label="区域二" value="beijing"></el-option>
          <el-option label="区域二" value="beijing"></el-option>
        </el-select>
        <div class="mt20">
          <el-checkbox-group v-model="labelSelect">
            <el-checkbox
              :label="item.label"
              v-for="(item, index) in labelList"
              :key="index"
            >{{item.value}}</el-checkbox>
          </el-checkbox-group>
        </div>
        <div>
          <el-button type="primary" @click="isAddTag = true">添加标签</el-button>
          <template v-if="isAddTag">
            <el-input v-model="tagAdd" placeholder="每个标签名称最多15个字。同时新建多个标签时，请用“空格”隔开"></el-input>
            <el-button type="primary" @click="addTag">添加</el-button>
            <el-button type="primary" @click="isAddTag = false">取消</el-button>
          </template>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as api from "@/api/customer";

export default {
  name: "Customer",
  components: {},
  props: {},
  data() {
    return {
      query: {
        pageNum: 1,
        pageSize: 10,
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
      dialogVisible: false,
      labelSelect: [],
      labelList: [
        {
          value: "选项1",
          label: "黄金糕",
        },
        {
          value: "选项2",
          label: "双皮奶",
        },
        {
          value: "选项3",
          label: "蚵仔煎",
        },
        {
          value: "选项4",
          label: "龙须面",
        },
        {
          value: "选项5",
          label: "北京烤鸭",
        },
      ],
    };
  },
  watch: {},
  computed: {},
  created() {},
  mounted() {},
  methods: {
    getList(page) {
      // console.log(this.dateRange);
      if (this.dateRange[0]) {
        this.query.beginTime = this.dateRange[0];
        this.query.endTime = this.dateRange[1];
      }
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
    sync() {
      api.sync().then(() => {
        this.msgSuccess("操作成功");
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
