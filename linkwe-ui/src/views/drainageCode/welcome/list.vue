<template>
  <div>
    <el-tabs :value="active">
      <el-tab-pane label="活码欢迎语" name="1">
        <Tab ref="tab1" type="1" @total="totalChange"></Tab>
      </el-tab-pane>
      <el-tab-pane label="员工欢迎语" name="2">
        <Tab ref="tab2" type="2" @total="totalChange"></Tab>
      </el-tab-pane>
      <el-tab-pane label="入群欢迎语" name="3">
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
    data () {
      return {
        active: '1',
        total1: 0,
        total2: 0,
        total3: 0
      }
    },
    watch: {},
    computed: {},
    created () {
      if (this.$route.query.type) {
        this.active = this.$route.query.type
      }
    },
    mounted () {
      this.$store.dispatch(
        'app/setBusininessDesc',
        `
        <div>活码欢迎语：客户通过员工活码添加员工时，系统自动回复欢迎语；新建活码时也可以从活码欢迎语模板中选择。</div>
        <div>员工欢迎语：客户通过搜索手机号等非员工活码方式添加员工时，系统自动回复欢迎语。</div>
        <div>入群欢迎语：客户加入群聊时，系统自动回复欢迎语。</div>
      `
      )
    },
    // beforeRouteLeave(to, from, next) {
    //   // this.$store.dispatch(('app/setBusininessDesc', ''))
    //   this.$store.state.app.busininessDesc = ''
    //   next()
    // },
    methods: {
      totalChange ([type, total]) {
        this['total' + type] = total
      }
    }
  }
</script>

<style lang="scss" scoped></style>
