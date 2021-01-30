<script>
export default {
  components: {},
  props: {
    message: {
      type: String,
      default: '',
    },
    isOther: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {}
  },
  watch: {},
  computed: {},
  created() {},
  mounted() {},
  methods: {},
}
</script>

<template>
  <div class="preview ac">
    <div class="top">小微</div>
    <div class="small">仟微科技-企微</div>
    <div class="time">凌晨2:20</div>
    <div class="flex msg-li">
      <el-avatar
        shape="square"
        size="small"
        :src="require('@/assets/image/profile.jpg')"
      ></el-avatar>
      <div class="msg">{{ message }}</div>
    </div>
    <div class="flex msg-li" v-if="isOther">
      <el-avatar
        shape="square"
        size="small"
        :src="require('@/assets/image/profile.jpg')"
      ></el-avatar>
      <div class="msg" style="line-height: 0; padding: 5px;">
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.preview {
  width: 260px;
  background: #eee;
  border-radius: 8px;
  padding: 15px 10px;
  height: 350px;
  line-height: 1.25;
  .top {
    font-weight: 600;
  }
  .small {
    font-size: 10px;
  }
  .time {
    font-size: 12px;
    color: #aaa;
    transform: scale(0.8);
    padding: 10px 0 5px;
  }
  .msg-li {
    margin-bottom: 10px;
  }
  .msg {
    min-height: 28px;
    background-color: #fff;
    border: 1px solid #e4e6e9;
    display: inline-block;
    position: relative;
    text-align: left;
    font-size: 14px;
    line-height: 22px;
    padding: 3px 6px;
    border-radius: 4px;
    max-width: 90%;
    min-width: 24px;
    box-sizing: border-box;
    word-break: break-all;
    white-space: pre-line;
    margin-left: 10px;
    &:before {
      content: ' ';
      display: block;
      position: absolute;
      left: -6px;
      border-width: 4px 6px;
      border-left: 0;
      width: 0;
      border-style: solid;
      border-color: transparent;
      border-right-color: #fff;
      top: 10px;
      z-index: 888;
    }
  }
}
.el-avatar {
  flex: none;
}
</style>
