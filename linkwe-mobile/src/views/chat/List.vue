<script>
import {
  getMaterialList,
  getCollectionList,
  addCollection,
  cancleCollection,
  getMaterialMediaId
} from '@/api/chat'
export default {
  components: {},
  props: {
    sideId: {
      type: String,
      default: ''
    },
    // mediaType 0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file) 4 文本 5 海报
    mediaType: {
      type: String,
      default: '0'
    },
    keyword: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      refreshing: false,
      loading: false,
      finished: false,
      error: false,
      pageNum: 1,
      pageSize: 10,
      list: [],
      collectList: [],
      mediaTypeObj: {
        '0': '图片',
        '1': '语音',
        '2': '视频',
        '3': '普通文件',
        '4': '文本',
        '5': '海报'
      }
    }
  },
  watch: {
    userId() {
      this.getList(1)
    }
  },
  computed: {
    userId() {
      return this.$store.state.userId
    }
  },
  created() {
    this.getList()
    // this.userId && this.getCollectionList()
  },
  mounted() {},
  methods: {
    getList(page) {
      this.loading = true
      this.finished = false
      let pageNum = page || this.pageNum
      let pageSize = this.pageSize
      let keyword = this.keyword
      if (pageNum == 1) {
        this.list = []
        this.pageNum = 1
      }
      ;(this.sideId
        ? getMaterialList({
            userId: this.userId,
            sideId: this.sideId,
            mediaType: this.mediaType,
            pageSize,
            pageNum,
            keyword
          })
        : getCollectionList({ userId: this.userId, pageSize, pageNum, keyword })
      )
        .then(({ rows, total }) => {
          // 判断我的列表
          if (!this.sideId) {
            rows.forEach((element) => {
              element.collection = 1
            })
          }

          this.list.push(...rows)

          this.loading = false
          this.refreshing = false
          // 数据全部加载完成
          if (this.list.length >= +total) {
            this.finished = true
          } else {
            this.pageNum++
          }
          if (this.list.length == 0) {
            this.pageNum = 1
          }
        })
        .catch(() => {
          this.error = true
        })
    },
    // getCollectionList() {
    //   let data = { userId: this.userId, pageSize: 1000, pageNum: 1 }
    //   getCollectionList(data).then(({ rows, total }) => {
    //     this.collectList = rows.map((d) => d.materialId)
    //   })
    // },
    send(data) {
      this.$toast.loading({
        message: '正在发送...',
        duration: 0,
        forbidClick: true
      })
      let entry = undefined
      let _this = this
      wx.invoke('getContext', {}, async function(res) {
        if (res.err_msg == 'getContext:ok') {
          entry = res.entry //返回进入H5页面的入口类型，目前 contact_profile、single_chat_tools、group_chat_tools
          let mes = {}
          try {
            if (!['single_chat_tools', 'group_chat_tools', 'normal'].includes(entry)) {
              // _this.$toast.clear()
              _this.$toast('入口错误：' + entry)
              return
            }

            // mediaType 0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file) 4 文本 5 海报
            // msgtype 文本(“text”)，图片(“image”)，视频(“video”)，文件(“file”)，H5(“news”）和小程序(“miniprogram”)

            let msgtype = {
              0: 'image',
              2: 'video',
              3: 'file',
              4: 'text',
              5: 'image'
            }
            switch (data.mediaType) {
              case '4':
              default:
                mes.text = {
                  content: data.content //文本内容
                }
                break
              case '0':
              case '2':
              case '3':
              case '5':
                let dataMediaId = {
                  url: data.materialUrl,
                  type: msgtype[data.mediaType],
                  name: data.materialName
                }
                let resMaterialId = await getMaterialMediaId(dataMediaId)
                if (!resMaterialId.data) {
                  _this.$toast('获取素材id失败')
                  return
                }
                mes[msgtype[data.mediaType]] = {
                  mediaid: resMaterialId.data.media_id //
                }
                break
              // case '5':
              //   mes.news = {
              //     link: '', //H5消息页面url 必填
              //     title: '', //H5消息标题
              //     desc: '', //H5消息摘要
              //     imgUrl: '' //H5消息封面图片URL
              //   }
              //   break
              // case '6':
              //   mes.miniprogram = {
              //     appid: 'wx8bd80126147df384', //小程序的appid
              //     title: '_this is title', //小程序消息的title
              //     imgUrl:
              //       'https://search-operate.cdn.bcebos.com/d054b8892a7ab572cb296d62ec7f97b6.png', //小程序消息的封面图。必须带http或者https协议头，否则报错 $apiName$:fail invalid imgUrl
              //     page: '/index/page.html', //小程序消息打开后的路径，注意要以.html作为后缀，否则在微信端打开会提示找不到页面
              //   }
              //   break
            }
            mes.msgtype = msgtype[data.mediaType]
            // _this.$dialog({ message: 'mes：' + JSON.stringify(mes) })
          } catch (err) {
            _this.$dialog({ message: 'err' + JSON.stringify(err) })
          }
          wx.invoke('sendChatMessage', mes, function(resSend) {
            if (resSend.err_msg == 'sendChatMessage:ok') {
              //发送成功 sdk会自动弹出成功提示，无需再加
              // _this.$toast('发送成功')
            } else {
              //错误处理
              // _this.$dialog({ message: '发送失败：' + JSON.stringify(resSend) })
            }
          })
          _this.$toast.clear()
        } else {
          _this.$toast.clear()
          //错误处理
          _this.$dialog({ message: '进入失败：' + JSON.stringify(res) })
        }
      })
    },
    collect(data) {
      this.$toast.loading({
        message: 'loading...',
        duration: 0,
        forbidClick: true
      })
      // collection 是否收藏 0未收藏 1 已收藏
      ;(data.collection == 1 ? cancleCollection : addCollection)({
        userId: this.userId,
        materialId: data.materialId
      })
        .then(() => {
          data.collection = [1, 0][data.collection]
          this.$toast('操作成功')
          this.$emit('collect')
        })
        .catch((err) => {
          this.$toast('操作失败')
          console.log(err)
        })
    }
  }
}
</script>

