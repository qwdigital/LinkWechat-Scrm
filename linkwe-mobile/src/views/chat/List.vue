<script>
import {
  getMaterialList,
  getCollectionList,
  addCollection,
  cancleCollection,
} from '@/api/chat'
export default {
  components: {},
  props: {
    sideId: {
      type: String,
      default: '0',
    },
    userId: {
      type: String,
      default: '',
    },
    keyword: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      loading: false,
      finished: false,
      error: false,
      pageSize: 1,
      pageNum: 10,
      list: [],
      collectList: [],
    }
  },
  watch: {},
  computed: {},
  created() {
    this.getList()
    this.userId && this.getCollectionList()
  },
  mounted() {},
  methods: {
    getList(page) {
      let pageSize = page || this.pageSize++
      let pageNum = this.pageNum
      let keyword = this.keyword
      page == 1 && (this.list = [])
      this.loading = true
      ;(this.sideId
        ? getMaterialList({ sideId: this.sideId, pageSize, pageNum, keyword })
        : getCollectionList({ userId: this.userId, pageSize, pageNum, keyword })
      )
        .then(({ rows, total }) => {
          this.list.push(...rows)
          this.loading = false
          // 数据全部加载完成
          if (this.list.length >= total) {
            this.finished = true
          }
        })
        .catch(() => {
          this.error = true
        })
    },
    getCollectionList() {
      let data = { userId: this.userId, pageSize: 1, pageNum: 1000 }
      getCollectionList(data).then(({ rows, total }) => {
        this.collectList = rows.map((d) => d.id)
      })
    },
    isCollected(id) {
      return this.collectList.includes(id)
    },
    send(data) {
      let entry = undefined
      wx.invoke('getContext', {}, function(res) {
        if (res.err_msg == 'getContext:ok') {
          entry = res.entry //返回进入H5页面的入口类型，目前有normal、contact_profile、single_chat_tools、group_chat_tools
          if (!['single_chat_tools', 'group_chat_tools'].includes(entry)) {
            return
          }
          // mediaType 0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file) 4 文本 5 海报
          // msgtype 文本(“text”)，图片(“image”)，视频(“video”)，文件(“file”)，H5(“news”）和小程序(“miniprogram”)
          let mes = {}
          let msgtype = {
            0: 'image',
            2: 'video',
            3: 'file',
            4: 'text',
            5: 'news',
          }
          switch (data.mediaType) {
            case '4':
            default:
              mes.text = {
                content: data.content, //文本内容
              }
              break
            case '0':
            case '2':
            case '3':
              mes[msgtype[data.mediaType]] = {
                mediaid: data.materialId, //
              }
              break
            case '5':
              mes.news = {
                link: '', //H5消息页面url 必填
                title: '', //H5消息标题
                desc: '', //H5消息摘要
                imgUrl: '', //H5消息封面图片URL
              }
              break
            // case '6':
            //   mes.miniprogram = {
            //     appid: 'wx8bd80126147df384', //小程序的appid
            //     title: 'this is title', //小程序消息的title
            //     imgUrl:
            //       'https://search-operate.cdn.bcebos.com/d054b8892a7ab572cb296d62ec7f97b6.png', //小程序消息的封面图。必须带http或者https协议头，否则报错 $apiName$:fail invalid imgUrl
            //     page: '/index/page.html', //小程序消息打开后的路径，注意要以.html作为后缀，否则在微信端打开会提示找不到页面
            //   }
            //   break
          }
          mes.msgtype = msgtype[data.mediaType]
          wx.invoke('sendChatMessage', mes, function(res) {
            if (res.err_msg == 'sendChatMessage:ok') {
              //发送成功
            }
          })
        } else {
          //错误处理
        }
      })
    },
    collect(materialId) {
      ;(this.isCollected(materialId) ? cancleCollection : addCollection)({
        userId: this.userId,
        materialId,
      }).then(({ data }) => {
        this.isCollected(materialId)
          ? this.isCollected.splice(this.isCollected.indexOf(materialId), 1)
          : this.isCollected.push(materialId)
      })
    },
  },
}
</script>

<template>
  <div>
    <van-list
      v-model="loading"
      :finished="finished"
      finished-text="没有更多了"
      :error.sync="error"
      error-text="请求失败，点击重新加载"
      @load="getList()"
    >
      <div v-for="(item, index) in list" class="list" :key="index">
        <div class="title">{{ item.content }}</div>
        <div class="info">
          {{ item.content }}
          <span>2020/2/4</span>

          <div class="fr flex">
            <div class="action" @click="send(item)">发送</div>
            <div v-if="!!userId" class="action" @click="collect(item.id)">
              {{ isCollected(item.id) ? '已' : '' }}收藏
            </div>
          </div>
        </div>
      </div>
    </van-list>
  </div>
</template>

<style lang="less" scoped></style>
