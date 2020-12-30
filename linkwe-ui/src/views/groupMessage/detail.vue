<script>
import { getPushResult } from "@/api/groupMessage";

export default {
  components: {},
  props: {},
  data() {
    return {
      query: {
        pageNum: 1,
        pageSize: 10,
        messageId: "",
        status: 0
      },
      // 表格数据
      list: [],
      form: {},
      activeName: "1",
      total1: 0,
      total2: 0
    };
  },
  watch: {},
  computed: {},
  created() {
    this.id = this.$route.query.id;
    this.id && this.getList();
  },
  mounted() {},
  methods: {
    getList(page) {
      this.query = {
        messageId: this.id,
        status: 0
      };
      page && (this.query.pageNum = page);
      this.loading = true;
      getPushResult(this.query)
        .then(({ rows, total }) => {
          this.list = rows;
          this.total = +total;
          this.loading = false;
          this.ids = [];
        })
        .catch(() => {
          this.loading = false;
        });
    }
  }
};
</script>

<template>
  <div>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane :label="`待发送(${total1})`" name="1">
        <div class="ar">
          <el-button type="primary">发布动态</el-button>
        </div>
        <el-table
          v-loading="loading"
          :data="list"
          @selection-change="handleSelectionChange"
        >
          <el-table-column label="发送员工" align="center" prop="title" />
          <el-table-column label="发送客户" align="center" prop="operTime">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.operTime) }}</span>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="query.pageNum"
          :limit.sync="query.pageSize"
          @pagination="getList"
        />
      </el-tab-pane>
      <el-tab-pane :label="`已发送(${total2})`" name="2">
        <el-table
          v-loading="loading"
          :data="list"
          @selection-change="handleSelectionChange"
        >
          <el-table-column label="发送员工" align="center" prop="title" />
          <el-table-column label="发送客户" align="center" prop="operTime">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.operTime) }}</span>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="query.pageNum"
          :limit.sync="query.pageSize"
          @pagination="getList"
        />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<style lang="less" scoped></style>
