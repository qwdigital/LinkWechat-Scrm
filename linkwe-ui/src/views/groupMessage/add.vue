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
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="群发类型" prop="postName">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="发送范围" prop="postCode">
          <el-button type="primary" size="mini">选择发送客户</el-button>
        </el-form-item>
        <el-form-item label="发送时间" prop="postSort">
          <el-date-picker
            v-model="form.status"
            size="small"
            style="width: 240px"
            value-format="yyyy-MM-dd"
            type="datetime"
            placeholder="开始日期"
          ></el-date-picker>
          <span>设置时间定时发送，不设置立即发送</span>
        </el-form-item>
        <el-form-item label=" ">
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
              <el-tab-pane label="配置管理" name="second">
                <span slot="label">
                  <i class="el-icon-date"></i> 网页
                </span>
                配置管理
              </el-tab-pane>
              <el-tab-pane label="角色管理" name="third">
                <span slot="label">
                  <i class="el-icon-date"></i> 图片
                </span>
                角色管理
              </el-tab-pane>
              <el-tab-pane label="定时任务补偿" name="fourth">
                <span slot="label">
                  <i class="el-icon-date"></i> 海报
                </span>
                定时任务补偿
              </el-tab-pane>
              <el-tab-pane label="定时任务补偿" name="fourth">
                <span slot="label">
                  <i class="el-icon-date"></i> 小程序
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
          <el-button type="primary" size="mini" icon="el-icon-search">通知成员发送</el-button>
          <span>通知成员，向选中的客户发送以上企业消息</span>
          <i class="el-icon-edit"></i>
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
