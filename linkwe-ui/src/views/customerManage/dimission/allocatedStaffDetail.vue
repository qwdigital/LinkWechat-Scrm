<script>
import AllocatedStaffDetailList from './allocatedStaffDetailList'

export default {
  name: 'AllocatedStaffDetail',
  components: { AllocatedStaffDetailList },
  props: {},
  data() {
    return {
      active: 'customer',
      dateRange: [] // 离职日期
    }
  },
  watch: {},
  computed: {},
  created() {},
  mounted() {},
  methods: {
    getList() {
      this.$refs[this.active].getList(1)
    },
    resetForm(formName) {
      this.dateRange = []
      this.$refs['queryForm'].resetFields()
      this.getList()
    }
  }
}
</script>

<template>
  <div class="page">
    <el-form
      ref="queryForm"
      :inline="true"
      label-width="70px"
      class="top-search"
    >
      <el-form-item label="离职日期">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          value-format="yyyy-MM-dd"
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
          >查询</el-button
        >
        <el-button
          v-hasPermi="['customerManage:dimission:query']"
          type="info"
          @click="resetForm('queryForm')"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-tabs v-model="active">
      <el-tab-pane label="已分配客户" name="customer">
        <AllocatedStaffDetailList
          ref="customer"
          :dateRange="dateRange"
        ></AllocatedStaffDetailList>
      </el-tab-pane>
      <el-tab-pane label="已分配群聊" name="group">
        <AllocatedStaffDetailList
          ref="group"
          :dateRange="dateRange"
          type="group"
        ></AllocatedStaffDetailList>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<style lang="scss" scoped></style>
