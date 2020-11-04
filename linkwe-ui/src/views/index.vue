<template>
  <div>
    <img class="img" src="@/assets/image/index.png" alt="" />
    <div class="button" @click="goWeb"></div>
  </div>
</template>

<script>
export default {
  name: 'Index',
  components: {},
  data() {
    return {}
  },
  methods: {
    goWeb() {
      window.open('https://gitee.com/LinkWeChat/link-wechat')
    },
  },
}
</script>

<style lang="scss" scoped>
.page {
  position: relative;
  padding: 0;
}
.img {
  width: 100%;
}
.button {
  position: absolute;
  top: 58%;
  left: 11%;
  width: 12%;
  height: 8%;
  cursor: pointer;
}
</style>
