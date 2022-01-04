<template>
  <div>
    <van-list
      v-model="loading"
      :finished="finished"
      finished-text="没有更多了"
      loading-text="上划加载更多"
    >
      <van-cell v-for="(item, index) in wList" :key="index">
        <!-- 时间 -->
        <p class="f12" style="position:relative; ">
          {{ dateFormat(item[0].createTime, 'yyyy-MM-dd w') }}
          <!-- {{item1.trajectoryType}} -->
        </p>
        <!-- <van-cell> -->
        <van-steps
          direction="vertical"
          inactive-color="#2c8cf0"
          :active="active"
          v-for="(item1, index) in item"
          :key="index"
        >
          <van-step class="msg">
            <span class="f12 po"> {{ dateFormat(item1.createTime, 'hh:mm') }}</span>
            <span class="fs14">{{ item1.title }}</span>
            <!-- <span class="finish-box">
              <span
                class="finish"
                v-if="item1.trajectoryType == 4 && item1.status != 3"
                @click="finDynamic(item1.id)"
              >
                完成
              </span>
              <span class="deldynamic" @click="delDynamic(item1.id)">删除</span>
            </span> -->

            <p class="fs14 con ">{{ item1.content }}</p>
          </van-step>
          <!-- <van-step>
          <span class="f12 po"> 12:40</span>
          <span class="fs14">社交动态</span>
          <span class="deldynamic">删除</span>
          <p class="fs14 con ">编辑编辑</p>
        </van-step> -->
        </van-steps>
      </van-cell>
      <!-- <van-divider /> -->
    </van-list>
  </div>
</template>

<script>
import { removeTrajectory, handleWait } from '@/api/portrait'
import { dateFormat } from '@/utils'
export default {
  props: ['stepList'],
  inject: ['reload'],
  data() {
    return {
      // 步骤条
      active: -1,
      //   轨迹外层按时间
      wlist: [],
      content: '',
      type: 0,
      wList: [],
      finished: false,
      loading: false,
      dateFormat
    }
  },
  watch: {
    stepList(newVal, oldVal) {
      //   console.log(newVal, oldVal);
      this.wList = []
      let dayList = []

      newVal.forEach((ele) => {
        let date = this.dateFormat(ele.createTime, 'yyyyMMdd')
        dayList.includes(date) || dayList.push(date)
      })
      dayList.sort((a, b) => b - a)
      for (let i = 0; i < dayList.length; i++) {
        let timeList = []
        for (let j = 0; j < newVal.length; j++) {
          // console.log(newVal[j].createTime);
          if (dayList[i] == this.dateFormat(newVal[j].createTime, 'yyyyMMdd')) {
            timeList.push(newVal[j])
          }
        }
        this.wList.push(timeList)
      }
      this.loading = false
      this.finished = true

      // console.log(this.wlist);
      // this.onLoad()
    }
  },
  methods: {
    onLoad() {
      // // 异步更新数据
      // // setTimeout 仅做示例，真实场景中一般为 ajax 请求
      // setTimeout(() => {
      //   //   debugger
      //   let total = this.wlist.length > 10 ? 10 : this.wlist.length
      //   for (let i = 0; i < total; i++) {
      //     this.wList.push(this.wlist[i])
      //   }
      //   console.log(this.wList)
      //   // 加载状态结束
      //   this.loading = false
      //   // 数据全部加载完成
      //   if (this.wList.length >= this.wlist.length) {
      //     this.finished = true
      //   }
      // }, 1000)
    }
    //   根据数字判断类型
    // chargeType(num) {
    //   if (num == 1) {
    //     return (this.type = '客户动态')
    //   } else if (num == 2) {
    //     return (this.type = '员工动态')
    //   } else if (num == 3) {
    //     return (this.type = '互动动态')
    //   } else if (num == 4) {
    //     return (this.type = '跟进动态')
    //   }
    // },

    // 删除轨迹
    // delDynamic(id) {
    //   this.$dialog
    //     .confirm({
    //       title: '警告',
    //       message: '确定要删除吗？'
    //     })
    //     .then(() => {
    //       return removeTrajectory(id)
    //     })
    //     .then((data) => {
    //       this.$toast.success('删除成功')
    //       // 重新获取全部数据
    //       this.$emit('reload')
    //     })
    //     .catch(() => {
    //       // on cancel
    //     })
    // },
    // 点击完成
    // finDynamic(id) {
    //   handleWait(id)
    //     .then((data) => {
    //       this.$emit('reload')
    //     })
    //     .catch((err) => {
    //       console.log(err)
    //     })
    // }
  }
}
</script>

<style lang="less" scoped>
.f12 {
  color: #9c9c9c;
  font-size: 12px;
  font-weight: 600;
}
/deep/.msg {
  &::after {
    border: none;
  }
  .po {
    position: relative;
  }
}
.fs14 {
  color: #333;
  font-size: 14px;
  position: relative;
  left: 20px;
}
// .deldynamic {
//   float: right;
//   color: #9c9c9c;
//   font-size: 12px;
//   font-weight: 600;
// }
.con {
  left: 51px;
  margin-top: 20px;
}
// .finish-box {
//   float: right;
//   position: relative;
//   width: 80px;
// }
.finish {
  position: absolute;
  color: #2c8cf0;
  font-size: 12px;
  font-weight: 600;
  //   right: 30px;
  //   top: 0px;
}
</style>
