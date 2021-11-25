<script>
import BenzAMRRecorder from 'benz-amr-recorder'
export default {
  name: 'Voice',
  props: {
    amrUrl: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      actived: false
      // dialogVisible: false,
      // audioSrc: []
    }
  },
  computed: {},
  watch: {},
  created() {
    this.__proto__.$isVoice || document.addEventListener('click', this.stop)
    this.__proto__.$isVoice = true
  },
  mounted() {},
  methods: {
    play() {
      if (this.amrUrl) {
        if (this.$playRec) {
          if (this.stop(true)) {
            return
          }
        }
        this.__proto__.$playRec = new BenzAMRRecorder()
        this.__proto__.$playRecSymbole = this.amrUrl
        this.$playRec
          .initWithUrl(this.amrUrl)
          .then(() => {
            this.actived = true
            this.$playRec.play()
            this.$playRec.onEnded(() => {
              this.actived = false
            })
          })
          .catch(e => {
            this.$message.error('播放录音失败')
          })
      }
      // this.audioSrc = [JSON.parse(this.message.contact)[type]]
    },
    //停止播放
    stop(noset) {
      console.log(1)
      if (this.$playRec && this.$playRec.isPlaying()) {
        this.$playRec.stop()
        if (!noset || this.__proto__.$playRecSymbole === this.amrUrl) {
          this.__proto__.$playRecSymbole = ''
          return true
        }
      }
    }
    // close() {
    //   this.dialogVisible = false
    //   const mp3 = this.$refs.AudioPlayer
    //   mp3.pause()
    // },
    // onBeforePlay(next) {
    //   next() // 开始播放
    // }
  }
}
</script>

<template>
  <div>
    <i
      :class="['el-icon-microphone', actived && 'actived']"
      style=" font-size: 40px; color: #199ed8;"
      @click.stop="play('attachment')"
    ></i>

    <!-- <AudioPlayer
          :audio-list="audioSrc"
          ref="AudioPlayer"
          :before-play="onBeforePlay"
        /> -->
    <!-- <el-dialog v-if="audioSrc[0]" :visible.sync="dialogVisible" width="30%" @close="close">
      <div class="shabowboxvidoe shabowboxaudio">
        <audio controls>
          <source :src="audioSrc[0]" type="audio/mpeg" />
        </audio>
      </div>
      <span slot="footer" class="dialog-footer"> </span>
    </el-dialog> -->
  </div>
</template>

<style lang="scss" scoped>
@keyframes play {
  from {
    // color: red;
  }
  to {
    color: red;
  }
}
.actived {
  animation: play 1s infinite alternate;
}
</style>
