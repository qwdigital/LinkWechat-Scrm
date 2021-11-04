<template>
  <div class="wrap" v-loading="loading">
    <el-form :model="form" ref="form" :rules="rules" label-width="100px">
      <el-form-item label="规则名称" prop="ruleName">
        <el-input v-model="form.ruleName" maxlength="30" show-word-limit placeholder="请输入" clearable />
      </el-form-item>

      <el-form-item label="执行群聊" prop="chatIdList">
        <el-tag size="medium" v-for="(group, index) in customerGroups" :key="index">{{ group.groupName }}</el-tag>
        <el-button
          type="primary"
          plain
          class="ml10"
          icon="el-icon-plus"
          size="mini"
          @click="dialogVisibleSelectCustomerGroup = true"
          >{{ customerGroups.length ? '修改' : '添加' }}</el-button
        >
      </el-form-item>

      <el-form-item label="内容名称" prop="title">
        <el-input
          v-model="form.title"
          maxlength="220"
          show-word-limit
          :autosize="{ minRows: 5, maxRows: 20 }"
          placeholder="请输入"
          clearable
        />
      </el-form-item>

      <el-form-item label="执行时间" required>
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

      <el-form-item label="消息内容" prop="content">
        <div class="content-left">
          <el-button class="create" @click="goRoute">新建素材</el-button>

          <el-tabs v-model="activeName">
            <el-tab-pane name="0">
              <span slot="label"> <i class="el-icon-date"></i> 文本 </span>
              <el-input
                v-model="form.content"
                type="textarea"
                maxlength="220"
                show-word-limit
                :autosize="{ minRows: 10, maxRows: 50 }"
                placeholder="请输入"
                class="mb8"
              ></el-input>

              <div v-for="text in textMaterialList" :key="text.id" class="text-wrapper">
                <div class="content overflow-ellipsis">
                  {{ text.content }}
                </div>

                <el-button icon="el-icon-close" class="remove-btn" size="mini" @click="removeTextMaterial(text)">
                </el-button>
              </div>
            </el-tab-pane>

            <el-tab-pane name="1">
              <span slot="label"> <i class="el-icon-date"></i> 图片 </span>

              <div v-for="image in imageMaterialList" :key="image.id" class="image-wrapper">
                <el-image :src="image.materialUrl" fit="fit"> </el-image>

                <el-button icon="el-icon-close" class="remove-btn" size="mini" @click="removeImageMaterial(image)">
                </el-button>
              </div>

              <!-- <div v-for="url in form.picList" :key="url" class="image-wrapper">
                <el-image :src="url" fit="fit"> </el-image>

                <el-button icon="el-icon-close" class="remove-btn" size="mini" @click="removeImage(url)"> </el-button>
              </div>

              <upload :fileUrl.sync="uploadImageUrl" class="image-uploader">
                <i class="el-icon-plus uploader-icon"></i>
              </upload> -->
            </el-tab-pane>

            <el-button type="primary" class="mt20" @click="dialogVisibleSelectMaterial = true">从素材库选择</el-button>
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

      <PhoneDialog :message="form.content || '请输入加群引导语'" :imageList="imageList" :messageList="messageList">
        <template #image="{ image }">
          <el-image style="border-radius: 6px;  width: 100px;" :src="image" fit="fit"> </el-image>
        </template>
        <template #text="{ text }">
          {{ text }}
        </template>
      </PhoneDialog>
    </div>

    <!-- 选择素材弹窗 -->
    <SelectMaterial
      v-if="activeName === '0'"
      :key="0"
      :visible.sync="dialogVisibleSelectMaterial"
      type="0"
      :showArr="[0]"
      @success="submitSelectMaterial"
    >
    </SelectMaterial>

    <SelectMaterial
      v-else
      :key="1"
      :visible.sync="dialogVisibleSelectMaterial"
      type="1"
      :showArr="[1]"
      @success="submitSelectMaterial"
    >
    </SelectMaterial>

    <!-- 选择客户群聊 -->
    <SelectCustomerGroup
      :visible.sync="dialogVisibleSelectCustomerGroup"
      @success="submitSelectCustomerGroup"
      :multiSelect="true"
    >
    </SelectCustomerGroup>
  </div>
</template>

<script>
import { getDetail, add, update } from '@/api/communityOperating/groupSOP'
import PhoneDialog from '@/components/PhoneDialog'
import SelectMaterial from '@/components/SelectMaterial'
import SelectCustomerGroup from '@/components/SelectCustomerGroup'

