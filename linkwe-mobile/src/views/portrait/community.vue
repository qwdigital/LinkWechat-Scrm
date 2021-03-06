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
      <span class="title"> 社群关系 </span>
    </div>
    <van-divider />
    <!-- 标签页 -->
    <div class="labelPge">
      <van-tabs
        v-model="active"
        color="#2C8CF0"
        line-width="50%"
        line-height="1.4px"
        title-active-color="#2C8CF0"
      >
        <van-tab title="添加的员工(5)">
          <van-list v-model="loading" :finished="finished" @load="onLoad">
            <van-cell v-for="item in list" :key="item">
              <div class="details">
                <div class="detail">
                  <div class="left">
                    <div class="img"><img src="" alt="" /></div>
                    <div class="right">
                      <div>
                        <span>张三 &nbsp; &nbsp;</span>
                      </div>
                      <div class="c9">
                        <span>添加时间：</span><span>2021-02-17 00:50</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </van-cell>
          </van-list>
        </van-tab>
        <van-tab title="添加的群聊(3)"
          >
          <div class="groupchat1">只看与我共同的群聊</div>
          <van-list v-model="loading" :finished="finished" @load="onLoad">
            <van-cell v-for="item in list" :key="item">
              <div class="details">
                <div class="detail">
                  <div class="left">
                    <div class="img"><img src="" alt="" /></div>
                    <div class="right">
                      <div>客户分享群(群人数)</div>
                      <div>
                        <span class="c9">群主：</span>
                        <span class="c9">张三</span>
                      </div>
                      <div class="c9">
                        <span>入群时间：</span><span>2021-02-17 00:50</span>
                      </div>
                    </div>
                  </div>
                  <div class="groupchat2">共同群聊</div>
                </div>
              </div>
            </van-cell>
          </van-list>
        </van-tab>
      </van-tabs>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      active: 0,
      //   添加员工
      list: [],
      loading: false,
      finished: false,
    };
  },
  methods: {
    onLoad() {
      setTimeout(() => {
        for (let i = 0; i < 2; i++) {
          this.list.push(this.list.length + 1);
        }

        // 加载状态结束
        this.loading = false;

        // 数据全部加载完成
        if (this.list.length >= 2) {
          this.finished = true;
        }
      }, 1000);
    },
  },
};
</script>

<style lang="less" scoped>
/deep/body {
  width: 100%;
  height: 600px;
}
.labelPge {
  width: 100%;
  height: 100%;
  background-color: #f2f2f2;
}
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
/* 添加的员工 */
.details {
  margin: 10px;
}
.detail {
  display: flex;
  justify-content: space-between;
  .c9 {
    color: #9c9c9c;
    font-size: 12px;
  }
}
.left {
  display: flex;
  margin-bottom: 20px;
  .img {
    width: 50px;
    height: 50px;
    background-color: #f2f2f2;
    margin-right: 10px;
    border-radius: 25px;
    vertical-align: bottom;
  }
  .right {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
}
// 添加的群聊
.groupchat1 {
    width: 120px;
    height: 20px;
    font-size: 12px;
    background-color: #fff;
    border-radius: 20px;
    text-align: center;
    line-height: 20px;
    margin: 10px 20px;
}
.groupchat2 {
      width: 60px;
    height: 20px;
    font-size: 12px;
    background-color: #fff;
    border-radius: 5px;
    text-align: center;
    line-height: 20px;
    margin: 10px 0px;
    color: #2C8CF0;
    border: 1px solid #2C8CF0;
}
</style>
