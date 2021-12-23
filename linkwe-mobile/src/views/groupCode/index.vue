<template>
  <div>
    <div v-if="!code || !code.actualQRCode">
      <van-empty :description="message" />
    </div>
    <div v-else class="code-wrapper">
      <div class="header">
        {{ code.activityName }}
      </div>

      <div class="welcome">
        {{ code.guide }}
      </div>

      <div class="code-name">
        {{ code.groupName }}
      </div>

      <div class="code-image">
        <van-image width="300" :src="code.actualQRCode"></van-image>
      </div>

      <div class="service" v-if="code.isOpenTip === '1'">
        <van-button type="danger" @click="dialog = true">无法加群?</van-button>
      </div>

      <van-dialog
        v-model="dialog"
        class="service-dialog"
        :showConfirmButton="false"
        :closeOnClickOverlay="true"
      >
        <div>
          <van-image :src="code.serviceQrCode" width="200"> </van-image>

          <div class="service-message">
            {{ code.tipMsg }}
          </div>

          <div class="tip">
            请长按二维码添加客服微信
          </div>
        </div>

        <van-icon name="cross" class="close" @click="dialog = false" />
      </van-dialog>
    </div>
  </div>
</template>

<script>
import { getDetail } from '@/api/groupCode'

export default {
  data() {
    return {
      groupCodeUUID: '', // 群活码ID
      code: {
        activityName: '', // 活码名称
        guide: '', // 加群引导语
        actualQRCode: '', // 实际群码
        isOpenTip: '', // 是否显示无法加群提示
        tipMsg: '', // 无法加群提示语
        serviceQrCode: '', // 客服二维码
        groupName: '' // 实际群名称
      },
      dialog: false,
      message: '抱歉，暂无可用实际群码'
    }
  },

  methods: {
    // 获取群活码详情
    getDetail() {
      if (!this.groupCodeUUID) return

      getDetail(this.groupCodeUUID)
        .then(({ data, msg }) => {
          this.code = Object.assign(this.code, data)
          document.title = data.activityName

          if (msg) this.message = msg
        })
        .catch(() => {})
    }
  },

  created() {
    this.groupCodeUUID = this.$route.query.id

    this.getDetail()
  }
}
</script>

<style lang="less" scoped>
.code-wrapper {
  margin: 20px 10px;

  .header {
    font-size: 18px;
    margin: 20px 10px;
    text-align: center;
  }

  .welcome {
    font-size: 16px;
    color: #aaaaaa;
    margin: 20px 10px 60px;
  }

  .code-name {
    font-size: 20px;
    margin: 20px 10px 2px;
    font-weight: bold;
    text-align: center;
  }

  .code-image {
    text-align: center;
    margin-bottom: 100px;
  }

  .service {
    text-align: center;

    .van-button {
      border-radius: 4px;
      height: 36px;
    }
  }
}

.service-dialog {
  text-align: center;
  border-radius: 0;
  padding: 20px;

  .service-message {
    margin-bottom: 30px;
  }

  .tip {
    font-size: 12px;
    color: #aaaaaa;
  }

  .close {
    position: absolute;
    top: 10px;
    right: 10px;
  }
}
</style>