export default {
  components: { PhoneDialog, SelectMaterial, SelectCustomerGroup },
  data() {
    const checkContent = (rule, value, callback) => {
      if (!this.form.content && this.form.picList.length === 0 && this.form.materialIdList.length === 0) {
        callback(new Error('该项为必填项'))
      } else {
        callback()
      }
    }
    return {
      ruleId: '',
      dialogVisibleSelectMaterial: false,
      dialogVisibleSelectCustomerGroup: false,
      loading: false, // 遮罩层
      // 表单参数
      form: {
        ruleName: '', // 规则名称
        chatIdList: [], // 群聊ID
        title: '', // 内容标题
        content: '', // 内容
        materialIdList: [], // 素材ID
        startExeTime: '', // 开始执行时间
        stopExeTime: '', // 结束执行时间
        picList: [] // 手动上传图片
      },
      dateRange: [],
      activeName: '',
      customerGroups: [],
      pickerOptions: {
        disabledDate(time) {
          // return time.getTime() < Date.now()
          return time.getTime() < new Date(new Date(new Date().toLocaleDateString()).getTime())
        }
      },
      uploadImageUrl: '',
      imageMaterialList: [],
      textMaterialList: [],
      rules: Object.freeze({
        ruleName: [{ required: true, message: '该项为必填项', trigger: 'blur' }],
        title: [{ required: true, message: '该项为必填项', trigger: 'blur' }],
        chatIdList: [{ required: true, message: '该项为必填项', trigger: 'change' }],
        startExeTime: [{ type: 'array', required: true, message: '该项为必填项', trigger: 'change' }],
        content: [
          // { required: true, message: '该项为必填项', trigger: 'change' },
          { required: true, validator: checkContent, trigger: 'change' }
        ]
      })
    }
  },
  watch: {
    // 日期选择器数据同步至查询参数
    dateRange(dateRange) {
      if (!dateRange || dateRange.length !== 2) {
        this.form.startExeTime = ''
        this.form.stopExeTime = ''
      } else {
        ;[this.form.startExeTime, this.form.stopExeTime] = dateRange
        // this.$nextTick(() => this.$refs.form.validateField('startExeTime'))
      }
    },
    customerGroups(groups) {
      this.form.chatIdList = groups.map((g) => g.chatId)
      this.$refs.form.validateField('chatIdList')
    },
    uploadImageUrl(url) {
      if (url) this.form.picList.push(url)
      this.$refs.form.validateField('content')
    }
  },
  computed: {
    imageMaterialUrls() {
      const urls = this.imageMaterialList.map((m) => m.materialUrl)
      return urls
    },
    messageList() {
      const texts = this.textMaterialList.map((t) => t.content)
      return texts
    },
    imageList() {
      const list = this.form.picList.concat(this.imageMaterialUrls)
      return list
    }
  },
  created() {
    this.ruleId = this.$route.query.id
    this.ruleId && this.getDetail(this.ruleId)
  },
  methods: {
    /** 获取详情 */
    getDetail(id) {
      this.loading = true
      getDetail(id).then(({ data }) => {
        this.dateRange = [data.startExeTime || '', data.stopExeTime || '']
        this.customerGroups = data.groupList || []

        this.form.ruleName = data.ruleName || ''
        this.form.title = data.title || ''
        this.form.content = data.content || ''
        this.form.picList = data.picList || []

        this.form.materialIdList = []

        const materialList = data.materialList || []

        for (let material of materialList) {
          if (material.materialUrl) {
            this.imageMaterialList.push(material)
          } else {
            this.textMaterialList.push(material)
          }

          this.form.materialIdList.push(material.id)
        }

        this.loading = false
      })
    },
    // 选择素材确认按钮
    submitSelectMaterial(text, image, file) {
      text &&
        text.id &&
        !this.form.materialIdList.includes(text.id) &&
        this.form.materialIdList.push(text.id) &&
        this.textMaterialList.push(text)
      image &&
        image.id &&
        !this.form.materialIdList.includes(image.id) &&
        this.form.materialIdList.push(image.id) &&
        this.imageMaterialList.push(image)
      this.$refs.form.validateField('content')
    },
    // 选择客户群聊确认
    submitSelectCustomerGroup(groups) {
      this.customerGroups = groups
    },
    submit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (!this.dateRange.length) {
            this.msgError('执行时间不能为空')
            return
          }
          // if (!this.dateRange.length) {
          //   this.msgError('执行时间不能为空')
          //   return
          // }
          this.loading = true
          if (this.ruleId) {
            update(this.ruleId, this.form)
              .then(({ data }) => {
                this.msgSuccess('操作成功')
                this.loading = false
                this.$router.back()
              })
              .catch(() => {
                this.loading = false
              })
          } else {
            add(this.form)
              .then(({ data }) => {
                this.msgSuccess('操作成功')
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
    goRoute() {
      let contentType = ['text', 'image', 'file']
      window.open('#/customerMaintain/material/' + contentType[this.activeName])
    },
    removeImage(url) {
      this.form.picList.splice(this.form.picList.indexOf(url), 1)
    },
    removeImageMaterial(image) {
      this.imageMaterialList.splice(this.imageMaterialList.indexOf(image), 1)
      this.form.materialIdList.splice(this.form.materialIdList.indexOf(image.id), 1)
    },
    removeTextMaterial(text) {
      this.textMaterialList.splice(this.textMaterialList.indexOf(text), 1)
      this.form.materialIdList.splice(this.form.materialIdList.indexOf(text.id), 1)
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
/deep/ .image-uploader {
  .uploader-icon {
    width: 80px;
    height: 80px;
    line-height: 80px;
  }

  .upload-img {
    width: 80px;
    height: 80px;
  }
}
.uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
  border-radius: 6px;
  border: 1px dashed #d9d9d9;
}
.image-wrapper {
  width: 100px;
  position: relative;
  display: inline-block;

  .remove-btn {
    top: 2px;
  }
}
.text-wrapper {
  width: 100px;
  position: relative;

  .content {
    background-color: #e8f4ff;
    border-color: #d1e9ff;
    padding: 0 10px;
    font-size: 12px;
    border-radius: 4px;
    color: #1890ff;
  }
  .remove-btn {
    top: 5px;
  }
}
.text-wrapper,
.image-wrapper {
  margin: 5px;

  .remove-btn {
    position: absolute;
    right: 2px;
    padding: 5px;
    border: none;
    background: transparent;
  }
}
.overflow-ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.phone-dialog-image {
  border-radius: 6px;
  width: 100px;
}
</style>
