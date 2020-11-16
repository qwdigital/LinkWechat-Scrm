<script>
import { add } from '@/api/groupMessage'
import PhoneDialog from '@/components/PhoneDialog'
import SelectUser from '@/components/SelectUser'
import SelectTag from '@/components/SelectTag'
import SelectMaterial from '@/components/SelectMaterial/index'

export default {
  components: { PhoneDialog, SelectTag, SelectUser, SelectMaterial },
  props: {},
  data() {
    return {
      // 表单参数
      form: {
        pushType: '0',
        pushRange: '0',
        toUser: [],
        toParty: [],
        toTag: [],
      },
      userParty: [],
      rules: {},
      statusOptions: Object.freeze([
        { label: '发送给客户', value: '0' },
        { label: '发送给客户群', value: '1' },
      ]),
      activeName: '0',
      dialogVisibleSelectCustomer: false,
      dialogVisibleSelectUser: false,
      dialogVisibleSelectTag: false,
      dialogVisibleSelectMaterial: false,
    }
  },
  watch: {},
  computed: {
    // 是否显示选择的客户标签
    isOnlyTag() {
      return this.form.toTag[0] && this.form.pushType == 0
    },
    // 是否显示选择范围后的文字说明
    isSelectedText() {
      return this.userParty[0] || this.isOnlyTag
    },
    // 选择范围后的文字说明
    selectedText() {
      return `将发送消息给${
        this.userParty[0] ? this.userParty[0] + '等部门或成员的' : ''
      }${this.userParty[0] && this.isOnlyTag ? '，且' : ''}${
        this.isOnlyTag ? '满足' + this.form.toTag[0] + '等标签的' : ''
      }${this.form.pushType == 0 ? '客户' : '客户群'}`
    },
  },
  created() {},
  mounted() {},
  methods: {
    // 显示选择客户范围弹窗
    showRangeDialog() {
      this[
        this.form.pushType == 0
          ? 'dialogVisibleSelectCustomer'
          : 'dialogVisibleSelectUser'
      ] = true
    },
    // 选择添加人确认按钮
    selectedUser(data) {
      this.userParty = data
    },
    // 选择标签确认按钮
    submitSelectTag(data) {
      this.form.toTag = data
    },
    // 新建素材按钮
    goRoute() {
      let contentType = ['text', 'image', 'file']
      window.open('#/material/' + contentType[this.activeName])
    },
    // 选择素材确认按钮
    submitSelectMaterial(text, image, file) {},
  },
}
</script>

