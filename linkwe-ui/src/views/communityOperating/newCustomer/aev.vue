<script>
import { getDetail, add, update } from '@/api/communityOperating/newCustomer'
import PhoneDialog from '@/components/PhoneDialog'
import SelectUser from '@/components/SelectUser'
import SelectTag from '@/components/SelectTag'
import SelectQrCode from '@/components/SelectQrCode'

export default {
  components: { PhoneDialog, SelectTag, SelectUser, SelectQrCode },
  data() {
    return {
      newGroupId: '',
      dialogVisibleSelectUser: false, // 选择员工会话
      dialogVisibleSelectTag: false, // 选择客户标签会话
      dialogVisibleSelectQrCode: false, // 选择群活码会话
      loading: false,
      form: {
        codeName: '', // 活码名称
        emplList: [], // 员工
        welcomeMsg: '', //加群引导语
        groupCodeId: undefined, // 群活码ID
        tagList: [], // 客户标签
        skipVerify: 0 // 无需确认自动加好友
      },
      tags: [],
      users: [],
      codes: [],
      groupQrCode: {},
      rules: Object.freeze({
        codeName: [{ required: true, message: '该项为必填项', trigger: 'blur' }],
        emplList: [{ required: true, message: '该项为必填项', trigger: 'blur' }],
        tagList: [{ required: true, message: '该项为必填项', trigger: 'change' }],
        groupCodeId: [{ required: true, message: '该项为必填项', trigger: 'blur' }],
        welcomeMsg: [{ required: true, message: '该项为必填项', trigger: 'blur' }]
      })
    }
  },
  watch: {
    tags(tags) {
      this.form.tagList = tags.map((t) => t.tagId)
    },
    users(users) {
      this.form.emplList = users.map((u) => u.businessId)
      this.$refs.form.validateField('emplList')
    }
  },
  created() {
    this.newGroupId = this.$route.query.id
    this.newGroupId && this.getDetail(this.newGroupId)
    this.$route.meta.title = (this.newGroupId ? '编辑' : '新建') + '新客自动拉群'
  },
  methods: {
    /** 获取详情 */
    getDetail(id) {
      this.loading = true
      getDetail(id).then(({ data }) => {
        this.form.codeName = data.codeName || ''
        this.form.skipVerify = data.skipVerify || 0
        this.form.welcomeMsg = data.welcomeMsg || ''

        if (data.groupCodeInfo && data.groupCodeInfo.id) {
          this.codes = [data.groupCodeInfo]
          this.groupQrCode = data.groupCodeInfo
          this.form.groupCodeId = data.groupCodeInfo.id
        } else {
          this.codes = []
          this.groupQrCode = {}
          this.form.groupCodeId = ''
        }

        this.tags = data.tagList || []
        this.users = data.emplList || []

        this.loading = false
      })
    },
    // 选择人员变化事件
    submitSelectUser(users) {
      this.users = users.map((d) => {
        return {
          businessId: d.id || d.userId,
          businessName: d.name,
          businessIdType: d.userId ? 2 : 1,
          mobile: d.mobile,
          empleCodeId: d.empleCodeId
        }
      })
    },
    // 客户标签选择
    submitSelectTag(tags) {
      this.tags = tags.map((t) => {
        return {
          tagId: t.tagId,
          tagName: t.name
        }
      })
    },
    // 选择二维码确认按钮
    submitSelectQrCode(data) {
      this.groupQrCode = data
      this.form.groupCodeId = data.id
      this.$refs.form.validateField('groupCodeId')
    },
    submit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.loading = true
          if (this.newGroupId) {
            update(this.newGroupId, this.form)
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
    }
  }
}
</script>

<template>
  <div class="wrap" v-loading="loading">
    <el-form :model="form" ref="form" :rules="rules" label-width="100px">
      <el-form-item label="活码名称" prop="codeName">
        <el-input
          v-model="form.codeName"
          maxlength="30"
          show-word-limit
          placeholder="请输入"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="使用员工" prop="emplList">
        <!-- closable -->
        <el-tag size="medium" v-for="(user, index) in users" :key="index">{{
          user.businessName
        }}</el-tag>
        <el-button
          type="primary"
          plain
          :class="users.length > 0 ? 'ml10' : ''"
          icon="el-icon-plus"
          size="mini"
          @click="dialogVisibleSelectUser = true"
          >{{ users.length ? '修改' : '添加' }}</el-button
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
        ></el-input>
      </el-form-item>
      <el-form-item label="选择群活码" prop="groupCodeId">
        <el-image
          v-if="groupQrCode && groupQrCode.codeUrl"
          :src="groupQrCode.codeUrl"
          class="code-image mr10"
        ></el-image>
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="dialogVisibleSelectQrCode = true"
          >{{ groupQrCode && groupQrCode.codeUrl ? '修改' : '选择' }}</el-button
        >
      </el-form-item>
      <el-form-item label="新客户标签" prop="tags">
        <!-- closable -->
        <el-tag size="medium" v-for="(tag, index) in tags" :key="index">{{ tag.tagName }}</el-tag>
        <el-button
          type="primary"
          :class="tags.length > 0 ? 'ml10' : ''"
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
      <el-form-item label="添加设置" prop="skipVerify">
        <el-checkbox :true-label="1" :false-label="0" v-model="form.skipVerify"
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
        :isOther="groupQrCode && groupQrCode.codeUrl ? true : false"
      >
        <el-image class="phone-dialog-image" :src="groupQrCode.codeUrl" fit="fit"></el-image>
      </PhoneDialog>
    </div>

    <!-- 选择使用员工弹窗 -->
    <SelectUser
      :key="form.codeType"
      :visible.sync="dialogVisibleSelectUser"
      title="选择使用员工"
      :isOnlyLeaf="form.codeType !== 2"
      :isSigleSelect="form.codeType == 1"
      @success="submitSelectUser"
    ></SelectUser>

    <!-- 选择标签弹窗 -->
    <SelectTag
      :visible.sync="dialogVisibleSelectTag"
      :selected="tags"
      @success="submitSelectTag"
    ></SelectTag>

    <!-- 选择二维码弹窗 -->
    <SelectQrCode
      :visible.sync="dialogVisibleSelectQrCode"
      @success="submitSelectQrCode"
      :selected="codes"
    ></SelectQrCode>
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
.phone-dialog-image {
  border-radius: 6px;
  width: 100px;
}
</style>
