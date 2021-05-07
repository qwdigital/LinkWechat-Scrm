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
      <div class="detail">
        <div class="left">
          <div class="img"><img :src="form.avatar" alt="" /></div>
          <div class="right">
            <div>
              <span
                >{{
                  form.remark
                    ? form.remark
                    : form.name + '_' + form.remarkCorpName
                }}
                &nbsp; &nbsp;</span
              ><span
                class="icon iconfont icon-man"
                v-if="form.gender == 1"
              ></span>
              <span
                class="icon iconfont icon-xingbie"
                v-else-if="form.gender == 2"
              ></span>
              <van-icon name="manager" color="#9c9c9c" v-else />
            </div>
            <div class="c9">
              <span>昵称：</span><span>{{ form.name }}</span>
            </div>
          </div>
        </div>
        <div class="data" @click="goRoute('/customerDetail')">详细资料></div>
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
    </div>
    <div class="divider"></div>
    <!-- 客户标签 -->
    <div class="userlabel">
      <div class="detail">
        <div>客户标签</div>
        <div class="data" is-link @click="labelEdit">编辑</div>
      </div>
      <van-row gutter="10" class="labelstyle">
        <van-col span="4.5" v-for="(item, index) in labels" :key="index">
          <div
            class="label"
            v-for="(item1, index1) in item.weTags"
            :key="index1"
          >
            {{ item1.name }}
          </div></van-col
        >
        <!-- <van-col span="4.5"> <div class="label">标签1</div></van-col>
        <van-col span="4.5"> <div class="label">标签1</div></van-col>
        <van-col span="4.5"> <div class="label">标签1</div></van-col>
        <van-col span="4.5"> <div class="label">标签1</div></van-col>
        <van-col span="4.5"> <div class="label">标签1</div></van-col>
        <van-col span="4.5"> <div class="label">标签1</div></van-col> -->
      </van-row>
    </div>
    <div class="divider"></div>
    <!-- 点击客户标签里的编辑触发弹出框开始 -->
    <van-action-sheet v-model="show">
      <van-nav-bar
        title="客户标签"
        right-text="取消"
        @click-right="show = false"
      />
      <div class="content">
        <span>测试:</span>
        <van-row gutter="8" class="labelstyle">
          <van-col span="4.5" v-for="(item, index) in grouplabel" :key="index"
            ><div
              class="label"
              @click="userLabel(item)"
              :style="
                addTag.some((item1) => item1.tagId == item.tagId)
                  ? isActive
                  : ''
              "
            >
              {{ item.name }}
            </div></van-col
          >
        </van-row>
        <div class="branch" v-if="true">
          <van-divider />
          <p>标签组:</p>
          <van-row gutter="8" class="labelstyle">
            <van-col span="4.5" v-for="(item, index) in alllabel" :key="index"
              ><div
                class="label"
                :class="{ styleactive: styleactive == item.groupId }"
                @click="changeLabel(item)"
              >
                {{ item.gourpName }}
              </div></van-col
            >
          </van-row>
          <van-button type="info" class="saveinfo" round @click="saveInfo"
            >保存</van-button
          >
        </div>
      </div>
    </van-action-sheet>
    <!-- 点击客户标签里的编辑触发弹出框结束 -->
    <!-- 社交关系 -->
    <div class="realationship">
      <div class="detail">
        <div>社交关系</div>
        <div class="data" @click="goRoute('/community')">详情</div>
      </div>
      <div class="detail">
        <div class="boxnumber">
          <p>添加的员工</p>
          <div class="number">{{ staff.length }}</div>
        </div>
        <div class="boxnumber">
          <p>添加的群聊</p>
          <div class="number">{{ groupChat.length }}</div>
        </div>
        <div class="boxnumber">
          <p>共同的群聊</p>
          <div class="number">{{ commonGroup.length }}</div>
        </div>
      </div>
    </div>
    <div class="divider"></div>
    <!-- 客户轨迹 -->
    <div class="addwaiting">
      <div class="detail">
        <div>客户轨迹</div>
        <div class="data" is-link @click="usershow = true">添加待办></div>
      </div>
      <van-row gutter="8" class="labelstyle">
        <van-col span="6">
          <div class="label1" @click="information" :style="styleActive1">
            信息动态
          </div></van-col
        >
        <van-col span="6">
          <div class="label1" @click="socialContact" :style="styleActive2">
            社交动态
          </div></van-col
        >
        <van-col span="6">
          <div class="label1" @click="activity" :style="styleActive3">
            活动动态
          </div></van-col
        >
        <van-col span="6">
          <div class="label1" @click="dealtWith" :style="styleActive4">
            待办动态
          </div></van-col
        >
      </van-row>

      <!-- 步骤条 -->

      <StepList :stepList="list"></StepList>
    </div>
    <!-- 点击添加待办触发弹出框开始 -->
    <van-action-sheet v-model="usershow">
      <van-nav-bar
        title="客户待办"
        right-text="取消"
        @click-right="usershow = false"
      />
      <!-- 表单 -->
      <van-form @submit="onSubmit">
        <!-- 待办内容 -->
        <van-field
          v-model="conagency"
          name="待办内容"
          label="待办内容"
          placeholder="请输入待办内容"
          type="textarea"
          required
          :rules="[{ required: true, message: '请输入待办内容' }]"
          class="conagency"
        />

        <!-- 待办日期 -->
        <van-field
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
        <!-- 待办时间 -->
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
            v-model="currentTime"
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
            v-model="currentTime"
            type="time"
            title="请选择结束时间"
            :min-hour="0"
            :max-hour="23"
            @cancel="timecancel"
            @confirm="endtimeconfirm"
          />
        </van-action-sheet>
        <!-- 保存 -->
        <div style="margin: 30px;">
          <van-button
            round
            block
            type="info"
            native-type="submit"
            @click="saveInfo2"
            >保存</van-button
          >
        </div>
      </van-form>
    </van-action-sheet>
    <!-- 点击添加待办触发弹出框结束 -->
    <!-- 点击待办动态触发弹出框开始 -->
    <van-action-sheet v-model="todonewsshow">
      <van-nav-bar
        title="客户待办"
        right-text="删除"
        @click-right="deltodoshow"
      />
      <!-- 待办内容 -->
      <van-field
        v-model="conagency"
        name="待办内容"
        label="待办内容"
        placeholder="请输入待办内容"
        type="textarea"
        required
        readonly
        :rules="[{ required: true, message: '请输入待办内容' }]"
        class="conagency"
      />

      <!-- 待办日期 -->
      <van-field
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
      <!-- 待办时间 -->
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
          v-model="currentTime"
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
          v-model="currentTime"
          type="time"
          title="请选择结束时间"
          :min-hour="0"
          :max-hour="23"
          @cancel="timecancel"
          @confirm="endtimeconfirm"
        />
      </van-action-sheet>
      <!-- 保存 -->
      <div style="margin: 16px;">
        <van-button round block type="info" native-type="submit"
          >保存</van-button
        >
      </div>
    </van-action-sheet>
    <!-- 点击添加待办触发弹出框结束 -->
    <div class="divider"></div>
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
} from '@/api/portrait'
// import { getUserInfo } from "@/api/common";
import StepList from '../../components/StepList.vue'
export default {
  data() {
    return {
      // 客户标签弹出框
      show: false,
      // 客户待办的弹出框开始
      usershow: false,
      conagency: '', // 待办内容

      // 待办日期
      dateagency: '',
      dateshow: false,
      minDate: new Date(2021, 0, 1),
      maxDate: new Date(2021, 12, 31),
      // 待办时间
      timeagency: '',
      starttimeshow: false,
      endtimeshow: false,
      currentTime: '12:00',
      startTime: '',
      endTime: '',
      // 客户待办的弹出框结束

      actions: [{ name: '选项一' }, { name: '选项二' }, { name: '选项三' }],
      // active:false,
      // 客户轨迹
      // 待办动态
      todonewsshow: false,
      // 接口开始
      //   externalUserid: "wm2H-nDQAACG5x4XjsM1OoW8UVfpbn3A", // 客户Id
      //   externalUserid: "wmiGuBCgAAgeijfvvpJ62cBfwrB-c4kw",
      externalUserid: '',
      // userId: this.$store.state.userId, // 员工Id
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
        weTagGroupList: [], // 客户标签合集
      },
      labels: [], // 客户标签
      alllabel: [], // 标签组
      grouplabel: [], // 一组标签
      // 点击测试组标签获取的变量
      groupId: '',
      name: '',
      tagId: '',
      addTag: [], // 添加的参数
      isactive: false,
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
      pageNum: 1,
      pageSize: 3,
      trajectoryType: 0,
      loading: false,
      finished: false,
      list: [],
      styleactive: '',
      flage: true,
    }
  },
  watch: {
    '$store.state.agentConfigStatus'(val) {
      val && this.init()
    },
  },
  computed: {
    userId() {
      return this.$store.state.userId
    },
    //   activeLabel : () => {
    //       this.addTag.forEach((value) => {
    //           value.name == this.name
    //       })
    //       return this.activelabel
    //   }
  },
  methods: {
    init() {
      let _this = this
      wx.invoke('getContext', {}, function(res) {
        if (res.err_msg == 'getContext:ok') {
          let entry = res.entry //返回进入H5页面的入口类型，目前有normal、contact_profile、single_chat_tools、group_chat_tools
          if (
            ![
              'single_chat_tools',
              'group_chat_tools',
              'contact_profile',
            ].includes(entry)
          ) {
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
              getAllTags()
                .then(({ data }) => {
                  // console.log(data);
                  _this.alllabel = data
                })
                .catch((err) => {
                  console.log(err)
                })
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
    //   客户待办点击保存事件
    saveInfo2() {},
    // 添加或编辑轨迹
    addOrEditWaitHandle(form) {
      addOrEditWaitHandle(form)
        .then((data) => {
          //  重新获取列表
          this.findAddaddEmployes()
          if (data.code == 200) {
            this.$toast.success('保存成功')
            this.usershow = false
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },
    //   获取轨迹信息
    findTrajectory() {
      let _this = this
      let form = _this.trajectoryType
        ? {
            trajectoryType: _this.trajectoryType,
          }
        : ''
      findTrajectory(form)
        .then((data) => {
          //   console.log(data.total);
          this.list = data.rows
        })
        .catch((err) => {
          console.log(err)
        })
    },
    // 点击信息动态
    information() {
      // console.log(123);
      ;(this.trajectoryType = 1), this.findTrajectory()
      this.styleActive1 = 'background:#1989fa;color:#fff'
      this.styleActive2 = ''
      this.styleActive3 = ''
      this.styleActive4 = ''
    },
    socialContact() {
      ;(this.trajectoryType = 2), this.findTrajectory()
      this.styleActive1 = ''
      this.styleActive2 = 'background:#1989fa;color:#fff'
      this.styleActive3 = ''
      this.styleActive4 = ''
    },
    activity() {
      this.trajectoryType = 3
      this.findTrajectory()
      this.styleActive1 = ''
      this.styleActive2 = ''
      this.styleActive3 = 'background:#1989fa;color:#fff'
      this.styleActive4 = ''
    },
    dealtWith() {
      ;(this.trajectoryType = 4), this.findTrajectory()
      this.styleActive1 = ''
      this.styleActive2 = ''
      this.styleActive3 = ''
      this.styleActive4 = 'background:#1989fa;color:#fff'
    },
    // 添加代办
    // 表单提交
    onSubmit() {
      let form = {
        trajectoryType: 4,
        externalUserid: this.externalUserid,
        content: this.conagency,
        createDate: new Date(this.dateagency),
        startTime: new Date(this.startTime),
        endTime: new Date(this.endTime),
        status: 1,
      }
      this.addOrEditWaitHandle(form)
      //   表单重置
      this.conagency = ''
      this.dateagency = ''
      this.startTime = ''
      this.endTime = ''
      // 重新获取列表
      this.findTrajectory()
    },
    // 待办日期
    formatDate(dateagency) {
      return `${dateagency.getFullYear()}-${dateagency.getMonth() +
        1}-${dateagency.getDate()}`
    },
    onConfirm(dateagency) {
      this.dateshow = false
      this.dateagency = this.formatDate(dateagency)
    },
    // 待办时间
    timecancel() {
      this.starttimeshow = false
    },
    starttimeconfirm(value) {
      this.startTime = value
      this.starttimeshow = false
      this.endtimeshow = true
    },
    endtimeconfirm(value) {
      this.endTime = value
      this.endtimeshow = false
      let time = ''
      if (this.startTime > this.endTime) {
        time = this.startTime
        this.startTime = this.endTime
        this.endTime = time
      }
      // console.log(this.startTime, this.endTime);
      this.endtimeshow = false
      this.timeagency = this.formatTime()
    },
    formatTime() {
      return `${this.startTime}-${this.endTime}`
    },
    // 待办动态
    // 点击删除按钮
    deltodoshow() {},

    goRoute(path) {
      this.$router.push({
        path,
        query: {
          customerId: this.externalUserid,
          //   type
        },
      })
    },
    // 第一层标签
    userLabel(item) {
      // debugger
      //   console.log(item.tagId);
      if (this.addTag.length == 0) {
        this.addTag.push({
          groupId: item.groupId,
          name: item.name,
          tagId: item.tagId,
        })
      } else {
        this.addTag.forEach((item1) => {
          if (item.tagId == item1.tagId) {
            this.flage = false // 数组里存在该对象
          }
        })
        // 数组里不存在该对象,则添加
        if (this.flage) {
          this.addTag.push({
            groupId: item.groupId,
            name: item.name,
            tagId: item.tagId,
          })
        } else {
          // 数组里存在该对象,则删除
          this.addTag = this.addTag.filter((element) => {
            return element.name !== item.name
          })
        }
      }
    },
    // 第二层标签
    changeLabel(item) {
      //   console.log(item.groupId);
      this.styleactive = item.groupId
      this.grouplabel = item.weTags
    },
    saveInfo() {
      // 更新客户画像标签 [{ groupId: this.groupId, name: this.name, tagId: this.tagId }]
      updateWeCustomerPorTraitTag({
        externalUserid: this.externalUserid,
        addTag: this.addTag,
      })
        .then((res) => {
          console.log(res)
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
    // 点击编辑按钮
    labelEdit() {
      this.show = true
      //   console.log(this.labels);
      if (this.labels) {
        this.labels.forEach((ele) => {
          this.addTag.push(ele.weTags)
        })
      }
      // 获取用户当前的lable,将当前用户的lable与所有lable进行对比，相同的弹框内蓝色展示
      // 弹框内的标签组选中时蓝色展示
      // 弹框内的子标签与选中时蓝色展示，点击时
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
      findAddGroupNum({
        externalUserid: this.externalUserid,
        userId: this.userId,
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

    getCustomerInfo() {
      this.$toast('userId:' + this.userId)
      getCustomerInfo({
        externalUserid: this.externalUserid,
        userId: this.userId,
      })
        .then(({ data }) => {
          // console.log(data);
          this.form = data
          this.labels = this.form.weTagGroupList
          // console.log(this.form);
        })
        .catch((err) => {
          console.log(err)
        })
    },
  },
  created() {
    this.$toast.loading({
      message: 'loading...',
      duration: 0,
      forbidClick: true,
    })
  },
  components: {
    StepList,
  },
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
  .c9 {
    color: #9c9c9c;
  }
}
.left {
  display: flex;
  margin-bottom: 20px;
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
  padding: 16px 16px 160px;
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
  // justify-content: space-between;
  padding-top: 10px;
  .label,
  .label1 {
    display: inline-block;
    width: 60px;
    height: 18px;
    font-size: 12px;
    // background-color: #f2f2f2;
    color: #2c8cf0;
    border: 1px solid #2c8cf0;
    text-align: center;
    margin: 10px 0 10px 10px;
    line-height: 16px;
    border-radius: 6px;
  }
}
.branch {
  position: relative;
  .saveinfo {
    position: absolute;
    width: 90%;
    height: 30px;
    left: 50%;
    top: 150%;
    transform: translate(-50%, -50%);
  }
}

//  社交关系
.boxnumber {
  height: 60px;
  width: 100%;
  margin: 20px;
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
    background-color: #f2f2f2;
    padding: 0 10px;
  }
}
/deep/.styleactive {
  //   background: #f00;
  color: #2c8cf0;
}
.iconfont {
  color: #2c8cf0;
}
.icon-xingbie {
  color: pink;
}
</style>
