<script>
import MaPage from '@/views/material/components/MaPage'
import Video from 'video.js'

export default {
  components: {
    MaPage,
  },
  data() {
    return {
      list: [], // 列表
      ids: [], // 选中数组
    }
  },
  watch: {},
  created() {},
  methods: {
    listChange(data) {
      this.list = data
    },
  },
}
</script>

<template>
  <MaPage ref="page" type="2" @listChange="listChange" :selected="ids">
    <el-row :gutter="20">
      <el-col
        :span="6"
        style="margin-top: 24px;min-width: 280px;"
        v-for="(item, index) in list"
        :key="index"
      >
        <el-card shadow="hover" body-style="padding: 0px;">
          <div class="img-wrap">
            <video
              id="video"
              class="video-js vjs-default-skin
            vjs-big-play-centered"
              controls
              webkit-playsinline="true"
              playsinline="true"
              autoplay="none"
              preload="auto"
              :poster="item.coverUrl"
            >
              <source :src="item.materialUrl" type="video/mp4" />
            </video>
            <div class="el-upload-list__item-actions">
              <span
                class="el-upload-list__item-preview"
                @click="$refs.page.download(item)"
              >
                <i class="el-icon-download"></i>
              </span>
              <span
                class="el-upload-list__item-preview"
                @click="$refs.page.edit(item)"
              >
                <i class="el-icon-edit"></i>
              </span>
              <span
                class="el-upload-list__item-"
                @click="$refs.page.remove(item)"
              >
                <i class="el-icon-delete"></i>
              </span>
            </div>
          </div>
          <div style="padding: 14px;">
            <el-checkbox v-model="ids" :label="item.id">{{
              item.createTime
            }}</el-checkbox>
            <div>{{ item.digest }}</div>
            <!-- <div>{{ item.digest }}</div> -->
          </div>
        </el-card>
      </el-col>
    </el-row>
  </MaPage>
</template>

<style lang="scss" scoped>
.img-wrap {
  position: relative;
  &:hover .el-upload-list__item-actions {
    opacity: 1;
  }
}
.el-upload-list__item-actions {
  position: absolute;
  width: 100%;
  height: 40px;
  left: 0;
  top: 0;
  cursor: default;
  text-align: center;
  color: #fff;
  opacity: 0;
  font-size: 20px;
  background-color: rgba(0, 0, 0, 0.5);
  transition: opacity 0.3s;
}
#video {
  width: 100%;
  height: 100%;
}
</style>
