<template>
  <div>
    <van-tabs :active="active" v-model="active">
      <van-tab :title="'未完成(' + todoNum + ')'" :name="0">
        <template v-if="todoMembers.length === 0">
          <van-empty description="暂无数据" />
        </template>
        <template v-else>
          <div v-for="member in todoMembers" :key="member.userId" class="cell">
            <van-image width="45" :src="member.avatar"></van-image>
            <div class="content"> {{ member.name }} </div>
          </div>
        </template>
      </van-tab>
      <van-tab :title="'已完成(' + doneNum + ')'" :name="1">
        <template v-if="doneMembers.length === 0">
          <van-empty description="暂无数据" />
        </template>
        <template v-else>
          <div v-for="member in doneMembers" :key="member.userId" class="cell">
            <van-image width="45" :src="member.avatar"></van-image>
            <div class="content"> {{ member.name }} </div>
          </div>
        </template>
      </van-tab>
    </van-tabs>

    <van-overlay :show="loading" class-name="overlay">
      <van-loading type="spinner"></van-loading>
    </van-overlay>
  </div>
</template>

<script>
import { getState } from '@/api/community'

export default {
  data () {
    return {
      taskId: '',               // 任务ID
      active: 0,                // 0: 未完成  1: 已完成
      taskType: 0,              // 0: 老客标签建群   1: 群SOP
      waitingCustomers: [],     // 未完成客户
      finishedCustomers: [],    // 已完成客户
      members: [],              // 任务关联员工列表
      loading: false,           // 加载状态
    }
  },

  methods: {
    // 获取任务状态
    getState () {
      if (!this.taskId) return

      this.loading = true
      getState(this.taskId, this.taskType).then((res) => {
        if (res.code === 200) {
          this.members = res.data || []
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    }
  },

  computed: {
    // 带处理人员
    todoMembers () {
      const members = this.members.filter((m) => {
        return !m.done
      })

      return members
    },

    todoNum () {
      return this.todoMembers.length
    },

    // 已处理人员
    doneMembers () {
      const members = this.members.filter((m) => {
        return m.done
      })

      return members
    },

    doneNum () {
      return this.doneMembers.length
    },
  },

  created () {
    this.taskId = this.$route.query.taskId
    this.active = this.$route.query.active || 0
    this.taskType = this.$route.query.taskType || 0

    this.getState()
  }
}
</script>

<style lang="less" scoped>
  /deep/ .van-tabs__wrap {
    border-bottom: 1px solid #EEEEEE;
  }

  .cell {
    display: flex;
    align-items: center;
    padding: 20px 15px;
    border-bottom: 1px solid #EEEEEE;

    :nth-child(n+2) {
      margin-left: 20px;
    }

    .content {
      font-size: 20px;
      // font-weight: bold;
    }
  }

  .overlay {
    display: flex;
    align-items: center;

    .van-loading {
      margin: auto;
    }
  }
</style>
