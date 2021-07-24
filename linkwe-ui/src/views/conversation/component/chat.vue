<template>
  <div class="takecontent">
    <ul>
      <li v-for="(item, index) in allChat" :key="index">
        <!-- <span v-if="item.fromInfo.name">{{item.fromInfo.name}}</span> -->
        <div :style="{ color: item.action == 'send' ? '#199ed8' : '#999' }">
          <span v-if="item.fromInfo">{{ item.fromInfo.name }}</span>
          <span
            :style="{ color: item.action == 'send' ? '#199ed8' : '#999' }"
            >{{ parseTime(item.msgTime) }}</span
          >
        </div>
        <div v-if="item.msgType == 'text'" class="msgtypetext">
          {{ item.text.content }}
        </div>
        <div v-else-if="item.msgType == 'image'" class="msgtypeimg">
          <img :src="item.image.attachment" @click="showImg(item)" />
        </div>
        <div
          v-else-if="item.msgType == 'file'"
          class="msgtypefile"
          @click="down(item.file)"
        >
          {{ item.file.fileName }}
        </div>
        <div v-else-if="item.msgType == 'voice'" class="msgtypevoice">
          <i
            class="el-icon-microphone"
            style=" font-size: 40px; color: #199ed8;"
            @click="playVideo(item)"
          ></i>
        </div>
        <div v-else-if="item.msgType == 'emotion'" class="msgtypeimg">
          <img :src="item.emotion.attachment" @click="showImg(item)" />
        </div>
        <div v-else-if="item.msgType == 'video'" class="msgtypevideo">
          <i
            class="el-icon-video-play"
            style=" font-size: 40px;  color: #199ed8;"
            @click="play(item, 'video')"
          ></i>
        </div>
        <div v-else-if="item.msgType == 'location'" class="msgtypecard">
          <div class="card_name">
            <el-amap
              ref="map"
              vid="amapDemo"
              :center="[item.location.longitude, item.location.latitude]"
              :zoom="zoom"
              class="amap-demo"
              style="pointer-events: none;"
            >
              <el-amap-marker
                :position="[item.location.longitude, item.location.latitude]"
              ></el-amap-marker>
            </el-amap>
          </div>
          <div class="card_foot">{{ item.location.address }}</div>
        </div>
        <div v-else-if="item.msgType == 'weapp'" class="msgtypecard">
          <div class="card_name">{{ item.weApp.title }}</div>
          <div class="card_foot">小程序</div>
        </div>
        <div v-else-if="item.msgType == 'card'" class="msgtypecard ">
          <div class="card_name">{{ item.card.corpName }}</div>
          <div class="card_foot">个人名片</div>
        </div>
      </li>
    </ul>

    <div class="shabowbox" v-show="dia">
      <div class="close" @click="dia = false">
        <i class="el-icon-circle-close"></i>
      </div>
      <div class="shabowboxvidoe">
        <video-player
          class="video-player vjs-custom-skin"
          ref="videoPlayer"
          id="videoPlayer"
          :playsinline="true"
          :options="playerOptions"
        ></video-player>
      </div>
    </div>
    <div class="shabowbox" v-show="diavioce">
      <div class="close" @click="vioceClose">
        <i class="el-icon-circle-close"></i>
      </div>
      <div class="shabowboxvidoe shabowboxaudio">
        <AudioPlayer
          :audio-list="vioceSrc"
          ref="AudioPlayer"
          :before-play="onBeforePlay"
        />
      </div>
    </div>
    <el-dialog :visible.sync="dialogVisible" width="30%">
      <img :src="imgSrc" style="width:100%;max-height:600px" />
      <span slot="footer" class="dialog-footer"> </span>
    </el-dialog>
  </div>
</template>

<script>
import { AMapManager } from 'vue-amap'
import 'video.js/dist/video-js.css'
import 'vue-video-player/src/custom-theme.css'

