<template>
  <div class="page">
    <BaseInfo ref="baseInfo" :groupCodeId="groupCodeId"></BaseInfo>

    <div class="fr">
      <el-button @click="$router.back()"> 取消 </el-button>
      <el-button type="primary" @click="save"> 保存 </el-button>
    </div>
  </div>
</template>

<script>
import BaseInfo from './baseInfo'

export default {
  components: {
    BaseInfo
  },

  data () {
    return {
      groupCode: null,
      groupCodeId: ''
    }
  },

  methods: {
    save () {
      this.$refs.baseInfo.submit()
      this.$router.back()
    }
  },

  created () {
    this.groupCodeId = this.$route.query.groupCodeId
  }
}
</script>

<style scoped lang="scss">

</style>
