<template>
  <div>
    <!-- 头部 -->
    <div class="header">
      <van-icon
        name="cross"
        color="#9c9c9c"
        size="16"
        @click="$router.back()"
      />
      <span class="title"> 客户画像 </span>
    </div>
    <van-divider />
    <!-- 详细资料 -->
    <div class="details">
      <div class="detail">
        <div class="left">
          <div class="img"><img src="" alt="" /></div>
          <div class="right">
            <div>
              <span>张三 &nbsp; &nbsp;</span
              ><van-icon name="manager" color="#9c9c9c" />
            </div>
            <div class="c9"><span>昵称：</span><span>王二毛</span></div>
          </div>
        </div>
        <div class="data" @click="goRoute">详细资料></div>
      </div>
      <div class="detail">
        <div class="c9">手机号</div>
        <div>123@qq.com</div>
      </div>
      <van-divider />
      <div class="detail">
        <div class="c9">年龄</div>
        <div>123@qq.com</div>
      </div>
      <van-divider />
      <div class="detail">
        <div class="c9">生日</div>
        <div>123@qq.com</div>
      </div>
      <van-divider />
      <div class="detail">
        <div class="c9">邮箱</div>
        <div>123@qq.com</div>
      </div>
    </div>
    <div class="divider"></div>
    <!-- 客户标签 -->
    <div class="userlabel">
      <div class="detail">
        <div>客户标签</div>
        <div class="data" is-link @click="show = true">编辑</div>
      </div>
      <van-row gutter="10" class="labels">
        <van-col span="4.5"> <div class="label">标签1</div></van-col>
        <van-col span="4.5"> <div class="label">标签1</div></van-col>
        <van-col span="4.5"> <div class="label">标签1</div></van-col>
        <van-col span="4.5"> <div class="label">标签1</div></van-col>
        <van-col span="4.5"> <div class="label">标签1</div></van-col>
        <van-col span="4.5"> <div class="label">标签1</div></van-col>
        <van-col span="4.5"> <div class="label">标签1</div></van-col>
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
        <van-row gutter="8" class="labels">
          <van-col span="4.5"
            ><div class="label" @click="userLabel">标签1</div></van-col
          >
          <van-col span="4.5"> <div class="label">标签1</div></van-col>
          <van-col span="4.5"> <div class="label">标签1</div></van-col>
          <van-col span="4.5"> <div class="label">标签1</div></van-col>
          <van-col span="4.5"> <div class="label">标签1</div></van-col>
          <van-col span="4.5"> <div class="label">标签1</div></van-col>
          <van-col span="4.5"> <div class="label">标签1</div></van-col>
        </van-row>
        <div class="branch" v-if="true">
          <van-divider />
          <p>标签组:</p>
          <van-row class="labels">
            <van-col span="4.5"
              ><div class="label" @click="changeLabel">标签1</div></van-col
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
        <div class="data" @click="detailGoRoute">详情</div>
      </div>
      <div class="detail">
        <div class="boxnumber">
          <p>添加的员工</p>
          <div class="number">8</div>
        </div>
        <div class="boxnumber">
          <p>添加的群聊</p>
          <div class="number">8</div>
        </div>
        <div class="boxnumber">
          <p>共同的群聊</p>
          <div class="number">8</div>
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
      <van-row gutter="8" class="labels">
        <van-col span="6"> <div class="label1">信息动态</div></van-col>
        <van-col span="6"> <div class="label1">社交动态</div></van-col>
        <van-col span="6"> <div class="label1">活动动态</div></van-col>
        <van-col span="6"> <div class="label1">待办动态</div></van-col>
      </van-row>

      <!-- 步骤条 -->
      <van-steps direction="vertical" inactive-color="#2c8cf0" :active="active">
        <!-- 时间 -->
        <p class="f12" style="position:relative; left:-32px;">
          2021-02-16 星期二
        </p>
        <van-step>
          <span class="f12 po"> 12:40</span>
          <span class="fs14">信息动态</span>
          <span class="deldynamic">删除</span>
          <p class="fs14 con ">编辑编辑</p>
        </van-step>
        <van-step>
          <span class="f12 po"> 12:40</span>
          <span class="fs14">社交动态</span>
          <span class="deldynamic">删除</span>
          <p class="fs14 con ">编辑编辑</p>
        </van-step>
      </van-steps>
      <van-divider />
      <van-steps direction="vertical" inactive-color="#2c8cf0" :active="active">
        <!-- 时间 -->
        <p class="f12" style="position:relative; left:-32px;">
          2021-02-16 星期二
        </p>
        <van-step @click="todonewsshow = true">
          <span class="f12 po"> 12:40</span>
          <span class="fs14">待办动态</span>
          <span class="finish">完成</span>
          <span class="deldynamic">删除</span>
          <p class="fs14 con ">编辑编辑</p>
        </van-step>
      </van-steps>
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
        <div style="margin: 16px;">
          <van-button round block type="info" native-type="submit"
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
import { getCustomerInfo } from '@/api/portrait' 
export default {
  data() {
    return {
      // 客户标签弹出框
      show: false,
      // 客户待办的弹出框开始
      usershow: false,
      conagency: "", // 待办内容

      // 待办日期
      dateagency: "",
      dateshow: false,
      minDate: new Date(2021, 0, 1),
      maxDate: new Date(2021, 12, 31),
      // 待办时间
      timeagency: "",
      starttimeshow: false,
      endtimeshow: false,
      currentTime: "12:00",
      startTime: "",
      endTime: "",
      // 客户待办的弹出框结束

      actions: [{ name: "选项一" }, { name: "选项二" }, { name: "选项三" }],
      // 步骤条
      active: -1,
      // 客户轨迹
      // 待办动态
      todonewsshow: false,
      // 接口开始
      externalUserid: "", // 客户Id
      userid: "", // 员工Id
    };
  },
  methods: {
    // 添加代办
    // 表单提交
    onSubmit() {},
    // 待办日期
    formatDate(dateagency) {
      return `${dateagency.getFullYear()}-${dateagency.getMonth() +
        1}-${dateagency.getDate()}`;
    },
    onConfirm(dateagency) {
      this.dateshow = false;
      this.dateagency = this.formatDate(dateagency);
    },
    // 待办时间
    timecancel() {
      this.starttimeshow = false;
    },
    starttimeconfirm(value) {
      this.startTime = value;
      this.starttimeshow = false;
      this.endtimeshow = true;
    },
    endtimeconfirm(value) {
      this.endTime = value;
      this.endtimeshow = false;
      let time = "";
      if (this.startTime > this.endTime) {
        time = this.startTime;
        this.startTime = this.endTime;
        this.endTime = time;
      }
      // console.log(this.startTime, this.endTime);
      this.endtimeshow = false;
      this.timeagency = this.formatTime();
    },
    formatTime() {
      return `${this.startTime}-${this.endTime}`;
    },
    // 待办动态
    // 点击删除按钮
    deltodoshow() {},

    goRoute() {
      this.$router.push({
        path: "/detail",
        query: {
          //   type
        },
      });
    },
    detailGoRoute() {
      this.$router.push({
        path: "/community",
        query: {
          //   type
        },
      });
    },
    // 第一层标签
    userLabel() {
      console.log(123);
    },
    // 第二层标签
    changeLabel() {
      console.log(456);
    },
    saveInfo() {},
  },
  created() {
    // 获取客户详细信息
    getCustomerInfo({
      externalUserid: this.externalUserid,
      userid: this.userid,
    })
      .then(({ data }) => {
        console.log(data);
      })
      .catch((err) => {
        console.log(err);
      });
  },
};
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
.labels {
  display: flex;
  flex-wrap: wrap;
  // justify-content: space-between;
  padding-top: 10px;
  .label,
  .label1 {
    width: 60px;
    height: 18px;
    font-size: 12px;
    background-color: #f2f2f2;
    text-align: center;
    margin: 10px 0px;
    line-height: 18px;
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
    top: 240%;
    transform: translate(-50%, -50%);
  }
}

//  社交关系
.boxnumber {
  height: 60px;
  width: 100%;
  margin: 20px;
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
  .labels {
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
</style>
