<template>
  <div class="list" v-loading="loading">
    <div v-if="personList.length">
      <ul>
        <li
          v-for="(item, index) in personList"
          :key="index"
          @click="liClick(item)"
        >
          <el-row>
            <span class="fl"> <img :src="item.avatar"/></span>
            <span class="fl" style="margin-left:8px">
              <p>{{ item.name }}</p>
            </span>
            <!-- <span class="fl">
              <p>{{ item.contact }}</p>
            </span> -->
          </el-row>
        </li>
      </ul>
    </div>
    <div class="ac" v-else>
      暂无数据
    </div>
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
      console.log(e, 'liClick')
      this.$emit('chatFn', e)
    },
    msgContentType(type, msg) {
      console.log(type, msg, 'msgContentType')
      //消息获取
      /*case msg = JSON.parse(msg);
      switch (type){
        case "text":
      }*/
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
      line-height: 40px;
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
