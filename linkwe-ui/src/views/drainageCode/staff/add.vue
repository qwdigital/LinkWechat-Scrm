<template>
  <div>
    <div class="tab">
      <el-steps style="margin-top:10px;" :active="currentActive" align-center>
        <el-step title="基础信息"></el-step>
        <el-step title="活码员工"></el-step>
        <el-step title="欢迎语"></el-step>
      </el-steps>
    </div>
    <div class="g-card g-pad20" v-if="currentActive === 1">
      <el-row>
        <div class="g-card">
          <div class="g-pad20">
            <el-col :span="10">
              <el-form ref="baseForm" :rules="baseRules" :model="baseForm" label-position="right" label-width="100px">
                <el-form-item label="活码名称" prop="qrName">
                  <el-input v-model="baseForm.qrName" maxlength="15" show-word-limit clearable></el-input>
                  <div class="sub-des">
                    活码名称创建完成后不可修改
                  </div>
                </el-form-item>
                <el-form-item label="活码分组" prop="qrGroupId">
                  <el-select v-model="baseForm.qrGroupId">
                    <el-option v-for="item in codeCategoryList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="自动通过好友">
                  <el-switch v-model="baseForm.qrAutoAdd" :active-value=1 :inactive-value=0></el-switch>
                  <div class="sub-des">
                    开启后，客户添加该企业微信时，无需好友验证，将会自动添加成功
                  </div>
                </el-form-item>
                <el-form-item label="新客户标签">
                  <template v-for="(item, index) in baseForm.weEmpleCodeTags">
                    <el-tag v-if="item.tagName" size="medium" :key="index">{{ item.tagName }}</el-tag>
                  </template>
                  <div>
                    <el-button size="mini" type="primary" icon="el-icon-plus" @click="selectedFn">{{ baseForm.weEmpleCodeTags.length == 0 ? '添加' : '编辑' }}标签</el-button>
                  </div>
                  <div class="sub-des">
                    添加成功后，该客户将会自动设置以上选择标签
                  </div>
                </el-form-item>
                <el-form-item>
                  <el-button plain @click="cancelFn">取消</el-button>
                  <el-button type="primary" @click="nextStep(2)">下一步</el-button>
                </el-form-item>
              </el-form>
            </el-col>
          </div>
        </div>
      </el-row>
    </div>
    <div class="g-card g-pad20" v-if="currentActive === 2">
      <el-row>
        <el-col :span="15">
          <el-form ref="codeForm" :rules="codeRules" :model="codeForm" label-position="right" label-width="100px">
            <el-form-item label="活码类型" prop="qrType">
              <el-radio-group v-model="codeForm.qrType" @change="changeType">
                <el-radio :label="1">单人</el-radio>
                <el-radio :label="2">多人</el-radio>
              </el-radio-group>
              <div class="sub-des">
                活码名称创建完成后不可修改
              </div>
            </el-form-item>
            <el-form-item label="排班类型" prop="qrRuleType">
              <el-radio-group v-model="codeForm.qrRuleType">
                <el-radio :label="1">全天在线</el-radio>
                <el-radio :label="2" :disabled="codeForm.qrType === 1">自动排班</el-radio>
              </el-radio-group>
              <div class="sub-des">
                <span v-if="codeForm.qrRuleType == 1">多人全天在线时随机分配，单人活码不支持排班</span>
                <span v-else-if="codeForm.qrRuleType == 2">选择自动排班时，默认生成一条全天在线规则，不可删除，保证有员工全天在线</span>
              </div>
            </el-form-item>
            <el-form-item label="活码员工" prop="weEmpleCodeUseScops" v-if="codeForm.qrRuleType == 1">
              <div v-if="codeForm.weEmpleCodeUseScops.length > 0">
                <el-tag size="medium" v-for="(item, index) in codeForm.weEmpleCodeUseScops" :key="index">{{ item.businessName }}</el-tag>
              </div>
              <el-button type="primary" plain size="mini" @click="onSelectUser">{{ codeForm.weEmpleCodeUseScops.length ? '修改' : '选择' }}员工</el-button>
              <div class="sub-des">单人活码只能选择一个员工，多人活码支持选择多个员工</div>
            </el-form-item>
            <el-form-item v-if="codeForm.qrRuleType == 2" label="活码排班" prop="empleCodeRosterDto">
              <template v-for="(item, index) in codeForm.empleCodeRosterDto">
                <el-card class="box-card roster-card" :key="item.id">
                  <el-form-item label="排班员工">
                    <el-tag size="medium" v-for="(tag, key) in item.weEmpleCodeUseScops" :key="key">{{tag.businessName}}</el-tag>
                    <el-button style="margin-left:10px;" size="mini" type="primary" plain @click="onSelectUser(index)">{{ item.weEmpleCodeUseScops.length ? '修改' : '选择' }}员工</el-button>
                  </el-form-item>
                  <el-form-item label="工作周期">
                    <el-checkbox-group v-model="item.weekday" @change="checkStartEnd($event, index)">
                      <el-checkbox :label="1" :disabled="index === 0">周一</el-checkbox>
                      <el-checkbox :label="2" :disabled="index === 0">周二</el-checkbox>
                      <el-checkbox :label="3" :disabled="index === 0">周三</el-checkbox>
                      <el-checkbox :label="4" :disabled="index === 0">周四</el-checkbox>
                      <el-checkbox :label="5" :disabled="index === 0">周五</el-checkbox>
                      <el-checkbox :label="6" :disabled="index === 0">周六</el-checkbox>
                      <el-checkbox :label="7" :disabled="index === 0">周日</el-checkbox>
                    </el-checkbox-group>
                  </el-form-item>
                  <el-form-item label="在线时间">
                    <el-time-select v-model="item.startDate" :disabled="index === 0" :picker-options="{
                      start: '00:00',
                      end: '23:59',
                      step:'00:05'
                    }" :end="item.endDate" @change="checkStartEnd($event, index)" placeholder="任意时间点">
                    </el-time-select>
                    <el-time-select :picker-options="{
                      start: '00:00',
                      end: '23:59',
                      step:'00:05'
                    }" :start="item.startDate" @change="checkStartEnd($event, index)" v-model="item.endDate" :disabled="index === 0" placeholder="任意时间点">
                    </el-time-select>
                  </el-form-item>
                </el-card>
                <el-button v-if="index !== 0" class="fr roster-btn-delete" type="text" icon="el-icon-delete" @click="onRemoveRoster(index)">删除</el-button>
              </template>
              <div class="mt20">
                <el-button size="mini" type="primary" plain @click="onAddRoster">添加工作周期</el-button>
              </div>

            </el-form-item>
            <el-form-item>
              <el-button @click="currentActive = 1">上一步</el-button>
              <el-button type="primary" @click="nextStep(3)">下一步</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </div>
    <div v-if="currentActive === 3">
      <welcome-content :showBack="true" @update="currentActive = 2" :baseData="materialData" @submit="getWelData"></welcome-content>
    </div>

    <!-- 选择标签弹窗 -->
    <SelectTag :visible.sync="dialogVisibleSelectTag" :defaultValues="selectedTagList" @success="submitSelectTag">
    </SelectTag>

    <!-- 选择使用员工弹窗 -->
    <SelectUser :key="codeForm.qrType" :defaultValues="selectedUserList" :visible.sync="dialogVisibleSelectUser" title="选择使用员工" :isOnlyLeaf="true" :isSigleSelect="codeForm.qrType == 1" @success="selectedUser"></SelectUser>

  </div>
