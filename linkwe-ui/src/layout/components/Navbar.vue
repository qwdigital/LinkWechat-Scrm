<template>
  <div class="navbar main-size">
    <logo />

    <el-scrollbar class="nav-scrollbar">
      <template v-for="(route, index) in permission_routes">
        <div
          v-if="!route.hidden"
          :class="['nav', $route.path.startsWith(route.path) && 'active']"
          :key="route.path + index"
          @click="goLink(route.path)"
        >
          {{ route.meta && route.meta.title }}
        </div>
      </template>
    </el-scrollbar>

    <div class="right-menu">
      <template v-if="device !== 'mobile'">
        <!-- <search id="header-search" class="right-menu-item" /> -->

        <el-tooltip content="源码地址" effect="dark" placement="bottom">
          <div class="right-menu-item hover-effect">
            <svg-icon icon-class="github" @click="goto(0)" />
          </div>
        </el-tooltip>

        <el-tooltip content="文档地址" effect="dark" placement="bottom">
          <div class="right-menu-item hover-effect">
            <i class="el-icon-reading document" @click="goto(1)"></i>
            <!-- <svg-icon icon-class="question" @click="goto(1)" /> -->
          </div>
        </el-tooltip>

        <screenfull id="screenfull" class="right-menu-item hover-effect" />
      </template>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          仟微
          <!-- <img :src="avatar" class="user-avatar"> -->
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/user/profile">
            <el-dropdown-item>个人中心</el-dropdown-item>
          </router-link>
          <el-dropdown-item @click.native="setting = true">
            <span>布局设置</span>
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout">
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { isExternal } from '@/utils/validate'
import { mapGetters } from 'vuex'
import Logo from './Logo'

import Search from '@/components/HeaderSearch'
import screenfull from '@/components/Screenfull'

export default {
  components: {
    Logo,
    Search,
    screenfull
  },
  computed: {
    ...mapGetters(['avatar', 'device', 'permission_routes']),
    setting: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    }
  },
  mounted() {
    // console.log(this.permission_routes)
  },
  methods: {
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = process.env.VUE_APP_BASE_URL
        })
      })
    },
    goto(type) {
      window.open(type ? 'https://www.yuque.com/linkwechat/help/dsatfs' : 'https://gitee.com/LinkWeChat/link-wechat')
    },
    goLink(path) {
      if (!isExternal(path)) {
        this.$router.push({ path })
      } else {
        window.open(path, '_blank')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  display: flex;
  align-items: center;
  width: 100%;
  height: 100%;
  overflow: hidden;
  position: relative;
  color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    position: absolute;
    right: 0;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      font-size: 18px;
      // color: #5a5e66;
      vertical-align: middle;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }

      .document {
        font-size: 20px;
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        position: relative;
        color: #fff;
        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 5px;
          font-size: 12px;
        }
      }
    }
  }
}

.nav-scrollbar {
  width: calc(100% - 450px);
  /deep/.el-scrollbar__view {
    white-space: nowrap;
    line-height: 58px;
  }
  .nav {
    display: inline-block;
    margin: 0 20px;
    flex: none;
    position: relative;
    cursor: pointer;
    &.active::after {
      content: '';
      display: inline-block;
      position: absolute;
      bottom: 2px;
      width: 42px;
      height: 2px;
      left: 50%;
      transform: translateX(-50%);
      border-radius: 6px;
      background: #fff;
    }
  }
}
</style>
