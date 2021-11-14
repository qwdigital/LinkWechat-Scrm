<template>
  <div>
    <div class="tab">
      <el-steps style="margin-top:10px;" :active="currentActive" align-center>
        <el-step title="基础信息"></el-step>
        <el-step title="群发内容"></el-step>
      </el-steps>
    </div>
    <div v-if="currentActive === 1">
      <el-form style="width: 60%;" ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="群发类型" required prop="chatType">
          <el-radio-group v-model="form.chatType">
            <el-radio :label=1>群发客户</el-radio>
            <el-radio :label=2>群发客户群</el-radio>
          </el-radio-group>
        </el-form-item>
        <template v-if="form.chatType == 2">
          <el-form-item label="群发客户群" required prop="clientGroup">
            <el-radio-group v-model="form.clientGroup">
              <el-radio :label=0>全部群</el-radio>
              <el-radio :label=1>部分群</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item v-if="form.clientGroup == 1">
            <div>
              <div class="item-magin aic">
                <div class="item-name">选择群主：</div>
                <el-tag v-for="(unit, unique) in selectCustomerGroupList" :key="unique">
                  {{ unit.groupLeaderName }}
                </el-tag>
                <el-button type="primary" size="mini" :class="selectCustomerGroupList.length == 1 && 'ml10'" icon="el-icon-circle-plus-outline" plain @click="onSelectCustomerGroup">选择群主</el-button>
              </div>
            </div>
          </el-form-item>

        </template>
        <template v-if="form.chatType == 1">
          <el-form-item label="群发客户" required prop="pushRange">
            <el-radio-group v-model="form.pushRange">
              <el-radio :label=0>全部客户</el-radio>
              <el-radio :label=1>部分客户</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item v-if="form.pushRange == 1">
            <div>
              <div class="item-magin aic">
                <div class="item-name">添加人</div>
                <el-button class="mr10" size="mini" icon="el-icon-circle-plus-outline" type="primary" plain @click="onSelectUser(2)">选择添加人</el-button>
                <el-tag v-for="item in form.sendClientUserList" :key="item.userId">{{ item.name }}</el-tag>
              </div>
              <div class="item-magin">
                <div class="item-name">发送性别</div>
                <el-select v-model="form.gender">
                  <el-option label="全部" value=""></el-option>
                  <el-option label="未知" :value=0></el-option>
                  <el-option label="男性" :value=1></el-option>
                  <el-option label="女性" :value=2></el-option>
                </el-select>
              </div>
              <div class="item-magin">
                <div class="item-name">添加时间</div>
                <el-date-picker v-model="form.rangeTime" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
                </el-date-picker>
              </div>
              <div class="item-magin aic">
                <div class="item-name">客户标签</div>
                <el-button class="mr10" size="mini" icon="el-icon-circle-plus-outline" type="primary" plain @click="onSelectTag">选择标签</el-button>
                <el-tag v-for="item in form.sendClientTagList" :key="item.tagId">{{ item.name }}</el-tag>
              </div>
              <div class="item-magin">
                <div class="item-name">跟进状态</div>
                <el-select v-model="form.trackState">
                  <el-option label="全部" value=""></el-option>
                  <el-option label="跟进中" :value=2></el-option>
                  <el-option label="待跟进" :value=1></el-option>
                  <el-option label="无意向" :value=4></el-option>
                  <el-option label="已成交" :value=3></el-option>
                  <el-option label="已流失" :value=5></el-option>
                </el-select>
              </div>
            </div>
          </el-form-item>
        </template>
        <el-form-item label="发送类型" required prop="isTask">
          <el-radio-group v-model="form.isTask">
            <el-radio :label=0>立即发送</el-radio>
            <el-radio :label=1>定时发送</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="form.isTask == 1">
          <div>
            <div class="item-magin">
              <div class="item-name">发送时间</div>
              <el-date-picker v-model="form.sendTime" format="yyyy-MM-dd HH:mm:ss" type="datetime" placeholder="选择日期时间" :picker-options="pickerOptions"></el-date-picker>
            </div>
          </div>
        </el-form-item>
        <el-form-item label-width="0">
          <!-- <el-button @click="currentActive = 1">取消</el-button> -->
          <el-button type="primary" @click="nextStep">下一步</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div v-if="currentActive === 2">
      <welcome-detail showBack showMorMaterial :currentActive.sync="currentActive" @update="currentActive = 1" @submit="save"></welcome-detail>
    </div>
    <!-- 选择添加人弹窗 -->
    <SelectUser :visible.sync="dialogVisibleSelectUser" title="选择添加人" :isOnlyLeaf="false" destroyOnClose :defaultValues="selectedUserList" @success="selectedUser"></SelectUser>

    <!-- 选择标签弹窗 -->
    <SelectTag :visible.sync="dialogVisibleSelectTag" title="选择标签" :defaultValues="form.tag" @success="submitSelectTag">
    </SelectTag>

    <!-- 选择客户群聊 -->
    <SelectCustomerGroup :visible.sync="dialogVisibleSelectCustomerGroup" @success="submitSelectCustomerGroup" :multiSelect="true"></SelectCustomerGroup>

  </div>
