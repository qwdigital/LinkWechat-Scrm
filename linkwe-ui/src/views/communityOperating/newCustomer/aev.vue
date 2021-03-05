<script>
import { getDetail, add, update } from '@/api/communityOperating/newCustomer'
// import { getList } from '@/api/drainageCode/welcome'
import PhoneDialog from '@/components/PhoneDialog'
import SelectUser from '@/components/SelectUser'
import SelectTag from '@/components/SelectTag'
import SelectQrCode from '@/components/SelectQrCode'
export default {
  components: { PhoneDialog, SelectTag, SelectUser, SelectQrCode },
  data() {
    return {
      dialogVisibleSelectUser: false,
      dialogVisibleSelectTag: false,
      dialogVisibleSelectQrCode: false,
      // 遮罩层
      loading: false,
      // 表单参数
      form: {
        activityScene: '',
        groupCodeId: 0,
        isJoinConfirmFriends: true,
        // mediaId: 0,
        qrCode: '',
        weEmpleCodeTags: [
          {
            delFlag: 0,
            empleCodeId: 0,
            id: 0,
            tagId: 'string',
            tagName: 'string',
          },
        ],
        weEmpleCodeUseScops: [
          {
            businessId: 'string',
            businessIdType: 0,
            businessName: 'string',
            delFlag: 0,
            empleCodeId: 0,
            id: 0,
            mobile: 'string',
          },
        ],
        welcomeMsg: '',
      },
      rules: Object.freeze({
        activityScene: [{ required: true, message: '必填项', trigger: 'blur' }],
        weEmpleCodeUseScops: [
          { required: true, message: '必填项', trigger: 'change' },
        ],
        welcomeMsg: [{ required: true, message: '必填项', trigger: 'blur' }],
        qrCode: [{ required: true, message: '必填项', trigger: 'change' }],
      }),
      materialSelected: '',
    }
  },
  created() {
    let id = this.$route.query.id
    id && this.getDetail(id)
    this.$route.meta.title = (id ? '编辑' : '新建') + '新客自动拉群'
  },
  methods: {
    /** 获取详情 */
    getDetail(id) {
      this.loading = true
      getDetail(id).then(({ data }) => {
        this.form = data
        this.materialSelected =
          data.weMaterial == null ? '' : data.weMaterial.materialUrl
        this.loading = false
      })
    },
    codeTypeChange() {
      this.form.weEmpleCodeUseScops = []
      this.form.qrCode = ''
    },
    // 选择人员变化事件
    selectedUser(users) {
      debugger
      this.form.weEmpleCodeUseScops = users.map((d) => {
        return {
          businessId: d.id || d.userId,
          businessName: d.name,
          businessIdType: d.userId ? 2 : 1,
          mobile: d.mobile,
          empleCodeId: d.empleCodeId,
        }
      })
    },
    submitSelectTag(data) {
      this.form.weEmpleCodeTags = data.map((d) => ({
        tagId: d.tagId,
        tagName: d.name,
      }))
    },
    // 选择二维码确认按钮
    submitSelectQrCode(data) {
      // debugger
      this.form.groupCodeId = data.id
      this.form.qrCode = data.codeUrl
      this.$refs.form.validateField('qrCode')
    },
    removeMaterial() {
      this.form.mediaId = ''
      this.materialSelected = ''
    },
    submit() {
      if (!this.form.weEmpleCodeUseScops.length) {
        this.msgError('请至少选择一名使用员工')
        return
      }
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.loading = true
          ;(this.form.id ? update : add)({ WeCommunityNewGroupDto: this.form })
            .then(({ data }) => {
              this.msgSuccess('操作成功')
              this.loading = false
              this.$router.back()
            })
            .catch(() => {
              this.loading = false
            })
        }
      })
    },
  },
}
</script>
<template>
  <div class="wrap" v-loading="loading">
    <el-form :model="form" ref="form" :rules="rules" label-width="100px">
      <el-form-item label="活码名称" prop="activityScene">
        <el-input
          v-model="form.activityScene"
          maxlength="30"
          show-word-limit
          placeholder="请输入"
          clearable
        />
      </el-form-item>
      <el-form-item label="使用员工" prop="weEmpleCodeUseScops">
        <!-- closable -->
        <el-tag
          size="medium"
          v-for="(item, index) in form.weEmpleCodeUseScops"
          :key="index"
          >{{ item.businessName }}</el-tag
        >
        <el-button
          type="primary"
          plain
          class="ml10"
          icon="el-icon-plus"
          size="mini"
          @click="dialogVisibleSelectUser = true"
          >{{ form.weEmpleCodeUseScops.length ? '修改' : '添加' }}</el-button
        >
      </el-form-item>

      <el-form-item label="加群引导语" prop="welcomeMsg">
        <el-input
          type="textarea"
          v-model="form.welcomeMsg"
          maxlength="220"
          show-word-limit
          :autosize="{ minRows: 5, maxRows: 20 }"
          placeholder="请输入"
          clearable
        />
      </el-form-item>
      <el-form-item label="选择群活码" prop="qrCode">
        <el-image v-if="form.qrCode" :src="form.qrCode" class="code-image">
        </el-image>

        <el-button
          type="primary"
          plain
          class="ml10"
          icon="el-icon-plus"
          size="mini"
          @click="dialogVisibleSelectQrCode = true"
          >{{ form.qrCode ? '修改' : '选择' }}</el-button
        >
      </el-form-item>
      <el-form-item label="新客户标签" prop="weEmpleCodeTags">
        <!-- closable -->
        <el-tag
          size="medium"
          v-for="(item, index) in form.weEmpleCodeTags"
          :key="index"
          >{{ item.tagName }}</el-tag
        >
        <el-button
          type="primary"
          class="ml10"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="dialogVisibleSelectTag = true"
          >添加标签</el-button
        >
        <!-- <div class="tip">
          根据使用场景做标签记录，扫码添加的客户，可自动打上标签
        </div> -->
      </el-form-item>
      <el-form-item label="添加设置" prop="isJoinConfirmFriends">
        <el-checkbox
          :true-label="1"
          :false-label="0"
          v-model="form.isJoinConfirmFriends"
          >客户添加时无需经过确认自动成为好友</el-checkbox
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
        :isOther="!!materialSelected"
      >
        <el-image style="border-radius: 6px;" :src="materialSelected" fit="fit">
        </el-image>
      </PhoneDialog>
    </div>

    <!-- 选择使用员工弹窗 -->
    <SelectUser
      :key="form.codeType"
      :visible.sync="dialogVisibleSelectUser"
      title="选择使用员工"
      :isOnlyLeaf="form.codeType !== 2"
      :isSigleSelect="form.codeType == 1"
      @success="selectedUser"
    ></SelectUser>

    <!-- 选择标签弹窗 -->
    <SelectTag
      :visible.sync="dialogVisibleSelectTag"
      :selected="form.toTag"
      @success="submitSelectTag"
    >
    </SelectTag>

    <!-- 选择二维码弹窗 -->
    <SelectQrCode
      :visible.sync="dialogVisibleSelectQrCode"
      @success="submitSelectQrCode"
    >
    </SelectQrCode>
  </div>
</template>
<style lang="scss" scoped>
.wrap {
  display: flex;
  margin-top: 24px;
  .el-form {
    margin-right: 10%;
  }
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
.code-image {
  width: 100px;
  height: 100px;
}
</style>
