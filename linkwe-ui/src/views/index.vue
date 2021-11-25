<template>
  <div class="index">
    <div class="index_l whitebg">
      <!-- <div class="box titlebox">
        <p>{{ parseTime(nowTime) }}</p>
      </div> -->
      <div class="tables">
        <div style="text-align:left">
          <el-row type="flex" class="row-bg" justify="space-between">
            <el-col :span="24" class="title_name">数据总览</el-col>
          </el-row>
          <el-row
            type="flex"
            class="row-bg"
            justify="space-between"
            style="margin-top:35px; text-align: center;"
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
            style="margin-top:10px;font-size:35px;font-weight:bold;color:#0079DE; text-align: center;"
          >
            <el-col :span="6">{{ table.userCount }}</el-col>
            <el-col :span="6">{{ table.customerCount }}</el-col>
            <el-col :span="6">{{ table.groupCount }}</el-col>
            <el-col :span="6">{{ table.groupMemberCount }}</el-col>
          </el-row>
        </div>
      </div>
      <div class="dataall" style="margin-top:10px">
        <div style="text-align:left">
          <el-row type="flex" class="row-bg" justify="space-between">
            <el-col :span="10" class="title_name"
              >实时数据 <span class=" fontgay">更新于{{ uptime }}</span></el-col
            >
            <el-col :span="10" style="text-align:right">
              <el-radio-group v-model="timeType" style="margin-top: 20px;" @change="timeTypeCheck">
                <el-radio-button label="today">今日</el-radio-button>
                <el-radio-button label="week">本周</el-radio-button>
                <el-radio-button label="month">本月</el-radio-button>
                <el-radio-button label="reset"><i class="el-icon-refresh"> </i></el-radio-button>
              </el-radio-group>
            </el-col>
          </el-row>

          <el-row
            type="flex"
            class="row-bg"
            justify="space-between"
            style="margin-top:30px; text-align: center;"
          >
            <el-col :span="6">发起申请数</el-col>
            <el-col :span="6">新增客户数</el-col>
            <el-col :span="6">群新增人数</el-col>
            <el-col :span="6">流失客户数</el-col>
          </el-row>
          <el-row
            type="flex"
            class="row-bg"
            justify="space-between"
            style="margin-top:10px;font-size:35px;font-weight:bold;color: #0079DE; text-align: center;"
          >
            <el-col :span="6">{{ erchatsTable.newApplyCnt }}</el-col>
            <el-col :span="6">{{ erchatsTable.newContactCnt }}</el-col>
            <el-col :span="6">{{
              erchatsTable.newMemberCnt ? erchatsTable.newMemberCnt : 0
            }}</el-col>
            <el-col :span="6">{{ erchatsTable.negativeFeedbackCnt }}</el-col>
          </el-row>
          <el-row
            type="flex"
            class="row-bg"
            justify="space-between"
            style="margin-top:10px; text-align: center;"
          >
            <el-col
              :span="6"
              :class="{
                greenicon: Number(erchatsTable.newApplyCntDiff) >= 1,
                redicon: Number(erchatsTable.newApplyCntDiff) < 0
              }"
              >较{{ time }}
              <span>{{ erchatsTable.newApplyCntDiff }}</span>
            </el-col>
            <el-col
              :span="6"
              :class="{
                greenicon: Number(erchatsTable.newContactCntDiff) >= 1,
                redicon: Number(erchatsTable.newContactCntDiff) < 0
              }"
              >较{{ time }}
              <span>{{ erchatsTable.newContactCntDiff }}</span>
            </el-col>
            <el-col
              :span="6"
              :class="{
                greenicon: Number(erchatsTable.newMemberCntDiff) >= 1,
                redicon: Number(erchatsTable.newMemberCntDiff) < 0
              }"
              >较{{ time }}
              <span>{{ erchatsTable.newMemberCntDiff ? erchatsTable.newMemberCntDiff : 0 }}</span>
            </el-col>
            <el-col
              :span="6"
              :class="{
                greenicon: Number(erchatsTable.negativeFeedbackCntDiff) >= 1,
                redicon: Number(erchatsTable.negativeFeedbackCntDiff) < 0
              }"
              >较{{ time }}
              <span>{{ erchatsTable.negativeFeedbackCntDiff }}</span>
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="erchatsdiv" style="margin-top:10px">
        <el-row :gutter="20">
          <el-col :span="12" class="erchats">
            <el-row type="flex" class="row-bg" justify="space-between">
              <div id="fatherbox">
                <div id="main" ref="views"></div>
              </div>
            </el-row>
          </el-col>
          <el-col :span="12" class="erchats">
            <el-row type="flex" class="row-bg" justify="space-between">
              <div id="fatherbox">
                <div id="main2" ref="views"></div>
              </div>
            </el-row>
          </el-col>
          <el-col :span="12" class="erchats">
            <el-row type="flex" class="row-bg" justify="space-between">
              <div id="fatherbox">
                <div id="main3" ref="views"></div>
              </div>
            </el-row>
          </el-col>
          <el-col :span="12" class="erchats">
            <el-row type="flex" class="row-bg" justify="space-between">
              <div id="fatherbox">
                <div id="main4" ref="views"></div>
              </div>
            </el-row>
          </el-col>
        </el-row>
      </div>

      <!-- -->
    </div>
    <div class="index_r whitebg">
      <div class="qy_R">
        <div class="inedx_r_top_t">
          <p class="p1">{{ $store.state.user.companyName }}，欢迎登陆本系统</p>
          <p class="p2">登录时间：{{ parseTime(new Date()) }}</p>
          <p class="p1 p3 margint20">LinkWechat 企业微信SCRM</p>
          <p class="p2 p4">版本信息：开源 <span class="fr">可用期限：永久</span></p>
          <el-row
            :gutter="20"
            type="flex"
            class="row-bg"
            justify="center"
            style="text-align:center"
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
            style="color:#0079DE"
            @click="msgInfo('《更新日志》正在加急上线中，敬请关注…')"
            >更多</span
          >
          <ul>
            <li v-for="(index, i) in 3" :key="i" @click="msgInfo('暂无内容')">
              即将上线，敬请期待 <span class="fr">03-26</span>
            </li>
          </ul>
        </div>
        <div class="inedx_r_top_bottom">
          <span class="inedx_r_top_bottomp1"><img src="../assets/index/qyxy.png" /> 私域学院</span>
          <span class="fr" style="color:#0079DE">更多</span>
          <ul>
            <li v-for="(index, i) in 3" :key="i" @click="msgInfo('暂无内容')">
              即将上线，敬请期待 <span class="fr">03-26</span>
            </li>
          </ul>
        </div>
        <div class="inedx_r_top_bottom" style="text-align:center">
          <p style="text-align:left">
            <span class="inedx_r_top_bottomp1">
              <img src="../assets/index/kfq.png" /> 官方交流</span
            >
          </p>
          <img :src="bossImg" class="img" />
        </div>
        <div class="inedx_r_top_bottom" style="text-align:center">
          <p style="text-align:left">
            <span class="inedx_r_top_bottomp1">
              <img src="../assets/index/khq.png" /> 联系客服</span
            >
          </p>
          <img :src="bossImg" class="img" />
        </div>
      </div>
    </div>
    <div class="car">
      <el-row type="flex" class="row-bg" justify="space-between">
        <el-col
          :span="24"
          style="font-size: 24px; line-height:80px
