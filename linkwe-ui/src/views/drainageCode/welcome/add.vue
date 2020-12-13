<script>
import { update, add } from '@/api/drainageCode/welcome'
import PhoneDialog from '@/components/PhoneDialog'
import SelectMaterial from '@/components/SelectMaterial'

export default {
  components: { PhoneDialog, SelectMaterial },
  props: {},
  data() {
    return {
      dialogVisible: false,
      // dialogVisible1: false,
      dialogVisibleSelectMaterial: false,
      form: {
        id: '',
        mediaId: '',
        welcomeMsgTplType: '',
        welcomeMsg: '',
        materialUrl: '',
      },
      // 遮罩层
      loading: false,
    }
  },
  watch: {},
  computed: {},
  created() {
    this.form = this.$route.query
  },
  mounted() {},
  methods: {
    getData() {},
    submit() {
      ;(this.form.id ? update : add)(this.form)
        .then(({ data }) => {
          this.msgSuccess('操作成功')
          this.loading = false
          this.$router.back()
        })
        .catch(() => {
          this.loading = false
        })
    },
    insertName() {
      this.form.welcomeMsg += '#客户昵称#'
    },
    // 选择素材确认按钮
    submitSelectMaterial(text, image, file) {
      this.form.mediaId = image.id
      this.form.materialUrl = image.materialUrl
      this.dialogVisibleSelectMaterial = false
    },
    removeMaterial() {
      this.form.mediaId = ''
      this.form.materialUrl = ''
    },
  },
}
</script>

<template>
  <div class="flex page" v-loading="loading">
    <el-form ref="form" :model="form" label-width="80px" class="form">
      <el-form-item label="欢迎语">
        <el-card shadow="never" class="card">
          <div style="height: 300px;">
            <el-input
              type="textarea"
              :rows="5"
              maxlength="100"
              show-word-limit
              placeholder="请输入欢迎语"
              v-model="form.welcomeMsg"
            ></el-input>
          </div>
          <div class="bfc-o">
            <el-button
              type="default"
              class="fr"
              round
              icon="el-icon-user-solid"
              @click="insertName()"
              >插入客户昵称</el-button
            >
          </div>
          <el-divider></el-divider>
          <div v-if="form.materialUrl">
            <el-image
              style="width: 100px; height: 100px; cursor: pointer;border-radius: 6px;"
              :src="form.materialUrl"
              fit="fit"
            >
            </el-image>
            <i class="el-icon-error" @click="removeMaterial"></i>
          </div>

          <!-- <el-popover placement="top-start" trigger="hover">
            <div class="ac">
              <Upload
                ><el-button>
                  <i class="el-icon-picture-outline"></i>
                  <p>图片</p>
                </el-button></Upload
              >

              <el-button @click="dialogVisible = true">
                <i class="el-icon-link"></i>
                <p>网页</p>
              </el-button>
              <el-button @click="dialogVisible1 = true">
                <i class="el-icon-link"></i>
                <p>小程序</p>
              </el-button>
            </div>
            <el-button slot="reference" icon="el-icon-plus" size="mini"
              >添加图片</el-button
            >
          </el-popover> -->

          <el-button
            icon="el-icon-plus"
            size="mini"
            @click="dialogVisibleSelectMaterial = true"
            >添加图片</el-button
          >
        </el-card>
      </el-form-item>
      <el-form-item label=" ">
        <el-button type="primary" @click="submit">保存</el-button>
        <el-button @click="$router.back()">取消</el-button>
      </el-form-item>
    </el-form>

    <PhoneDialog
      style="margin-left: 10%;"
      :message="form.welcomeMsg || '请输入欢迎语'"
      :isOther="!!form.materialUrl"
    >
      <el-image style="border-radius: 6px;" :src="form.materialUrl" fit="fit">
      </el-image
    ></PhoneDialog>

    <!-- 选择素材弹窗 -->
    <SelectMaterial
      :visible.sync="dialogVisibleSelectMaterial"
      type="1"
      :showArr="[1]"
      @success="submitSelectMaterial"
    >
    </SelectMaterial>

    <!-- <el-dialog title="添加网页消息" :visible.sync="dialogVisible" width="width">
      <el-form :model="form" inline>
        <el-form-item label="添加网页消息" label-width="200">
          <el-input
            style="width: 400px;"
            v-model="j"
            placeholder="以http或https开头"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false"
          >确 定</el-button
        >
      </div>
    </el-dialog> -->

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

.filter-wrap {
  justify-content: space-between;
}
.list-wrap {
  flex-wrap: wrap;
}
</style>
