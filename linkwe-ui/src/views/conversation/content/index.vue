<template>
  <div class="content">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="员工检索" name="1">
        <employee></employee>
      </el-tab-pane>
      <el-tab-pane label="客户检索" name="2">
        <user v-if="activeName == 2"></user>
      </el-tab-pane>
      <el-tab-pane label="全局检索" name="3">
        <global v-if="activeName == 3"></global>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import employee from './employee.vue'
import user from './customer.vue'
import global from './global.vue'
export default {
  components: { employee, user, global },
  data() {
    return {
      activeName: '1'
    }
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event)
    }
  }
}
</script>
<style lang="scss" scoped>
::v-deep.pd15 {
  padding: 15px;
}
</style>
