<script>
import { getAgentTicket, getAppTicket } from '@/api/common'
export default {
  name: 'App',
  data() {
    return {
      appId: 'appId',
      agentId: '1000005',
    }
  },
  created() {
    // this.wxConfig()
  },
  watch: {
    // 通过config接口注入权限验证配置
    // 所有需要使用JS-SDK的页面必须先注入配置信息，否则将无法调用（同一个url仅需调用一次，对于变化url的SPA（single-page application）的web app可在每次url变化时进行调用）
    $route() {
      this.wxConfig()
    },
  },
  methods: {
    wxConfig() {
      getAgentTicket(window.location.href.split('#')[0]).then(({ data }) => {
        let { timestamp, nonceStr, signature } = data
        wx.agentConfig({
          corpid: this.appId, // 必填，企业微信的corpid，必须与当前登录的企业一致
          agentid: this.agentId, // 必填，企业微信的应用id （e.g. 1000247）
          timestamp, // 必填，生成签名的时间戳
          nonceStr, // 必填，生成签名的随机串
          signature, // 必填，签名，见附录-JS-SDK使用权限签名算法
          jsApiList: ['sendChatMessage'], //必填
          success: (res) => {
            // 回调
          },
          fail: (res) => {
            if (res.errMsg.indexOf('function not exist') > -1) {
              alert('版本过低请升级')
            }
          },
        })
      })
    },
    _wxConfig() {
      // 获取企业的jsapi_ticket
      getAppTicket(window.location.href).then(({ data }) => {
        let { timestamp, nonceStr, signature } = data
        wx.config({
          beta: true, // 必须这么写，否则wx.invoke调用形式的jsapi会有问题
          debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
          appId: this.appId, // 必填，企业微信的corpID
          timestamp, // 必填，生成签名的时间戳
          nonceStr, // 必填，生成签名的随机串
          signature, // 必填，签名，见 附录-JS-SDK使用权限签名算法
          jsApiList: ['getContext', 'sendChatMessage'], // 必填，需要使用的JS接口列表，凡是要调用的接口都需要传进来
        })
        wx.ready(() => {
          // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
          getAgentTicket(window.location.href).then(({ data }) => {
            let { timestamp, nonceStr, signature } = data
            wx.agentConfig({
              corpid: this.appId, // 必填，企业微信的corpid，必须与当前登录的企业一致
              agentid: this.agentId, // 必填，企业微信的应用id （e.g. 1000247）
              timestamp, // 必填，生成签名的时间戳
              nonceStr, // 必填，生成签名的随机串
              signature, // 必填，签名，见附录-JS-SDK使用权限签名算法
              jsApiList: ['sendChatMessage'], //必填
              success: (res) => {
                // 回调
              },
              fail: (res) => {
                if (res.errMsg.indexOf('function not exist') > -1) {
                  alert('版本过低请升级')
                }
              },
            })
          })
        })

        wx.error(function(res) {
          console.error(res)
          // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
        })
      })
    },
  },
}
</script>
<template>
  <div id="app">
    <router-view class="page" />
  </div>
</template>

<style lang="less">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
</style>
