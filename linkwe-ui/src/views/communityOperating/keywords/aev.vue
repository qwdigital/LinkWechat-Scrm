<template>
  <div class="wrap" v-loading="loading">
    <el-form :model="form" ref="form" :rules="rules" label-width="100px">
      <el-form-item label="活码名称" prop="taskName">
        <el-input
          v-model="form.taskName"
          maxlength="30"
          show-word-limit
          placeholder="请输入名称"
          clearable
        />
      </el-form-item>

      <el-form-item label="关键词" prop="keywords">
        <el-input
          v-model="form.keywords"
          placeholder="请输入关键词,多个词用英文逗号分隔,每个词不超过10个字"
          clearable
        >
        </el-input>
      </el-form-item>

      <el-form-item label="加群引导语" prop="welcomeMsg">
        <el-input
          type="textarea"
          v-model="form.welcomeMsg"
          maxlength="220"
          show-word-limit
          :autosize="{ minRows: 5, maxRows: 20 }"
          placeholder="请输入加群引导语"
          clearable
        />
      </el-form-item>

      <el-form-item label="选择群活码">
        <el-image
          v-if="groupQrCode && groupQrCode.codeUrl"
          :src="groupQrCode.codeUrl"
          class="code-image"
        >
        </el-image>

        <el-button
          type="primary"
          plain
          class="ml10"
          icon="el-icon-plus"
          size="mini"
          @click="dialogVisibleSelectQrCode = true"
          >{{ groupQrCode && groupQrCode.codeUrl ? '修改' : '添加' }}</el-button
        >
      </el-form-item>

      <el-form-item label=" ">
        <el-button type="primary" @click="submit">保存</el-button>
        <el-button @click="$router.back()">取消</el-button>
      </el-form-item>
    </el-form>

    <div class="preview-wrap">
      <!-- 预览 -->
      <div class="tip">欢迎语样式</div>

      <PhoneDialog
        :message="form.welcomeMsg || '请输入加群引导语'"
        :isOther="groupQrCode && groupQrCode.codeUrl ? true : false"
      >
        <el-image style="border-radius: 6px; width: 100px;" :src="groupQrCode.codeUrl"> </el-image>
      </PhoneDialog>
    </div>

    <SelectQrCode
      :visible.sync="dialogVisibleSelectQrCode"
      @success="submitSelectQrCode"
      :selected="codes"
    >
    </SelectQrCode>
  </div>
</template>

<script>
import { getDetail, add, update } from '@/api/communityOperating/keywords'
import PhoneDialog from '@/components/PhoneDialog'
import SelectQrCode from '@/components/SelectQrCode'

export default {
  components: { PhoneDialog, SelectQrCode },
  data() {
    return {
      taskId: '',
      dialogVisibleSelectQrCode: false,
      loading: false, // 遮罩层
      // 表单参数
      form: {
        taskName: '', // 任务名称
        groupCodeId: '', // 群活码ID
        welcomeMsg: '', // 欢迎语
        keywords: '' // 关键词
      },
      codes: [],
      groupQrCode: {},
      rules: Object.freeze({
        taskName: [{ required: true, message: '该项为必填项', trigger: 'blur' }],
        welcomeMsg: [{ required: true, message: '该项为必填项', trigger: 'blur' }],
        keywords: [{ required: true, message: '该项为必填项', trigger: 'blur' }],
        groupCodeId: [{ required: true, message: '该项为必填项', trigger: 'change' }]
      })
    }
  },
  created() {
    this.taskId = this.$route.query.id
    this.taskId && this.getDetail(this.taskId)
  },
  methods: {
    /** 获取详情 */
    getDetail(id) {
      this.loading = true
      getDetail(id).then(({ data }) => {
        this.form.taskName = data.taskName
        this.form.welcomeMsg = data.welcomeMsg

        // const keywordList = data.keywordList || []
        // const keywords = keywordList.map((k) => k.keyword)
        // this.form.keywords = keywords.join(',')
        this.form.keywords = data.keywords

        if (data.groupCodeInfo && data.groupCodeInfo.id) {
          this.form.groupCodeId = data.groupCodeInfo.id
          this.codes = [data.groupCodeInfo]
          this.groupQrCode = data.groupCodeInfo
        } else {
          this.form.groupCodeId = ''
          this.codes = []
          this.groupQrCode = {}
        }

        this.loading = false
      })
    },
    submit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.loading = true
          if (this.taskId) {
            update(this.taskId, this.form)
              .then(() => {
                this.msgSuccess('更新成功')
                this.loading = false
                this.$router.back()
              })
              .catch(() => {
                this.loading = false
              })
          } else {
            add(this.form)
              .then(() => {
                this.msgSuccess('添加成功')
                this.loading = false
                this.$router.back()
              })
              .catch(() => {
                this.loading = false
              })
          }
        }
      })
    },
    // 选择二维码确认按钮
    submitSelectQrCode(data) {
      this.groupQrCode = data
      this.form.groupCodeId = data.id
      this.$refs.form.validateField('groupCodeId')
    }
  }
}
</script>

<style lang="scss" scoped>
.wrap {
  display: flex;
  margin-top: 24px;
  .el-form {
    margin-right: 10%;
  }
}
.el-form-item {
  width: 500px;
}
.preview-wrap {
  line-height: 26px;
}
.tip {
  color: #999;
}
.welcome-input {
  display: table;
  width: 80%;
  margin: 0 auto 20px;
}
.phone-dialog-image {
  border-radius: 6px;
  width: 100px;
}
</style>
