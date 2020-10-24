<script>
import * as api from "@/api/customer/group";

export default {
  name: "CustomerDetail",
  data() {
    return {
      customer: {},
    };
  },
  created() {
    this.customer = this.$route.query;
  },
  methods: {
    getList(page) {
      page && (this.query.pageNum = page);
      this.loading = false;
      api.getMembers(this.query).then((response) => {
        this.list = response.rows;
        this.total = +response.total;
        this.loading = false;
      });
    },
  },
};
</script>

<template>
  <div>
    <!-- <el-button slot="append" circle icon="el-icon-back" @click="$router.back()"></el-button>返回 -->
    <div class="flex aic">
      <el-avatar
        :size="100"
        src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
      ></el-avatar>
      <div class="info-wrap">
        <div style="margin-bottom: 20px;">
          {{customer.groupName}}
          <span
            :style="{color: customer.type === 1 ? '#4bde03' : '#f9a90b'}"
          >{{ ({1: '@微信', 2: '@企业微信'})[customer.type] }}</span>
          <i :class="['el-icon-s-custom', ({1: 'man', 2: 'woman'})[customer.gender]]"></i>
        </div>
        <div class="info">
          出生日期：{{customer.createTime || '--'}}
          <i class="el-icon-edit"></i>
          <el-date-picker v-model="customer.createTime" type="date" placeholder="选择日期"></el-date-picker>
        </div>
      </div>
    </div>

    <el-card shadow="never" :body-style="{width: '400px'}">
      <div>
        <el-row :gutter="10">
          <el-col :span="10">备注名：</el-col>
          <el-col :span="12">Dora</el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">标签：</el-col>
          <el-col :span="12">Dora</el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">个人标签：</el-col>
          <el-col :span="12">Dora</el-col>
        </el-row>
        <el-divider></el-divider>
        <el-row :gutter="10">
          <el-col :span="10">添加人：</el-col>
          <el-col :span="12">Dora</el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">所在部门：</el-col>
          <el-col :span="12">Dora</el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">添加时间：</el-col>
          <el-col :span="12">Dora</el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<style lang="scss" scoped>
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

.info-wrap {
  margin-left: 20px;
  .info {
    color: #aaa;
  }
}

.el-card {
  display: inline-block;
  margin-top: 20px;
  .el-row {
    color: #666;
    margin-bottom: 10px;
  }
  .el-col-10 {
    width: 100px;
    text-align: right;
  }
}
</style>