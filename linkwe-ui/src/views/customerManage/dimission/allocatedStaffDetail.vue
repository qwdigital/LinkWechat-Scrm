<script>
import { getAllocateCustomers } from "@/api/customer/dimission";
import AllocatedStaffDetailList from "./allocatedStaffDetailList";

export default {
  name: "AllocatedStaffDetail",
  components: { AllocatedStaffDetailList },
  props: {},
  data() {
    return {
      // 查询参数
      query: {
        pageNum: 1,
        pageSize: 10,
        handoverUserId: undefined,
        beginTime: undefined,
        endTime: undefined,
      },
      loading: false,
      total: 0,
      list: [],
      dateRange: [], // 离职日期
      // 日期快捷选项
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
        shortcuts: [
          {
            text: "最近一周",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近一个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近三个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit("pick", [start, end]);
            },
          },
        ],
      },
    };
  },
  watch: {},
  computed: {},
  created() {},
  mounted() {},
  methods: {
    getList() {
      this.$refs.AllocatedStaffDetailList.getList(1);
    },
    resetForm(formName) {
      this.dateRange = [];
      this.$refs["queryForm"].resetFields();
    },
  },
};
</script>

<template>
  <div class="page">
    <el-form ref="queryForm" :inline="true" :model="query" label-width="100px" class="top-search">
      <el-form-item label="离职日期">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          :picker-options="pickerOptions"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          align="right"
        ></el-date-picker>
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

    <el-tabs value="first" type="card">
      <el-tab-pane label="已分配客户" name="first">
        <AllocatedStaffDetailList ref="AllocatedStaffDetailList" :dateRange="dateRange"></AllocatedStaffDetailList>
      </el-tab-pane>
      <el-tab-pane label="已分配群聊" name="second">
        <AllocatedStaffDetailList ref="AllocatedStaffDetailList" :dateRange="dateRange"></AllocatedStaffDetailList>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<style lang="scss" scoped>
</style>
