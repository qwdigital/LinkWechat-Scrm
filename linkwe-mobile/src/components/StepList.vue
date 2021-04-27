<template>
  <div>
    <van-list
      v-model="loading"
      :finished="finished"
      finished-text="没有更多了"
      loading-text="上划加载更多"
    >
      <van-cell v-for="(item, index) in wList" :key="index">
        <!-- {{item[0].createDate}} -->
        <!-- 时间 -->
        <p class="f12" style="position:relative; ">
          {{ getTime1(item[0].createDate) }}
          {{ getTime3(getTime1(item[0].createDate)) }}
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
            <span class="f12 po">
              {{ getTime2(getTime1(item1.createDate)) }}</span
            >
            <span class="fs14">{{ chargeType(item1.trajectoryType) }}</span>
            <span class="finish-box">
                <span
              class="finish"
              v-if="item1.trajectoryType == 4"
              @click="finDynamic(item1.id)"
              >完成</span
            >
            <span class="deldynamic" @click="delDynamic(item1.id)">删除</span>
            </span>
            
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
      <van-divider />
    </van-list>
  </div>
</template>

<script>
import { removeTrajectory, handleWait } from "@/api/portrait";
export default {
  props: ["stepList"],
  inject:['reload'],
  data() {
    return {
      // 步骤条
      active: -1,
      //   轨迹外层按时间
      wlist: [],
      // 轨迹内层
      nlist: [],
      oldele: [],
      content: "",
      type: 0,
      wList: [],
      finished: false,
      loading: false,
    };
  },
  watch: {
    stepList(newVal, oldVal) {
    //   console.log(newVal, oldVal);
      if (newVal != oldVal) {
        //   debugger
        this.wlist = [];
        this.wList = [];
        this.oldele = [];
        this.nlist = [];
      }

      newVal.forEach((ele) => {
        this.oldele.push(parseInt(this.getTime(ele.createDate)));
        // console.log(parseInt(this.getTime(ele.createDate)));
      });
      //   console.log(this.oldele);
      this.oldele = this.newArr(this.oldele).sort(this.f);
      //   console.log(this.oldele);
      for (let i = 0; i < this.oldele.length; i++) {
        for (let j = 0; j < newVal.length; j++) {
          // console.log(newVal[j].createDate);
          if (this.oldele[i] == this.getTime(newVal[j].createDate)) {
            this.nlist.push(newVal[j]);
            // console.log(this.nlist);
          }
        }
        this.wlist.push(this.nlist);
        this.nlist = [];
      }
      // console.log(this.wlist);
      this.onLoad()
    },
  },
  methods: {
    onLoad() {
      // 异步更新数据
      // setTimeout 仅做示例，真实场景中一般为 ajax 请求
      setTimeout(() => {
        //   debugger
        let total = this.wlist.length > 10 ? 10 : this.wlist.length;
        for (let i = 0; i < total; i++) {
          this.wList.push(this.wlist[i]);
        }
        // 加载状态结束
        this.loading = false;

        // 数据全部加载完成
        if (this.wList.length >= this.wlist.length) {
          this.finished = true;
        }
      }, 1000);
    },
    //   根据数字判断类型
    chargeType(num) {
      if (num == 1) {
        return (this.type = "信息动态");
      } else if (num == 2) {
        return (this.type = "社交动态");
      } else if (num == 3) {
        return (this.type = "活动规则");
      } else if (num == 4) {
        return (this.type = "待办动态");
      }
    },
    //   数组去重
    newArr(arr) {
      return Array.from(new Set(arr));
    },
    // 数组由大到小排序
    f(a, b) {
      //排序函数
      return -(a - b); //取反并返回比较参数
    },
    // 时间处理器
    getTime(data) {
      const date = new Date(data);
      // console.log(timer.getFullYear());
      var Y = date.getFullYear();
      var M =
        date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1;
      var D = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
      return Y + M + D;
    },
    getTime1(data) {
      const date = new Date(data);
      // console.log(timer.getFullYear());
      var Y = date.getFullYear() + "-";
      var M =
        (date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1) + "-";
      var D = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
      return Y + M + D;
    },
    getTime2(data) {
      const date = new Date(data);
      // console.log(timer.getFullYear());
      var h =
        (date.getHours() < 10 ? "0" + date.getHours() : date.getHours()) + ":";
      var m =
        date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
      return h + m;
    },
    // 处理星期几
    getTime3(time) {
      var dateArray = time.split("-");
      var date = new Date(
        dateArray[0],
        parseInt(dateArray[1] - 1),
        dateArray[2]
      );
      var week = "星期" + "日一二三四五六".charAt(date.getDay());
      return week;
    },
    // 删除轨迹
    delDynamic(id) {
      removeTrajectory(id)
        .then((data) => {
          //   console.log(data.code);
          if (data.code == 200) {
            //   提示删除成功
            // this.msgSuccess("删除成功");
            // debugger
            // location.reload();
            this.$toast.success('删除成功');
            this.reload ()
            // 重新获取全部数据
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // 点击完成
    finDynamic(id) {
      handleWait(id)
        .then((data) => {
          if (data.code == 200) {
            this.delDynamic(id);
            this.reload();
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
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
.deldynamic {
  float: right;
  color: #9c9c9c;
  font-size: 12px;
  font-weight: 600;
}
.con {
  left: 51px;
  margin-top: 20px;
}
.finish-box {
     display: inline-block;
  position: relative;
  color: #2c8cf0;
  font-size: 12px;
  font-weight: 600;
  left: 140px;
  top: 0px;
  width: 65px;
}
.finish {
   
  position: absolute;
  color: #2c8cf0;
  font-size: 12px;
  font-weight: 600;
//   right: -138px;
  top: 0px;
}
</style>
