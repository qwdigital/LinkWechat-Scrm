<script>
  import { upload } from '@/api/material'
  import Video from 'video.js'
  import BenzAMRRecorder from 'benz-amr-recorder'
  export default {
    name: 'Upload',
    components: {},
    props: {
      fileUrl: {
        type: String,
        default: ''
      },
      fileName: {
        type: String,
        default: ''
      },
      // 0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file)
      type: {
        type: String,
        default: '0'
      },
      // 上传文件大小不能超过 maxSize MB
      maxSize: {
        type: Number,
        default: 2
      },
      // 图片的宽高像素限制 [width(number), height(number)],默认null不限制
      maxImgPx: {
        type: Array,
        default: null // () => [100, 100]
      },
      // 限制允许上传的文件格式 eg:["bmp", "jpg", "png", "jpeg", "gif"]
      format: {
        type: Array,
        default: null
      }
      // beforeUpload: {
      //   type: Function,
      //   default: function() {
      //     return function() {}
      //   },
      // },
    },
    data () {
      return {
        loading: false,
        action: process.env.VUE_APP_BASE_API + '/common/uploadFile2Cos',
        // (this.type == 0
        //   ? '/wecom/material/uploadimg'
        //   : '/common/uploadFile2Cos'),
        headers: window.CONFIG.headers,
        domain: process.env.VUE_APP_BASE_API
      }
    },
    watch: {},
    computed: {
      accept () {
        return ['image/*', 'amr/*', 'video/*', '*'][this.type]
      }
    },
    created () { },
    mounted () { },
    methods: {
      async handleBeforeUpload (file) {
        this.loading = true
        let isFormat = true
        let isSize = true

        if (this.format && this.format.length) {
          // 校验格式
          let reg = /\.(\w+)$/g
          let match = file.name.match(reg)
          let fileFormat = match && match[0].replace('.', '').toLowerCase()
          if (!(isFormat = this.format.includes(fileFormat))) {
            this.$message.error('文件格式不正确，请重新选择')
          }
        }

        if (this.type === '0') {
          // 图片
          isFormat = file.type === 'image/jpeg' || file.type === 'image/png'
          isSize = file.size / 1024 / 1024 < this.maxSize

          if (!isFormat) {
            this.$message.error('上传文件只能是 JPG 格式!')
          }
          if (!isSize) {
            this.$message.error('上传文件大小不能超过 2MB!')
          }

          if (this.maxImgPx) {
            try {
              await new Promise((resolve) => {
                let width, height
                let image = new Image()
                //加载图片获取图片真实宽度和高度
                image.onload = () => {
                  width = image.width
                  height = image.height
                  if (width > this.maxImgPx[0]) {
                    isSize = false
                    this.$message.error('图片“宽”度超限，请重新选择')
                  } else if (height > this.maxImgPx[1]) {
                    this.$message.error('图片“高”度超限，请重新选择')
                    isSize = false
                  }
                  window.URL && window.URL.revokeObjectURL(image.src)
                  resolve()
                }
                if (window.URL) {
                  let url = window.URL.createObjectURL(file)
                  image.src = url
                } else if (window.FileReader) {
                  let reader = new FileReader()
                  reader.onload = function (e) {
                    let data = e.target.result
                    image.src = data
                  }
                  reader.readAsDataURL(file)
                }
              })
            } catch (e) {
              console.error(e)
            }
          }
        } else if (this.type === '1') {
          // 语音
          isFormat = /.amr$/gi.test(file.name)
          isSize = file.size / 1024 / 1024 < 2

          if (!isFormat) {
            this.$message.error('上传文件只能是 amr 格式!')
          }
          if (!isSize) {
            this.$message.error('上传文件大小不能超过 2MB!')
          }
          let amr = new BenzAMRRecorder()
          try {
            await amr.initWithBlob(file)
            isSize = amr.getDuration() <= 60
            if (!isSize) {
              this.$message.error('上传文件时长不能超过 60秒!')
            }
          } catch (error) {
            console.log(error)
            this.$message.error('文件损坏')
          }
        } else if (this.type === '2') {
          // 视频
          isFormat = file.type === 'video/mp4'
          isSize = file.size / 1024 / 1024 < 10

          if (!isFormat) {
            this.$message.error('上传文件只能是 mp4 格式!')
          }
          if (!isSize) {
            this.$message.error('上传文件大小不能超过 10MB!')
          }
        } else if (this.type === '3') {
          // 普通文件
          isSize = file.size / 1024 / 1024 < 20

          if (!isSize) {
            this.$message.error('上传文件大小不能超过 20MB!')
          }
        }
        if (!isFormat || !isSize) {
          this.loading = false
        }

        // if (beforeUpload) {
        //   return beforeUpload(file)
        // }
        return (isFormat && isSize) || Promise.reject()
      },
      onSuccess (res, file) {
        if (res.code === 200) {
          this.loading = false
          // this.$emit('update:fileUrl', res.data.materialUrl)
          // this.$emit('update:fileName', res.data.materialName)

          // if (this.type == 0) {
          //   res.url = res.data.url
          //   res.fileName = res.data.url
          // }
          this.$emit('update:fileUrl', res.data.url)
          this.$emit('update:fileName', res.data.fileName)
          this.$emit('update:file', file)
          // this.fileUrl = URL.createObjectURL(file.raw)
        } else {
          this.loading = false
          this.$message.error(res.msg)
        }
      },
      onError (err, file, fileList) {
        this.loading = false
        this.$message.error('上传文件失败')
      }
    }
  }
</script>

<template>
  <div>
    <el-upload v-loading="loading" element-loading-text="正在上传..." class="uploader" :accept="accept" :action="action" :headers="headers" :data="{ mediaType: type }" :show-file-list="false" :on-success="onSuccess" :on-error="onError" :before-upload="handleBeforeUpload">
      <slot>
        <template v-if="fileUrl">
          <img v-if="type === '0'" :src="fileUrl" class="upload-img" />
          <div v-else-if="type === '2'">
            <video id="myVideo" class="video-js vjs-default-skin
            vjs-big-play-centered" width="100%" controls webkit-playsinline="true" playsinline="true" :autoplay="false" preload="auto">
              <source :src="fileUrl" type="video/mp4" />
            </video>
          </div>
          <div v-else>{{ fileName }}</div>
        </template>
        <i v-else class="el-icon-plus uploader-icon"></i>
      </slot>
    </el-upload>
    <div class="tip">
      <slot name="tip"></slot>
    </div>
  </div>
</template>

<style lang="scss" scoped>
  /deep/.uploader {
    display: inline-block;
    .el-upload {
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      &:hover {
        border-color: #409eff;
      }
    }
  }

  .uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
    border-radius: 6px;
    border: 1px dashed #d9d9d9;
  }
  .upload-img {
    width: 178px;
    height: 178px;
    display: block;
  }
  .tip {
    color: #aaa;
    font-size: 12px;
  }
</style>
