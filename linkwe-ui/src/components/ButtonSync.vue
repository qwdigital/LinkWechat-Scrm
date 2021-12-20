<script>
export default {
  name: 'ButtonSync',
  props: {
    customeBtn: {
      type: Boolean,
      default: false
    },
    lastSyncTime: {
      type: [String, Number],
      default: 0
    }
  },
  components: {},
  data() {
    return {}
  },
  computed: {
    disabled() {
      return this.lastSyncTime ? (+new Date() - +new Date(this.lastSyncTime)) / 3600000 < 2 : false
    }
  },
  watch: {},
  created() {},
  mounted() {},
  methods: {
    sync() {
      // if (this.disabled) {
      //   this.msgError('由于企业微信开放平台的限制，两小时内不得重复同步操作')
      //   return
      // }
      this.$emit('click')
    }
  }
}
</script>

<template>
  <div class="button-sync">
    <el-tooltip
      v-if="disabled"
      effect="light"
      class="item"
      content="由于企业微信开放平台的限制，两小时内不得重复同步操作"
      placement="top-start"
    >
      <el-tag type="info" size="default"><slot></slot></el-tag>
    </el-tooltip>
    <el-button v-else v-preventReClick type="primary" @click="sync">
      <slot></slot>
    </el-button>
  </div>
</template>

<style lang="scss" scoped>
.button-sync {
  display: inline-block;
}
</style>
