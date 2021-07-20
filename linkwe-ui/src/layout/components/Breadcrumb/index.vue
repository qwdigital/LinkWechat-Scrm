<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item key="1">
        <svg-icon
          @click="handleLink({ path: '/index' })"
          iconClass="home"
          class="cp home"
        ></svg-icon>
      </el-breadcrumb-item>
      <el-breadcrumb-item v-for="(item, index) in levelList" :key="item.path">
        <span
          v-if="
            item.redirect === 'noRedirect' ||
              index == levelList.length - 1 ||
              !/^\//gi.test(item.redirect)
          "
          class="no-redirect"
          >{{ item.meta.title }}</span
        >
        <a v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
      </el-breadcrumb-item>
    </transition-group>
    <el-popover
      v-if="busininessDesc"
      placement="top-start"
      title="引导语"
      trigger="hover"
    >
      <div v-html="busininessDesc"></div>
      <i class="el-icon-question" slot="reference"></i>
    </el-popover>
  </el-breadcrumb>
</template>

<script>
import pathToRegexp from 'path-to-regexp'

export default {
  data() {
    return {
      levelList: null
    }
  },
  watch: {
    $route(route) {
      // if you go to the redirect page, do not update the breadcrumbs
      if (route.path.startsWith('/redirect/')) {
        return
      }
      this.$store.state.app.busininessDesc = ''
      this.getBreadcrumb()
    }
  },
  computed: {
    busininessDesc() {
      return this.$store.state.app.busininessDesc
    }
  },
  created() {
    this.getBreadcrumb()
  },
  methods: {
    getBreadcrumb() {
      // only show routes with meta.title
      let matched = this.$route.matched.filter(
        (item) => item.meta && item.meta.title
      )
      const first = matched[0]

      // if (!this.isDashboard(first)) {
      //   matched = [{ path: '/index', meta: { title: '首页' } }].concat(matched)
      // }

      this.levelList = matched.filter(
        (item) => item.meta && item.meta.title && item.meta.breadcrumb !== false
      )
    },
    isDashboard(route) {
      const name = route && route.name
      if (!name) {
        return false
      }
      return name.trim() === '首页'
    },
    pathCompile(path) {
      const { params } = this.$route
      var toPath = pathToRegexp.compile(path)
      return toPath(params)
    },
    handleLink(item) {
      const { redirect, path } = item
      if (redirect) {
        this.$router.push(redirect)
        return
      }
      this.$router.push(this.pathCompile(path))
    }
  }
}
</script>

<style lang="scss" scoped>
.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  vertical-align: middle;
  font-size: 14px;
  line-height: 50px;
  margin-left: 8px;

  .no-redirect {
    color: #999;
    cursor: text;
  }
}
.el-icon-question {
  color: #888;
  margin-left: 10px;
}
.home {
  color: $blue;
}
</style>
