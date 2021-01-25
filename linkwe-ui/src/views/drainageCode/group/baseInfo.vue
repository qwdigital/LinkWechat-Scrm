<template>
  <div>
    <el-row>
      <el-col
        :sm="24"
        :md="16"
        :lg="14"
      >
        <el-form
          :model="form"
          :rules="rules"
          ref="form"
          label-width="100px"
        >
          <el-row>
            <el-col
              :sm="24"
              :md="12"
            >
              <el-form-item
                label="活码名称"
                prop="activityName"
              >
                <el-input v-model="form.activityName" placeholder="请输入名称"></el-input>
              </el-form-item>
            </el-col>

            <el-col
              :sm="24"
              :md="12"
            >
              <el-form-item
                label="活码描述"
                prop="activityDesc"
              >
                <el-input v-model="form.activityDesc" type="textarea" placeholder="请输入描述"></el-input>
              </el-form-item>
            </el-col>

            <el-col
              :sm="24"
              :md="12"
            >
              <el-form-item
                label="活码头像"
              >
                <upload
                  :fileUrl.sync="form.activityHeadUrl"
                  class="image-uploader"
                >
                  <div slot="tip">
                    注: 只支持2M以内的jpg/png格式图片
                  </div>
                </upload>
              </el-form-item>
            </el-col>

            <el-col
              :sm="24"
              :md="12"
            >
              <el-form-item
                label="加群引导语"
              >
                <el-input v-model="form.guide" type="textarea" placeholder="请输入加群引导语"></el-input>
              </el-form-item>
            </el-col>

            <el-col
              :span="24"
            >
              <el-divider></el-divider>
            </el-col>

            <el-col
              :span="24"
            >
              <el-form-item
                label="无法加群提示"
              >
                <el-switch
                  v-model="form.joinGroupIsTip"
                  :active-value="1"
                  :inactive-value="0"
                >
                </el-switch>
                <div class="el-upload__tip">开启后, 页面底部显示无法进群按钮, 点击可查看提示内容</div>
              </el-form-item>
            </el-col>

            <transition name="el-fade-in-linear">
              <el-col
                v-show="form.joinGroupIsTip === 1"
                :sm="24"
                :md="12"
              >
                <el-form-item
                  label="提示语"
                >
                  <el-input v-model="form.tipMsg" placeholder="请输入提示语"></el-input>
                </el-form-item>
              </el-col>
            </transition>

            <transition name="el-fade-in-linear">
              <el-col
                v-show="form.joinGroupIsTip === 1"
                :sm="24"
                :md="12"
              >
                <el-form-item
                  label="客服二维码"
                >
                  <upload
                    :fileUrl.sync="form.customerServerQrCode"
                    class="image-uploader"
                  >
                    <div slot="tip">
                      注: 只支持2M以内的jpg/png格式图片
                    </div>
                  </upload>
                </el-form-item>
              </el-col>
            </transition>
          </el-row>
        </el-form>
      </el-col>

      <el-col
        :sm="24"
        :md="8"
        :lg="10"
      >
        <div class="preview">
          <div class="preview-content">
            <div class="preview-header">
              <i class="el-icon-close"></i>
              <div> 群活码 </div>
              <i class="el-icon-more"></i>
            </div>

            <el-divider></el-divider>

            <div class="preview-name">
              <template v-if="form.activityName">
                {{ form.activityName }}
              </template>
              <template v-else>
                活码名称
              </template>
            </div>

            <div class="preview-guide">
              <template v-if="form.guide">
                {{ form.guide }}
              </template>
              <template v-else>
                这是加群引导语
              </template>
            </div>

            <div class="preview-code-box">
              <div class="code-box-title">
                <svg-icon icon-class="user" class="code-user"></svg-icon>
                实际群名称
              </div>

              <div class="code-content">
                <svg-icon icon-class="qrcode" class="code-svg"></svg-icon>
              </div>
            </div>

            <transition name="el-fade-in-linear">
              <div v-show="form.joinGroupIsTip" class="preview-customer-service">
                <el-button type="danger"> 无法加群? </el-button>
              </div>
            </transition>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { add, update, getDetail } from '@/api/drainageCode/group'

export default {
  props: {
    // 实际群活码
    groupCodeId: {
      type: String,
      default: ''
    }
  },

  data () {
    return {
      form: {
        activityName: '',
        activityDesc: '',
        activityHeadUrl: '',
        guide: '',
        joinGroupIsTip: 0,
        tipMsg: '',
        customerServerQrCode: ''
      },
      rules: {
        activityName: [
          { required: true, message: '请输入活码名称', trigger: 'blur' }
        ],
        activityDesc: [
          { required: true, message: '请输入活码描述', trigger: 'blur' }
        ]
      }
    }
  },

  methods: {
    // 新增群活码
    add () {
      this.$refs.form.validate((valid) => {
        if (!valid) return

        add(this.form).then((res) => {
          this.$emit('next', res.id)
        })
      })
    },

    // 更新群活码
    update () {
      this.$refs.form.validate((valid) => {
        if (!valid) return

        update(this.groupCodeId, this.form).then((res) => {
          this.$emit('next', this.groupCodeId)
        })
      })
    },

    // 获取群活码信息
    getGroupDetail () {
      if (!this.groupCodeId) return

      getDetail(this.groupCodeId).then((res) => {
        if (res.code === 200) {
          this.form = {
            activityName: res.data.activityName,
            activityDesc: res.data.activityDesc,
            activityHeadUrl: res.data.activityHeadUrl,
            guide: res.data.guide,
            joinGroupIsTip: parseInt(res.data.joinGroupIsTip),
            tipMsg: res.data.tipMsg,
            customerServerQrCode: res.data.customerServerQrCode
          }
        } else {
        }
      })
    },

    // 提交
    submit () {
      if (!this.groupCodeId) return this.add()

      this.update()
      // this.$emit('next')
    }
  },

  created () {
    if (this.groupCodeId) this.getGroupDetail()
  }
}
</script>

<style scoped lang="scss">
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

  .el-form-item {
    margin-bottom: 30px;
  }

  .preview {
    .preview-content {
      width: 250px;
      height: 480px;
      background-color: #e9e9e9;
      margin-left: 50px;
      border-radius: 6px;

      .preview-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 10px 4px;
      }

      .el-divider {
        margin: 3px 0;
      }

      .preview-name {
        padding: 20px 6px;
        text-align: center;
        // line-height: 50px;
        word-wrap: break-word;
        word-break: normal;
        line-height: 20px;
      }

      .preview-guide {
        padding: 6px;
        color: #888888;
        margin: 0 15px 10px;
      }

      .preview-code-box {
        height: 200px;
        margin: 0 20px;
        background-color: white;
        padding: 10px;
        font-weight: bold;
        color: #666666;

        .code-user {
          font-size: 30px;
          color: #4185f4;
        }

        .code-content {
          text-align: center;

          .code-svg {
            font-size: 150px;
          }
        }
      }
      
      .preview-customer-service {
        text-align: center;
        padding: 20px 10px;
      }
    }
  }
</style>
