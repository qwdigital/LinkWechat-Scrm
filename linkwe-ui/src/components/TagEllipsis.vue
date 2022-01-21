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
    <el-popover placement="bottom" trigger="hover" :disabled="list.length <= +limit">
      <div>
        <el-tag v-for="(item, unique) in list" :key="unique">
          {{ item[defaultProps] || item }}
        </el-tag>
      </div>
      <div slot="reference">
        <el-tag v-for="(item, unique) in list.slice(0, +limit)" :key="unique">
          {{ item[defaultProps] || item }}
        </el-tag>
        <el-tag key="a" v-if="list.length > +limit">...</el-tag>
      </div>
    </el-popover>
  </div>
</template>

<style lang="scss" scoped>
.tag-ellipsis {
  display: inline-block;
}
</style>
