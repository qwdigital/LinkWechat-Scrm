<script>
// import { AMapManager } from 'vue-amap'
// import Video from 'video.js'
export default {
  name: 'ChatContent',
  props: {
    message: {
      type: Object,
      default: () => ({})
    }
  },
  components: {},
  data() {
    return {
      dialogVisible: false,
      audioSrc: []
    }
  },
  computed: {
    content() {
      let typeDict = {
        text: 'content',
        image: 'attachment',
        emotion: 'attachment',
        file: 'filename',
        docmsg: 'title',
        video: 'attachment',
        voice: 'attachment',
        location: 'address',
        weapp: 'title'
        // card: 'corpname'
      }
      let attr = typeDict[this.message.msgType]
      if (attr) {
        return JSON.parse(this.message.contact)[typeDict[this.message.msgType]]
      } else {
        return JSON.parse(this.message.contact)
      }
    }
  },
  watch: {},
  created() {},
  mounted() {},
  methods: {
    close() {
      this.dialogVisible = false
      const mp3 = this.$refs.AudioPlayer
      mp3.pause()
    },
    playAudio(type) {
      this.audioSrc = [JSON.parse(this.message.contact)[type]]
      this.dialogVisible = true
    },
    onBeforePlay(next) {
      next() // 开始播放
    },
    play(e) {
      this.dia = true
      const player = this.$refs.videoPlayer.player
      this.playerOptions['sources'][0]['src'] = e.attachment
      player.play()
    }
  }
}
</script>

