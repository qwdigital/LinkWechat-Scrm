<template>
  <div class="login">
    <div class="login-wrap">
      <img class="login-bg1" src="@/assets/image/login_bg1.png" alt="" />
      <img class="login-bg2" src="@/assets/image/login_bg2.png" alt="" />
      <img class="login-bg3" src="@/assets/image/login_bg3.png" alt="" />
      <div class="login-form-wrap">
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
          <h3 class="title">LinkWeChat</h3>
          <el-form-item prop="username">
            <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="账号">
              <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              auto-complete="off"
              placeholder="密码"
              @keyup.enter.native="handleLogin"
            >
              <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
          <el-form-item prop="code">
            <el-input
              v-model="loginForm.code"
              auto-complete="off"
              placeholder="验证码"
              style="width: 63%"
              @keyup.enter.native="handleLogin"
            >
              <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
            </el-input>
            <div class="login-code">
              <img :src="codeUrl" @click="getCode" class="login-code-img" />
            </div>
          </el-form-item>
          <el-checkbox class="fr" v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
          <el-checkbox v-model="isDemonstrationLogin" @change="changeDemonAccount" style="margin:0px 0px 25px 0px;"
            >演示账号登录</el-checkbox
          >
          <el-form-item style="width:100%;">
            <el-button
              :loading="loading"
              size="medium"
              type="primary"
              style="width:100%;"
              @click.native.prevent="handleLogin"
            >
              <span v-if="!loading">登 录</span>
              <span v-else>登 录 中...</span>
            </el-button>
          </el-form-item>
          <el-form-item class="ac" v-if="authLink">
            <a :href="authLink">
              <img
                src="//wwcdn.weixin.qq.com/node/wwopen/wwopenmng/style/images/independent/brand/300x40_white$4dab5411.png"
                srcset="
                  //wwcdn.weixin.qq.com/node/wwopen/wwopenmng/style/images/independent/brand/300x40_white_2x$6a1f5234.png 2x
                "
                alt="企业微信登录"
              />
            </a>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2018-2021 LinkWeChat All Rights Reserved.</span>
    </div>

    <!-- <el-dialog
      title="开源软件评选"
      :visible.sync="dialogVisible"
      width="400px"
      class="c"
    >
      <div>
        <p>2020年度最佳人气项目开源软件评选。</p>
        <p>
          请为
          <strong style="color: #FF0036;" class="cp" @click="goVote"
            >LinkWeChat</strong
          >
          投票，谢谢支持。
        </p>
        <div class="ac">
          <img src="@/assets/image/vote-code.png" alt />
          <p style="color: #FF0036;">☛☛微信扫码投票☚☚</p>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="goVote">朕要支持</el-button>
        <el-button type="primary" @click="dialogVisible = false"
          >残忍拒绝</el-button
        >
      </div>
    </el-dialog> -->
  </div>
</template>

<script>
import axios from 'axios'
import { getCodeImg, findWxQrLoginInfo } from '@/api/login'
import Cookies from 'js-cookie'
import { encrypt, decrypt } from '@/utils/jsencrypt'

export default {
  name: 'Login',
  data() {
    return {
      codeUrl: '',
      cookiePassword: '',
      loginForm: {
        username: '',
        password: '',
        rememberMe: false,
        code: '',
        uuid: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', message: '用户名不能为空' }],
        password: [{ required: true, trigger: 'blur', message: '密码不能为空' }],
        code: [{ required: true, trigger: 'change', message: '验证码不能为空' }]
      },
      loading: false,
      redirect: undefined,
      authLink: '',
      dialogVisible: true,
      isDemonstrationLogin: false
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {
    // data.wxQrLoginRedirectUri http://192.168.0.101/#/authCallback
    findWxQrLoginInfo().then(({ data }) => {
      let authParams = {
        appid: data.corpId, // * 服务商的CorpID
        redirect_uri: encodeURIComponent(data.wxQrLoginRedirectUri), // * 授权登录之后目的跳转网址，需要做urlencode处理。所在域名需要与授权完成回调域名一致
        state: '', // ? 用于企业或服务商自行校验session，防止跨域攻击
        usertype: 'admin' // ? 支持登录的类型。admin代表管理员登录（使用微信扫码）,member代表成员登录（使用企业微信扫码），默认为admin
      }
      this.authLink = `https://open.work.weixin.qq.com/wwopen/sso/3rd_qrConnect?appid=${authParams.appid}&redirect_uri=${authParams.redirect_uri}&state=${authParams.state}&usertype=${authParams.usertype}`
    })

    this.getCode()
    this.getCookie()
  },
  methods: {
    getCode() {
      getCodeImg().then((res) => {
        this.codeUrl = 'data:image/gif;base64,' + res.img
        this.loginForm.uuid = res.uuid
      })
    },
    getCookie() {
      const username = Cookies.get('username')
      const password = Cookies.get('password')
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = true
          if (this.loginForm.rememberMe) {
            Cookies.set('username', this.loginForm.username, { expires: 30 })
            Cookies.set('password', encrypt(this.loginForm.password), {
              expires: 30
            })
            Cookies.set('rememberMe', this.loginForm.rememberMe, {
              expires: 30
            })
          } else {
            Cookies.remove('username')
            Cookies.remove('password')
            Cookies.remove('rememberMe')
          }
          this.$store
            .dispatch('Login', this.loginForm)
            .then(() => {
              this.$router.push({ path: this.redirect || '/' })
            })
            .catch(() => {
              this.loading = false
              this.getCode()
            })
        }
      })
    },
    goVote() {
      window.open('https://www.oschina.net/p/linkwechat')
    },
    changeDemonAccount(val) {
      this.loginForm.username = val ? 'Wecome' : ''
      this.loginForm.password = val ? '123456' : ''
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  // background-image: url('../assets/image/login-background.png');
  background-size: cover;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #027dfe;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
  position: relative;
  top: 120px;
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  // color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
  z-index: 90;
}
.login-code-img {
  height: 38px;
}
.login /deep/.el-dialog {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  margin-top: 0vh !important;
}
.el-dialog__body {
  padding: 0px 20px;
}
[class*='login-bg'] {
  position: absolute;
  z-index: -1;
}
.login-form-wrap {
  position: relative;
  background: #fff;
  height: 609px;
  top: 50px;
  left: 100px;
}
.login-wrap {
  position: relative;
  width: 1180px;
  height: 720px;
}
.login-bg1 {
  top: 0;
  left: 0;
}
.login-bg2 {
  top: 50px;
  left: 370px;
  z-index: 10;
}
.login-bg3 {
  top: 220px;
  right: 25px;
  z-index: 20;
}
</style>
