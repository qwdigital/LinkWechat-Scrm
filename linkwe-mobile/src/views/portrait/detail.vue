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
    <van-form @submit="saveUserInformation" :disabled="flage" >
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
        </div>
      </div>
      <van-field
        v-model="phonenumber"
        name="手机号"
        label="手机号"
        placeholder="手机号"
        input-align="right"
      />
      <van-field
        v-model="userage"
        name="年龄"
        label="年龄"
        placeholder="年龄"
        input-align="right"
      />
      <div class="details">
      <div class="detail">
        <div class="c9" >生日</div>
        <div class="c9" >123@qq.com</div>
      </div>
      </div>
      <!-- -------------------------- -->
       <van-field
        v-model="emaile"
        name="邮箱"
        label="邮箱"
        placeholder="邮箱"
        input-align="right"
      />
       <van-field
        v-model="address"
        name="地址"
        label="地址"
        placeholder="地址"
        input-align="right"
      />
       <van-field
        v-model="userQQ"
        name="QQ"
        label="QQ"
        placeholder="QQ"
        input-align="right"
      />
       <van-field
        v-model="occupation"
        name="职业"
        label="职业"
        placeholder="职业"
        input-align="right"
      />
       <van-field
        v-model="company"
        name="公司"
        label="公司"
        placeholder="公司"
        input-align="right"
      />
       <van-field
        v-model="others"
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
export default {
  data() {
    return {
      flage: true,
      // 表单数据
      phonenumber: "456456",
      userage:'23',
      emaile:'227385@qq.com',
      address:'address',
      userQQ:'227385',
      occupation:'occupation',
      company:'company',
      others:'11111111111111111111111111111111111111111111111111111111111'

    };
  },
  methods: {
    edit() {
      this.flage = !this.flage;
    },
    // 点击保存按钮提交表单
    saveUserInformation() {
      this.flage = !this.flage;
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
