<template>
  <div>
    <div class="crumb-title">
      <div class="crumb">
        当前位置：
        <el-breadcrumb separator=">">
          <el-breadcrumb-item>
            <a href="/customerMaintain/groupMessage">消息群发</a>
          </el-breadcrumb-item>
          <el-breadcrumb-item>群发详情</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div>群发详情</div>
    </div>
    <el-row style="margin-top: 10px;" :gutter="10">
      <el-col :span="18">
        <div class="g-card g-pad20">
          <el-tabs v-model="activeName">
            <el-tab-pane label="待发送员工" name="0">
              <TabContent type="0" :total.sync="total0"></TabContent>
            </el-tab-pane>
            <el-tab-pane label="已发送员工" name="1">
              <TabContent type="1" :total.sync="total1"></TabContent>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="g-card g-pad20">
          <PhoneDialog style="margin: 0 auto;" :message="data.welcomeMsg || '请输入欢迎语'">
            <!-- <el-image style="border-radius: 6px;" :src="materialSelected" fit="fit">
						</el-image> -->
          </PhoneDialog>
        </div>

      </el-col>
    </el-row>

    <!-- <el-card shadow="hover" class="mb10">
      <span class="label">创建人：</span>
      {{ data.sender }}
      <div class="mt10 flex aic">
        <span class="label">消息内容：</span>
        <el-image
          v-if="/^http.*\.(png|jpeg|jpg|gif)$/gi.test(data.content)"
          style="width: 80px; height: 80px;border-radius:6px"
          :src="data.content"
          fit="fit"
        ></el-image>
        <span v-else>{{ data.content }}</span>
      </div>
    </el-card> -->
    <!-- <el-button class="notice" type="primary" @click="notice"
      >通知员工发送</el-button
    > -->

  </div>
</template>

<script>
  import {
    getDetail
  } from '@/api/groupMessage'
  import TabContent from './TabContent'
  import PhoneDialog from '@/components/PhoneDialog'
  export default {
    components: {
      TabContent,
      PhoneDialog
    },
    props: {},
    data () {
      return {
        data: {},
        activeName: '0',
        total1: 0,
        total0: 0
      }
    },
    watch: {},
    computed: {},
    created () {

    },
    mounted () { },
    methods: {
      getDetail () {
        getDetail(this, this.$route.query.id).then(({ data }) => {
          if (res.code == 200) {
            this.data = data
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