</template>

<script>
  import {
    getDetail,
    add,
    update,
    getCodeCategoryList
  } from '@/api/drainageCode/staff'
  import PhoneDialog from '@/components/PhoneDialog'
  import SelectUser from '@/components/SelectUser'
  import SelectTag from '@/components/SelectTag'
  import SelectMaterial from '@/components/SelectMaterial'
  import WelcomeContent from '@/components/WelcomeContent.vue'

  export default {
    components: {
      PhoneDialog,
      SelectTag,
      SelectUser,
      SelectMaterial,
      WelcomeContent
    },
    data () {
      return {
        operationIndex: null,
        formTemp: {
        }, // 编辑基础数据
        materialData: {
          welcomeMsg: '',
          materialMsgList: []
        },
        title: '新建',
        currentActive: 1,
        baseForm: {
          qrName: '',
          qrGroupId: '',
          qrAutoAdd: 1, // 自动通过
          weEmpleCodeTags: [],  // 标签
        },
        baseRules: {
          qrName: [{
            required: true,
            message: '请输入活码名称',
            trigger: 'blur'
          },],
          qrGroupId: [{
            required: true,
            message: '请选择分组',
            trigger: 'blur'
          }]
        },
        codeForm: {
          qrType: 1,
          qrRuleType: 1,
          weEmpleCodeUseScops: [],
          empleCodeRosterDto: [
            {
              type: 0,
              weEmpleCodeUseScops: [],  // 员工
              weekday: [1, 2, 3, 4, 5, 6, 7],  // 周期
              qrRuleType: 2,
              startDate: '00:00',  // 开始时间
              endDate: '23:59',  // 结束时间
            }
          ],
        },
        codeRules: {
          qrType: [{
            required: true,
            message: '请选择',
            trigger: 'blur'
          },],
          qrRuleType: [{
            required: true,
            message: '请选择',
            trigger: 'blur'
          }],
          weEmpleCodeUseScops: [{
            required: true,
            message: '请选择',
            trigger: 'blur',
            validator: (rule, value, callback) => {
              if (value.length == 0) {
                callback(new Error('请选择'))
              } else {
                callback();
              }
            }
          }]
        },
        isInsertClientName: true,
        dialogVisibleSelectUser: false,
        dialogVisibleSelectTag: false,
        dialogVisibleSelectMaterial: false,
        dialogVisibleSelectWel: false,
        loading: false,
        codeCategoryList: [], // 活码分组
        selectedUserList: [], // 需要回显的选中员工
        selectedTagList: [],
        timeConflict: false
      }
    },
    created () {
      let id = this.$route.query.id
      let groupId = this.$route.query.groupId
      if (groupId) {
        this.baseForm.qrGroupId = groupId
      }
      this.getCodeCategoryList()
      if (id) {
        this.title = '编辑'
        this.getDetailFn(id)
      }
    },
    methods: {
      checkStartEnd (e, index) {
        this.timeConflict = false
        this.operationIndex = index
        let goto = this.allTimeConflict() // 默认全天员工冲突
        // let timeGo = true // 非默认周期时间冲突
        if (goto) {
          // if (this.operationIndex > 0) {
          if (this.codeForm.empleCodeRosterDto[this.operationIndex].weekday.length && (this.codeForm.empleCodeRosterDto[this.operationIndex].endDate || this.codeForm.empleCodeRosterDto[this.operationIndex].startDate)) {
            this.someTimeConflict()
          }
          // }
        }
      },
      someTimeConflict () {
        let conflictUserItem = [] // 存在冲突的当前数据
        let userList = [] // 当前重复人员
        let passList = [] // 当前之前的人员 不包含默认的第一条

        let current = this.codeForm.empleCodeRosterDto[this.operationIndex].weEmpleCodeUseScops.map(i => {
          return { userId: i.businessId, name: i.businessName }
        })
        this.codeForm.empleCodeRosterDto.forEach((data, key) => {
          data.weEmpleCodeUseScops.map((i, index) => {
            if (key != this.operationIndex) {
              passList.push({ userId: i.businessId, name: i.businessName, index: key, week: [], goto1: true, goto2: true })
            }
          })
        })
        current.map(dd => {
          passList.map(ff => {
            if (dd.userId === ff.userId) {
              userList.push(ff)
            }
          })
        })
        // 重复人员下的时间校验
        let repeatWeek = []
        let currentWeekday = this.codeForm.empleCodeRosterDto[this.operationIndex].weekday
        userList.forEach(user => {
          const arr = this.codeForm.empleCodeRosterDto[user.index].weekday
          for (var i = 0; i < arr.length; i++) {
            if (currentWeekday.indexOf(arr[i]) != -1) {
              user.week.push(arr[i])
            }
          }
        })
        userList.forEach(ddd => {
          if (ddd.week.length) {
            let start = this.codeForm.empleCodeRosterDto[ddd.index].startDate
            let end = this.codeForm.empleCodeRosterDto[ddd.index].endDate
            if (start && end) {
              if (this.codeForm.empleCodeRosterDto[this.operationIndex].startDate) {
                ddd.goto1 = this.checkAuditTime(start, end, this.codeForm.empleCodeRosterDto[this.operationIndex].startDate)
              }
              if (this.codeForm.empleCodeRosterDto[this.operationIndex].endDate) {
                ddd.goto2 = this.checkAuditTime(start, end, this.codeForm.empleCodeRosterDto[this.operationIndex].endDate)
              }
            }
          }
        })
        let str = ''
        userList.forEach(fdfd => {
          if (!fdfd.goto1 || !fdfd.goto2) {
            let ss = fdfd.week.map((data) => {
              return data === 7 ? '日' : data
            })
            str += "员工" + fdfd.name + '在周' + ss.join("、") + "时间存在冲突;"
          }
        })
        if (str) {
          this.timeConflict = true
          this.msgError(str)
        } else {
          this.timeConflict = false
        }
      },
      allTimeConflict () {
        if (this.operationIndex === 0) {
          return true
        }
        let current = this.codeForm.empleCodeRosterDto[this.operationIndex].weEmpleCodeUseScops.map(i => {
          return { userId: i.businessId, name: i.businessName }
        })
        let first = this.codeForm.empleCodeRosterDto[0].weEmpleCodeUseScops.map(i => {
          return { userId: i.businessId, name: i.businessName }
        })
        let result = []
        current.map(dd => {
          first.map(ff => {
            if (dd.userId === ff.userId) {
              result.push(dd)
            }
          })
        })
        if (result.length) {
          let str = result.map((obj, index) => {
            return obj.name
          }).join(",")
          this.msgError('员工 ' + str + ' 已是全天在线，时间存在冲突！')
          this.timeConflict = true
          return false
        } else {
          this.timeConflict = false
          return true
        }
      },
      checkAuditTime (beginTime, endTime, current) {
        let strb = beginTime.split(":")
        if (strb.length != 2) {
          return false
        }
        let stre = endTime.split(":")
        if (stre.length != 2) {
          return false
        }
        let cur = current.split(':')
        if (cur.length != 2) {
          return false
        }
        let b = new Date()
        let e = new Date()
        let n = new Date()
        b.setHours(strb[0])
        b.setMinutes(strb[1])
        e.setHours(stre[0])
        e.setMinutes(stre[1])
        n.setHours(cur[0])
        n.setMinutes(cur[1])
        if (n.getTime() - b.getTime() >= 0 && n.getTime() - e.getTime() <= 0) {
          return false
        } else {
          return true
        }
      },
      cancelFn () {
        this.$router.back()
      },
      getWelData (data) {
        this.submit(data)
      },
      changeType (e) {
        if (e === 1) {
          this.codeForm.qrRuleType = 1
        }
      },
      nextStep (nextStep) {
        let form = ''
        if (nextStep == 2) {
          form = 'baseForm'
        } else if (nextStep == 3) {
          form = 'codeForm'
        }
        this.$refs[form].validate(validate => {
          if (validate) {
            if (nextStep === 3) {
              if (this.codeForm.qrRuleType === 2) {
                let go = true
                let week = true
                let time = true
                this.codeForm.empleCodeRosterDto.forEach(ddd => {
                  if (!ddd.weEmpleCodeUseScops.length) {
                    go = false
                  }
                  if (!ddd.weekday.length) {
                    week = false
                  }
                  if (!ddd.startDate || !ddd.endDate) {
                    time = false
                  }
                })
                if (go) {
                  if (week) {
                    if (time) {
                      if (this.timeConflict) {
                        this.msgError('排班存在冲突！')
                      } else {
                        this.currentActive = nextStep
                      }
                    } else {
                      this.msgError('请选择活码排班每项的在线时间！')
                    }
                  } else {
                    this.msgError('活码排班每项工作周期至少勾选一项！')
                  }
                } else {
                  this.msgError('活码排班每项至少选择一名员工！')
                }
              } else {
                this.currentActive = nextStep
              }
            } else {
              this.currentActive = nextStep
            }
          }
        })
      },
      /** 获取详情 */
      getDetailFn (id) {
        this.loading = true
        getDetail(id).then((res) => {
          this.formTemp = res.data
          let base = JSON.parse(JSON.stringify(res.data))
          this.baseForm = {
            qrName: base.name,
            qrGroupId: base.groupId,
            qrAutoAdd: base.autoAdd, // 自动通过
            weEmpleCodeTags: base.qrTags,  // 标签
          }
          this.codeForm.qrType = base.type
          this.codeForm.qrRuleType = base.ruleType
          if (base.ruleType === 1) {
            let arr = []
            base.qrUserInfos[0].weQrUserList.forEach(dd => {
              let obj = {
                scopeId: base.qrUserInfos[0].scopeId,
                businessId: dd.userId,
                businessName: dd.userName
              }
              arr.push(obj)
            })
            this.codeForm.weEmpleCodeUseScops = arr
          } else {
            let arr = []
            base.qrUserInfos.forEach(dd => {
              let obj = {
                type: dd.type,
                startDate: dd.beginTime,
                endDate: dd.endTime,
                weekday: dd.workCycle ? dd.workCycle.split(',').map(i => parseInt(i, 0)) : [],
                scopeId: dd.scopeId,
                weEmpleCodeUseScops: dd.weQrUserList.map(ff => { return { businessId: ff.userId, businessName: ff.userName } })
              }
              arr.push(obj)
            })
            this.codeForm.empleCodeRosterDto = arr
          }
          this.materialData = {
            welcomeMsg: base.qrAttachments ? base.qrAttachments[0].content : '',
            materialMsgList: base.qrAttachments ? this.setEditList(base.qrAttachments.slice(1)) : []
          }
          // this.materialData.materialMsgList.forEach(ddd => {
          //   ddd.msgType = Number(ddd.msgType)
          // })
          this.loading = false
        })
      },
      setEditList (list) {
        let arr = []
        if (list && list.length) {
          list.forEach(dd => {
            if (dd.msgType === 'image') {
              let obj = {
                msgType: '0',
                materialUrl: dd.picUrl
              }
              arr.push(obj)
            } else if (dd.msgType === 'link') {
              let ob = {
                msgType: '7',
                materialName: dd.title,
                content: dd.linkUrl
              }
              arr.push(ob)
            } else if (dd.msgType === 'miniprogram') {
              let ff = {
                msgType: '8',
                materialUrl: dd.appId,
                materialName: dd.title,
                coverUrl: dd.picUrl,
                content: dd.linkUrl
              }
              arr.push(ff)
            }
          })
        }
        return arr
      },
      // 获取活码分组
      getCodeCategoryList () {
        getCodeCategoryList({ mediaType: 6 }).then(res => {
          this.codeCategoryList = res.data
        })
      },
      // 选择人员变化事件
      selectedUser (users) {
        let params = {
          userIds: [],
          departmentIds: []
        }
        const selectedUserList = users.map((d) => {
          d.userId && params.userIds.push(d.userId)
          d.id && params.departmentIds.push(d.id)
          return {
            businessId: d.userId || d.id,
            businessName: d.name,
            businessIdType: d.userId ? 2 : 1,
          }
        })
        params.userIds += ''
        params.departmentIds += ''
        // 全天排班
        if (this.codeForm.qrRuleType == 1) {
          this.codeForm.weEmpleCodeUseScops = selectedUserList
        } else {
          // 自动排班
          this.codeForm.empleCodeRosterDto[this.selectedRosterIndex].weEmpleCodeUseScops = selectedUserList
          this.checkStartEnd(null, this.selectedRosterIndex)
        }
      },
      // 自动排班-选择员工
      onSelectUser (index) {
        // 设置回显数据
        this.selectedUserList = []
        if (this.codeForm.qrRuleType == 2) {
          this.selectedRosterIndex = index
          let arr = []
          arr = this.codeForm.empleCodeRosterDto[this.selectedRosterIndex].weEmpleCodeUseScops.map((dd) => {
            return {
              userId: dd.businessId,
              // id: dd.businessIdType === 1 ? dd.businessId:'',
              name: dd.businessName
            }
          })
          this.selectedUserList = arr
          // this.selectedUserList = [...this.codeForm.weEmpleCodeUseScops[index]]
        } else {
          // this.selectedUserList = [...this.codeForm.weEmpleCodeUseScops]
          let arr = []
          arr = this.codeForm.weEmpleCodeUseScops.map((dd) => {
            return {
              userId: dd.businessId,
              // id: dd.businessIdType === 1 ? dd.businessId:'',
              name: dd.businessName
            }
          })
          this.selectedUserList = arr
        }
        this.dialogVisibleSelectUser = true
      },
      // 添加工作周期
      onAddRoster () {
        this.codeForm.empleCodeRosterDto.push({
          weEmpleCodeUseScops: [],  // 员工
          weekday: [],  // 周期
          type: 1,
          startDate: '',  // 开始时间
          endDate: '',  // 结束时间
        })
      },
      // 删除工作周期
      onRemoveRoster (index) {
        this.codeForm.empleCodeRosterDto.splice(index, 1)
      },
      // 工作周期时间变化
      onRosterTimeChange (e, index) {
        const [startTime, endTime] = e
        this.codeForm.empleCodeRosterDto[index].startDate = startTime
        this.codeForm.empleCodeRosterDto[index].endDate = endTime
        this.codeForm.empleCodeRosterDto[index].time = e
      },
      selectedFn () {
        if (this.baseForm.weEmpleCodeTags) {
          this.selectedTagList = this.baseForm.weEmpleCodeTags.map((dd) => ({
            tagId: dd.tagId,
            name: dd.tagName
          }))
        }
        this.dialogVisibleSelectTag = true
      },
      submitSelectTag (data) {
        this.baseForm.weEmpleCodeTags = data.map((d) => ({
          tagId: d.tagId,
          tagName: d.name,
        }))
      },
      resetData (list) {
        let arr = []
        if (list && list.length) {
          list.forEach(dd => {
            if (dd.msgType === '0') {
              let obj = {
                msgType: 'image',
                picUrl: dd.materialUrl
              }
              arr.push(obj)
            } else if (dd.msgType === '7') {
              let ob = {
                msgType: 'link',
                title: dd.materialName,
                linkUrl: dd.content
              }
              arr.push(ob)
            } else if (dd.msgType === '8') {
              let ff = {
                msgType: 'miniprogram',
                appId: dd.materialUrl,
                title: dd.materialName,
                picUrl: dd.coverUrl,
                linkUrl: dd.content
              }
              arr.push(ff)
            }
          })
        }
        return arr
      },
      submit (data) {
        let list = this.resetData(data.materialMsgList)
        let myObj = {
          attachments: [{
            content: data.welcomeMsg,
            msgType: 'text'
          }]
        }
        myObj.attachments.push(...list)
        if (this.codeForm.qrRuleType === 1) {
          let obj = {
            type: 0,
            scopeId: this.codeForm.weEmpleCodeUseScops[0].scopeId ? this.codeForm.weEmpleCodeUseScops[0].scopeId : '',
            userIds: this.codeForm.weEmpleCodeUseScops.map(dd => dd.businessId)
          }
          this.codeForm.qrUserInfos = [obj]
        } else {
          this.codeForm.qrUserInfos = []
          this.codeForm.empleCodeRosterDto.forEach(fff => {
            let obj = {
              scopeId: fff.scopeId ? fff.scopeId : '',
              type: fff.type,
              beginTime: fff.startDate,
              endTime: fff.endDate,
              userIds: fff.weEmpleCodeUseScops.map(dd => dd.businessId),
              workCycle: fff.weekday
            }
            this.codeForm.qrUserInfos.push(obj)
          })
        }
        this.baseForm.qrTags = this.baseForm.weEmpleCodeTags.map(ccc => ccc.tagId)
        let obj = Object.assign({}, this.baseForm, this.codeForm, myObj)
        delete obj.weEmpleCodeTags
        delete obj.weEmpleCodeUseScops
        delete obj.empleCodeRosterDto
        if (this.formTemp.id) {
          obj.qrId = this.formTemp.id
          obj.configId = this.formTemp.configId
        }
        (this.formTemp.id ? update : add)(obj)
          .then(({
            data
          }) => {
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

<style>
  .el-form-item__label {
    font-size: 12px;
  }
  .el-checkbox__label {
    font-size: 12px;
  }
</style>
<style lang="scss" scoped>
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
    height: 78px;
    background: #fff;
    border-top: 1px solid #f1f1f1;
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
      &:hover {
        opacity: 0.8;
      }
    }
  }
  .roster-btn-delete {
    margin-top: -16%;
    margin-right: -8%;
  }
  .roster-card:not(:first-child) {
    margin-top: 20px;
  }
</style>
