<template>
  <div>
    <!-- 头部 -->
    <!-- <div class="header">
      <van-icon
        name="cross"
        color="#9c9c9c"
        size="16"
        @click="$router.back()"
      />
      <span class="title"> 客户画像 </span>
    </div>
    <van-divider /> -->
    <!-- 详细资料 -->
    <div class="details">
      <div class="detail mb20">
        <div class="left">
          <div class="img"><img :src="form.avatar" alt="" /></div>
          <div class="right">
            <div>
              <div>
                {{ form.name || '' }}
                <!-- {{ (form.name || '') + (form.remarkCorpName ? '-' + form.remarkCorpName : '') }} -->
              </div>
              <div class="flex aic">
                <van-icon
                  name="manager"
                  :class="['gender', { 1: 'man', 2: 'woman' }[form.gender]]"
                ></van-icon>
                <span
                  :style="{
                    color: form.customerType === 1 ? '#4bde03' : '#f9a90b',
                    'font-size': '12px'
                  }"
                >
                  {{ { 1: '@微信', 2: '@企业微信' }[form.customerType] }}
                </span>
              </div>
            </div>
          </div>
        </div>
        <van-button
          v-if="dictTrackState[form.trackState]"
          :type="dictTrackState[form.trackState].color"
          size="mini"
          plain
          @click="usershow = true"
          >{{ dictTrackState[form.trackState].name }}&gt;</van-button
        >
      </div>
      <div class="detail">
        <div class="c9">手机号</div>
        <div>{{ form.remarkMobiles }}</div>
      </div>
      <van-divider />
      <div class="detail">
        <div class="c9">年龄</div>
        <div>{{ form.age }}</div>
      </div>
      <van-divider />
      <div class="detail">
        <div class="c9">生日</div>
        <div>{{ form.birthday ? form.birthday.substring(0, 10) : '' }}</div>
      </div>
      <van-divider />
      <div class="detail">
        <div class="c9">邮箱</div>
        <div>{{ form.email }}</div>
      </div>
      <van-divider />
      <div class="detail">
        <div></div>
        <div class="data" @click="goRoute('/customerDetail')">更多详细资料></div>
      </div>
    </div>
    <div class="divider"></div>
    <!-- 企业标签 -->
    <div class="userlabel">
      <div class="detail">
        <div>企业标签</div>
        <div class="data" is-link @click="labelEdit()">编辑</div>
      </div>
      <div v-if="form.tags" class="labelstyle mt15">
        <div class="label" v-for="(unit, unique) in form.tags" :key="unique">
          {{ unit.name }}
        </div>
      </div>
      <van-empty v-else image-size="50" description="暂无数据" />
    </div>
    <div class="divider"></div>

    <!-- 个人标签 -->
    <div class="userlabel">
      <div class="detail">
        <div>个人标签</div>
        <div class="data" is-link @click="labelEdit('person')">编辑</div>
      </div>
      <div v-if="form.personTags" class="labelstyle mt15">
        <div class="label" v-for="(unit, unique) in form.personTags" :key="unique">
          {{ unit.name }}
        </div>
      </div>
      <van-empty v-else image-size="50" description="暂无数据" />
    </div>
    <div class="divider"></div>

    <!-- 社交关系 -->
    <div class="realationship">
      <div class="detail">
        <div>社交关系</div>
        <div class="data" @click="goRoute('/community')">详情</div>
      </div>
      <div class="detail">
        <div class="boxnumber">
          <p>跟进员工</p>
          <div class="number">{{ staff.length }}</div>
        </div>
        <div class="boxnumber">
          <p>所在群聊</p>
          <div class="number">{{ groupChat.length }}</div>
        </div>
        <div class="boxnumber">
          <p>共同群聊</p>
          <div class="number">{{ commonGroup.length }}</div>
        </div>
      </div>
    </div>
    <div class="divider"></div>
    <!-- 客户轨迹 -->
    <div class="addwaiting">
      <div class="detail">
        <div>客户轨迹</div>
        <div class="data" is-link @click="sync">同步</div>
      </div>

      <van-tabs v-model="query.trajectoryType" @change="changeInfo">
        <van-tab :name="0" title="全部"></van-tab>
        <van-tab :name="1" title="客户动态"></van-tab>
        <van-tab :name="2" title="员工动态"></van-tab>
        <van-tab :name="4" title="跟进动态"></van-tab>
        <van-tab :name="3" title="互动动态"></van-tab>
      </van-tabs>

      <!-- 步骤条 -->

      <div class="ac">
        <van-loading v-if="loadingStep" type="spinner" />
      </div>
      <StepList :stepList="list" @reload="changeInfo()"></StepList>
    </div>

    <!-- 点击客户标签里的编辑触发弹出框开始 -->
    <van-action-sheet v-model="show">
      <van-nav-bar title="客户标签管理" right-text="取消" @click-right="show = false">
        <template v-if="editLabelType === 'person'" slot="left">
          <van-button type="primary" size="mini" @click="showAddTag = true">
            添加标签
          </van-button>
        </template>
      </van-nav-bar>
      <div class="content">
        <div v-if="editLabelType === 'person'" class="labelstyle">
          <div
            v-for="(unit, unique) in alllabel"
            :key="unique"
            class="label"
            :style="addTag.some((e) => e.tagId == unit.tagId) ? isActive : ''"
            @click="clickLabel(unit)"
          >
            {{ unit.name }}
          </div>
        </div>

        <template v-else>
          <div v-for="(item, index) in alllabel" :key="index">
            <div class="mb10 mt5">{{ item.gourpName }}：</div>
            <div class="labelstyle">
              <div
                v-for="(unit, unique) in item.weTags"
                :key="unique"
                class="label"
                :style="addTag.some((e) => e.tagId == unit.tagId) ? isActive : ''"
                @click="clickLabel(unit)"
              >
                {{ unit.name }}
              </div>
            </div>
          </div>
        </template>

        <van-empty v-if="!alllabel || !alllabel.length" image-size="50" description="暂无数据" />
      </div>
      <div class="saveinfo">
        <van-button type="info" size="small" block round @click="saveCustomerTag">
          保存
        </van-button>
      </div>
    </van-action-sheet>

    <van-dialog
      v-model="showAddTag"
      title="添加标签"
      show-cancel-button
      @confirm="submitNewPersonTag"
    >
      <van-field
        v-model="newPersonTag"
        placeholder="请输入标签(不超过15字)"
        required
        :rules="[{ required: true, message: '请输入标签(不超过15字)' }]"
        class="conagency"
      />
    </van-dialog>
    <!-- 点击客户标签里的编辑触发弹出框结束 -->

    <!-- 点击跟进状态触发弹出框开始 -->
    <van-action-sheet v-model="usershow">
      <van-nav-bar title="跟进记录" @click-right="usershow = false">
        <template #right>
          <van-icon name="cross" size="18" />
        </template>
      </van-nav-bar>
      <!-- 表单 -->
      <van-form @submit="onSubmit">
        <van-field
          name="radio"
          label="跟进状态"
          required
          :rules="[{ required: true, message: '必填项' }]"
        >
          <template #input>
            <van-radio-group v-model="trackState" direction="horizontal">
              <van-radio name="2">跟进中</van-radio>
              <van-radio name="3">已成交</van-radio>
              <van-radio name="4">无意向</van-radio>
            </van-radio-group>
          </template>
        </van-field>

        <!-- 跟进内容 -->
        <van-field
          v-model="conagency"
          name="跟进内容"
          label="跟进内容"
          placeholder="请输入跟进内容"
          type="textarea"
          required
          :rules="[{ required: true, message: '必填项' }]"
          class="conagency"
        />

        <!-- 待办日期 -->
        <!-- <van-field
          v-model="dateagency"
          is-link
          readonly
          label="待办日期"
          placeholder="请选择"
          @click="dateshow = true"
          required
          :rules="[{ required: true, message: '请输入待办日期' }]"
        />
        <van-calendar
          v-model="dateshow"
          @confirm="onConfirm"
          color="#1989fa"
          :min-date="minDate"
          :max-date="maxDate"
        />
        <van-field
          v-model="timeagency"
          is-link
          readonly
          label="待办时间"
          placeholder="请选择"
          @click="starttimeshow = true"
          required
          :rules="[{ required: true, message: '请输入待办时间' }]"
        />
        <van-action-sheet v-model="starttimeshow">
          <van-datetime-picker
            v-model="startTime"
            type="time"
            title="请选择开始时间"
            :min-hour="0"
            :max-hour="23"
            @cancel="timecancel"
            @confirm="starttimeconfirm"
          />
        </van-action-sheet>
        <van-action-sheet v-model="endtimeshow">
          <van-datetime-picker
            v-model="endTime"
            type="time"
            title="请选择结束时间"
            :min-hour="minHour"
            :max-hour="23"
            @cancel="timecancel"
            @confirm="endtimeconfirm"
          />
        </van-action-sheet> -->
        <!-- 保存 -->
        <div style="width: 90%;margin: 20px auto 40px">
          <van-button round block size="small" type="info" native-type="submit">保存</van-button>
        </div>
      </van-form>
    </van-action-sheet>
    <!-- 点击跟进状态触发弹出框结束 -->
    <!-- <div class="divider"></div> -->
  </div>
