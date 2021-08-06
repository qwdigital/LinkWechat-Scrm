<script>
import BaseInfo from './baseInfo'
import RealCode from './realCode'
import GroupCode from './groupCode'

export default {
  components: {
    BaseInfo,
    RealCode,
    GroupCode
  },
  data() {
    return {
      // 当前群活码ID
      groupCodeId: null,
      // 当前激活的步骤
      active: 0
    }
  },
  methods: {
    // 下一步
    next(groupCodeId) {
      if (groupCodeId) this.groupCodeId = groupCodeId
      this.active += 1
      if (this.active > 3) this.active = 1
    },
    // 上一步
    prev() {
      this.active -= 1
      if (this.active < 0) this.active = 0
    },
    // 完成
    finished() {
      this.next()

      this.$router.back()
    },
    // 新增或更新群活码
    handleGroupCode() {
      this.$refs.baseInfo.submit()
    },
    // 管理实际群活码
    handleRealCode() {
      this.next()
    }
  }
}
</script>

<template>
  <div class="page">
    <el-steps :active="active" finish-status="success" align-center>
      <el-step title="基本信息" description="设置群活码基本信息"></el-step>
      <el-step title="实际群码" description="添加并管理微信实际群码"></el-step>
      <el-step title="群活码" description="设置完成后生成群活码"></el-step>
    </el-steps>

    <div class="page-content">
      <div v-if="active === 0">
        <BaseInfo
          ref="baseInfo"
          :groupCodeId="groupCodeId"
          @next="next"
        ></BaseInfo>
      </div>
      <div v-if="active === 1">
        <RealCode
          ref="realCode"
          :groupCodeId="groupCodeId"
          @next="next"
        ></RealCode>
      </div>
      <div v-if="active === 2">
        <GroupCode :groupCodeId="groupCodeId"></GroupCode>
      </div>
    </div>

    <div class="ac">
      <template v-if="active === 0">
        <el-button @click="$router.back()"> 取消 </el-button>
        <el-button type="primary" @click="handleGroupCode"> 下一步 </el-button>
      </template>
      <template v-else-if="active === 1">
        <el-button @click="prev"> 上一步 </el-button>
        <el-button type="primary" @click="handleRealCode"> 下一步 </el-button>
      </template>
      <template v-else>
        <el-button @click="prev"> 上一步 </el-button>
        <el-button type="primary" @click="finished"> 完成 </el-button>
      </template>
    </div>
  </div>
</template>

<style scoped lang="scss">
.page-content {
  padding-top: 50px;
  padding-bottom: 20px;
}
</style>
