<script>
import {
  getMaterialList,
  getCollectionList,
  addCollection,
  cancleCollection,
} from '@/api/chat'
export default {
  components: {},
  props: {
    sideId: {
      type: String,
      default: '0',
    },
    userId: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      list: [{}],
      loading: false,
      finished: false,
      collectList: [],
    }
  },
  watch: {},
  computed: {},
  created() {},
  mounted() {},
  methods: {
    getList() {
      this.loading = true
      ;(this.userId ? getCollectionList : getMaterialList)()
        .then(({ rows, total }) => {
          this.list = rows
          this.loading = false
          this.finished = true
        })
        .catch(() => {
          this.loading = false
          this.finished = true
        })
    },
    getCollectionList() {
      getCollectionList().then(({ rows, total }) => {
        this.collectList = rows.map((d) => d.id)
      })
    },
    isCollected(id) {
      return this.collectList.includes(id)
    },
    send() {},
    collect(materialId) {
      ;(this.isCollected(materialId) ? cancleCollection : addCollection)({
        userId,
        materialId,
      }).then(({ data }) => {
        this.isCollected.splice(this.isCollected.indexOf(materialId), 1)
      })
    },
  },
}
</script>

<template>
  <div>
    <van-list
      v-model="loading"
      :finished="finished"
      finished-text="没有更多了"
      @load="getList"
    >
      <div v-for="(item, index) in list" class="list" :key="index">
        <div class="title">dsdsds</div>
        <div class="info">
          素材库
          <span>2020/2/4</span>

          <div class="fr flex">
            <div class="action" @click="send">发送</div>
            <div class="action" @click="collect(item.id)">
              {{ isCollected(item.id) ? '已' : '' }}收藏
            </div>
          </div>
        </div>
      </div>
    </van-list>
  </div>
</template>

<style lang="less" scoped></style>
