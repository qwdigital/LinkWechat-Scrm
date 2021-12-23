<script>
export default {
  name: 'TagEllipsis',
  components: {},
  props: {
    list: {
      type: Array,
      default: () => []
    },
    limit: {
      type: [String, Number],
      default: 2
    },
    defaultProps: {
      type: String,
      default: 'name'
    }
  },
  data() {
    return {}
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {}
}
</script>

<template>
  <div class="tag-ellipsis">
    <template v-for="(item, index) in list">
      <el-tag size="medium" v-if="index < limit" :key="index">
        {{ item[defaultProps] || item }}
      </el-tag>
    </template>
    <el-tag v-if="list.length && list.length > limit">...</el-tag>
  </div>
</template>

<style lang="scss" scoped>
.tag-ellipsis {
  display: inline-block;
}
</style>
