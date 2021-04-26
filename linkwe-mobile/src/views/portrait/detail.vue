<template>
  <div>
    <!-- 头部 -->
    <div class="header">
      <van-icon
        name="arrow-left"
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
        v-model="form.age"
        name="年龄"
        label="年龄"
        placeholder="年龄"
        input-align="right"
      />
      <van-field
        v-model="form.birthday"
        name="生日"
        label="生日"
        placeholder="生日"
        input-align="right"
      />
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
import { getCustomerInfo, getWeCustomerInfo } from '@/api/portrait'
import { getUserInfo } from '@/api/common'
export default {
  data() {
    return {
      flage: true,
      // 接口开始
      // 表单数据
      form: {
        externalUserid: '', // 客户Id
        userId: this.$store.state.userId, // 员工Id
        name: '', // 昵称
        remarkMobiles: '', // 手机号
        birthday: '', // 客户生日
        age: '', // 年龄
        email: '', // 邮箱
        address: '', // 地址
        qq: '', // qq
        position: '', // 职业
        remarkCorpName: '', // 公司
        description: '', // 其他描述
      },
    }
  },
  computed: {
    // 获取客户年龄
  },
  created() {
    this.form.externalUserid = this.$route.query.customerId
    // 获取客户详细信息
    getCustomerInfo({
      externalUserid: this.form.externalUserid,
      userId: this.form.userId,
    })
      .then(({ data }) => {
        console.log(data)
        this.form = data
        console.log(this.form)
      })
      .catch((err) => {
        console.log(err)
      })
  },
  methods: {
    edit() {
      this.flage = !this.flage
    },
    // 点击保存按钮提交表单
    saveUserInformation() {
      this.flage = !this.flage

      getWeCustomerInfo(this.form)
        .then((data) => {
          console.log(data)
        })
        .catch((err) => {
          console.log(err)
        })
    },
  },
}
</script>

<style lang="less" scoped>
.header {
  margin: 20px 10px 10px;
  vertical-align: center;
//   text-align: center;
}
.van-icon-cross {
  position: absolute;
  left: 10px;
}
.title {
  text-align: center;
  font-size: 14px;
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
