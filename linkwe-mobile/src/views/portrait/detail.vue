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
      <span class="title"> 客户资料 </span>
      <div class="data" v-if="flage" @click="edit">编辑</div>
      <div class="data" v-else @click="saveUserInformation">保存</div>
    </div>
    <van-divider />
    <!-- 详细资料 -->
    <van-form @submit="saveUserInformation" :disabled="flage">
      <div class="details">
        <div class="detail">
          <div class="left">
            <div class="img"><img src="" alt="" /></div>
            <div class="right">
              <div>
                <span>张三 &nbsp; &nbsp;</span
                ><van-icon name="manager" color="#9c9c9c" />
              </div>
              <div class="c9">
                <span>昵称：</span><span>{{ form.name }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <van-field
        v-model="form.remarkMobiles"
        name="手机号"
        label="手机号"
        placeholder="手机号"
        input-align="right"
      />
      <van-field
        v-model="getage"
        name="年龄"
        label="年龄"
        placeholder="年龄"
        input-align="right"
      />
      <div class="details">
        <div class="detail">
          <div class="c9">生日</div>
          <div class="c9">{{ form.birthday }}</div>
        </div>
      </div>
      <!-- -------------------------- -->
      <van-field
        v-model="form.email"
        name="邮箱"
        label="邮箱"
        placeholder="邮箱"
        input-align="right"
      />
      <van-field
        v-model="form.address"
        name="地址"
        label="地址"
        placeholder="地址"
        input-align="right"
      />
      <van-field
        v-model="form.qq"
        name="QQ"
        label="QQ"
        placeholder="QQ"
        input-align="right"
      />
      <van-field
        v-model="form.position"
        name="职业"
        label="职业"
        placeholder="职业"
        input-align="right"
      />
      <van-field
        v-model="form.remarkCorpName"
        name="公司"
        label="公司"
        placeholder="公司"
        input-align="right"
      />
      <van-field
        v-model="form.description"
        name="其他描述"
        label="其他描述"
        placeholder="其他描述"
        input-align="left"
        type="textarea"
        class="others"
      />
    </van-form>
  </div>
</template>
<script>
import { getCustomerInfo, getWeCustomerInfo } from "@/api/portrait";
import { getUserInfo } from "@/api/common";
export default {
  data() {
    return {
      flage: true,
      // 接口开始
      // 表单数据
      form: {
        externalUserid: "wm2H-nDQAACG5x4XjsM1OoW8UVfpbn3A", // 客户Id
        userId: "45DuXiangShangQingXie", // 员工Id
        name: "", // 昵称
        remarkMobiles: "", // 手机号
        birthday: "", // 客户生日
        email: "", // 邮箱
        address: "", // 地址
        qq: "", // qq
        position: "", // 职业
        remarkCorpName: "", // 公司
        description: "", // 其他描述
      },
    };
  },
  computed: {
    // 获取客户年龄
    getage: function() {
      // `this` 指向 vm 实例
      let nowdata = new Date();
      let yearnow = nowdata.getFullYear();
      let mouthnow = nowdata.getMonth() + 1;
      let daynow = nowdata.getDate();
      // console.log(yearnow,mouthnow,daynow);
      // this.form.birthday = "1995-03-11";
      if (this.form.birthday) {
        const year = this.form.birthday.substring(0, 4);
        const mouth =
          this.form.birthday.substring(5, 6) == 0
            ? this.form.birthday.substring(6, 7)
            : this.form.birthday.substring(5, 7);
        const day =
          this.form.birthday.substring(7, 8) == 0
            ? this.form.birthday.substring(8, 9)
            : this.form.birthday.substring(8, 10);
        // console.log(day);
        // console.log(mouth);
        // console.log(typeof(birthday));
        // console.log(birthday);
        console.log(parseInt(mouthnow), parseInt(mouth));
        if (parseInt(mouthnow) > parseInt(mouth)) {
          let age = parseInt(yearnow) - parseInt(year);
          return age;
        } else if (
          parseInt(mouthnow) == parseInt(mouth) &&
          parseInt(daynow) >= parseInt(day)
        ) {
          let age = parseInt(yearnow) - parseInt(year);
          return age;
        } else {
          let age = parseInt(yearnow) - parseInt(year) - 1;
          return age;
        }
      } else {
        return "";
      }
    },
  },
  created() {
    // 获取用户Id
    //  let auth_code = location.search
    //   .slice(1)
    //   .split('&')[0]
    //   .split('=')[1]
    // if (!auth_code) {
    //   this.$toast('未获得授权')
    //   return
    // }
    // getUserInfo(auth_code)
    //   .then(({ data }) => {
    //     this.form.userId = data.userId
    //     // this.$toast('userId:' + this.userId)
    //     console.log(this.userId);
    //   })
    //   .catch((err) => {
    //     Dialog.confirm({
    //       title: '标题',
    //       message: err,
    //     })
    //   })
    // 获取客户详细信息
    getCustomerInfo({
      externalUserid: this.form.externalUserid,
      userId: this.form.userId,
    })
      .then(({ data }) => {
        console.log(data);
        this.form = data;
        console.log(this.form);
      })
      .catch((err) => {
        console.log(err);
      });
  },
  methods: {
    edit() {
      this.flage = !this.flage;
    },
    // 点击保存按钮提交表单
    saveUserInformation() {
      this.flage = !this.flage;
      getWeCustomerInfo(this.form)
        .then((data)=> {
          console.log(data);
        })
        .catch((err) => {
          console.log(err);
        });
    },
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
.details {
  margin: 10px 16px;
}
.detail {
  display: flex;
  justify-content: space-between;
  .c9 {
    color: #c8c9cc;
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
  color: #5fa7f3;
  float: right;
}
.divider {
  width: 100%;
  height: 10px;
  background-color: #f2f2f2;
}
/deep/.others {
  line-height: 40px;
  flex-direction: column;
  .van-field__control--left {
    float: left;
    line-height: 20px;
    .textarea {
      margin-top: 20px;
    }
  }
}
</style>