</template>

<script>
  import {
    add, getCustomerList
  } from '@/api/groupMessage'
  import {
    getMaterialMediaId
  } from '@/api/material'
  import PhoneDialog from '@/components/PhoneDialog'
  import SelectUser from '@/components/SelectUser'
  import SelectTag from '@/components/SelectTag'
  import WelcomeDetail from '@/components/WelcomeContent.vue'
  import SelectCustomerGroup from '@/components/SelectCustomerGroup'
  import { parseTime } from '@/utils/common'
  export default {
    components: {
      PhoneDialog,
      SelectTag,
      SelectUser,
      WelcomeDetail,
      SelectCustomerGroup
    },
    props: {},
    data () {
      return {
        currentActive: 1,
        loading: false,
        // 表单参数
        form: {
          chatType: 1,
          pushRange: 0,
          clientGroup: 0,	// 群发客户群
          gender: '',	// 发送客户-性别
          trackState: '',	// 发送客户-跟进状态
          rangeTime: [],	// 发送客户-添加时间
          sendClientTagList: [],	// 发送客户标签
          sendClientUserList: [],	// 发送客户添加人
          tag: [],
          department: [],
          staffId: [],
          isTask: 0,
          sendTime: '',
          customerList: [], // 客户列表 临时存储
        },
        userParty: [],
        rules: {
          clientGroup: [{ validator: this.validateClientGroup, trigger: 'blur' }],
          isTask: [{ validator: this.validateSettingTimeType, trigger: 'blur' }],
        },
        statusOptions: Object.freeze([{
          label: '发送给客户',
          value: '0'
        },
        {
          label: '发送给客户群',
          value: '1'
        }
        ]),
        activeName: '0',
        dialogVisibleSelectCustomer: false,
        dialogVisibleSelectUser: false,
        selectedUserList: [],	// 选中回显员工
        dialogVisibleSelectTag: false,
        dialogVisibleSelectCustomerGroup: false,
        selectCustomerGroupList: [],
        pickerOptions: {
          disabledDate (time) {
            var date = new Date()
            date.setFullYear(date.getFullYear() + 2)
            date.setDate(date.getDate() - 1)
            return (time.getTime() < Date.now() - 8.64e7) || (time.getTime() > date.getTime())
          },
          selectableRange: (() => {
            let data = new Date()
            let hour = data.getHours()
            let minute = data.getMinutes()
            let second = data.getSeconds()
            return [`${hour}:${minute}:${second} - 23:59:59`]
          })(),
        }
      }
    },
    watch: {},
    computed: {
      // 是否显示选择的客户标签
      isOnlyTag () {
        return this.form.tag[0] && this.form.chatType == 0
      },
      // 是否显示选择范围后的文字说明
      isSelectedText () {
        return this.userParty[0] || this.isOnlyTag
      },
      // 选择范围后的文字说明
      selectedText () {
        return `将发送消息给${
          this.userParty[0] ? this.userParty[0].name + '等部门或成员的' : ''
          }${this.userParty[0] && this.isOnlyTag ? '，且' : ''}${
          this.isOnlyTag ? '满足' + this.form.tag[0].name + '等标签的' : ''
          }${this.form.chatType == 0 ? '客户' : '客户群'}`
      }
    },
    created () {
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
    mounted () { },
    methods: {
      // validatePushUserRange(rule, value, callback) {
      // 	if(value == 1 && this.userParty.length == 0) {
      // 		callback('请选择员工')
      // 	} else {
      // 		callback()
      // 	}
      // },
      validateClientGroup (rule, value, callback) {
        if (
          this.form.chatType == 1 &&
          this.form.clientGroup == 1 &&
          this.selectCustomerGroupList.length == 0
        ) {
          callback('请选择群主')
        } else {
          callback()
        }
      },
      validateSettingTimeType (rule, value, callback) {
        if (
          this.form.isTask == 1 &&
          !this.form.sendTime
        ) {
          callback('请选择发送时间')
        } else {
          callback()
        }
      },
      // 下一步
      // 校验数据
      nextStep () {
        this.$refs.form.validate(validate => {
          if (!validate) return

          if (this.form.chatType === 1) {
            let data = {
              userIds: '',
              tagIds: '',
              beginTime: '',
              endTime: '',
              gender: '',
              trackState: ''
            }
            if (this.form.pushRange == 1) {
              data.userIds = this.form.sendClientUserList.map(i => i.userId).join(',')
              data.tagIds = this.form.sendClientTagList.map(i => i.tagId).join(',')
              data.beginTime = this.form.rangeTime[0] ? parseTime(new Date(this.form.rangeTime[0])) : ''
              data.endTime = this.form.rangeTime[1] ? parseTime(new Date(this.form.rangeTime[1])) : ''
              data.gender = this.form.gender
              data.trackState = this.form.trackState
              getCustomerList(data).then(res => {
                this.form.customerList = res.data
                this.currentActive = 2
              })
            } else {
              this.currentActive = 2
            }
          } else {
            this.currentActive = 2
          }
        })
      },
      newArryById (arr) {
        let map = {}
        let dest = []
        for (let i = 0; i < arr.length; i++) {
          let ai = arr[i]
          if (!map[ai.firstUserId]) {
            dest.push({
              userId: ai.firstUserId,
              customerList: [ai.externalUserid]
            })
            map[ai.firstUserId] = ai
          } else {
            for (let j = 0; j < dest.length; j++) {
              let dj = dest[j]
              if (dj.userId == ai.firstUserId) {
                dj.customerList.push(ai.externalUserid)
                break
              }
            }
          }
        }
        return dest
      },
      save (materialData) {
        let data = Object.assign({}, this.form, {
          content: materialData.welcomeMsg,
          attachmentsList: materialData.materialMsgList
        })
        data = JSON.parse(JSON.stringify(data))
        if (data.chatType === 1) {
          if (data.pushRange === 1) {
            data.senderList = this.newArryById(this.form.customerList)
          } else {
            data.senderList = data.sendClientUserList.map((i) => { return { userId: i.userId } })
          }
        } else {
          if (data.clientGroup === 1) {
            data.senderList = this.selectCustomerGroupList.map((i) => { return { userId: i.owner } })
          } else {
            data.senderList = []
          }
        }
        // data.sendClientUserList.map(i => i.userId)
        // 发送类型
        data.sendTime = data.isTask == 1 ? parseTime(new Date(data.sendTime)) : ''
        // data.department = data.department.join(',')
        delete data.department
        delete data.clientGroup
        delete data.pushRange
        delete data.rangeTime
        delete data.sendClientUserList
        delete data.sendClientTagList
        delete data.customerList
        delete data.tag
        delete data.trackState
        delete data.gender
        delete data.staffId
        add(data).then(res => {
          if (res.code == 200) {
            // this.$router.go(-1)
            this.$router.push({
              path: '/customerMaintain/groupMessage/record'
            })
            this.msgSuccess('操作成功')
          } else {
            this.msgSuccess(res.msg || '操作失败')
          }
        })
      },
      onSelectCustomerGroup () {
        this.dialogVisibleSelectCustomerGroup = true
      },
      // 选择群主
      submitSelectCustomerGroup (data) {
        let newData = []
        if (data && data.length > 0) {
          let keyMap = new Set()
          data.forEach(i => {
            if (!keyMap.has(i.owner)) {
              newData.push(i)
              keyMap.add(i.owner)
            }
          })
        }
        this.selectCustomerGroupList = newData
        this.$refs.form.clearValidate('clientGroup')
      },
      onSelectTag () {
        this.selectTagType = 1
        this.selectedTagList = this.form.sendClientTagList
        this.dialogVisibleSelectTag = true
      },
      /**
       * @param {Number} type 1:群发员工 2: 群发客户-部分客户-添加人
       */
      onSelectUser (type) {
        if (type == 1) {
          this.selectedUserList = this.userParty
        } else if (type == 2) {
          this.selectedUserList = this.form.sendClientUserList
        }
        this.selectUserType = type
        this.dialogVisibleSelectUser = true
      },
      // 显示选择客户范围弹窗
      showRangeDialog () {
        this[
          this.form.chatType == 0 ?
            'dialogVisibleSelectCustomer' :
            'dialogVisibleSelectUser'
        ] = true
      },
      // 选择添加人确认按钮
      selectedUser (users) {
        // 选择员工
        if (this.selectUserType == 1) {
          users.forEach((d) => {
            d.userId && this.form.staffId.push(d.userId)
            d.id && this.form.department.push(d.id)
          })
          this.userParty = users
          // this.$refs.form.clearValidate('pushUserRange')
        } else if (this.selectUserType == 2) {
          this.form.sendClientUserList = users
        }
      },
      // 选择标签确认按钮
      submitSelectTag (data) {
        if (this.selectTagType == 1) {
          this.form.sendClientTagList = data
        } else {
          this.form.tag = data
        }
        this.selectTagType = ''
      }
    }
  }
</script>


<style lang="scss" scoped>
  .content-border {
    padding: 10px 20px;
    background: #ffffff;
    border: 1px solid #f1f1f1;
    .item-name {
      width: 100px;
    }
    .item-magin {
      display: flex;
      margin: 10px 0;
    }
  }

  .crumb- {
    // 一级 页面标题
    &title {
      display: flex;
      flex-direction: column;
      justify-content: center;
      height: 90px; // line-height: 90px;
      font-size: 18px;
      font-weight: 500;
      color: #333;
      padding: 0 20px;
      background: #fff;
      border-top-left-radius: 4px;
      border-top-right-radius: 4px;
    }
  }

  .tab {
    height: 68px;
    border-bottom-left-radius: 4px;
    border-bottom-right-radius: 4px;
  }

  .crumb {
    font-size: 12px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #666666;
    display: flex;
  }

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