import { parseTime, yearMouthDay } from '@/utils/common.js'
export default {
  components: {
    AMapManager
  },
  props: {
    allChat: {
      type: Array,
      defluat: () => []
    }
  },
  mounted() {},
  data() {
    return {
      dia: false,
      diavioce: false,
      dialogVisible: false,
      imgSrc: '',
      vioceSrc: [],
      playerOptions: {
        playbackRates: [0.7, 1.0, 1.5, 2.0], //播放速度
        autoplay: false, //如果true,浏览器准备好时开始回放。
        controls: true, //控制条
        preload: 'auto', //视频预加载
        muted: false, //默认情况下将会消除任何音频。
        loop: false, //导致视频一结束就重新开始。
        language: 'zh-CN',
        aspectRatio: '16:9', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
        fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
        sources: [
          {
            type: 'video/mp4',
            src: 'https://v-cdn.zjol.com.cn/280443.mp4' //你所放置的视频的地址，最好是放在服务器上
          }
        ],
        poster: '', //你的封面地址（覆盖在视频上面的图片）
        width: document.documentElement.clientWidth,
        height: '475',
        notSupportedMessage: '此视频暂无法播放，请稍后再试' //允许覆盖Video.js无法播放媒体源时显示的默认信息。
      },

      zoom: 15,
      center: [117.148118, 36.660223],
      markers: [
        {
          position: [117.148118, 36.660223],
          visible: false,
          draggable: false
        }
      ]
    }
  },
  methods: {
    vioceClose() {
      this.diavioce = false
      const mp3 = this.$refs.AudioPlayer
      mp3.pause()
    },
    playVideo(e) {
      this.vioceSrc = [e.voice.attachment]
      this.diavioce = true
    },
    onBeforePlay(next) {
      next() // 开始播放
    },
    showImg(e) {
      this.imgSrc = e.image.attachment
      this.dialogVisible = true
    },
    play(e) {
      this.dia = true
      const player = this.$refs.videoPlayer.player
      this.playerOptions['sources'][0]['src'] = e.video.attachment
      player.play()
    },
    down(e) {
      const url = window.URL.createObjectURL(
        new Blob([e.attachment], {
          type:
            'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8'
        })
      )
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', e.filename) // 下载文件的名称及文件类型后缀
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link) // 下载完成移除元素
      window.URL.revokeObjectURL(url) // 释放掉blob对象
    }
  }
}
</script>
<style lang="scss" scoped>
// * {
//   padding: 0;
//   margin: 0;
// }

#videoPlayer /deep/ .vjs-tech {
  height: 450px;
}

.shabowbox {
  position: fixed;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  left: 0;
  top: 0;
  z-index: 2000;
}

.shabowboxvidoe {
  position: fixed;
  width: 800px;
  height: 475px;
  left: 50%;
  margin-left: -400px;
  top: 50%;
  margin-top: -235px;
  z-index: 2001;
  background: #fff;
}

.shabowboxaudio {
  height: 125px;
  padding: 12px;
}

.close {
  position: fixed;
  width: 50px;
  height: 50px;
  right: 10px;
  z-index: 2012;
  top: 10px;
  text-align: center;
  line-height: 50px;
  font-size: 20px;
  color: #fff;
  cursor: pointer;
  font-size: 43px;
}

.takecontent {
  text-align: left;
  width: 100%;
  height: 600px;
  overflow-y: scroll;

  ::-webkit-scrollbar {
    display: none;
  }

  ul li {
    padding: 8px;
  }

  .msgtypetext {
    padding: 10px 0;
  }

  .msgtypevoice {
    margin: 10px;
  }

  .msgtypefile {
    margin: 10px;
    width: 200px;
    height: 40px;
    line-height: 40px;
    cursor: pointer;
    color: #199ed8;
    text-indent: 10px;
    box-shadow: 0 1px 4px 0 rgba(0, 0, 0, 0.2), 0 1px 4px 0 rgba(0, 0, 0, 0.19);
  }

  .card_name {
    width: 320px;
    height: 105px;
    line-height: 80px;
    text-indent: 10px;
  }

  .msgtypevideo {
    margin: 10px;

    cursor: pointer;

    border-radius: 8px;
  }

  .msgtypeimg {
    width: 100px;
    height: 80px;
    margin: 10px;

    img {
      width: 100px;
      height: 80px;
    }
  }

  .msgtypecard {
    width: 320px;
    height: 140px;
    margin: 10px;
    border-radius: 8px;
    -webkit-box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2),
      0 6px 8px 0 rgba(0, 0, 0, 0.19);
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 8px 0 rgba(0, 0, 0, 0.19);
    position: relative;

    .card_foot {
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
}
</style>
