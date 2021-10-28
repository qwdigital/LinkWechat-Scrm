<template>
  <div>
    <div>
      <van-search v-model="keyword" placeholder="搜索活码名或关键词" shape="round" @search="onSearch" @clear="clear">
      </van-search>
    </div>

    <div class="history-wrapper">
      <template v-for="k of history">
        <van-tag :key="k" size="large" closeable @click="search(k)" @close="removeHistory(k)"> {{ k }} </van-tag>
      </template>
    </div>

    <div class="bottom-line"></div>

    <div>
      <template v-if="!tasks || tasks.length === 0">
        <van-empty description="暂无数据" />
      </template>
      <template v-else>
        <template v-for="task of tasks">
          <KeywordPanel :key="task.taskId" :task="task" :state="false"></KeywordPanel>
        </template>
      </template>
    </div>

    <van-overlay :show="loading" class-name="overlay">
      <van-loading type="spinner"></van-loading>
    </van-overlay>
  </div>
</template>

<script>
import KeywordPanel from './panel'
import { getKeywordTasks } from '@/api/community'

export default {
  components: { KeywordPanel },

  data() {
    return {
      keyword: '',
      storageKey: 'keywords',
      history: [],
      tasks: [],
      loading: false,
      focus: false
    }
  },

  methods: {
    getTasks() {
      this.loading = true
      getKeywordTasks(this.keyword)
        .then((res) => {
          if (res.code === 200) {
            let timeStamp = (dateString) => (dateString ? +new Date(dateString.replace(/-/g, '/')) : 0)
            this.tasks = res.rows.sort((a, b) => timeStamp(b.updateTime) - timeStamp(a.updateTime))
          }
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    },

    setHistory(keyword) {
      if (!keyword) return

      if (!this.history.includes(keyword)) {
        this.history.push(keyword)
        localStorage.setItem(this.storageKey, JSON.stringify(this.history))
      }
    },

    removeHistory(keyword) {
      if (this.history.includes(keyword)) {
        this.history.splice(this.history.indexOf(keyword), 1)
        localStorage.setItem(this.storageKey, JSON.stringify(this.history))
      }
    },

    getHistory() {
      this.history = JSON.parse(localStorage.getItem(this.storageKey)) || []
    },

    onSearch() {
      this.setHistory(this.keyword)

      this.getTasks()
    },

    search(keyword) {
      this.keyword = keyword

      this.getTasks()
    },

    clear() {
      this.keyword = ''
      this.getTasks()
    }
  },

  computed: {
    url() {
      return window.location.href
    }
  },

  created() {
    this.getHistory()

    this.getTasks()
  }
}
</script>

<style lang="less" scoped>
.history-wrapper {
  padding: 0 20px 20px;

  .van-tag {
    margin: 10px 10px 0 0;
  }
}

.bottom-line {
  background-color: #f2f2f2;
  height: 15px;
}

.overlay {
  display: flex;
  align-items: center;

  .van-loading {
    margin: auto;
  }
}
</style>
