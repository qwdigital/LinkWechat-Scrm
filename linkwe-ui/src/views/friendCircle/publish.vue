<template>
  <div>
    <el-row :gutter="10" type="flex" style="margin-top: 10px">
      <el-col>
        <el-form label-width="110px" label-position="right">
          <div class="g-card g-pad20">
            <el-form-item label="可见客户" required>
              <el-radio-group v-model="form.scopeType">
                <el-radio :label="1">全部可见</el-radio>
                <el-radio :label="0">部分可见</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="客户标签" v-if="form.scopeType === 0">
              <el-tag sizi="mini" v-for="(unit, key) in selectedTagList" :key="key">{{
                unit.name
              }}</el-tag>
              <div>
                <el-button type="primary" size="mini" plain @click="selectedFn"
                  >选择客户标签</el-button
                >
              </div>
            </el-form-item>
            <el-form-item label="添加人" v-if="form.scopeType === 0">
              <el-tag sizi="mini" v-for="(unit, key) in selectedUserList" :key="key">{{
                unit.name
              }}</el-tag>
              <div>
                <el-button type="primary" size="mini" plain @click="onSelectUser"
                  >选择添加人</el-button
                >
              </div>
            </el-form-item>
            <div class="my-cord">
              <el-form-item
                label="动态内容"
                required
                style="width: 50%; margin-bottom: 0"
                :error="welcomeMsgError"
              >
                <el-input
                  ref="msgTextarea"
                  type="textarea"
                  v-model="form.content"
                  maxlength="2000"
                  show-word-limit
                  placeholder="请输入"
                  :autosize="{ minRows: 5, maxRows: 20 }"
                  clearable
                />
              </el-form-item>
            </div>
          </div>

          <!-- 附件 -->
          <div class="g-card g-pad20" v-for="(item, index) in form.otherContent" :key="index">
            <!-- 图片上传 -->
            <div class="my-cord" v-if="item.annexType == 'image'">
              <div class="operation">
                <div class="algin" @click="onRemoveMaterial(index)">
                  <img
                    style="margin-right: 3px; height: 12px; width: 12px"
                    :src="require('@/assets/drainageCode/delete.png')"
                    alt=""
                  />删除
                </div>
              </div>
              <el-form-item label="图片上传" required :error="item.contentError">
                <upload
                  :maxImgPx="limit"
                  class="image-uploader"
                  :fileUrl.sync="item.annexUrl"
                  type="0"
                ></upload>
              </el-form-item>
            </div>
            <!-- 图文 -->
            <div class="my-cord" v-else-if="item.annexType == 'link'">
              <div class="operation">
                <div class="algin" @click="onRemoveMaterial(index)">
                  <img
                    style="margin-right: 3px; height: 12px; width: 12px"
                    :src="require('@/assets/drainageCode/delete.png')"
                    alt=""
                  />删除
                </div>
              </div>
              <el-form-item label="图文链接" style="width: 50%" required :error="item.contentError">
                <el-input v-model="item.annexUrl" placeholder="请输入图文链接"></el-input>
                <div class="sub-des">仅支持公众号图文链接，且必须以 http://或 https://开头</div>
              </el-form-item>
            </div>
            <div class="my-cord" v-else-if="item.annexType == 'video'">
              <div class="operation">
                <div class="algin" @click="onRemoveMaterial(index)">
                  <img
                    style="margin-right: 3px; height: 12px; width: 12px"
                    :src="require('@/assets/drainageCode/delete.png')"
                    alt=""
                  />删除
                </div>
              </div>
              <el-form-item label="视频上传" required :error="item.contentError">
                <upload class="image-uploader" :fileUrl.sync="item.annexUrl" type="2"></upload>
              </el-form-item>
            </div>
          </div>
          <!-- 添加附件 -->
          <div
            class="g-card g-pad20 add-continue"
            v-show="
              form.otherContent.length <= 8 &&
              form.contentType !== 'video' &&
              form.contentType !== 'link'
            "
          >
            <el-dropdown style="margin-left: 10px" @command="onInsertMaterial">
              <div style="display: flex; align-items: center">
                <img
                  style="margin-right: 5px"
                  :src="require('@/assets/drainageCode/plus-circle.png')"
                  alt=""
                />继续添加附件
              </div>
              <el-dropdown-menu slot="dropdown" trigger="click">
                <!-- <el-dropdown-item :command="4">
                  <el-button type="text">文字</el-button>
                </el-dropdown-item> -->
                <el-dropdown-item
                  v-if="form.contentType === 'image' || form.otherContent.length === 0"
                  :command="'image'"
                >
                  <el-button type="text">图片</el-button>
                </el-dropdown-item>
                <el-dropdown-item v-if="form.otherContent.length === 0" :command="'video'">
                  <el-button type="text">视频</el-button>
                </el-dropdown-item>
                <el-dropdown-item v-if="form.otherContent.length === 0" :command="'link'">
                  <el-button type="text">网页</el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
          <el-form-item label-width="0" style="margin-top: 20px; margin-bottom: 0">
            <el-button @click="onBackStep">取消</el-button>
            <el-button type="primary" @click="submit">保存</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <!-- 选择标签弹窗 -->
    <SelectTag
      :visible.sync="dialogVisibleSelectTag"
      :defaultValues="selectedTagList"
      @success="submitSelectTag"
    >
    </SelectTag>
    <SelectUser
      :defaultValues="selectedUserList"
      :visible.sync="dialogVisibleSelectUser"
      title="选择使用员工"
      @success="selectedUser"
    ></SelectUser>
  </div>
