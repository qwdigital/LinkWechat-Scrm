<style lang="scss" scoped>
.content-wrap {
  height: 500px;
  align-items: stretch;
  justify-content: space-between;
  padding-right: 6%;
  .el-tabs,
  .preview {
    width: 60%;
    background: #eee;
    border-radius: 8px;
    padding: 10px;
  }
  .preview {
    width: 260px;
  }
}
</style>

<template>
  <div class="app-container">
    <el-card shadow="never" :body-style="{padding: '40px 20px 20px'}">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="选择员工" prop="postCode">
          <el-button type="primary" size="mini">选择</el-button>
        </el-form-item>
        <el-form-item label="朋友圈内容">
          <div class="flex content-wrap">
            <el-tabs v-model="activeName" @tab-click="handleClick">
              <el-button class="fr" type="primary" size="mini">新建素材</el-button>
              <el-tab-pane name="first">
                <span slot="label">
                  <i class="el-icon-date"></i> 文本
                </span>
                <el-upload action="#" list-type="picture-card" :auto-upload="false">
                  <i slot="default" class="el-icon-plus"></i>
                  <div slot="file" slot-scope="{file}">
                    <img class="el-upload-list__item-thumbnail" :src="file.url" alt />
                    <span class="el-upload-list__item-actions">
                      <span
                        class="el-upload-list__item-preview"
                        @click="handlePictureCardPreview(file)"
                      >
                        <i class="el-icon-zoom-in"></i>
                      </span>
                      <span
                        v-if="!disabled"
                        class="el-upload-list__item-delete"
                        @click="handleDownload(file)"
                      >
                        <i class="el-icon-download"></i>
                      </span>
                      <span
                        v-if="!disabled"
                        class="el-upload-list__item-delete"
                        @click="handleRemove(file)"
                      >
                        <i class="el-icon-delete"></i>
                      </span>
                    </span>
                  </div>
                </el-upload>
              </el-tab-pane>
              <el-tab-pane label="角色管理" name="third">
                <span slot="label">
                  <i class="el-icon-date"></i> 图片
                </span>
                角色管理
              </el-tab-pane>
              <el-tab-pane label="定时任务补偿" name="fourth">
                <span slot="label">
                  <i class="el-icon-date"></i> 视频
                </span>
                定时任务补偿
              </el-tab-pane>
            </el-tabs>

            <div class="preview">
              <div class="top">房管局会</div>
            </div>
          </div>
        </el-form-item>
        <el-form-item label=" ">
          <el-button type="primary" size="small">提交</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  components: {},
  props: {},
  data() {
    return {
      // 表单参数
      form: {},
      statusOptions: [
        { label: "发送给客户", value: "" },
        { label: "发送给客户群", value: "" },
      ],
      activeName: "second",
    };
  },
  watch: {},
  computed: {},
  created() {},
  mounted() {},
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },
  },
};
</script>
