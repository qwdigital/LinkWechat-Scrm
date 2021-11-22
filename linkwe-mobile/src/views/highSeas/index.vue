<template>
  <div>
    <van-tabs v-model="active">
      <van-tab :title="'待添加（' + total + '）'">
        <van-cell-group>
          <van-cell>
            <template v-for="(unit, key) in list">
              <div class="content" :key="key">
                <div>{{unit.phone}}</div>
                <div>
                  {{unit.customerName}}
                </div>
                <div>
                  <van-button plain size="mini" round type="info" @click="copyFn($event, unit.phone)">复制</van-button>
                </div>
              </div>
            </template>
          </van-cell>
        </van-cell-group>
      </van-tab>
      <van-tab :title="'待通过（' + total2 + '）'">
        <van-cell-group>
          <van-cell>
            <template v-for="(unit, key) in list2">
              <div class="content" :key="key">
                <div>{{unit.phone}}</div>
                <div>
                  {{unit.customerName}}
                </div>
              </div>
            </template>
          </van-cell>
        </van-cell-group>
      </van-tab>
      <van-tab :title="'已添加（' + total2 + '）'">
        <van-cell-group>
          <van-cell>
            <template v-for="(unit, key) in list2">
              <div class="content" :key="key">
                <div>{{unit.phone}}</div>
                <div>
                  {{unit.customerName}}
                </div>
              </div>
            </template>
          </van-cell>
        </van-cell-group>
      </van-tab>
    </van-tabs>
  </div>
</template>
 <script src="https://open.work.weixin.qq.com/wwopen/js/jwxwork-1.0.0.js"></script>
<script>
  import ClipboardJS from 'clipboard'
  import { getTypeList } from '@/api/seas.js'
  export default {
    name: 'highSeas-index',
    data () {
      return {
        active: 0,
        query: {
          pageSize: 500,
          pageNum: 1,
          addState: 0, // 0:待添加;1:已添加;3:待通过
          userId: ''
        },
        list: [],
        total: 0,
        total1: 0,
        total2: 0,
        list1: [],
        list2: []
      }
    },
    methods: {
      getData () {
        let obj = {
          pageSize: 500,
          pageNum: 1,
          addState: 0, // 0:待添加;1:已添加;3:待通过
          userId: this.userId
        }
        getTypeList(obj).then(res => {
          this.total = res.total
          this.list = res.rows
        })
      },
      getData1 () {
        let obj = {
          pageSize: 500,
          pageNum: 1,
          addState: 1, // 0:待添加;1:已添加;3:待通过
          userId: this.userId
        }
        getTypeList(obj).then(res => {
          this.total1 = res.total
          this.list1 = res.rows
        })
      },
      getData2 () {
        let obj = {
          pageSize: 500,
          pageNum: 1,
          addState: 3, // 0:待添加;1:已添加;3:待通过
          userId: this.userId
        }
        getTypeList(obj).then(res => {
          this.total2 = res.total
          this.list3 = res.rows
        })
      },
      copyFn (e, text) {
        const clipboard = new ClipboardJS(e.target, { text: () => text })
        clipboard.on('success', e => {
          this.$toast({ type: 'success', message: '复制成功' })
          // wx.invoke('navigateToAddCustomer',
          //   {},
          //   function (res) {
          //   });
          // 释放内存
          clipboard.off('error')
          clipboard.off('success')
          clipboard.destroy()
        })
        clipboard.on('error', e => {
          // 不支持复制
          this.$toast({ type: 'fail', message: '该浏览器不支持自动复制', icon: 'none' })
          // 释放内存
          clipboard.off('error')
          clipboard.off('success')
          clipboard.destroy()
        })
        clipboard.onClick(e)
      }
    },
    mounted () {

      // wx.agentConfig({
      //   corpid: '', // 必填，企业微信的corpid，必须与当前登录的企业一致
      //   agentid: '', // 必填，企业微信的应用id （e.g. 1000247）
      //   timestamp: '', // 必填，生成签名的时间戳
      //   nonceStr: '', // 必填，生成签名的随机串
      //   signature: '',// 必填，签名，见附录-JS-SDK使用权限签名算法
      //   jsApiList: ['selectExternalContact'], //必填，传入需要使用的接口名称
      //   success: function (res) {
      //     // 回调
      //   },
      //   fail: function (res) {
      //     if (res.errMsg.indexOf('function not exist') > -1) {
      //       alert('版本过低请升级')
      //     }
      //   }
      // });
    },
    created () {
      this.userId = this.$route.query.userId
      this.getData()
      this.getData2()
      this.getData1()
    }
  }
</script>
<style lang="less" scoped>
  .page {
    height: 100vh;
    background: #f6f6f6;
  }
  .content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    line-height: 60px;
  }

  /deep/ .van-button--info {
    border: none;
  }
</style>
