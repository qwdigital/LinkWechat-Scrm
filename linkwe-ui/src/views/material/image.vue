<script>
import MaPage from '@/views/material/components/MaPage'

export default {
  name: 'Image',
  components: { MaPage },
  data() {
    return {
      srcList: [],
      ids: [] // 选中数组
    }
  },
  watch: {},
  created() {},
  methods: {
    listChange(data) {
      this.srcList = data.map((item) => item.materialUrl)
    }
  }
}
</script>

<template>
  <MaPage ref="page" type="0" @listChange="listChange" :selected="ids" v-slot="{ list }">
    <el-row :gutter="20">
      <el-col :span="6" style="margin-bottom: 24px;min-width: 220px;" v-for="(item, index) in list" :key="index">
        <el-card shadow="hover" body-style="padding: 0px;">
          <div class="img-wrap">
            <el-image :src="item.materialUrl" :preview-src-list="srcList" fit="contain"></el-image>
            <div class="actions">
              <i v-hasPermi="['wechat:material:edit']" class="el-icon-edit cp" @click="$refs.page.edit(item)"></i>
              <i
                v-hasPermi="['wechat:material:remove']"
                class="el-icon-delete cp"
                @click="$refs.page.remove(item.id)"
              ></i>
            </div>
          </div>
          <div style="padding: 14px;">
            <el-checkbox v-model="ids" :label="item.id">{{ item.materialName }}</el-checkbox>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </MaPage>
</template>

<style lang="scss" scoped>
.img-wrap {
  position: relative;
  height: 0;
  padding: 70% 0 0 0;
  border-bottom: 1px solid #e6ebf5;
  &:hover .actions {
    opacity: 1;
  }
}
.actions {
  position: absolute;
  width: 100%;
  height: 50px;
  left: 0;
  top: 0;
  text-align: center;
  color: #fff;
  opacity: 0;
  font-size: 20px;
  background-color: rgba(0, 0, 0, 0.5);
  transition: opacity 0.3s;
  .el-icon-edit {
    margin-right: 20px;
  }
}
.el-image {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
}
</style>
