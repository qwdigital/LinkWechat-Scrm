2
<script>
import { getUserInfo } from '@/api/common'
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
      show: false,
      userId: '',
    }
  },
  watch: {},
  computed: {},
  beforeCreate() {
    // http://106.13.201.219/?auth_code=xxx#/authWehatCallback
    // console.log('routerbeforeCreate', this.$route);
    // let auth_code = location.search
    //   .slice(1)
    //   .split('&')[0]
    //   .split('=')[1]
    // if (!auth_code) {
    //   this.$toast('未获得授权')
    //   return
    // }
    // getUserInfo(auth_code)
    //   .then(({ data }) => {
    //     this.userId = data.userId
    //     this.$toast('userId:' + this.userId)
    //   })
    //   .catch((err) => {
    //     this.$toast('err:' + err)
    //   })
  },
  created() {
    let auth_code = location.search
      .slice(1)
      .split('&')[0]
      .split('=')[1]
    if (!auth_code) {
      this.$toast('未获得授权')
      return
    }
    getUserInfo(auth_code)
      .then(({ data }) => {
        this.userId = data.userId
        this.$toast('userId:' + this.userId)
      })
      .catch((err) => {
        Dialog.confirm({
          title: '标题',
          message: err,
        })
      })
    this.getList()
  },
  mounted() {},
  methods: {
    getList() {
      getTypeList().then(({ rows, total }) => {
        this.list = rows
      })
    },
    search() {
      this.$refs['list' + this.active].getList(1)
    },
    add() {},
  },
}
</script>

<template>
  <div>
    <van-search
      v-model="keyword"
      show-action
      placeholder="请输入搜索关键词"
      @search="search"
    >
      <!-- <template #action>
        <van-icon name="plus" @click="add" />
      </template> -->
    </van-search>
    <van-tabs v-model="active">
      <van-tab v-if="!!userId" title="我的">
        <List ref="list0" :userId="userId" :keyword="keyword"></List>
      </van-tab>
      <van-tab
        :title="item.sideName"
        v-for="(item, index) in list"
        :key="index"
      >
        <List
          :ref="'list' + (index + 1)"
          :sideId="item.sideId"
          :userId="userId"
          :keyword="keyword"
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