<template>
  <div>
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="群发类型" prop="pushType">
        <el-radio-group v-model="form.pushType">
          <el-radio
            v-for="dict in statusOptions"
            :key="dict.value"
            :label="dict.value"
            >{{ dict.label }}</el-radio
          >
        </el-radio-group>
      </el-form-item>
      <el-form-item label="发送范围" prop="pushRange">
        <el-button
          v-show="!isSelectedText"
          type="text"
          @click="showRangeDialog()"
          >{{
            isSelectedText
              ? '修改'
              : form.pushType == 0
              ? '选择发送客户'
              : '按群主选择客户群'
          }}</el-button
        >
        <span v-show="isSelectedText">{{ selectedText }}</span>
      </el-form-item>
      <el-form-item label="发送时间" prop="settingTime">
        <el-date-picker
          v-model="form.settingTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="datetime"
          placeholder="选择日期时间"
        ></el-date-picker>
        <span class="small-tip">设置时间定时发送，不设置立即发送</span>
      </el-form-item>
      <el-form-item label=" ">
        <div class="flex content-wrap">
          <div class="content-left">
            <el-button class="create" @click="goRoute">新建素材</el-button>
            <el-tabs v-model="activeName">
              <el-tab-pane name="0">
                <span slot="label"> <i class="el-icon-date"></i> 文本 </span>
                <el-input
                  v-model="form.messageJson"
                  type="textarea"
                  maxlength="220"
                  show-word-limit
                  :autosize="{ minRows: 10, maxRows: 50 }"
                  placeholder="请输入"
                ></el-input>
              </el-tab-pane>
              <el-tab-pane name="1">
                <span slot="label"> <i class="el-icon-date"></i> 图片 </span>
              </el-tab-pane>
              <el-tab-pane name="2">
                <span slot="label"> <i class="el-icon-date"></i> 文件 </span>
              </el-tab-pane>
              <el-button
                type="primary"
                class="mt20"
                @click="dialogVisibleSelectMaterial = true"
                >从素材库选择</el-button
              >
              <!-- <el-tab-pane label="配置管理" name="second">
                <span slot="label"> <i class="el-icon-date"></i> 网页 </span>
                配置管理
              </el-tab-pane> -->

              <!-- <el-tab-pane label="定时任务补偿" name="fourth">
                <span slot="label"> <i class="el-icon-date"></i> 海报 </span>
                定时任务补偿
              </el-tab-pane> -->
            </el-tabs>
            <div class="mt15">
              <el-button type="primary">通知成员发送</el-button>
              <span class="small-tip"
                >通知成员，向选中的客户发送以上企业消息</span
              >
              <i class="el-icon-edit"></i>
            </div>
          </div>

          <!-- 预览 -->
          <PhoneDialog
            :message="form.messageJson || '请输入欢迎语'"
          ></PhoneDialog>
        </div>
      </el-form-item>
    </el-form>

    <!-- 选择发送客户弹窗 -->
    <el-dialog title="选择发送客户" :visible.sync="dialogVisibleSelectCustomer">
      <el-form :model="form" label-width="80px">
        <el-form-item label="发送消息">
          <el-radio-group v-model="form.pushRange">
            <el-radio label="0">全部客户</el-radio>
            <el-radio label="1">按条件筛选客户</el-radio>
          </el-radio-group>
        </el-form-item>
        <div v-show="form.pushRange == 1">
          <el-form-item label="添加人">
            <div class="input-wrap" @click="dialogVisibleSelectUser = true">
              <span class="placeholder" v-if="!userParty.length">请选择</span>
              <template v-else>
                <el-tag
                  type="info"
                  v-for="(unit, unique) in userParty"
                  :key="unique"
                  >{{ unit.name }}</el-tag
                >
              </template>
            </div>
          </el-form-item>
          <el-form-item label="标签">
            <div class="input-wrap" @click="dialogVisibleSelectTag = true">
              <span class="placeholder" v-if="!form.toTag.length">请选择</span>
              <template v-else>
                <el-tag
                  type="info"
                  v-for="(unit, unique) in form.toTag"
                  :key="unique"
                  >{{ unit.name }}</el-tag
                >
              </template>
            </div>
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisibleSelectCustomer = false"
          >取 消</el-button
        >
        <el-button type="primary" @click="dialogVisibleSelectCustomer = false"
          >确 定</el-button
        >
      </div>
    </el-dialog>

    <!-- 选择添加人弹窗 -->
    <SelectUser
      :visible.sync="dialogVisibleSelectUser"
      title="选择添加人"
      :isOnlyLeaf="false"
      @success="selectedUser"
    ></SelectUser>

    <!-- 选择标签弹窗 -->
    <SelectTag
      :visible.sync="dialogVisibleSelectTag"
      title="选择标签"
      :selected="form.toTag"
      @success="submitSelectTag"
    >
    </SelectTag>

    <SelectMaterial
      :visible.sync="dialogVisibleSelectMaterial"
      :type.sync="activeName"
      @success="submitSelectMaterial"
    ></SelectMaterial>
  </div>
</template>

<style lang="scss" scoped>
.small-tip {
  font-size: 12px;
  color: #999;
  margin-left: 12px;
}
.content-wrap {
  height: 500px;
  align-items: stretch;
  justify-content: space-between;
  padding-right: 6%;
  .content-left {
    position: relative;
    width: 60%;
    .create {
      position: absolute;
      right: 10px;
      top: 14px;
      z-index: 1;
    }
  }

  .el-tabs {
    height: 90%;
    background: #eee;
    border-radius: 8px;
    padding: 10px;
  }
  .select {
  }
}
.input-wrap {
  width: 240px;
  display: inline-flex;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  align-items: center;
  padding: 0 15px;
  overflow: hidden;
  height: 32px;
  .placeholder {
    color: #bbb;
    font-size: 14px;
  }
}
</style>