<template>
  <div>
    <van-pull-refresh v-model="refreshing" success-text="刷新成功" @refresh="getList(1)">
      <van-list
        v-model="loading"
        :finished="finished"
        finished-text="没有更多了"
        :error.sync="error"
        error-text="请求失败，点击重新加载"
        @load="getList()"
      >
        <div v-for="(item, index) in list" class="list" :key="index">
          <div class="content bfc-o">
            <!-- 图片 -->
            <van-image
              v-if="[0, 5].includes(+item.mediaType)"
              width="80"
              height="80"
              :src="item.materialUrl"
            />
            <!-- 视频 -->
            <video
              v-if="item.mediaType == 2"
              class="video"
              controls
              webkit-playsinline="true"
              playsinline="true"
              :autoplay="false"
              preload="auto"
              :poster="item.coverUrl"
            >
              <source :src="item.materialUrl" type="video/mp4" />
            </video>
            <!-- <van-image v-if="item.mediaType == 2" width="80" height="80" :src="item.coverUrl" /> -->
            <span class="title">
              {{ item.mediaType == 4 ? item.content : item.materialName }}
            </span>
          </div>
          <div class="info">
            {{ mediaTypeObj[item.mediaType] }}类型
            <span class="time">{{ item.createTime }}</span>

            <div class="fr flex">
              <div class="action" @click="send(item)">发送</div>
              <div v-if="!!userId" class="action" @click="collect(item)">
                {{ item.collection == 1 ? '取消' : '' }}收藏
              </div>
            </div>
          </div>
        </div>
      </van-list>
    </van-pull-refresh>
  </div>
</template>

<style lang="less" scoped>
.content {
  // display: flex;
  .van-image,
  .video {
    float: left;
    width: 80px;
    height: 80px;
    margin: 0 10px 5px 0;
    border: 1px solid #eee;
  }
  .title {
    word-break: break-all;
  }
}
.time {
  margin-left: 5px;
}
</style>
