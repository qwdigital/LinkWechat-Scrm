<script>
import { add } from '@/api/groupMessage'
import { getMaterialMediaId } from '@/api/material'
import PhoneDialog from '@/components/PhoneDialog'
import SelectUser from '@/components/SelectUser'
import SelectTag from '@/components/SelectTag'
import SelectMaterial from '@/components/SelectMaterial/index'

export default {
  components: { PhoneDialog, SelectTag, SelectUser, SelectMaterial },
  props: {},
  data() {
    return {
      loading: false,
      // 表单参数
      form: {
        pushType: '0',
        pushRange: '0',
        tag: [],
        department: [],
        staffId: [],
        settingTime: '',
        messageType: '0',
        imageMessage: {
          media_id: '', // '图片的media_id',
          pic_url: '' // '图片的链接',
        },
        linkMessage: {
          title: '', // '图文消息标题',
          picurl: '', // '图文消息封面的url',
          desc: '', // '图文消息的描述，最多512个字节',
          url: '' // '图文消息的链接',
        },
        textMessage: {
          content: '' //'消息文本内容，最多4000个字节',
        },
        miniprogramMessage: {
          title: '', // '小程序消息标题，最多64个字节',
          pic_media_id: '', // '小程序消息封面的mediaid，封面图建议尺寸为520*416',
          appid: '', // '小程序appid，必须是关联到企业的小程序应用',
          page: '' // '小程序page路径',
        }
      },
      userParty: [],
      rules: {},
      statusOptions: Object.freeze([
        { label: '发送给客户', value: '0' },
        { label: '发送给客户群', value: '1' }
      ]),
      activeName: '0',
      dialogVisibleSelectCustomer: false,
      dialogVisibleSelectUser: false,
      dialogVisibleSelectTag: false,
      dialogVisibleSelectMaterial: false
    }
  },
  watch: {},
  computed: {
    // 是否显示选择的客户标签
    isOnlyTag() {
      return this.form.tag[0] && this.form.pushType == 0 && this.form.pushRange == 1
    },
    // 是否显示选择范围后的文字说明
    isSelectedText() {
      return this.userParty[0] || this.isOnlyTag || this.form.pushRange == 0
    },
    // 选择范围后的文字说明
    selectedText() {
      return this.form.pushType == 0 && this.form.pushRange == 0
        ? '全部客户'
        : `将发送消息给${this.userParty[0] ? this.userParty[0].name + '等部门或成员的' : ''}${
            this.userParty[0] && this.isOnlyTag ? '，且' : ''
          }${this.isOnlyTag ? '满足' + this.form.tag[0].name + '等标签的' : ''}${
            this.form.pushType == 0 ? '客户' : '客户群'
          }`
    }
  },
  created() {
    this.$store.dispatch(
      'app/setBusininessDesc',
      `
        <div>
          <h5>一、什么是群发助手？</h5>
          <p>
            成员可把通知、祝福、活动等消息批量发送给不同的客户，并进行后续的服务；管理员或负责人也可创建企业服务消息，由成员发送并进行后续的服务。
          </p>
          <h5>二、谁可以使用群发助手？</h5>
          <p>
            具有客户联系使用权限的成员，都可以使用群发助手给自己的客户发送消息；超级管理员和业务负责人还可向管理范围内成员的客户创建企业服务消息。
          </p>
          <h5>三、群发是否有频率限制？</h5>
          <p>
            每个自然月最多接收来自同一企业的管理员或业务负责人的4条群发消息。
          </p>
          <h5>四、群发助手其他注意事项</h5>
          <p>
            1、群发消息不得出现违反法律法规或侵犯他人合法权益的内容，企业应对其制作、发送和传播的群发消息内容承担全部法律责任。
          </p>
          <p>
            2、不得以任何方式滥用群发助手实施恶意骚扰用户、开展过度营销等行为。
          </p>
          <p>
            3、如企业微信帐号存在异常，则可能导致群发消息发送失败或异常等情况。
          </p>
        </div>
      `
    )
  },
  mounted() {},
  methods: {
    // 显示选择客户范围弹窗
    showRangeDialog() {
      this[this.form.pushType == 0 ? 'dialogVisibleSelectCustomer' : 'dialogVisibleSelectUser'] = true
    },
    // 选择添加人确认按钮
    selectedUser(users) {
      users.map((d) => {
        d.userId && this.form.staffId.push(d.userId)
        d.id && this.form.department.push(d.id)
      })
      this.userParty = users
    },
    // 选择标签确认按钮
    submitSelectTag(data) {
      this.form.tag = data
    },
    // 新建素材按钮
    goRoute() {
      let contentType = ['text', 'image', 'file']
      window.open('#/customerMaintain/material/' + contentType[this.activeName])
    },
    // 选择素材确认按钮
    submitSelectMaterial(text, image, file) {
      if (this.activeName == 0) {
        this.form.textMessage.content = text.content
      } else if (this.activeName == 1) {
        this.form.imageMessage.pic_url = image.materialUrl
        this.form.imageMessage._materialName = image.materialName
      }
    },
    submit() {
      this.loading = true
      let form = JSON.parse(JSON.stringify(this.form))
      Promise.resolve()
        .then(() => {
          if (form.imageMessage.pic_url) {
            // debugger
            let dataMediaId = {
              url: form.imageMessage.pic_url,
              type: '0',
              name: form.imageMessage._materialName
            }
            return getMaterialMediaId(dataMediaId).then((res) => {
              // debugger
              form.imageMessage.media_id = res.data.media_id
            })
          }
        })
        .then(() => {
          if (form.imageMessage.pic_url && form.textMessage.content) {
            form.messageType = '4'
          } else if (form.imageMessage.pic_url) {
            form.messageType = '1'
          } else if (form.textMessage.content) {
            form.messageType = '0'
          } else {
            this.msgInfo('消息内容不能为空')
            return Promise.reject()
          }
          // debugger
          form.tag = form.tag.map((d) => d.tagId) + ''
          form.department += ''
          form.staffId += ''
          return add(form)
        })
        .then(({ data }) => {
          this.msgSuccess('操作成功')
          this.loading = false
          this.$router.push('record')
        })
        .catch(() => {
          this.loading = false
        })
    }
  }
}
</script>

