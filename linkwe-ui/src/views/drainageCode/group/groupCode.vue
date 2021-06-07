<script>
import { getDetail, download } from '@/api/drainageCode/group'
import ClipboardJS from 'clipboard'

export default {
  props: {
    // 实际群活码
    groupCodeId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      groupCode: null,
      clipboard: null
    }
  },
  computed: {
    codeUrl() {
      return this.groupCode ? this.groupCode.codeUrl : ''
    }
  },
  mounted() {
    this.clipboard = new ClipboardJS('#copyUrl')

    this.clipboard.on('success', (e) => {
      this.$notify({
        title: '成功',
        message: '链接已复制到剪切板，可粘贴。',
        type: 'success',
      })
    })

    this.clipboard.on('error', (e) => {
      this.$message.error('链接复制失败')
    })
  },
  created() {
    this.getGroupDetail()
  },
  destroyed() {
    this.clipboard.destroy()
  },
  methods: {
    // 获取群活码信息
    getGroupDetail() {
      if (!this.groupCodeId) return

      getDetail(this.groupCodeId)
        .then((res) => {
          if (res.code === 200) {
            this.groupCode = res.data
          } else {
          }
        })
    },
    // 下载
    handleDownload() {
      if (!this.groupCode) return

      const name = this.groupCode.activityName + '.png'

      download(this.groupCode.id)
        .then((res) => {
          if (res != null) {
            let blob = new Blob([res], { type: 'application/zip' })
            let url = window.URL.createObjectURL(blob)
            const link = document.createElement('a') // 创建a标签
            link.href = url
            link.download = name // 重命名文件
            link.click()
            URL.revokeObjectURL(url) // 释放内存
          }
        })
    },
  }
}
</script>

<template>
  <div class="page">
    <div class="code-wrapper">
      <div class="code-success">
        <el-button type="success" icon="el-icon-check" circle size="medium"></el-button>
      </div>
      <div class="code-text">
        群活码创建成功，支持复制二维码链接或直接下载
      </div>
      <el-image :src="codeUrl" class="code-image"></el-image>
      <div class="code-actions">
        <el-button
          id="copyUrl"
          type="text"
          size="medium"
          :data-clipboard-text="codeUrl"
          >复制链接</el-button
        >
        <el-button
          type="text"
          size="medium"
          @click="handleDownload()"
          >下载二维码</el-button
        >
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.code-wrapper {
  text-align: center;

  .code-success, .code-text, .code-image, .code-actions {
    padding: 20px 0;
  }
}
</style>
