<template>
  <div class="home">
    <div class="logo ac">linkWeChat</div>
    <router-link
      class="router-link"
      v-for="(item, index) in $router.options.routes"
      :to="item.path"
      :key="index"
      >{{ item.meta.title }}</router-link
    >
  </div>
</template>

<script>
// @ is an alias to /src

export default {
  name: 'Home',
  components: {},
}
</script>

<style lang="less" scoped>
.logo {
  margin: 0 auto;
  width: 150px;
  height: 50px;
  line-height: 50px;
  border: 2px solid #6582ff;
  border-radius: 50px;
}
.router-link {
  display: block;
  text-align: center;
  line-height: 40px;
}
</style>
