<template>
  <div class="list" v-loading="loading">
    <div v-if="personList.length >= 1">
      <ul>
        <li
          v-for="(item, index) in personList"
          :key="index"
          @click="liClick(item)"
        >
          <el-row style="padding:10px">
            <el-col :span="3"><img :src="item.avatar"/></el-col>
            <el-col :span="21">
              <p>
                {{ item.name }}
                <span class="fr gray">{{ item.msgTime }}</span>
              </p>
              <p class="gray padt10" v-if="item.contact">
                {{ JSON.parse(item.contact).content }}
              </p>
            </el-col>
          </el-row>
        </li>
      </ul>
    </div>
    <div v-else></div>
  </div>
</template>
<script>
import { parseTime } from '@/utils/common.js'
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
      loadings: true
    }
  },
  methods: {
    liClick(e) {
      this.$emit('chatFn', e)
    }
  }
}
</script>
<style lang="scss" scoped>
.list {
  overflow-y: scroll;
  height: calc(100vh - 328px);
  ::-webkit-scrollbar {
    display: none;
  }

  /deep/ .el-loading-spinner {
    margin-top: 20px;
  }
  .gray {
    color: #999;
  }
  .padt10 {
    padding-top: 10px;
  }
  ul li {
    padding: 10px;
    overflow: hidden;
    border-bottom: 1px solid #efefef;
    cursor: pointer;
    p {
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
    :hover {
      background: #efefef;
    }
    img {
      width: 40px;
      height: 40px;
      float: left;
    }
  }
}
</style>
