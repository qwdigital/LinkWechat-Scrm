<template>
  <div class="index">
    <div class="index_l whitebg">
      <!-- <div class="box titlebox">
        <p>{{ parseTime(nowTime) }}</p>
      </div> -->
      <div class="tables">
        <div style="text-align: left">
          <el-row type="flex" class="row-bg" justify="space-between">
            <el-col :span="24" class="title_name"
              >数据总览
              <span class="fontgay">最近更新：{{ uptime }}</span>
            </el-col>
          </el-row>
          <el-row
            type="flex"
            class="row-bg"
            justify="space-between"
            style="margin-top: 35px; text-align: center"
          >
            <el-col :span="6" class="col_style">企业成员总数</el-col>
            <el-col :span="6" class="col_style">客户总人数</el-col>
            <el-col :span="6" class="col_style">客户群总数</el-col>
            <el-col :span="6" class="col_style">群成员总数</el-col>
          </el-row>
          <el-row
            type="flex"
            class="row-bg"
            justify="space-between"
            style="
              margin-top: 10px;
              font-size: 35px;
              font-weight: bold;
              color: #0079de;
              text-align: center;
            "
          >
            <el-col :span="6">
              <count-to :start-val="0" :end-val="table.userCount" :duration="durationCount" />
            </el-col>
            <el-col :span="6">
              <count-to :start-val="0" :end-val="table.customerCount" :duration="durationCount" />
            </el-col>
            <el-col :span="6">
              <count-to :start-val="0" :end-val="table.groupCount" :duration="durationCount" />
            </el-col>
            <el-col :span="6">
              <count-to
                :start-val="0"
                :end-val="table.groupMemberCount"
                :duration="durationCount"
              />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="dataall" style="margin-top: 10px">
        <div style="text-align: left">
          <el-row type="flex" class="row-bg" justify="space-between">
            <el-col :span="10" class="title_name">数据趋势</el-col>
            <el-col :span="10" style="text-align: right">
              <el-radio-group v-model="timeType" style="margin-top: 20px" @change="timeTypeCheck">
                <el-radio-button label="today">今日</el-radio-button>
                <el-radio-button label="week">本周</el-radio-button>
                <el-radio-button label="month">本月</el-radio-button>
                <el-radio-button label="reset">
                  <i class="el-icon-refresh"> </i>
                </el-radio-button>
              </el-radio-group>
            </el-col>
          </el-row>

          <el-row
            type="flex"
            class="row-bg"
            justify="space-between"
            style="margin-top: 30px; text-align: center"
          >
            <el-col :span="6">新增客户数</el-col>
            <el-col :span="6">新增客群数</el-col>
            <el-col :span="6">群新增人数</el-col>
            <el-col :span="6">流失客户数</el-col>
          </el-row>
          <el-row
            type="flex"
            class="row-bg"
            justify="space-between"
            style="
              margin-top: 10px;
              font-size: 35px;
              font-weight: bold;
              color: #0079de;
              text-align: center;
            "
          >
            <el-col :span="6">{{ erchatsTable.newContactCnt }}</el-col>
            <el-col :span="6">{{ erchatsTable.newChatCnt }}</el-col>
            <el-col :span="6">{{ erchatsTable.newMemberCnt || 0 }} </el-col>
            <el-col :span="6">{{ erchatsTable.negativeFeedbackCnt }}</el-col>
          </el-row>
          <el-row
            type="flex"
            class="row-bg"
            justify="space-between"
            style="margin-top: 10px; text-align: center"
          >
            <el-col :span="6">
              比{{ time }}
              <span :class="getClass('newContactCntDiff')">
                {{ getArrow('newContactCntDiff') }}
                {{ Math.abs(erchatsTable.newContactCntDiff) }}
              </span>
            </el-col>
            <el-col :span="6">
              比{{ time }}
              <span :class="getClass('newChatCntDiff')">
                {{ getArrow('newChatCntDiff') }}
                {{ Math.abs(erchatsTable.newChatCntDiff) }}
              </span>
            </el-col>
            <el-col :span="6">
              比{{ time }}
              <span :class="getClass('newMemberCntDiff')">
                {{ getArrow('newMemberCntDiff') }}
                {{ Math.abs(erchatsTable.newMemberCntDiff) }}
              </span>
            </el-col>
            <el-col :span="6">
              比{{ time }}
              <span :class="getClass('negativeFeedbackCntDiff')">
                {{ getArrow('negativeFeedbackCntDiff') }}
                {{ Math.abs(erchatsTable.negativeFeedbackCntDiff) }}
              </span>
            </el-col>
          </el-row>
        </div>
        <div id="fatherbox">
          <div id="echart" ref="views"></div>
        </div>
      </div>

      <div class="car card">
        <el-row type="flex" justify="space-between">
          <el-col :span="24" style="font-size: 24px; line-height: 80px">快捷功能 </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6" v-for="(item, i) in car" :key="i">
            <div class="grid-content" @click="$router.push(item.url)">
              <div class="ac"><img :src="item.img" alt="" /></div>
              <div class="ac craname">{{ item.name }}</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div class="index_r whitebg">
      <div class="qy_R">
        <div class="inedx_r_top_t">
          <p class="p1">{{ $store.state.user.companyName }}，欢迎登陆本系统</p>
          <p class="p2">登录时间：{{ parseTime(new Date()) }}</p>
          <p class="p1 p3 margint20">LinkWechat 企业微信SCRM</p>
          <p class="p2 p4">
            版本信息：V2.x开源
            <span class="fr">可用期限：永久</span>
          </p>
          <el-row
            :gutter="20"
            type="flex"
            class="row-bg"
            justify="center"
            style="text-align: center"
          >
            <el-col :span="10" class="flexspan">
              <img src="../assets/index/bzwd.png" />
              <a href="https://www.yuque.com/linkwechat/help"> 帮助手册</a>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col
              :span="10"
              class="flexspan"
              @click.native="msgInfo('《使用手册》正在加急上线中，敬请关注…')"
              ><img src="../assets/index/kfsc.png" /> 开发文档</el-col
            >
          </el-row>
        </div>
        <div class="inedx_r_top_bottom">
          <span class="inedx_r_top_bottomp1"><img src="../assets/index/gxrz.png" /> 更新日志</span>
          <span
            class="fr"
            style="color: #0079de"
            @click="msgInfo('《更新日志》正在加急上线中，敬请关注…')"
            >更多</span
          >
          <ul>
            <li v-for="(index, i) in 3" :key="i" @click="msgInfo('暂无内容')">
              即将上线，敬请期待
              <span class="fr">03-26</span>
            </li>
          </ul>
        </div>
        <div class="inedx_r_top_bottom">
          <span class="inedx_r_top_bottomp1"><img src="../assets/index/qyxy.png" /> 私域学院</span>
          <span class="fr" style="color: #0079de">更多</span>
          <ul>
            <li v-for="(index, i) in 3" :key="i" @click="msgInfo('暂无内容')">
              即将上线，敬请期待
              <span class="fr">03-26</span>
            </li>
          </ul>
        </div>
        <div class="inedx_r_top_bottom" style="text-align: center">
          <p style="text-align: left">
            <span class="inedx_r_top_bottomp1">
              <img src="../assets/index/kfq.png" /> 官方交流</span
            >
          </p>
          <img :src="bossImg" class="img" />
        </div>
        <div class="inedx_r_top_bottom" style="text-align: center">
          <p style="text-align: left">
            <span class="inedx_r_top_bottomp1">
              <img src="../assets/index/khq.png" /> 联系客服</span
            >
          </p>
          <img :src="bossImg" class="img" />
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import * as api from '@/api/index'
import echarts from 'echarts'
import CountTo from 'vue-count-to'
export default {
  name: 'Index',
  components: { CountTo },
  data() {
    return {
      bossImg: require('@/assets/index/boss.png'),
      car: [
        {
          name: '企业客户',
          url: '/customerManage/customer',
          img: require('@/assets/index/kehgl.png')
        },
        {
          name: '员工活码',
          url: '/drainageCode/staff',
          img: require('@/assets/index/neircd.png')
        },
        {
          name: '新增群发',
          url: '/customerMaintain/groupMessage/add',
          img: require('@/assets/index/renwb.png')
        },
        {
          name: '任务宝',
          url: '/application/taskGroup',
          img: require('@/assets/index/xiaoxqf.png')
        },
        {
          name: '新客拉群',
          url: '/communityOperating/newCustomer',
          img: require('@/assets/index/yuanghm.png')
        },
        {
          name: '素材中心',
          url: '/customerMaintain/material',
          img: require('@/assets/index/zidgl.png')
        },
        {
          name: '关键词群',
          url: '/communityOperating/keywords',
          img: require('@/assets/index/yuanghm.png')
        },
        {
          name: '会话存档',
          url: '/riskControl/conversation/content',
          img: require('@/assets/index/zidgl.png')
        }
      ],
      table: {
        userCount: 0,
        customerCount: 0,
        groupCount: 0,
        groupMemberCount: 0
      },
      erchatsTable: {},
      allData: {},
      time: '昨天',
      uptime: '',
      timeType: 'today',
      durationCount: 2600
    }
  },
  mounted() {
    this.erchatInfo()
    this.tableInfo()
  },
  methods: {
    timeTypeCheck(type) {
      switch (type) {
        case 'today':
        case 'week':
        case 'month':
          let json = {
            today: '昨天',
            week: '上周',
            month: '上月'
          }
          this.time = json[type]
          this.erchatsTable = this.allData[type]
          this.serErchat()
          break

        default:
          this.time = '昨天'
          this.timeType = 'today'
          this.refresh()
          break
      }
    },
    tableInfo() {
      api.indexTable().then((res) => {
        this.table = res.data
      })
    },
    erchatInfo() {
      api.indexEchart().then((res) => {
        this.allData = res.data
        this.uptime = res.data.updateTime
        this.erchatsTable = this.allData.today
        this.serErchat()
      })
    },
    serErchat() {
      let data = {
        arr0: [],
        arr1: [],
        arr2: [],
        arr3: [],
        xData: []
      }
      let series = []
      this.erchatsTable.dataList.forEach((a) => {
        data.xData.push(a.xtime)
        data.arr0.push(a.newContactCnt)
        data.arr2.push(a.newMemberCnt)
        data.arr1.push(a.newChatCnt)
        data.arr3.push(a.negativeFeedbackCnt)
      })

      for (let index = 0; index < 4; index++) {
        series.push({
          name: ['新增客户数', '新增客群数', '群新增人数', '流失客户数'][index],
          type: 'line',
          // stack: '总量',
          smooth: true,
          data: data['arr' + index],
          // itemStyle: {
          //   normal: {
          //     lineStyle: {
          //       color: colors[index]
          //     }
          //   }
          // },
          textStyle: {
            color: '#fff' // 主标题文字的颜色。
          }
        })
      }
      this.drawLine(data.xData, series)
    },
    drawLine(xData, series) {
      let obj = document.getElementById('echart')
      let charts = echarts.init(obj)
      let timer = 0
      window.onresize = function () {
        clearTimeout(timer)
        timer = setTimeout(() => {
          charts.resize()
        }, 200)
      }
      charts.setOption({
        color: ['#088AEE', '#E74E59', '#14BF48', '#FA7216'],
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['新增客户数', '新增客群数', '群新增人数', '流失客户数']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: xData,
          axisLine: {
            lineStyle: {
              color: '#ccc'
            }
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: '#ccc'
            }
          }
        },
        series
      })
    },
    refresh() {
      api.refresh().then((res) => {
        this.erchatInfo()
      })
    },
    getClass(key) {
      let val = Number(this.erchatsTable[key])
      if (val > 0) {
        return 'ascend'
      } else if (val < 0) {
        return 'descend'
      }
    },
    getArrow(key) {
      let val = Number(this.erchatsTable[key])
      if (val > 0) {
        return '↑'
      } else if (val < 0) {
        return '↓'
      } else {
        return '-'
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.index {
  margin: 0;
  width: 100%;
  background: #f1f1f1;

  // overflow: hidden;
  .title_name {
    font-size: 24px;
    font-weight: bold;
    color: #000000;
    padding-top: 15px;
  }

  .col_style {
    font-size: 18px;
    font-weight: 500;
    color: #666;
  }

  .fontgay {
    text-indent: 4em;
    color: #999;
    font-size: 14px;
    font-weight: 200;
  }

  #fatherbox {
    padding-top: 30px;
    height: 380px;
    overflow-y: scroll;
    overflow: hidden;
    border-radius: 5px;
    font-size: 16px;
    background: #fff;
  }

  #echart {
    width: 100%;
    height: 100%;
  }

  .fr {
    float: right;
  }

  .descend {
    color: #ff0000;
  }

  .ascend {
    color: green;
  }

  .tables {
    width: 100%;
    height: 200px;
    background: #fff;
    border-radius: 5px;
    padding: 15px;
    font-size: 16px;
  }

  .whitebg {
    min-height: 1000px;
    float: left;
    border-radius: 5px;
  }

  .index_l {
    width: 76%;
  }

  .circle {
    width: 80px;
    height: 80px;
    background: #999;
    border-radius: 50%;
    margin: 0 auto;

    img {
      border-radius: 50%;
      width: 80px;
      height: 80px;
    }
  }

  .index_r {
    width: 23%;
    margin-left: 1%;

    .qy_R {
      background: #fff;
      min-height: 1267px;
      border-radius: 5px;

      .inedx_r_top_t {
        border-radius: 5px;
        height: 270px;
        background-size: 100% 100%;
        background: url('../assets/index/yd.png') no-repeat;
        background-size: cover;
        padding: 20px 10px 10px 10px;
        color: #fff;

        .flexspan {
          height: 40px;
          line-height: 40px;
          background: #fff;
          color: #0079de;
          border-radius: 4px;
          margin-top: 30px;
        }

        .margint20 {
          margin-top: 31px;
        }

        p {
          padding-top: 9px;
        }

        .p1 {
          font-size: 16px;
          font-weight: 500;
          color: #ffffff;
        }

        .p2 {
          font-size: 14px;
          font-weight: 400;
          color: #d3e8ff;
        }

        .p3 {
          font-size: 18px;
        }

        .p4 {
          color: #ffffff;
          padding-top: 10px;
        }
      }

      .img {
        margin: 30px;
        width: 175px;
        height: 175px;
      }

      .inedx_r_top_bottom {
        padding: 10px 30px;
        margin-top: 29px;

        .inedx_r_top_bottomp1 {
          font-size: 16px;
          color: #333333;
        }

        ul {
          margin-top: 20px;
        }

        ul li {
          font-size: 14px;
          line-height: 35px;
          font-weight: 500;
          color: #666;

          span {
            color: #999999;
          }
        }
      }
    }
  }

  .box {
    width: 100%;
  }

  .titlebox {
    height: 80px;
    padding: 10px 0;
    background: #f1f1f1;

    p {
      line-height: 30px;
      margin: 0;
      padding: 0;
    }
  }

  .adminname {
    font-size: 20px;
    font-weight: bold;
  }

  .dataall {
    width: 100%;
    background: #fff;
    border-radius: 5px;
    padding: 20px;
    font-size: 16px;
  }

  .car {
    line-height: 70px;

    .grid-content {
      overflow: hidden;
      background: #fff;
      border-radius: 5px;
      padding: 10px 30px;

      .craname {
        line-height: 50px;
        font-size: 16px;
      }

      img {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        background: #eee;
      }
    }
  }
}

.card {
  margin-top: 40px;
  background: #fff;
  border-radius: 5px;
  padding: 20px;
  font-size: 16px;
}
</style>
