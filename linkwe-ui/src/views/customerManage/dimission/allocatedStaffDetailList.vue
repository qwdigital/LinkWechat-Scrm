<script>
import { getAllocateCustomers } from "@/api/customer/dimission";

export default {
  name: "AllocatedStaffDetailList",
  components: {},
  props: {
    dateRange: {
      type: Array,
      default: () => [],
    },
  },
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
    };
  },
  watch: {},
  computed: {},
  created() {
    this.query.handoverUserId = this.$route.query.userId;
    this.getList();
  },
  mounted() {},
  methods: {
    /** 查询 */
    getList(page) {
      if (this.dateRange[0]) {
        this.query.beginTime = this.dateRange[0];
        this.query.endTime = this.dateRange[1];
      }
      page && (this.query.pageNum = page);
      this.loading = true;
      getAllocateCustomers(this.query)
        .then(({ rows, total }) => {
          this.list = rows;
          this.total = +total;
          this.loading = false;
        })
        .catch(() => {
          this.loading = false;
        });
    },
  },
};
</script>

<template>
  <div class="page">
    <el-table ref="multipleTable" :data="list" tooltip-effect="dark" style="width: 100%">
      <el-table-column prop="customerName" label="客户名称"></el-table-column>
      <el-table-column prop="takeUserName" label="接替员工"></el-table-column>
      <el-table-column prop="department" label="接替员工所属部门" show-overflow-tooltip></el-table-column>
      <el-table-column prop="allocateTime" label="分配时间" show-overflow-tooltip></el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['customerManage:dimission:edit']"
            @click="$router.push({path: '/customerManage/customerDetail', query: {id: scope.row.id}})"
            type="text"
            size="small"
          >查看</el-button>
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
  </div>
</template>

<style lang="scss" scoped>
</style>
