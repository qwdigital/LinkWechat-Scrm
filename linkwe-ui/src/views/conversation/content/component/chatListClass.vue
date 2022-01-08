<template>
  <div>
    <div class="top">
      <div class="name">
        <span v-if="queryChat"> 与{{ queryChat.receiveName }}的聊天</span>
        <span class="fr download" @click="exportList()">下载会话</span>
      </div>
    </div>
    <el-tabs v-model="activeTab" @tab-click="(v) => opened.includes(v.name) || opened.push(v.name)">
      <el-tab-pane v-for="(item, index) of list" :key="index" :label="item.label" :name="item.type">
        <chatListClassTab
          v-if="opened.includes(item.type)"
          :queryChat="queryChat"
          :type="item.type"
        ></chatListClassTab>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import chatListClassTab from './chatListClassTab.vue'
import * as api from '@/api/conversation/content.js'
export default {
  components: { chatListClassTab },
  props: {
    queryChat: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      activeTab: 'all',
      opened: ['all'],
      list: [
        {
          label: '全部',
          type: 'all'
        },
        {
          label: '图片及视频',
          type: 'image'
        },
        {
          label: '文件',
          type: 'file'
        },
        {
          label: '链接',
          type: 'link'
        },
        {
          label: '语音通话',
          type: 'voice'
        }
      ]
    }
  },
  watch: {
    queryChat() {
      this.opened = ['all']
    }
  },
  mounted() {},
  methods: {
    exportList() {
      this.$confirm('是否确认导出所有数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          return api.exportList(this.queryChat)
        })
        .then((response) => {
          this.download(response.msg)
        })
        .catch(function (err) {
          console.log(err)
        })
    }
  }
}
</script>
<style lang="scss" scoped>
::v-deep .el-tabs__header {
  margin: 0;
}

.top {
  padding: 15px;
}

.name {
  font-size: 18px;
  min-height: 20px;
}

.download {
  color: #199ed8;
  text-align: right;
  font-size: 16px;
  cursor: pointer;
}
</style>