"
          >功能直通车
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="4" v-for="(index, i) in car" :key="i">
          <div class="grid-content" @click="$router.push(index.url)">
            <span class="fl"> <img :src="index.img" alt="" /> </span
            ><span class="fl craname">{{ index.name }}</span>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
import * as content from '@/api/conversation/content.js'
import echarts from 'echarts'
export default {
  name: 'Index',
  components: {},
  data() {
    return {
      nowTime: new Date(),
      bossImg: require('@/assets/index/boss.png'),
      car: [
        {
          name: '客户管理',
          url: '/customerManage/customer',
          img: require('@/assets/index/kehgl.png')
        },
        {
          name: '内容存档',
          url: '/conversation/content',
          img: require('@/assets/index/neircd.png')
        },
        {
          name: '任务宝',
          url: '/appTool/task',
          img: require('@/assets/index/renwb.png')
        },
        {
          name: '消息群发',
          url: '/groupMessage/add',
          img: require('@/assets/index/xiaoxqf.png')
        },
        {
          name: '员工活码',
          url: '/drainageCode/staff',
          img: require('@/assets/index/yuanghm.png')
        },
        {
          name: '字典管理',
          url: '/system/dict',
          img: require('@/assets/index/zidgl.png')
        }
      ],
      table: {},
      erchatsTable: {},
      xAxis: ['1', '2', '3', '4', '5'],
      allData: {},
      time: '昨天',
      uptime: '',
      timeType: 'today',
      opinionData: {
        opinionData1: [],
        opinionData2: [],
        opinionData3: [],
        opinionData4: []
      }
    }
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
          break
      }
    },
    tableInfo() {
      content.indexTable().then(res => {
        this.table = res.data
      })
    },
    erchatInfo() {
      content.indexEchart().then(res => {
        this.allData = res.data
        this.uptime = res.data.updateTime
        this.erchatsTable = this.allData.today
        this.serErchat()
      })
    },
    serErchat() {
      let data = {
        arr1: [],
        arr2: [],
        arr3: [],
        arr4: [],
        time: []
      }
      this.erchatsTable.dataList.forEach(a => {
        data.arr1.push(a.newApplyCnt)
        data.time.push(a.xtime)
        data.arr3.push(a.newMemberCnt)
        data.arr2.push(a.newContactCnt)
        data.arr4.push(a.negativeFeedbackCnt)
      })
      this.xAxis = data.time
      this.resizeArr = []
      this.drawLine('main', ['发起申请数'], data.arr1, '#088AEE')
      this.drawLine('main2', ['新增客户数'], data.arr2, '#E74E59')
      this.drawLine('main3', ['群新增人数'], data.arr3, '#14BF48')
      this.drawLine('main4', ['流失客户数'], data.arr4, '#FA7216')
    },
    drawLine(id, arrData, data, color) {
      let obj = document.getElementById(id)
      obj.style.width = '100%'
      obj.style.height = '380px'
      let charts = echarts.init(obj)
      this.resizeArr.push(charts)

      let resize = function() {
        this.resizeArr.forEach(element => {
          element.resize()
        })
      }
      function debounce(method, context) {
        clearTimeout(method.tId)
        method.tId = setTimeout(function() {
          method.call(context)
        }, 100)
      }
      window.onresize = debounce.bind(this, resize, this)
      charts.setOption({
        color: [color],
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: arrData
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
          data: this.xAxis,
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
        series: [
          {
            name: arrData,
            type: 'line',
            stack: '总量',
            smooth: true,
            data: data,
            itemStyle: {
              normal: {
                lineStyle: {
                  color: color
                }
              }
            },
            textStyle: {
              color: '#fff' // 主标题文字的颜色。
            }
          }
        ]
      })
    }
  },
  //调用
  mounted() {
    this.erchatInfo()
    this.tableInfo()
  }
}
</script>
<style lang="scss" scoped>
.index {
  margin: 0;
  padding: 0.5% 1%;
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
    width: 100%;
    height: 380px;
    overflow-y: scroll;
  }

  #main {
    width: 100%;
    height: 380px;
  }

  .fr {
    float: right;
  }

  .redicon {
    color: #ff0000;
  }

  .greenicon {
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
    width: 75%;
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
    margin-left: 2%;

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
    height: 238px;
    background: #fff;
    border-radius: 5px;
    padding: 20px;
    font-size: 16px;
  }

  .erchatsdiv {
    width: 100%;
    height: auto;
    overflow: hidden;
    border-radius: 5px;
    font-size: 16px;

    /deep/ .el-col-12 {
      width: 49%;
    }

    .erchats {
      margin: 0 5px;
      height: 400px;
      border-radius: 4px;
      margin-bottom: 10px;
      background: #fff;
    }
  }

  .car {
    height: 200px;
    margin-top: 40px;
    width: 100%;
    overflow: hidden;
    line-height: 70px;

    .grid-content {
      overflow: hidden;
      height: 70px;
      background: #fff;
      border-radius: 5px;
      padding: 10px 30px;

      .craname {
        height: 50px;
        line-height: 50px;
        font-size: 16px;
        display: block;
        text-indent: 1em;
      }

      img {
        width: 30px;
        height: 30px;
      }
    }
  }
}
</style>