<template>
  <div v-loading="loading">
    <div class="flex content-wrap">
      <el-form style="margin-right: 170px;" ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="群发类型" prop="pushType">
          <el-radio-group v-model="form.pushType">
            <el-radio v-for="dict in statusOptions" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="发送范围" prop="pushRange">
          <span v-show="isSelectedText">{{ selectedText }}</span>
          <el-button type="text" size="medium" @click="showRangeDialog()">{{
            isSelectedText ? '修改' : form.pushType == 0 ? '选择发送客户' : '按群主选择客户群'
          }}</el-button>
        </el-form-item>
        <el-form-item label="发送时间" prop="settingTime">
          <el-date-picker
            v-model="form.settingTime"
            style="width: 240px"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetime"
            placeholder="选择日期时间"
          ></el-date-picker>
          <span class="small-tip">设置时间定时发送，不设置立即发送</span>
        </el-form-item>
        <el-form-item label-width="0">
          <div class="content-left">
            <el-button class="create" @click="goRoute">新建素材</el-button>
            <el-tabs v-model="activeName">
              <el-tab-pane name="0">
                <span slot="label"> <i class="el-icon-date"></i> 文本 </span>
                <el-input
                  v-model="form.textMessage.content"
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
                  v-if="form.imageMessage.pic_url"
                  style="width: 200px;"
                  :src="form.imageMessage.pic_url"
                  fit="fit"
                ></el-image>
              </el-tab-pane>
              <!-- <el-tab-pane name="2">
                <span slot="label"> <i class="el-icon-date"></i> 文件 </span>
              </el-tab-pane> -->
              <el-button type="primary" class="mt20" @click="dialogVisibleSelectMaterial = true"
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
          </div>
        </el-form-item>
      </el-form>
      <!-- 预览 -->
      <PhoneDialog :message="form.textMessage.content || '请输入欢迎语'" :isOther="!!form.imageMessage.pic_url">
        <el-image style="border-radius: 6px;" :src="form.imageMessage.pic_url" fit="fit"> </el-image>
      </PhoneDialog>
    </div>
    <div class="mt15" style="margin-left: 40px;">
      <el-button type="info" @click="submit">通知成员发送</el-button>
      <span class="small-tip">
        通知成员，向选中的客户发送以上企业消息
      </span>
    </div>

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
                <el-tag type="info" v-for="(unit, unique) in userParty" :key="unique">{{ unit.name }}</el-tag>
              </template>
            </div>
          </el-form-item>
          <el-form-item label="标签">
            <div class="input-wrap" @click="dialogVisibleSelectTag = true">
              <span class="placeholder" v-if="!form.tag.length">请选择</span>
              <template v-else>
                <el-tag type="info" v-for="(unit, unique) in form.tag" :key="unique">{{ unit.name }}</el-tag>
              </template>
            </div>
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisibleSelectCustomer = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisibleSelectCustomer = false">确 定</el-button>
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
    <SelectTag :visible.sync="dialogVisibleSelectTag" title="选择标签" :selected="form.tag" @success="submitSelectTag">
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
  background: #ffffff;
  border-radius: 10px;
  padding: 20px;
}
.content-left {
  position: relative;
  .create {
    position: absolute;
    right: 10px;
    top: 14px;
    z-index: 1;
  }

  .el-tabs {
    height: 90%;
    border-radius: 8px;
    padding: 10px;
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
