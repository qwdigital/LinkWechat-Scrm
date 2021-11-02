<template>
  <div>
    <div class="task-wrapper">
      <div class="header">
        <span> {{ task.taskName }} </span>
      </div>

      <div class="keywords">
        <template v-for="keyword of keywords">
          <van-tag size="medium" :key="keyword"> {{ keyword }} </van-tag>
        </template>
      </div>

      <div class="guide" v-if="task.welcomeMsg">
        <div @touchstart.prevent="touchStart" @touchend="touchEnd">
          {{ task.welcomeMsg }}
        </div>
        <div class="copy-wrapper" v-show="showCopy">
          <van-button :class="'copy-btn_' + task.taskId" :data-clipboard-text="task.welcomeMsg">
            复制
          </van-button>
        </div>
      </div>

      <div class="content">
        <div v-if="task.groupCodeInfo && task.groupCodeInfo.codeUrl">
          <van-image width="50" :src="task.groupCodeInfo.codeUrl"></van-image>
        </div>

        <div class="group">
          <span> {{ task.taskName }} </span>
        </div>
      </div>

      <div class="bottom-area">
        <span class="time"> {{ task.createTime }} </span>

        <van-button type="info" size="mini" @click="send">发送</van-button>
      </div>
    </div>

    <div class="bottom-line"></div>
  </div>
</template>

<script>
import ClipboardJS from 'clipboard'

export default {
  name: 'KeywordPanel',
  props: {
    task: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      showCopy: false, // 展示复制按钮
      touchDelay: 750, // 触发显示按钮的长按时常
      copyEvent: null,
      touch: false
    }
  },
  methods: {
    send() {
      let _this = this
      this.$toast.loading({
        message: '正在发送...',
        duration: 0,
        forbidClick: true
      })
      try {
        let news = {
          link: this.groupCodeUrl,
          title: this.task.taskName,
          desc: this.task.welcomeMsg,
          imgUrl: this.imgUrl
        }
        wx.invoke(
          'sendChatMessage',
          {
            msgtype: 'news',
            news
          },
          function(res) {
            if (res.err_msg == 'sendChatMessage:ok') {
            } else {
              if (res.err_code == 1) {
                // 用户取消发送
              } else {
                _this.$dialog({ message: 'sendChatMessage失败：' + JSON.stringify(res) })
              }
            }
            _this.$toast.clear()
          }
        )
      } catch (err) {
        _this.$toast.clear()
        _this.$dialog({ message: 'err:' + err.name + ',' + err.message })
      }
    },
    touchStart() {
      clearTimeout(this.copyEvent)

      this.copyEvent = setTimeout(() => {
        this.touch = true
      }, this.touchDelay)
    },
    touchEnd() {
      clearTimeout(this.copyEvent)
      if (this.touch) this.showCopy = true
      this.touch = false
    }
  },
  computed: {
    keywords() {
      if (!this.task || !this.task.keywordList) return []
      const keywords = this.task.keywordList.map((k) => k.keyword)
      return keywords
    },
    groupCodeUrl() {
      let groupCodeInfo = this.task.groupCodeInfo
      if (window.location.hash[0] === '#') {
        return (
          window.location.origin + window.location.pathname + '#/groupCode?id=' + (groupCodeInfo && groupCodeInfo.id)
        )
      }

      return window.location.origin + window.location.pathname + '#/groupCode?id=' + (groupCodeInfo && groupCodeInfo.id)
    },
    imgUrl() {
      let groupCodeInfo = this.task.groupCodeInfo
      return groupCodeInfo && groupCodeInfo.codeUrl ? groupCodeInfo.codeUrl : ''
    }
  },
  mounted() {
    this.clipboard = new ClipboardJS('.copy-btn_' + this.task.taskId)

    this.clipboard.on('success', (e) => {
      this.showCopy = false
    })

    this.clipboard.on('error', (e) => {
      this.showCopy = false
    })
  }
}
</script>

<style lang="less" scoped>
.task-wrapper {
  padding: 10px 8px;

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 5px 0 12px;

    span {
      font-size: 18px;
      font-weight: bold;
    }
  }

  .keywords {
    margin-bottom: 15px;

    .van-tag {
      margin-right: 10px;
      background-color: #f2f2f2;
      color: rgba(0, 0, 0, 0.6);
    }
  }

  .guide {
    padding: 15px;
    background-color: #f2f2f2;
    font-size: 16px;
    line-height: 20px;
    color: #999999;
    margin-bottom: 15px;
    min-height: 30px;
    position: relative;

    .copy-wrapper {
      position: absolute;
      width: 100%;
      height: 100%;
      left: 0;
      top: 0;
      background-color: rgba(0, 0, 0, 0.4);
      display: flex;
      align-items: center;
      justify-content: space-around;
    }
  }

  .content {
    padding: 10px;
    background-color: #f2f2f2;
    display: flex;

    .group {
      padding: 0 10px;
      display: flex;
      flex-direction: column;
      justify-content: space-between;

      .group-name {
        color: #999999;
      }
    }
  }

  .van-cell {
    padding: 10px 6px;
  }

  .bottom-area {
    padding: 10px 5px 5px;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .time {
      color: rgba(0, 0, 0, 0.3);
    }

    .van-button {
      padding: 0 12px;
      margin-right: 12px;
      border-radius: 4px;
      font-size: 12px;
    }
  }
}

.bottom-line {
  background-color: #f2f2f2;
  height: 15px;
}
</style>
