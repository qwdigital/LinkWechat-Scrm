<script>
import { getDetail, add, update, getQrcode } from '@/api/drainageCode/staff'
import { getList } from '@/api/drainageCode/welcome'
import PhoneDialog from '@/components/PhoneDialog'
import SelectUser from '@/components/SelectUser'
import SelectTag from '@/components/SelectTag'
import SelectMaterial from '@/components/SelectMaterial'
export default {
  components: { PhoneDialog, SelectTag, SelectUser, SelectMaterial },
  data() {
    return {
      dialogVisibleSelectUser: false,
      dialogVisibleSelectTag: false,
      dialogVisibleSelectMaterial: false,
      dialogVisibleSelectWel: false,
      // 遮罩层
      loading: false,
      // 表单参数
      form: {
        codeType: 1,
        qrCode: '',
        isJoinConfirmFriends: 0,
        weEmpleCodeTags: [],
        weEmpleCodeUseScops: [],
      },
      materialSelected: '',
      welQuery: { welcomeMsg: '' },
      welLoading: false,
      welList: [],
      welSelected: {},
      type: { 1: '单人', 2: '多人', 3: '批量单人' },
      activeName: '',
    }
  },
  created() {
    let id = this.$route.query.id
    id && this.getDetail(id)
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
    /** 获取欢迎语列表 */
    getWelList() {
      this.welLoading = true
      getList(this.welQuery).then(({ rows }) => {
        this.welList = rows
        this.$refs.table.$forceUpdate()
        this.welLoading = false
      })
    },
    codeTypeChange() {
      this.form.weEmpleCodeUseScops = []
      this.form.qrCode = ''
    },
    // 选择人员变化事件
    selectedUser(users) {
      let params = { userIds: [], departmentIds: [] }
      this.form.weEmpleCodeUseScops = users.map((d) => {
        d.userId && params.userIds.push(d.userId)
        d.id && params.departmentIds.push(d.id)
        return {
          businessId: d.id || d.userId,
          businessName: d.name,
          businessIdType: d.userId ? 2 : 1,
        }
      })
      params.userIds += ''
      params.departmentIds += ''
      getQrcode(params).then(({ data }) => {
        this.$set(this.form, 'qrCode', data.qr_code)
      })
    },
    submitSelectTag(data) {
      this.form.weEmpleCodeTags = data.map((d) => ({
        tagId: d.tagId,
        tagName: d.name,
      }))
    },
    // 选择素材确认按钮
    submitSelectMaterial(text, image, file) {
      this.form.mediaId = image.id
      this.materialSelected = image.materialUrl
      this.dialogVisibleSelectMaterial = false
    },
    removeMaterial() {
      this.form.mediaId = ''
      this.materialSelected = ''
    },
    // 欢迎语确认按钮
    selectWelcome() {
      this.form.welcomeMsg = this.welSelected.welcomeMsg
      this.form.mediaId = this.welSelected.mediaId
      this.dialogVisibleSelectWel = false
    },
    submit() {
      if (!this.form.weEmpleCodeUseScops.length) {
        this.msgError('请至少选择一名使用员工')
        return
      }
      this.loading = true
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
  },
}
</script>
<template>
  <div class="wrap" v-loading="loading">
    <el-form :model="form" ref="form" label-width="100px">
      <el-form-item label="规则名称" prop="activityScene">
        <el-input
          v-model="form.activityScene"
          maxlength="30"
          show-word-limit
          placeholder="请输入"
          clearable
        />
      </el-form-item>
      <el-form-item label="执行群聊">
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

      <el-form-item label="内容名称" prop="isJoinConfirmFriends">
        <el-input
          v-model="form.welcomeMsg"
          maxlength="220"
          show-word-limit
          :autosize="{ minRows: 5, maxRows: 20 }"
          placeholder="请输入"
          clearable
        />
      </el-form-item>
      <el-form-item label="执行时间" prop="codeType">
        <el-date-picker
          v-model="dateRange"
          value-format="yyyy-MM-dd"
          type="daterange"
          :picker-options="pickerOptions"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          align="right"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="消息内容" prop="activityScene">
        <div class="content-left">
          <el-button class="create" @click="goRoute">新建素材</el-button>
          <el-tabs v-model="activeName">
            <el-tab-pane name="0">
              <span slot="label"> <i class="el-icon-date"></i> 文本 </span>
              <el-input
                v-model="form.textMessage"
                type="textarea"
                maxlength="220"
                show-word-limit
                :autosize="{ minRows: 10, maxRows: 50 }"
                placeholder="请输入"
              ></el-input>
            </el-tab-pane>
            <el-tab-pane name="1">
              <span slot="label"> <i class="el-icon-date"></i> 图片 </span>
              <el-image
                v-if="form.imageMessage"
                style="width: 200px;"
                :src="form.imageMessage"
                fit="fit"
              ></el-image>
            </el-tab-pane>
            <el-button
              type="primary"
              class="mt20"
              @click="dialogVisibleSelectMaterial = true"
              >从素材库选择</el-button
            >
          </el-tabs>
        </div>
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

    <!-- 选择素材弹窗 -->
    <SelectMaterial
      :visible.sync="dialogVisibleSelectMaterial"
      type="1"
      :showArr="[1]"
      @success="submitSelectMaterial"
    >
    </SelectMaterial>

    <el-dialog
      key="a"
      title="选择欢迎语"
      :visible.sync="dialogVisibleSelectWel"
      width="500"
    >
      <div>
        <el-input
          class="welcome-input"
          placeholder="请输入关键字"
          v-model="welQuery.welcomeMsg"
        >
          <el-button slot="append" @click="getWelList">查询</el-button>
        </el-input>
        <el-table
          ref="table"
          v-loading="welLoading"
          :data="welList"
          :max-height="300"
          :show-header="false"
          highlight-current-row
          @current-change="(val) => (welSelected = val)"
        >
          <el-table-column
            property="welcomeMsg"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column width="60">
            <template slot-scope="{ row }">
              <i
                v-if="welSelected.id === row.id"
                class="el-icon-check"
                style="color: rgb(65, 133, 244); font-size: 25px;"
              ></i>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <el-button @click="dialogVisibleSelectWel = false">取 消</el-button>
        <el-button type="primary" @click="selectWelcome">确 定</el-button>
      </div>
    </el-dialog>
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
.el-icon-error {
}
</style>
