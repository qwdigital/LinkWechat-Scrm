<style lang="scss" scoped>
</style>

<template>
  <div>
    <el-tabs :value="active" type="card">
      <el-tab-pane label="员工欢迎语(45)" name="1">
        <Tab ref="staff" type="1"></Tab>
      </el-tab-pane>
      <el-tab-pane label="部门员工欢迎语(4)" name="2">
        <Tab ref="department" type="2"></Tab>
      </el-tab-pane>
      <el-tab-pane label="客户群欢迎语(4)" name="3">
        <Tab ref="customer" type="3"></Tab>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import Tab from "./components/Tab";

export default {
  name: "Welcome",
  components: { Tab },
  props: {},
  data() {
    return {
      active: "1",
    };
  },
  watch: {},
  computed: {},
  created() {},
  mounted() {},
  methods: {},
};
</script>
