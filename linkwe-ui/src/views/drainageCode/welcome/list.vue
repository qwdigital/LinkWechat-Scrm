<style lang="scss" scoped></style>

<template>
  <div>
    <el-tabs :value="active" type="card">
      <el-tab-pane :label="`员工欢迎语(${total1})`" name="1">
        <Tab ref="tab1" type="1" @total="totalChange"></Tab>
      </el-tab-pane>
      <el-tab-pane :label="`部门员工欢迎语(${total2})`" name="2">
        <Tab ref="tab2" type="2" @total="totalChange"></Tab>
      </el-tab-pane>
      <el-tab-pane :label="`客户群欢迎语(${total3})`" name="3">
        <Tab ref="tab3" type="3" @total="totalChange"></Tab>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import Tab from './components/Tab'

export default {
  name: 'Welcome',
  components: { Tab },
  props: {},
  data() {
    return {
      active: '1',
      total1: 0,
      total2: 0,
      total3: 0,
    }
  },
  watch: {},
  computed: {},
  created() {},
  mounted() {},
  methods: {
    totalChange([type, total]) {
      this['total' + type] = total
    },
  },
}
</script>