</template>

<script>
import {
  getCustomerInfo,
  getAllTags,
  updateWeCustomerPorTraitTag,
  findAddaddEmployes,
  findAddGroupNum,
  findTrajectory,
  addOrEditWaitHandle,
  addOrUpdatePersonTags,
  sync
} from '@/api/portrait'
// import { getUserInfo } from "@/api/common";
import StepList from '@/components/StepList.vue'
import { param2Obj } from '@/utils/index'
export default {
  components: {
    StepList
  },
  data() {
    return {
      // 客户标签弹出框
      show: false,
      // 跟进状态的弹出框开始
      usershow: false,
      trackState: '', // 跟进状态
      conagency: '', // 跟进内容

      // 待办日期
      // dateagency: '',
      // dateshow: false,
      // minDate: new Date(),
      // maxDate: new Date(2030, 12, 31),
      // 待办时间
      // timeagency: '',
      // starttimeshow: false,
      // endtimeshow: false,
      // currentTime: '12:00',
      // startTime: '',
      // endTime: '',
      // 客户待办的弹出框结束

      actions: [{ name: '选项一' }, { name: '选项二' }, { name: '选项三' }],
      // active:false,
      // 客户轨迹
      // 待办动态
      todonewsshow: false,
      // 接口开始
      externalUserid:
        process.env.NODE_ENV !== 'development' ? '' : 'wmbhUTLgAA6ByLnY45U-uiC4ccoMvwng',
      // externalUserid: 'wmiGuBCgAAoCBD1frD3hRplbsXoBLx6g', // 客户IdwmiGuBCgAAoCBD1frD3hRplbsXoBLx6g
      // externalUserid: 'wmiGuBCgAA617zOzAIg-0sZG3Vok7BUA',
      form: {
        name: '', // 昵称
        remarkMobiles: '', // 手机号
        age: '', // 年龄
        birthday: '', // 客户生日
        email: '', // 邮箱
        address: '', // 地址
        qq: '', // qq
        position: '', // 职业
        remarkCorpName: '', // 公司
        description: '', // 其他描述
        tags: [], // 企业标签合集
        personTags: [] // 个人标签合集
      },
      alllabel: [], // 标签组
      // 点击测试组标签获取的变量
      groupId: '',
      name: '',
      tagId: '',
      addTag: [], // 添加的参数
      isActive: 'background:#1989fa;color:#fff',
      styleActive1: '',
      styleActive2: '',
      styleActive3: '',
      styleActive4: '',
      //   activelabel:true
      staff: [], // 添加的员工
      groupChat: [], // 添加的群聊
      commonGroup: [], // 共同的群聊
      //   客户轨迹
      query: {
        page: 1,
        trajectoryType: null
      },

      loading: false,
      finished: false,
      list: [],
      agentId: '', // 1000012,
      loadingStep: false,
      dictTrackState: Object.freeze({
        1: { name: '待跟进', color: 'info' },
        2: { name: '跟进中', color: 'info' },
        3: { name: '已成交', color: 'primary' },
        4: { name: '无意向', color: 'warning' },
        5: { name: '已流失', color: 'danger' }
      }),
      editLabelType: '', // 编辑标签类型
      showAddTag: false,
      newPersonTag: ''
    }
  },
  watch: {
    '$store.state.agentConfigStatus'(val) {
      val && this.init()
    }
  },
  computed: {
    userId() {
      return process.env.NODE_ENV !== 'development' ? this.$store.state.userId : 'XuXueJun'
    }
    //   activeLabel : () => {
    //       this.addTag.forEach((value) => {
    //           value.name == this.name
    //       })
    //       return this.activelabel
    //   }
    // minHour() {
    //   let date = new Date()
    //   return this.dateagency == this.getTime() ? date.getHours() : 0
    // }
  },
  created() {
    if (process.env.NODE_ENV === 'production') {
      this.$toast.loading({
        message: 'loading...',
        duration: 0,
        forbidClick: true
      })
      // 获取agentId
      let query = param2Obj(window.location.search)
      let hash = param2Obj(window.location.hash)
      query = Object.assign(query, hash)
      this.agentId = query.agentId
    } else {
      this.findAddaddEmployes()
      this.findAddGroupNum()
      this.getCustomerInfo()
      this.findTrajectory()
      // this.getAllTags()
    }
  },

  methods: {
    init() {
      let _this = this
      wx.invoke('getContext', {}, function(res) {
        if (res.err_msg == 'getContext:ok') {
          let entry = res.entry //返回进入H5页面的入口类型，目前有normal、contact_profile、single_chat_tools、group_chat_tools
          if (!['single_chat_tools', 'group_chat_tools', 'contact_profile'].includes(entry)) {
            // _this.$toast.clear()
            _this.$toast('入口错误：' + entry)
            return
          }
          wx.invoke('getCurExternalContact', {}, (res) => {
            if (res.err_msg == 'getCurExternalContact:ok') {
              _this.externalUserid = res.userId //返回当前外部联系人userId
              // 获取客户信息
              _this.findAddaddEmployes()
              _this.findAddGroupNum()
              _this.getCustomerInfo()
              _this.findTrajectory()
            } else {
              //错误处理
              _this.$dialog({ message: '进入失败：' + JSON.stringify(res) })
            }
            _this.$toast.clear()
          })
        } else {
          //错误处理
          _this.$toast.clear()
          _this.$dialog({ message: '进入失败：' + JSON.stringify(res) })
        }
      })
    },
    getCustomerInfo() {
      // this.$toast('userId:' + this.userId)
      getCustomerInfo({
        externalUserid: this.externalUserid,
        userId: this.userId
      })
        .then(({ data }) => {
          if (data.tagIds && data.tagNames) {
            data.tagIds = data.tagIds.split(',')
            data.tagNames = data.tagNames.split(',')
            data.tags = data.tagIds.map((unit, index) => ({
              tagId: unit,
              name: data.tagNames[index]
            }))
          }

          if (data.personTagIds && data.personTagNames) {
            data.personTagIds = data.personTagIds.split(',')
            data.personTagNames = data.personTagNames.split(',')
            data.personTags = data.personTagIds.map((unit, index) => ({
              tagId: unit,
              name: data.personTagNames[index]
            }))
          }
          this.form = data
          // console.log(this.form);
        })
        .catch((err) => {
          console.log(err)
        })
    },
    getAllTags(type) {
      this.$toast.loading({
        message: 'loading...',
        duration: 0,
        forbidClick: true
      })
      let params = {
        groupTagType: 1
      }
      if (type === 'person') {
        params = {
          groupTagType: 3,
          userId: this.userId
        }
      }
      return getAllTags(params)
        .then(({ data }) => {
          this.alllabel = this.listTagOneArray = data

          if (this.editLabelType !== 'person') {
            this.listTagOneArray = []
            data.forEach((element) => {
              element.weTags.forEach((d) => {
                this.listTagOneArray.push(d)
              })
            })
          }
          this.$toast.clear()
        })
        .catch((err) => {
          console.log(err)
        })
    },
    //   获取轨迹信息
    findTrajectory(page) {
      this.loadingStep = true
      let query = {
        pageNum: page,
        pageSize: 10,
        userId: this.userId,
        externalUserid: this.externalUserid
      }
      Object.assign(query, this.query)
      query.trajectoryType == 0 && (query.trajectoryType = null)
      findTrajectory(query)
        .then((data) => {
          //   console.log(data.total);
          this.list = data.rows
          this.loadingStep = false
        })
        .catch((err) => {
          console.log(err)
          this.loadingStep = false
        })
    },
    // 点击信息动态
    changeInfo() {
      this.findTrajectory()
    },

    // 编辑跟进动态
    onSubmit() {
      let form = {
        userId: this.userId,
        externalUserid: this.externalUserid,
        content: this.conagency,
        trackState: this.trackState //1:待跟进;2:跟进中;3:已成交;4:无意向;5:已流失
      }
      addOrEditWaitHandle(form)
        .then((data) => {
          this.getCustomerInfo()
          this.findTrajectory()

          if (data.code == 200) {
            this.$toast.success('保存成功')
            this.usershow = false
            this.conagency = ''
            this.trackState = ''
          }
        })
        .catch((err) => {
          console.log(err)
        })

      //   表单重置
      // this.dateagency = ''
      // this.startTime = ''
      // this.endTime = ''
    },

    // 待办日期
    // formatDate(dateagency) {
    //   return `${dateagency.getFullYear()}-${dateagency.getMonth() + 1}-${dateagency.getDate()}`
    // },
    // onConfirm(dateagency) {
    //   this.dateshow = false
    //   this.dateagency = this.formatDate(dateagency)
    // },
    // 待办时间
    // timecancel() {
    //   this.starttimeshow = false
    // },
    // starttimeconfirm(value) {
    //   this.startTime = value
    //   this.starttimeshow = false
    //   this.endtimeshow = true
    // },
    // endtimeconfirm(value) {
    //   this.endTime = value
    //   this.endtimeshow = false
    //   let time = ''
    //   if (this.startTime > this.endTime) {
    //     time = this.startTime
    //     this.startTime = this.endTime
    //     this.endTime = time
    //   }
    //   // console.log(this.startTime, this.endTime);
    //   this.endtimeshow = false
    //   this.timeagency = this.formatTime()
    // },
    // formatTime() {
    //   return `${this.startTime}-${this.endTime}`
    // },
    // 待办动态

    goRoute(path) {
      this.$router.push({
        path,
        query: {
          customerId: this.externalUserid
          //   type
        }
      })
    },
    // 点击编辑按钮
    async labelEdit(type) {
      this.editLabelType = type
      await this.getAllTags(type)
      this.addTag = []

      let tags = this.form[type === 'person' ? 'personTags' : 'tags']
      let hasErrorTag = [] // 异常活已经删除的标签
      // let repeat = [] // 重复的标签
      tags &&
        tags.forEach((unit) => {
          // 判断是否有重复标签
          // let isRepeat = this.listTagOneArray.some((d) => {
          //   return d.tagId === unit.tagId
          // })
          // // 去重
          // if (isRepeat) {
          //   repeat.push(unit.name)
          //   return
          // }
          let filter = this.listTagOneArray.find((d) => {
            return d.tagId === unit.tagId
          })
          // 如果没有匹配到，则说明该便签处于异常状态，可能已被删除或破坏
          if (!filter) {
            hasErrorTag.push(unit.name)
            return
          }

          this.addTag.push(filter)
        })
      this.show = true
      // 获取用户当前的lable,将当前用户的lable与所有lable进行对比，相同的弹框内蓝色展示
      // 弹框内的标签组选中时蓝色展示
      // 弹框内的子标签与选中时蓝色展示，点击时
    },
    // 点击选择标签
    clickLabel(item) {
      let index = this.addTag.findIndex((e) => {
        return item.tagId == e.tagId
      })
      // 数组里不存在该对象,则添加
      if (index == -1) {
        this.addTag.push({
          groupId: item.groupId,
          name: item.name,
          tagId: item.tagId
        })
      } else {
        // 数组里存在该对象,则删除
        this.addTag.splice(index, 1)
      }
    },
    saveCustomerTag() {
      // 更新客户画像标签 [{ groupId: this.groupId, name: this.name, tagId: this.tagId }]
      updateWeCustomerPorTraitTag({
        externalUserid: this.externalUserid,
        userId: this.userId,
        isCompanyTag: this.editLabelType !== 'person', //是否是企业标签true是;false:不是
        addTag: this.addTag.map((e) => ({ tagId: e.tagId }))
      })
        .then((res) => {
          //   console.log(res);
          if (res.code == 200) {
            this.show = false
            //   重新获取客户标签
            this.getCustomerInfo()
            this.$toast.success('保存成功')
          }

          // console.log(123);
        })
        .catch((err) => {
          console.log(err)
        })
    },

    findAddaddEmployes() {
      findAddaddEmployes(this.externalUserid)
        .then(({ data }) => {
          // console.log(data);
          this.staff = data
        })
        .catch((err) => {
          console.log(err)
        })
    },
    findAddGroupNum() {
      // this.$toast('userId:' + this.userId)
      findAddGroupNum({
        externalUserid: this.externalUserid,
        userId: this.userId
      })
        .then(({ data }) => {
          //   console.log(data);
          this.groupChat = data
          //   console.log(this.groupChat);
          this.commonGroup = this.groupChat.filter((ele) => {
            //   debugger
            return ele.groupMemberNum == 1
          })
          //   console.log(this.commonGroup);
        })
        .catch((err) => {
          console.log(err)
        })
    },
    // 添加个人标签
    submitNewPersonTag() {
      let params = {
        weTags: [{ name: this.newPersonTag, owner: this.userId }]
      }
      addOrUpdatePersonTags(params)
        .then((res) => {
          this.$toast.success('添加成功')

          this.getAllTags('person')
        })
        .catch((err) => {
          console.log(err)
        })
    },
    sync() {
      this.$toast.loading({
        message: 'loading...',
        duration: 0,
        forbidClick: true
      })
      sync(this.userId).then((res) => {
        this.$toast.success('同步成功')
        this.findTrajectory()
        this.$toast.clear()
      })
    }
  }
}
</script>

