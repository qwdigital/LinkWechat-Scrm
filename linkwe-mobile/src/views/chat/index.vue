<script>
import { getTypeList } from '@/api/chat'
import List from './List'
export default {
  components: { List },
  props: {},
  data() {
    return {
      keyword: '',
      active: 0,
      list: [],
      loading: false,
      finished: false,
      show: false
      // userId: this.$store.state.userId,
    }
  },
  watch: {
    userId() {
      this.getList()
    }
  },
  computed: {
    userId() {
      return this.$store.state.userId
    }
  },
  beforeCreate() {},
  created() {
    // this.$toast('userId:' + this.$store.state.userId)
    this.userId && this.getList()
  },
  mounted() {},
  methods: {
    getList() {
      getTypeList().then(({ rows, total }) => {
        this.list = []
        this.userId &&
          this.list.push({
            sideName: '我的'
          })
        rows && this.list.push(...rows)
      })
    },
    search(pageNum) {
      this.$refs['list'][this.active].getList(pageNum)
      // this.$refs['list' + this.active] && this.$refs['list' + this.active][0].getList(pageNum)
    },
    cancel() {
      this.$nextTick(() => this.search(1))
    },
    refreshCollect() {
      this.userId && this.$refs['list'][0].getList(1)
    }
    // add() {},
  }
}
</script>

<template>
  <div>
    <van-search
      v-model="keyword"
      show-action
      placeholder="请输入搜索关键词"
      @cancel="cancel"
      @search="search(1)"
    >
      <!-- <template #action>
        <van-icon name="plus" @click="add" />
      </template> -->
    </van-search>
    <van-tabs v-model="active">
      <!-- <van-tab v-if="!!userId" title="我的">
        <List ref="list0" :userId="userId" :keyword="keyword"></List>
      </van-tab> -->
      <van-tab :title="item.sideName" v-for="(item, index) in list" :key="index">
        <List
          ref="list"
          :sideId="item.sideId"
          :mediaType="item.mediaType"
          :keyword="keyword"
          @collect="refreshCollect"
        ></List>
      </van-tab>
    </van-tabs>

    <!-- <van-dialog
      v-model="show"
      :title="`添加&quot;我的&quot;${radio}`"
      show-cancel-button
    >
      <van-form @submit="onSubmit">
        <van-field name="radio" label="添加类型">
          <template #input>
            <van-radio-group v-model="radio" direction="horizontal">
              <van-radio name="1">文本</van-radio>
              <van-radio name="2">分类</van-radio>
            </van-radio-group>
          </template>
        </van-field>
        <van-field
          v-model="username"
          name="分类名称"
          label="分类名称"
          placeholder="分类名称"
          :rules="[{ required: true, message: '请填写分类名称' }]"
        />
        <template>
          <van-field
            readonly
            clickable
            name="picker"
            :value="value"
            label="文本分类"
            placeholder="点击选择文本分类"
            @click="showPicker = true"
          />
          <van-popup v-model="showPicker" position="bottom">
            <van-picker
              show-toolbar
              :columns="columns"
              @confirm="onConfirm"
              @cancel="showPicker = false"
            />
          </van-popup>

          <van-field
            v-model="message"
            rows="5"
            autosize
            label="文本信息"
            type="textarea"
            maxlength="150"
            placeholder="请输入文本信息"
            show-word-limit
          />
        </template>
      </van-form>
    </van-dialog> -->
  </div>
</template>

<style lang="less" scoped>
.page {
  height: 100vh;
  background: #f6f6f6;
}
/deep/ .list {
  padding: 10px;
  background: #fff;
  border-top: 1px solid #eee;
  .info {
    padding-top: 10px;
  }
  .action {
    padding-left: 5px;
  }
}
</style>
