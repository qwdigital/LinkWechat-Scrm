<template>
  <div class="list" v-loading="loading">
    <div v-if="personList.length">
      <ul>
        <li
          v-for="(item, index) in personList"
          :key="index"
          @click="liClick(item)"
        >
          <el-row type="flex" v-if="item.msgType == 'text'">
            <div class="ninebox">
              <ul v-if="item.avatar">
                <li v-for="(a, i) in item.avatar.split(',')" :key="i">
                  <img :src="a" />
                </li>
              </ul>
            </div>
            <div class="fl" style="margin-left:8px;line-height:60px">
              <p>
                {{ item.name }}
                <!-- <span class="fr gray">{{ item.msgTime }}</span> -->
              </p>
              <!-- <p class="gray" v-if="item">
                {{ item.name }}:
                {{ item.content }}
              </p> -->
            </div>
          </el-row>
          <!-- <el-row style="padding:10px" v-if="item.finalChatContext.msgtype=='file'">
                <el-col :span="3">&nbsp;</el-col>
                <el-col :span="21">
                   <p><span class="fr gray">{{parseTime(item.finalChatContext.msgtime)}}</span></p>
                   <p class="gray padt10" >{{item.finalChatContext.from}}:
                       <span v-if="item.finalChatContext.file.fileext=='mp4'">[视频]</span>
                       </p>
                </el-col>
                </el-row>   -->
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
      this.$emit('groupFn', e)
    }
  }
}
</script>
<style lang="scss" scoped>
.ninebox {
  flex: none;
  width: 54px;
  height: 52px;
  border: 1px solid #199ed8;
  ul li {
    float: left;
    width: 15px;
    height: 15px;
    padding: 0 !important;
    margin: 1px 1px 1px 1px;
  }
}
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
    padding: 3px 10px;
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
      width: 100%;
      height: 100%;
      float: left;
    }
  }
}
</style>
