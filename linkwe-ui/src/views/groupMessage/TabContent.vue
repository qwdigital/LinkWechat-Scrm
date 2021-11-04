<script>
import { getPushResult } from "@/api/groupMessage";

export default {
  components: {},
  props: {
    type: {
      type: Number | String,
      default: "1"
    },
    total: {
      type: Number | String,
      default: 0
    }
  },
  data() {
    return {
      // 查询参数
      query: {
        pageNum: 1,
        pageSize: 10,
        messageId: undefined,
        status: 0
      },
      loading: false,
      list: []
    };
  },
  watch: {},
  computed: {},
  created() {
    this.query.messageId = this.$route.query.id;
    this.query.status = this.type;
    this.getList();
  },
  mounted() {},
  methods: {
    /** 查询 */
    getList(page) {
      page && (this.query.pageNum = page);
      this.loading = true;
      getPushResult(this.query)
        .then(({ rows, total }) => {
          this.list = rows;
          this.loading = false;
          this.$emit("update:total", +total);
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
    <el-table v-loading="loading" :data="list">
      <el-table-column label="发送员工" align="center" prop="userName" />
      <el-table-column label="发送客户" align="center" prop="customers">
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="query.pageNum"
      :limit.sync="query.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<style lang="less" scoped></style>