</template>

<script>
import { gotoPublish } from '@/api/circle'
import SelectTag from '@/components/SelectTag'
import SelectUser from '@/components/SelectUser'
import upload from '@/components/Upload'
export default {
  name: 'publish-detail',
  components: {
    upload,
    SelectUser,
    SelectTag
  },
  data() {
    return {
      form: {
        scopeType: 1, //可见类型:0:部分可见;1:公开;
        customerTag: '', //客户标签，多个使用逗号隔开
        noAddUser: '', //发送员工，使用逗号隔开
        content: '', //文本框内容
        otherContent: [],
        contentType: '' //图片:image 视频:video 图文链接:link
      },
      welcomeMsgError: '',
      // 遮罩层
      loading: false,
      selectedUserList: [],
      dialogVisibleSelectUser: false,
      dialogVisibleSelectTag: false,
      selectedTagList: [],
      limit: [1440, 1080]
    }
  },
  mounted() {},
  methods: {
    selectedFn() {
      this.dialogVisibleSelectTag = true
    },
    submitSelectTag(data) {
      console.log(data)
      this.selectedTagList = data
    },
    onSelectUser() {
      this.dialogVisibleSelectUser = true
    },
    selectedUser(users) {
      this.selectedUserList = users
    },
    onInsertMaterial(e) {
      this.form.contentType = e
      this.form.otherContent.push({
        annexType: e,
        annexUrl: '',
        contentError: ''
      })
    },
    onRemoveMaterial(index) {
      this.form.otherContent.splice(index, 1)
      if (!this.form.otherContent.length) {
        this.form.contentType = ''
      }
    },
    // 上一步
    onBackStep() {
      this.$router.go(-1)
    },
    validateMaterial() {
      let goto = true
      this.form.otherContent.forEach((dd) => {
        if (!dd.annexUrl) {
          dd.contentError = '内容不能为空'
          goto = false
        } else {
          dd.contentError = ''
        }
      })
      return goto
    },
    submit() {
      if (!this.form.content) {
        this.welcomeMsgError = '请输入动态内容'
        return
      } else {
        this.welcomeMsgError = ''
      }
      if (this.form.scopeType === 0) {
        this.form.customerTag = this.selectedTagList
          .map((dd) => {
            return dd.tagId
          })
          .join(',')
        this.form.noAddUser = this.selectedUserList
          .map((dd) => {
            return dd.userId
          })
          .join(',')
      }
      const checkMaterialResult = this.validateMaterial()
      if (checkMaterialResult) {
        let goto = true
        this.form.otherContent.forEach((dd) => {
          delete dd.contentError
          if (dd.annexType === 'link') {
            let reUrl = /(http|https):\/\/([\w.]+\/?)\S*/
            if (!reUrl.test(dd.annexUrl)) {
              goto = false
              dd.contentError = '必须以 http://或 https://开头'
            }
          }
        })
        this.$forceUpdate()
        if (!this.form.otherContent.length) {
          this.form.contentType = 'text'
        }
        if (goto) {
          gotoPublish(this.form).then((res) => {
            if (res.code === 200) {
              this.msgSuccess('操作成功')
              this.$router.go(-1)
            }
          })
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .image-uploader {
  .uploader-icon {
    width: 120px;
    height: 114px;
    line-height: 114px;
  }
  .upload-img {
    width: 100px;
    height: 94px;
  }
}
.sub-des {
  font-size: 12px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #999999;
}

.add-continue {
  cursor: pointer;
  font-size: 14px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #3c88f0;
  display: flex;
  align-items: center;
  justify-content: center;

  &:hover {
    opacity: 0.8;
  }
}

.my-cord {
  position: relative;

  .operation {
    z-index: 100;
    cursor: pointer;
    position: absolute;
    top: 0;
    right: 0;
    font-size: 12px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #1785ff;
    display: flex;
    align-items: center;

    .algin {
      margin-left: 20px;
      display: flex;
      align-items: center;
    }

    &:hover {
      opacity: 0.8;
    }
  }
}
</style>
