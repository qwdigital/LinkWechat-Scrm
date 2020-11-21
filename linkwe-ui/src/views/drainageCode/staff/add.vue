<script>
import { getDetail, add, update } from '@/api/drainageCode/staff'
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
        isJoinConfirmFriends: 0,
        weEmpleCodeTags: [],
        weEmpleCodeUseScops: [],
      },
      welQuery: { welcomeMsg: '' },
      welLoading: false,
      welList: [],
      welSelected: {},
    }
  },
  created() {
    let id = this.$route.query.id
    id && this.getData(id)
  },
  methods: {
    selectedUser(data) {
      this.form.weEmpleCodeUseScops = data.map((d) => {
        return {
          businessId: d.id || d.userId,
          businessName: d.name,
          businessIdType: d.userId ? 2 : 1,
        }
      })
    },
    submitSelectTag(data) {
      this.form.weEmpleCodeTags = data.map((d) => ({
        tagId: d.tagId,
        tagName: d.name,
      }))
    },
    // 选择素材确认按钮
    submitSelectMaterial(text, image, file) {},
    /** 获取详情 */
    getData(id) {
      this.loading = true
      getDetail(id).then(({ data }) => {
        this.form = data
        this.loading = false
      })
    },
    /** 获取欢迎语列表 */
    getWelList() {
      this.welLoading = true
      getList(this.welQuery).then(({ rows }) => {
        this.welList = rows
        this.welLoading = false
      })
    },
    // 欢迎语确认按钮
    selectWelcome() {
      this.form.welcomeMsg = this.welSelected.welcomeMsg
      this.form.mediaId = this.welSelected.mediaId
      this.dialogVisibleSelectWel = false
    },
    submit() {
      this.loading = true
      add(this.form).then(({ data }) => {
        this.loading = false
        this.$router.back()
      })
    },
  },
}
</script>
<template>
  <div class="wrap" v-loading="loading">
    <el-form :model="form" ref="form" label-width="100px">
      <el-form-item label="类型" prop="codeType">
        <el-radio-group v-model="form.codeType">
          <el-radio :label="1">单人</el-radio>
          <el-radio :label="2">多人</el-radio>
          <el-radio :label="3">批量</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="使用员工">
        <el-tag
          closable
          v-for="(item, index) in form.weEmpleCodeUseScops"
          :key="index"
          >{{ item.businessName }}</el-tag
        >
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="dialogVisibleSelectUser = true"
          >{{ form.weEmpleCodeUseScops.length ? '修改' : '添加' }}</el-button
        >
      </el-form-item>
      <el-form-item label="添加设置" prop="isJoinConfirmFriends">
        <el-checkbox
          :true-label="1"
          :false-label="0"
          v-model="form.isJoinConfirmFriends"
          >客户添加时无需经过确认自动成为好友</el-checkbox
        >
      </el-form-item>
      <el-form-item label="活动场景" prop="activityScene">
        <el-input
          v-model="form.activityScene"
          maxlength="30"
          show-word-limit
          placeholder="请输入"
          clearable
        />
      </el-form-item>
      <el-form-item label="扫码标签" prop="weEmpleCodeTags">
        <el-tag
          closable
          v-for="(item, index) in form.weEmpleCodeTags"
          :key="index"
          >{{ item.tagName }}</el-tag
        >
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="dialogVisibleSelectTag = true"
          >添加标签</el-button
        >
        <div class="tip">
          根据使用场景做标签记录，扫码添加的客户，可自动打上标签
        </div>
      </el-form-item>
      <el-form-item label="欢迎语">
        <el-card shadow="never">
          <el-input
            type="textarea"
            v-model="form.welcomeMsg"
            maxlength="220"
            show-word-limit
            :autosize="{ minRows: 5, maxRows: 20 }"
            placeholder="请输入"
            clearable
          />
          <el-divider></el-divider>
          <!-- <el-popover placement="top-start" width="200" trigger="hover">
            <div class="flex">
              <div>图片</div>
              <div>网页</div>
              <div>小程序</div>
            </div>
            <el-button
              slot="reference"
              icon="el-icon-plus"
              size="mini"
              @click="resetQuery"
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
        <el-button
          icon="el-icon-plus"
          type="primary"
          size="mini"
          @click="getWelList(), (dialogVisibleSelectWel = true)"
          >从欢迎语模板选取</el-button
        >
        <div class="tip">
          设置个性化欢迎语，扫描该员工活码添加的客户，将自动推送该欢迎语
        </div>
      </el-form-item>
      <el-form-item label=" ">
        <el-button type="primary" @click="submit">保存</el-button>
        <el-button @click="$router.back()">取消</el-button>
      </el-form-item>
    </el-form>

    <div class="preview-wrap">
      <el-image
        style="width: 180px; height: 180px"
        src="@/assets/image/profile.jpg"
        fit="fit"
      ></el-image>
      <div class="tip mb20">二维码预览</div>

      <el-card shadow="never" :body-style="{ padding: '10px 10px 20px' }">
        <i class="el-icon-user-solid" style="font-size: 20px;"></i> 63136
        <el-divider></el-divider>
        <div>性别：男</div>
        <div>设置：备注和描述</div>
        <div>
          标签：
          <el-tag>试用版1重要（1w以上）</el-tag>
          <el-tag>试用版1</el-tag>
        </div>
      </el-card>
      <div class="tip mb20">扫码标签样式</div>

      <!-- 预览 -->
      <PhoneDialog :message="form.welcomeMsg || '请输入欢迎语'"></PhoneDialog>
      <div class="tip">欢迎语样式</div>
    </div>

    <!-- 选择使用员工弹窗 -->
    <SelectUser
      :key="form.codeType"
      :visible.sync="dialogVisibleSelectUser"
      title="选择使用员工"
      :isOnlyLeaf="false"
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
</style>
