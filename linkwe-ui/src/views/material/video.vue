<script>
import MaPage from '@/views/material/components/MaPage'
import Video from 'video.js'

export default {
  name: 'Video',
  components: {
    MaPage,
  },
  data() {
    return {
      ids: [], // 选中数组
    }
  },
  watch: {},
  created() {},
  methods: {},
}
</script>

<template>
  <MaPage ref="page" type="2" :selected="ids" v-slot="{ list }">
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
              class="video-js vjs-default-skin vjs-big-play-centered"
              controls
              webkit-playsinline="true"
              playsinline="true"
              :autoplay="false"
              preload="auto"
              :poster="item.coverUrl"
            >
              <source :src="item.materialUrl" type="video/mp4" />
            </video>
            <div class="actions">
              <i
                v-hasPermi="['material:download']"
                class="el-icon-download"
                @click="$refs.page.download(item)"
              ></i>
              <i
                v-hasPermi="['material:edit']"
                class="el-icon-edit cp"
                @click="$refs.page.edit(item)"
              ></i>
              <i
                v-hasPermi="['material:remove']"
                class="el-icon-delete cp"
                @click="$refs.page.remove(item.id)"
              ></i>
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
  &:hover .actions {
    opacity: 1;
  }
}
.actions {
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
  .el-icon-edit {
    margin: 0 20px;
  }
}
#video {
  width: 100%;
  height: 100%;
}
</style>
