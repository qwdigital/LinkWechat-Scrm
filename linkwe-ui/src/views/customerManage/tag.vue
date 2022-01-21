<script>
import List from './tag/list'
export default {
  name: '',
  components: { List },
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
    <el-tabs v-model="activeName">
      <el-tab-pane label="客户标签" name="customer">
        <List type="1"></List>
      </el-tab-pane>
      <el-tab-pane label="客群标签" name="group">
        <List type="2"></List>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<style lang="scss" scoped>
::v-deep.el-tabs__header {
  margin-bottom: 0;
}
</style>
