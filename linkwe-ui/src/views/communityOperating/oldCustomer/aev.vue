<template>
  <div class="wrap" v-loading="loading">
    <el-form :model="form" ref="form" :rules="rules" label-width="100px">
      <el-form-item label="任务名称" prop="taskName">
        <el-input
          v-model="form.taskName"
          maxlength="30"
          show-word-limit
          placeholder="请输入"
          clearable
        />
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

      <el-form-item label="选择群活码" prop="groupCodeId">
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
          >{{ groupQrCode && groupQrCode.codeUrl ? '修改' : '选择' }}</el-button
        >
      </el-form-item>

      <!-- <el-form-item label="发送方式" prop="sendType">
        <el-radio-group v-model="form.sendType">
          <el-radio
            v-for="(sendType, index) in sendTypeOptions"
            :key="index"
            :label="sendType.value"
            >{{ sendType.label }}</el-radio
          >

          <div class="tip">
            注：客户每天只能接收来自一名成员的一条群发消息，每月最多接收来自同一企业的四条群发消息。
          </div>
        </el-radio-group>
      </el-form-item> -->

      <el-form-item label="发送范围" prop="sendScope">
        <el-radio-group v-model="form.sendScope">
          <el-radio
            v-for="(target, index) in sendScopeOptions"
            :key="index"
            :label="target.value"
            >{{ target.label }}</el-radio
          >
        </el-radio-group>
      </el-form-item>

      <el-form-item label="发送性别" prop="sendGender">
        <el-radio-group v-model="form.sendGender" :disabled="form.sendScope == 0">
          <el-radio
            v-for="(sendGender, index) in sendGenderOptions"
            :key="index"
            :label="sendGender.value"
            >{{ sendGender.label }}</el-radio
          >
        </el-radio-group>
      </el-form-item>

      <el-form-item label="添加时间">
        <el-date-picker
          v-model="dateRange"
          value-format="yyyy-MM-dd"
          type="daterange"
          :picker-options="pickerOptions"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          align="right"
          :disabled="form.sendScope == 0"
        ></el-date-picker>
      </el-form-item>

      <el-form-item label="客户标签" prop="tagList">
        <el-tag size="medium" v-for="(tag, index) in tags" :key="index">{{ tag.name }}</el-tag>
        <el-button
          type="primary"
          class="ml10"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="dialogVisibleSelectTag = true"
          :disabled="form.sendScope == 0"
          >添加标签</el-button
        >
      </el-form-item>

      <el-form-item label="添加人">
        <el-tag size="medium" v-for="(user, index) in users" :key="index">{{ user.name }}</el-tag>
        <el-button
          type="primary"
          plain
          class="ml10"
          icon="el-icon-plus"
          size="mini"
          @click="dialogVisibleSelectUser = true"
          :disabled="form.sendScope == 0"
          >{{ users.length ? '修改' : '添加' }}</el-button
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

    <!-- 选择使用员工弹窗 -->
    <SelectUser
      :visible.sync="dialogVisibleSelectUser"
      title="选择使用员工"
      @success="submitSelectUser"
    ></SelectUser>

    <!-- 选择标签弹窗 -->
    <SelectTag :visible.sync="dialogVisibleSelectTag" :selected="tags" @success="submitSelectTag">
    </SelectTag>

    <!-- 选择群活码弹窗 -->
    <SelectQrCode
      :visible.sync="dialogVisibleSelectQrCode"
      @success="submitSelectQrCode"
      :selected="codes"
    >
    </SelectQrCode>
  </div>
</template>

<script>
import { getDetail, add, update } from '@/api/communityOperating/oldCustomer'
import PhoneDialog from '@/components/PhoneDialog'
import SelectUser from '@/components/SelectUser'
import SelectTag from '@/components/SelectTag'
import SelectQrCode from '@/components/SelectQrCode'
export default {
  components: { PhoneDialog, SelectTag, SelectUser, SelectQrCode },
  data() {
    return {
      taskId: '',
      dialogVisibleSelectUser: false,
      dialogVisibleSelectTag: false,
      dialogVisibleSelectQrCode: false,
      loading: false,
      // 表单参数
      form: {
        taskName: '', // 任务名称
        welcomeMsg: '', // 加群引导语
        sendType: 1, // 发送方式
        groupCodeId: '', // 群活码ID
        tagList: [], // 标签
        scopeList: [], // 员工
        sendScope: 0, // 发送范围
        sendGender: 0, // 发送性别
        cusBeginTime: '', // 目标客户添加起始时间
        cusEndTime: '' // 目标客户添加结束时间
      },
      tags: [],
      users: [],
      codes: [],
      groupQrCode: {}, // 选择的群活码链接
      dateRange: [],
      // sendTypeOptions: [
      //   { label: '企业群发', value: 0 },
      //   { label: '个人群发', value: 1 }
      // ],
      sendGenderOptions: [
        { label: '全部', value: 0 },
        { label: '男', value: 1 },
        { label: '女', value: 2 },
        { label: '未知', value: 3 }
      ],
      sendScopeOptions: [
        { label: '全部客户', value: 0 },
        { label: '部分客户', value: 1 }
      ],
      rules: Object.freeze({
        taskName: [{ required: true, message: '该项为必填项', trigger: 'blur' }],
        welcomeMsg: [{ required: true, message: '该项为必填项', trigger: 'blur' }],
        groupCodeId: [{ required: true, message: '该项为必填项', trigger: 'blur' }],
        // sendType: [{ required: true, message: '该项为必填项', trigger: 'blur' }],
        sendScope: [{ required: true, message: '该项为必填项', trigger: 'blur' }],
        tagListValidate: [{ required: true, message: '该项为必填项', trigger: 'change' }],
        scopeListValidate: [{ required: true, message: '该项为必填项', trigger: 'change' }]
      })
    }
  },
  watch: {
    // 日期选择器数据同步至查询参数
    dateRange(dateRange) {
      if (!dateRange || dateRange.length !== 2) {
        this.form.cusBeginTime = ''
        this.form.cusEndTime = ''
      } else {
        ;[this.form.cusBeginTime, this.form.cusEndTime] = dateRange
      }
    },
    users(users) {
      this.form.scopeList = users.map((user) => {
        return user.userId
      })
      this.$refs.form.validateField('scopeList')
    },
    tags(tags) {
      this.form.tagList = tags.map((tag) => {
        return tag.tagId
      })
      this.$refs.form.validateField('tagList')
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
        this.form.taskName = data.taskName || ''
        this.form.welcomeMsg = data.welcomeMsg || ''
        this.form.sendType = data.sendType || 1
        this.form.sendScope = data.sendScope || 0
        this.form.sendGender = data.sendGender || 0

        this.tags = data.tagList || []
        this.users = data.scopeList || []
        this.dateRange = [data.cusBeginTime || '', data.cusEndTime || '']

        if (data.groupCodeInfo && data.groupCodeInfo.id) {
          this.codes = [data.groupCodeInfo]
          this.groupQrCode = data.groupCodeInfo
          this.form.groupCodeId = this.groupQrCode.id
        } else {
          this.codes = []
          this.groupQrCode = {}
          this.form.groupCodeId = ''
        }

        this.loading = false
      })
    },
    // 选择人员事件
    submitSelectUser(users) {
      this.users = users
    },
    // 选择tag事件
    submitSelectTag(tags) {
      this.tags = tags
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
