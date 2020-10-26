<script>
import * as api from "@/api/drainageCode/welcome";

export default {
  components: {},
  props: {},
  data() {
    return {
      dialogVisible: false,
      dialogVisible1: false,
      form: { id: "", welcomeMsgTplType: "", welcomeMsg: "" },
      imageUrl: "",
    };
  },
  watch: {},
  computed: {},
  created() {
    this.form = this.$route.query;
  },
  mounted() {},
  methods: {
    getData() {},
    submit() {
      api.edit(this.form);
    },
    insertName() {
      this.form.welcomeMsg += "#客户昵称#";
    },
    uploadSuccess(res, file) {
      debugger;
      this.imageUrl = URL.createObjectURL(file.raw);
    },
    beforeUpload(file) {
      debugger;
      const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },
  },
};
</script>

<template>
  <div class="flex page">
    <el-form ref="form" :model="form" label-width="80px" class="form">
      <el-form-item label="欢迎语">
        <el-card shadow="never" class="card">
          <div style="height: 300px;">
            <el-input
              type="textarea"
              :rows="5"
              maxlength="100"
              show-word-limit
              v-model="form.welcomeMsg"
              placeholder
            ></el-input>
          </div>
          <div class="bfc-o">
            <el-button
              type="default"
              class="fr"
              round
              icon="el-icon-user-solid"
              @click="insertName()"
            >插入客户昵称</el-button>
          </div>
          <el-divider></el-divider>
          <img v-if="imageUrl" :src="imageUrl" />

          <el-popover placement="top-start" trigger="hover">
            <div class="flex">
              <el-upload
                class="mr10"
                action
                :show-file-list="false"
                :on-success="uploadSuccess"
                :before-upload="beforeUpload"
              >
                <el-button>
                  <i class="el-icon-picture-outline"></i>
                  <p>图片</p>
                </el-button>
              </el-upload>

              <el-button @click="dialogVisible = true">
                <i class="el-icon-link"></i>
                <p>网页</p>
              </el-button>
              <!-- <el-button @click="dialogVisible1 = true">
                <i class="el-icon-link"></i>
                <p>小程序</p>
              </el-button>-->
            </div>
            <el-button slot="reference" icon="el-icon-plus" size="mini">添加图片/网页</el-button>
          </el-popover>
        </el-card>
      </el-form-item>
      <el-form-item label=" ">
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="submit">保存</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="$router.back()">取消</el-button>
      </el-form-item>
    </el-form>

    <div class="preview ac">
      <div class="top">小微</div>
      <div class="small">塬微科技-企微</div>
      <div class="time">凌晨2:20</div>
      <div class="flex">
        <el-avatar shape="square" size="small" :src="require('@/assets/image/profile.jpg')"></el-avatar>
        <div class="msg">{{form.welcomeMsg}}</div>
      </div>
    </div>

    <el-dialog title="添加网页消息" :visible.sync="dialogVisible" width="width">
      <el-form :model="form" inline>
        <el-form-item label="添加网页消息" label-width="200">
          <el-input style="width: 400px;" v-model="j" placeholder="以http或https开头"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </div>
    </el-dialog>

    <!-- <el-dialog
      title="添加小程序消息"
      :visible.sync="dialogVisible1"
      width="width"
    >
      <div class="flex filter-wrap">
        <el-select v-model="model" placeholder="请选择分组">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
        <el-input v-model="j" style="width: 240px;"></el-input>
        <el-button type="primary">搜索</el-button>
        <div class="filter-right">
          <i class="el-icon-arrow-left"></i> 1/1
          <i class="el-icon-arrow-right"></i>
        </div>
      </div>

      <el-divider></el-divider>

      <div class="flex list-wrap">
        <el-card v-for="(item, index) in 5" :key="index">
          <div>2020-20-02-02 20:20:20</div>
          <div>标题标题标题</div>
          <img src="@/assets/image/profile.jpg" alt />
        </el-card>
      </div>
      <div slot="footer">
        <el-button @click="dialogVisible1 = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible1 = false">确 定</el-button>
      </div>
    </el-dialog>-->
  </div>
</template>

<style lang="scss" scoped>
.page {
  padding: 30px;
  background-color: #fff;
  border-radius: 8px;
}
.form {
  width: 50%;
  max-width: 500px;
}
.preview {
  width: 260px;
  background: #eee;
  border-radius: 8px;
  padding: 15px 10px;
  height: 450px;
  margin-left: 10%;
  .top {
    font-weight: 600;
  }
  .small {
    font-size: 10px;
  }
  .time {
    font-size: 12px;
    color: #aaa;
    transform: scale(0.8);
    padding: 10px 0 5px;
  }
  .msg {
    min-height: 28px;
    background-color: #fff;
    border: 1px solid #e4e6e9;
    display: inline-block;
    position: relative;
    text-align: left;
    font-size: 14px;
    line-height: 22px;
    padding: 3px 6px;
    border-radius: 4px;
    max-width: 70%;
    min-width: 24px;
    box-sizing: border-box;
    word-break: break-all;
    white-space: pre-line;
    margin-left: 10px;
    &:before {
      content: " ";
      display: block;
      position: absolute;
      left: -6px;
      border-width: 4px 6px;
      border-left: 0;
      width: 0;
      border-style: solid;
      border-color: transparent;
      border-right-color: #fff;
      top: 10px;
      z-index: 888;
    }
  }
}
.filter-wrap {
  justify-content: space-between;
}
.list-wrap {
  flex-wrap: wrap;
}
</style>