<style lang="less" scoped>
.header {
  margin: 20px 10px 10px;
  vertical-align: center;
  text-align: center;
}
.van-icon-cross {
  position: absolute;
  left: 10px;
}
.title {
  text-align: center;
}
//  详细资料
.details,
.userlabel,
.realationship,
.addwaiting {
  margin: 10px;
}
.detail {
  display: flex;
  justify-content: space-between;
  align-items: center;
  .c9 {
    color: #9c9c9c;
  }
}
.left {
  display: flex;
  align-items: center;
  .img {
    width: 40px;
    height: 40px;
    background-color: #f2f2f2;
    margin-right: 10px;
  }
  .right {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
}
.data {
  font-size: 12px;
  color: #2c8cf0;
}
.divider {
  width: 100%;
  height: 10px;
  background-color: #f2f2f2;
}
.content {
  max-height: 60vh;
  position: relative;
  margin: 16px 0 20px;
  padding: 0 16px;
  overflow: auto;
}
.van-action-sheet__header {
  .van-icon-cross {
    left: 85%;
    font-size: 16px;
  }
}
// 客户标签
.labelstyle {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  .label {
    display: inline-block;
    min-width: 45px;
    font-size: 12px;
    padding: 4px 5px 3px;
    // background-color: #f2f2f2;
    color: #2c8cf0;
    border: 1px solid #2c8cf0;
    text-align: center;
    margin: 0 0 10px 10px;
    line-height: 16px;
    border-radius: 4px;
  }
}
.branch {
  position: relative;
}
.saveinfo {
  width: 90%;
  margin: 20px auto 40px;
}

//  社交关系
.boxnumber {
  height: 60px;
  width: 100%;
  margin: 20px;
  text-align: center;
  p {
    font-size: 12px;
  }
  .number {
    text-align: center;
    margin-top: 20px;
    font-size: 24px;
    font-weight: 700;
  }
}
.boxnumber:first-child {
  border-right: 1px solid #f2f2f2;
  padding-right: 16px;
}
.addwaiting {
  .labelstyle {
    .label1 {
      width: 80px;
      margin-bottom: 20px;
      border-radius: 40px;
    }
  }
}
//  客户轨迹
.f12 {
  color: #9c9c9c;
  font-size: 12px;
  font-weight: 600;
}
.po {
  position: relative;
}
.fs14 {
  color: #333;
  font-size: 14px;
  position: relative;
  left: 20px;
}
.con {
  left: 51px;
  margin-top: 20px;
}
/deep/.van-step__circle {
  width: 8px;
  height: 8px;
  margin-left: 110px;
}
/deep/.van-step__line {
  left: 39px;
}
.finish {
  position: relative;
  color: #2c8cf0;
  font-size: 12px;
  font-weight: 600;
  right: -165px;
}
.deldynamic {
  float: right;
  color: #9c9c9c;
  font-size: 12px;
  font-weight: 600;
}
.van-step--vertical:not(:last-child)::after {
  border-width: 1px;
}
/deep/.conagency {
  line-height: 40px;
  flex-direction: column;
  .van-field__control {
    // background-color: #f2f2f2;
    background-color: #f6fbff;
    padding: 0 10px;
  }
}

.gender {
  color: #9c9c9c;
}
.man {
  color: #2c8cf0;
}
.woman {
  color: pink;
}

.van-divider {
  margin: 10px 0;
}
</style>
