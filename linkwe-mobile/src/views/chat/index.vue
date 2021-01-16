<script>
import { getTypeList } from '@/api/chat'
import List from './List'
export default {
  components: { List },
  props: {},
  data() {
    return {
      keywords: '',
      active: 2,
      list: [],
      loading: false,
      finished: false,
      show: false,
    }
  },
  watch: {},
  computed: {},
  created() {
    this.getList()
  },
  mounted() {},
  methods: {
    getList() {
      getTypeList().then(({ rows, total }) => {
        this.list = rows
      })
    },
    search() {},
    add() {},
  },
}
</script>

<template>
  <div>
    <van-search
      v-model="keywords"
      show-action
      placeholder="请输入搜索关键词"
      @search="search"
    >
      <!-- <template #action>
        <van-icon name="plus" @click="add" />
      </template> -->
    </van-search>
    <van-tabs v-model="active">
      <van-tab title="标签 1">
        <List></List>
      </van-tab>
      <van-tab title="标签 2"><List></List></van-tab>
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
