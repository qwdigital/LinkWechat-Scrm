<script>
import MaPage from '@/views/material/components/MaPage'
import Video from 'video.js'

export default {
  name: 'Video',
  components: {
    MaPage
  },
  data() {
    return {
      ids: [] // 选中数组
    }
  },
  watch: {},
  created() {},
  methods: {}
}
</script>

<template>
  <MaPage ref="page" type="2" :selected="ids" v-slot="{ list }">
    <el-table :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="视频" align="center" prop="materialUrl" width="200">
        <template slot-scope="{ row }">
          <video
            id="video"
            class="video-js vjs-default-skin vjs-big-play-centered"
            controls
            webkit-playsinline="true"
            playsinline="true"
            :autoplay="false"
            preload="auto"
            :poster="row.coverUrl"
          >
            <source :src="row.materialUrl" type="video/mp4" />
          </video>
        </template>
      </el-table-column>

      <el-table-column label="最近更新时间" align="center" prop="updateTime"> </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="100"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            type="text"
            @click="$refs.page.edit(scope.row)"
            v-hasPermi="['wechat:material:edit']"
            >修改</el-button
          >
          <el-button
            type="text"
            @click="$refs.page.remove(scope.row.id)"
            v-hasPermi="['wechat:material:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <!-- <el-row :gutter="20">
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
                class="el-icon-download"
                @click="$refs.page.download(item)"
              ></i>
              <i
                v-hasPermi="['wechat:material:edit']"
                class="el-icon-edit cp"
                @click="$refs.page.edit(item)"
              ></i>
              <i
                v-hasPermi="['wechat:material:remove']"
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
          </div>
        </el-card>
      </el-col>
    </el-row> -->
  </MaPage>
</template>

<style lang="scss" scoped>
// .img-wrap {
//   position: relative;
//   height: 200px;
//   &:hover .actions {
//     opacity: 1;
//   }
// }
// .actions {
//   position: absolute;
//   width: 100%;
//   height: 40px;
//   left: 0;
//   top: 0;
//   cursor: default;
//   text-align: center;
//   color: #fff;
//   opacity: 0;
//   font-size: 20px;
//   background-color: rgba(0, 0, 0, 0.5);
//   transition: opacity 0.3s;
//   .el-icon-edit {
//     margin: 0 20px;
//   }
// }
#video {
  width: 100%;
  height: 100%;
}
</style>
