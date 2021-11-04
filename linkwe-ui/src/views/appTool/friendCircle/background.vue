<style lang="scss" scoped>
.preview {
    width: 260px;
    height: 500px;
    background: #eee;
    border-radius: 8px;
    padding: 10px;
  }
</style>

<template>
  <div>
    <el-card shadow="never">
      <el-row type="flex" justify="center" :gutter="50">
        <el-col :span="10">
          <el-form ref="form" :model="form" label-width="80px">
            <el-form-item label="公司名称">
              <el-input v-model="model"></el-input>
            </el-form-item>
            <el-form-item label="背景图片">
              <el-upload action :show-file-list="false" :on-success="d" :before-upload="d">
                <img v-if="imageUrl" :src="imageUrl" />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
            <el-form-item label=" ">
              <el-button type="primary" size="small">提交</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="10">
          <div class="preview">
              <div class="top">房管局会</div>
            </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
export default {
  components: {},
  props: {},
  data() {
    return {};
  },
  watch: {},
  computed: {},
  created() {},
  mounted() {},
  methods: {},
};
</script>
