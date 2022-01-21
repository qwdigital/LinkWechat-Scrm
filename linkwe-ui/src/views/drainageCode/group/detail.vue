<script>
import BaseInfo from './baseInfo'

export default {
  components: { BaseInfo },
  data() {
    return {
      groupCode: null,
      groupCodeId: ''
    }
  },
  created() {
    this.groupCodeId = this.$route.query.groupCodeId
  },
  methods: {
    save() {
      this.$refs.baseInfo.submit()
      this.$router.back()
    }
  }
}
</script>

<template>
  <div class="page">
    <BaseInfo ref="baseInfo" :groupCodeId="groupCodeId"></BaseInfo>

    <div class="ac">
      <el-button @click="$router.back()"> 取消 </el-button>
      <el-button type="primary" @click="save"> 保存 </el-button>
    </div>
  </div>
</template>
