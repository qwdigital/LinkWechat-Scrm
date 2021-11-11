<template>
  <div>
    <el-row style="margin-top: 10px;" type="flex" :gutter="10">
      <el-col :span="18">
        <div class="g-card g-pad20">

        </div>
      </el-col>
      <el-col style="width: 350px">
        <div class="g-card g-pad20" style="height: 100%">
          <preview-client :list="form"></preview-client>
        </div>

      </el-col>
    </el-row>
  </div>
</template>

<script>
  import {
    getDetail
  } from '@/api/groupMessage'
  import PreviewClient from '@/components/previewInMobileClient.vue'

  export default {
    components: {
      PreviewClient
    },
    props: {},
    data () {
      return {
        form: {
          welcomeMsg: '',
          materialMsgList: [],
        },
        data: {},
        activeName: '0',
        total1: 0,
        total0: 0
      }
    },
    watch: {},
    computed: {},
    created () {
      this.getDetail()
    },
    mounted () { },
    methods: {
      getDetail () {
        getDetail(this.$route.query.id).then(res => {
          if (res.code == 200) {
            this.data = res.data
            this.form.welcomeMsg = res.data.content
            this.form.materialMsgList = res.data.attachments
          } else {
            this.msgError(res.msg || '获取失败')
          }
        })
      }
    }
  }
</script>



<style lang="scss" scoped>
  .label {
    width: 70px;
    display: inline-block;
    text-align: right;
  }

  .crumb {
    font-size: 12px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #666666;
    display: flex;
  }

  .crumb- {
    // 一级 页面标题
    &title {
      display: flex;
      flex-direction: column;
      justify-content: center;
      height: 90px; // line-height: 90px;
      font-size: 18px;
      font-weight: 500;
      color: #333;
      padding: 0 20px;
      background: #fff;
      border-top-left-radius: 4px;
      border-top-right-radius: 4px;
    }
  }
</style>
