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
                  <van-button plain round type="info" @click="copyFn($event, unit.phone)">复制</van-button>
                </div>
              </div>
            </template>
          </van-cell>
        </van-cell-group>
      </van-tab>
      <!-- <van-tab :title="'待通过（' + total2 + '）'">
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
      </van-tab> -->
      <van-tab :title="'已添加（' + total1 + '）'">
        <van-cell-group>
          <van-cell>
            <template v-for="(unit, key) in list1">
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
  import { getTypeList, setState } from '@/api/seas.js'

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
     watch: {
    '$store.state.agentConfigStatus'(val) {
      val && this.init()
    }
  },
    methods: {
      init() {
        this.getData()
        this.getData1()
      },
    
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
      setGoto (text) {
        setState({ phone: text }).then(res => {
          this.getData()
          this.getData1()
          wx.invoke('navigateToAddCustomer',
            {},
            function (res) {
            });
        })
      },
      copyFn (e, text) {
        const clipboard = new ClipboardJS(e.target, { text: () => text })
        clipboard.on('success', e => {
          this.setGoto(text)
          this.$toast({ type: 'success', message: '复制成功' })
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
      // this.init()
    },
    computed: {
      userId() {
        return this.$store.state.userId // 员工Id
      }
    },
    created () {
      
    }
  }
</script>
<style lang="less" scoped>
  .page {
    height: 100vh;
    background: #f6f6f6;
  }
  .content {
    font-size: 15px;
    font-weight: 500;
    display: flex;
    justify-content: space-between;
    align-items: center;
    line-height: 60px;
  }

  /deep/ .van-button--info {
    border: none;
  }
</style>
