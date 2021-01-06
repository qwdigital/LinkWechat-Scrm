<script>
import { getDetail } from '@/api/groupMessage'
import TabContent from './TabContent'

export default {
  components: {
    TabContent,
  },
  props: {},
  data() {
    return {
      data: {},
      activeName: '0',
      total1: 0,
      total0: 0,
    }
  },
  watch: {},
  computed: {},
  created() {
    getDetail(this.$route.query.id).then(({ data }) => {
      this.data = data
    })
  },
  mounted() {},
  methods: {
    notice() {},
  },
}
</script>

<template>
  <div>
    <div>
      创建人：{{ data.sender }}
      <div>消息内容：{{ data.content }}</div>
    </div>
    <!-- <el-button class="notice" type="primary" @click="notice"
      >通知员工发送</el-button
    > -->
    <el-tabs v-model="activeName">
      <el-tab-pane :label="`待发送(${total0})`" name="0">
        <TabContent type="0" :total.sync="total0"></TabContent>
      </el-tab-pane>
      <el-tab-pane :label="`已发送(${total1})`" name="1">
        <TabContent type="1" :total.sync="total1"></TabContent>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<style lang="less" scoped></style>
