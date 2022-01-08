<template>
  <div class="userList" v-loading="loading">
    <ul v-if="personList.length">
      <li
        v-for="(item, index) in personList"
        class="list"
        :class="{ active: index == active }"
        :key="index"
        @click="liClick(item, index)"
      >
        <img class="fl mr10" :src="item.avatar" />
        <div class="name" :title="item.name">
          {{ item.name }}
        </div>
      </li>
    </ul>
    <el-empty v-else :image-size="100"></el-empty>
  </div>
</template>
<script>
export default {
  props: {
    personList: {
      type: Array,
      defluat: () => []
    },
    loading: {
      type: Boolean,
      defluat: false
    }
  },

  data() {
    return {
      loadings: true,
      active: undefined
    }
  },
  methods: {
    liClick(data, index) {
      this.active = index
      this.$emit('chatFn', data)
    }
  }
}
</script>
<style lang="scss" scoped>
.userList {
  overflow-y: auto;
  height: calc(100vh - 288px);

  ::-webkit-scrollbar {
    display: none;
  }

  ::v-deep .el-loading-spinner {
    margin-top: 20px;
  }

  .gray {
    color: #999;
  }
  .list {
    padding: 10px;
    overflow: hidden;
    border-bottom: 1px solid #efefef;
    cursor: pointer;
    text-align: left;
    .name {
      white-space: nowrap;
      overflow: hidden;
      line-height: 40px;
      text-overflow: ellipsis;
    }
    &:hover {
      background: #efefef;
    }
    &.active {
      background: #ebf4fc;
    }
    img {
      width: 40px;
      height: 40px;
      float: left;
    }
  }
}
</style>
