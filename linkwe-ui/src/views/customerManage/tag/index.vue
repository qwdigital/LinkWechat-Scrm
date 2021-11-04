<script>
import customer from './customer'
import group from './group'
export default {
  name: '',
  components: { customer, group },
  data() {
    return {
      activeName: 'customer'
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {}
}
</script>

<template>
  <div>
    <div class="g-title g-bb1">
      企业标签
      <div class="desc">
        <i class="el-icon-question"></i>
        管理客户标签与客户群标签，其中客户标签可以同步企微标签
      </div>
    </div>

    <el-tabs v-model="activeName">
      <el-tab-pane label="客户标签" name="customer">
        <customer></customer>
      </el-tab-pane>
      <el-tab-pane label="客群标签" name="group">
        <group></group>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<style lang="scss" scoped>
/deep/.el-tabs__header {
  margin-bottom: 0;
  background: #fff;
}
</style>
