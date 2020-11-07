<script>
import MaPage from '@/views/material/components/MaPage'

export default {
  components: { MaPage },
  data() {
    return {
      srcList: [
        'https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg',
        'https://fuss10.elemecdn.com/1/8e/aeffeb4de74e2fde4bd74fc7b4486jpeg.jpeg',
      ],
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.userId)
    },
  },
}
</script>

<template>
  <MaPage ref="page" type="0" @listChange="listChange">
    <el-row :gutter="20">
      <el-col
        :span="6"
        style="margin-bottom: 24px;"
        v-for="(item, index) in list"
        :key="index"
      >
        <el-card shadow="hover" body-style="padding: 0px;">
          <div class="img-wrap">
            <el-image
              :src="require('@/assets/image/profile.jpg')"
              :preview-src-list="srcList"
            ></el-image>
            <div class="el-upload-list__item-actions">
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
            <el-checkbox v-model="ids">{{ item.name }}</el-checkbox>
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
  height: 50px;
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
</style>
