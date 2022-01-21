<template>
  <div class="groupList" v-loading="loading">
    <ul v-if="personList.length">
      <li
        v-for="(item, index) in personList"
        :key="index"
        :class="{ active: index == active }"
        @click="liClick(item, index)"
      >
        <el-row type="flex" align="middle">
          <div class="ninebox">
            <ul v-if="item.avatar">
              <li v-for="(a, i) in item.avatar.split(',')" :key="i">
                <img :src="a" />
              </li>
            </ul>
          </div>
          <div style="margin-left: 8px; line-height: 60px" class="toe" :title="item.name">
            {{ item.name }}
            <!-- <p>
              <span class="fr gray">{{ item.msgTime }}</span>
            </p> -->
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
    <el-empty v-else :image-size="100"></el-empty>
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
      loadings: true,
      active: undefined
    }
  },
  methods: {
    liClick(data, index) {
      this.active = index
      this.$emit('groupFn', data)
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
  overflow: hidden;
  ul li {
    float: left;
    width: 15px;
    height: 15px;
    padding: 0 !important;
    margin: 1px 1px 1px 1px;
  }
}
.groupList {
  overflow-y: auto;
  height: calc(100vh - 328px);

  ::-webkit-scrollbar {
    display: none;
  }

  ::v-deep .el-loading-spinner {
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
    &:hover {
      background: #efefef;
    }
    &.active {
      background: #ebf4fc;
    }
    img {
      width: 100%;
      height: 100%;
      float: left;
    }
  }
}
</style>
