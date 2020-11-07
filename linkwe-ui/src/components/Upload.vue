<script>
import { upload } from '@/api/material'
export default {
  components: {},
  props: {
    fileUrl: {
      type: String,
      default: '',
    },
    // 0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file)
    type: {
      type: String,
      default: '0',
    },
    beforeUpload: {
      type: Function,
      default: () => () => {},
    },
  },
  data() {
    return {
      action:
        process.env.VUE_APP_BASE_API + window.CONFIG.services.wecom + '/upload',
      headers: window.CONFIG.headers,
    }
  },
  watch: {},
  computed: {
    accept() {
      return ['image/*', 'audio/*', 'video/*', '*'][this.type]
    },
  },
  created() {
    upload()
  },
  mounted() {},
  methods: {
    handleBeforeUpload(file) {
      if (this.type === '0') {
        // 图片
        const isFormat = file.type === 'image/jpeg'
        const isSize = file.size / 1024 / 1024 < 2

        if (!isFormat) {
          this.$message.error('上传文件只能是 JPG 格式!')
        }
        if (!isSize) {
          this.$message.error('上传文件大小不能超过 2MB!')
        }
        return isFormat && isSize
      } else if (this.type === '1') {
        // 语音
        const isFormat = file.type === 'audio/amr'
        const isSize = file.size / 1024 / 1024 < 2

        if (!isFormat) {
          this.$message.error('上传文件只能是 amr 格式!')
        }
        if (!isSize) {
          this.$message.error('上传文件大小不能超过 2MB!')
        }
        return isFormat && isSize
      } else if (this.type === '2') {
        // 视频
        const isFormat = file.type === 'video/mp4'
        const isSize = file.size / 1024 / 1024 < 10

        if (!isFormat) {
          this.$message.error('上传文件只能是 mp4 格式!')
        }
        if (!isSize) {
          this.$message.error('上传文件大小不能超过 10MB!')
        }
        return isFormat && isSize
      } else if (this.type === '3') {
        // 普通文件
        const isSize = file.size / 1024 / 1024 < 20

        if (!isSize) {
          this.$message.error('上传文件大小不能超过 20MB!')
        }
        return isSize
      }
      return beforeUpload(file)
    },
    onSuccess(res, file) {
      this.fileUrl = URL.createObjectURL(file.raw)
    },
  },
}
</script>

<template>
  <div>
    <el-upload
      class="uploader"
      :accept="accept"
      :action="action"
      :headers="headers"
      :data="{ type: type }"
      :show-file-list="false"
      :on-success="onSuccess"
      :before-upload="handleBeforeUpload"
    >
      <img v-if="fileUrl" :src="fileUrl" class="upload-img" />
      <i v-else class="el-icon-plus uploader-icon"></i>
    </el-upload>
    <div class="tip">
      <slot name="tip"></slot>
    </div>
  </div>
</template>

<style lang="scss" scoped>
/deep/.uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
/deep/.uploader .el-upload:hover {
  border-color: #409eff;
}
.uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
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