<template>
  <div class="message">
    <template v-if="message.msgType === 'text'">
      {{ content }}
    </template>
    <template v-else-if="'image,emotion'.includes(message.msgType)">
      <el-image style="width: 100px; height: 100px" :src="content" fit="fit" :preview-src-list="[content]"> </el-image>
    </template>
    <a
      v-else-if="'file'.includes(message.msgType)"
      class="msgtypefile"
      :href="JSON.parse(this.message.contact).attachment"
      target="balnk"
    >
      {{ content }}
    </a>
    <a
      v-else-if="'docmsg'.includes(message.msgType)"
      class="msgtypefile"
      :href="JSON.parse(this.message.contact).link_url"
      target="balnk"
    >
      {{ content }}
    </a>
    <div v-else-if="message.msgType === 'voice'">
      <i class="el-icon-microphone" style=" font-size: 40px; color: #199ed8;" @click="playAudio('attachment')"></i>
    </div>
    <template v-else-if="message.msgType === 'video'">
      <video
        id="video"
        class="video-js vjs-default-skin vjs-big-play-centered"
        controls
        webkit-playsinline="true"
        playsinline="true"
        :autoplay="false"
        preload="none"
      >
        <source :src="content" type="video/mp4" />
      </video>
      <!-- <video-player
        class="video-player vjs-custom-skin"
        ref="videoPlayer"
        style="width: 150px;"
        :playsinline="true"
        :options="{
          playbackRates: [0.7, 1.0, 1.5, 2.0], //播放速度
          autoplay: false, //如果true,浏览器准备好时开始回放。
          controls: true, //控制条
          preload: 'none', //视频预加载
          muted: false, //默认情况下将会消除任何音频。
          loop: false, //导致视频一结束就重新开始。
          language: 'zh-CN',
          aspectRatio: '16:9', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（
          fluid: true,
          sources: [
            {
              type: 'video/mp4',
              src: content
            }
          ],
          poster: '',
          width: '175',
          height: '75',
          notSupportedMessage: '此视频暂无法播放，请稍后再试'
        }"
      ></video-player> -->
    </template>
    <div v-else-if="message.msgType === 'location'" class="msgtypecard">
      <div class="card-content">
        <el-amap
          ref="map"
          vid="amapDemo"
          :center="[JSON.parse(message.contact).longitude, JSON.parse(message.contact).latitude]"
          :zoom="15"
          class="amap-demo"
          style="pointer-events: none;"
        >
          <el-amap-marker
            :position="[JSON.parse(message.contact).longitude, JSON.parse(message.contact).latitude]"
          ></el-amap-marker>
        </el-amap>
      </div>
      <div class="card-foot">{{ content }}</div>
    </div>
    <div v-else-if="message.msgType === 'weapp'" class="msgtypecard">
      <div class="card-content">{{ content }}</div>
      <div class="card-foot">小程序</div>
    </div>
    <div v-else-if="message.msgType === 'card'" class="msgtypecard ">
      <div class="card-content">
        <span style="flex: none;"> {{ content.corpname }}： </span>
        <span>
          {{ content.userid }}
        </span>
      </div>
      <div class="card-foot">个人名片</div>
    </div>
    <!-- 图文链接 -->
    <div v-else-if="message.msgType === 'link'" class="msgtypecard ">
      <a :href="content.link_url">
        <div class="card-content fxbw">
          <div>
            <div class="card--link-title">{{ content.title }}</div>
            <div class="card--link-desc">{{ content.description }}</div>
          </div>
          <el-image style="width: 50px; height: 50px" :src="content.image_url" fit="fit"></el-image>
        </div>
      </a>
    </div>
    <div v-else>不支持的消息类型：{{ message.msgType }}</div>

    <el-dialog v-if="audioSrc[0]" :visible.sync="dialogVisible" width="30%" @close="close">
      <div class="shabowboxvidoe shabowboxaudio">
        <!-- <AudioPlayer
          :audio-list="audioSrc"
          ref="AudioPlayer"
          :before-play="onBeforePlay"
        /> -->
        <audio controls>
          <source :src="audioSrc[0]" type="audio/mpeg" />
        </audio>
      </div>
      <span slot="footer" class="dialog-footer"> </span>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.message {
  position: relative;
  padding: 10px;
  line-height: 14px;
  border-radius: 5px;
  background: $blue;
  color: #fff;
  display: inline-block;
  &::before {
    content: '';
    display: inline-block;
    position: absolute;
    left: -13px;
    width: 0;
    height: 0;
    border: 7px solid transparent;
    border-right-color: $blue;
  }
}
.msgtypefile {
  margin: 5px;
  width: 200px;
  display: inline-block;
  line-height: 30px;
  cursor: pointer;
  color: #199ed8;
  // text-indent: 10px;
  // box-shadow: 0 1px 4px 0 rgba(0, 0, 0, 0.2), 0 1px 4px 0 rgba(0, 0, 0, 0.19);
}

.msgtypecard {
  width: 320px;
  // height: 140px;
  margin: 10px 5px;
  border-radius: 8px;
  -webkit-box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 8px 0 rgba(0, 0, 0, 0.19);
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 8px 0 rgba(0, 0, 0, 0.19);
  position: relative;
  background: #f8f8f8;
  overflow: hidden;

  .card-content {
    width: 320px;
    min-height: 105px;
    height: 100%;
    display: flex;
    align-items: center;
    padding: 10px;
    line-height: 1.5;
    // text-indent: 10px;
    color: #333;
    .card--link-title {
      font-size: 16px;
      font-weight: 500;
      margin-bottom: 5px;
      text-overflow: -o-ellipsis-lastline;
      overflow: hidden;
      text-overflow: ellipsis;
      word-break: break-all;
      display: -webkit-box;
      white-space: pre-wrap;
      -webkit-line-clamp: 2;
      line-clamp: 2;
      -webkit-box-orient: vertical;
    }
    .card--link-desc {
      font-size: 14px;
      color: #aaa;
    }
  }
  .card-foot {
    position: absolute;
    height: 20px;
    border-top: 1px solid #efefef;
    text-align: left;
    bottom: 15px;
    padding: 10px;
    color: #333;
    font-weight: bold;
    width: 100%;
  }
}
</style>
