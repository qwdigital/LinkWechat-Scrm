<template>
  <div class="index">
    <div class="index_l whitebg">
      <div class="box titlebox">
        <p class="adminname">{{getTimeState()}},Admin</p>
        <p>{{parseTime(nowTime)}}</p>
      </div>
      <div class="tables">
        <div style="text-align:left">
          <el-row type="flex" class="row-bg" justify="space-between">
            <el-col :span="24" style="font-weight:bold">实时数据</el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="space-between" style="margin-top:20px">
            <el-col :span="6">企业成员总数</el-col>
            <el-col :span="6">客户总人数</el-col>
            <el-col :span="6">客户群总数</el-col>
            <el-col :span="6">群成员总数</el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="space-between"
            style="margin-top:20px;font-size:35px;font-weight:bold">
            <el-col :span="6">{{table.customerCount}}</el-col>
            <el-col :span="6">{{table.groupMemberCount}}</el-col>
            <el-col :span="6">{{table.groupCount}}</el-col>
            <el-col :span="6">{{table.userCount}}</el-col>
          </el-row>
        </div>
      </div>
      <div class="dataall" style="margin-top:20px">
        <div style="text-align:left">
          <el-row type="flex" class="row-bg" justify="space-between">
            <el-col :span="24"  style="font-weight:bold">实时数据 <span class="fr fontgay">更新于{{uptime}}</span></el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="space-between">
            <el-col :span="24">
              <el-radio-group v-model="timeType" style="margin-top: 20px;" @change="timeTypeCheck">
                <el-radio-button label="day">今日</el-radio-button>
                <el-radio-button label="week">本周</el-radio-button>
                <el-radio-button label="month">本月</el-radio-button>
                <el-radio-button label="reset"><i class="el-icon-refresh"> </i></el-radio-button>
              </el-radio-group>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="space-between" style="margin-top:20px">
            <el-col :span="6">发起申请数</el-col>
            <el-col :span="6">新增客户数</el-col>
            <el-col :span="6">群新增人数</el-col>
            <el-col :span="6">流失客户数</el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="space-between"
            style="margin-top:20px;font-size:35px;font-weight:bold">
            <el-col :span="6">{{erchatsTable.newApplyCnt}}</el-col>
            <el-col :span="6">{{erchatsTable.newContactCnt}}</el-col>
            <el-col :span="6">{{erchatsTable.newMemberCnt?erchatsTable.newMemberCnt:0}}</el-col>
            <el-col :span="6">{{erchatsTable.negativeFeedbackCnt}}</el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="space-between" style="margin-top:20px">
            <el-col :span="6">比{{time}} <i
                :class="{'el-icon-top':Number(erchatsTable.newApplyCntDiff)>=1,'el-icon-bottom':Number(erchatsTable.newApplyCntDiff)<0,'redicon':Number(erchatsTable.newApplyCntDiff)>=1,'greenicon':Number(erchatsTable.newApplyCntDiff)<0}"></i>
              <span
                :class="{'redicon':Number(erchatsTable.newApplyCntDiff)>=1,'greenicon':Number(erchatsTable.newApplyCntDiff)<0}">{{erchatsTable.newApplyCntDiff}}</span>
            </el-col>
            <el-col :span="6">比{{time}} <i
                :class="{'el-icon-top':Number(erchatsTable.newContactCntDiff)>=1,'el-icon-bottom':Number(erchatsTable.newContactCntDiff)<0,'redicon':Number(erchatsTable.newContactCntDiff)>=1,'greenicon':Number(erchatsTable.newContactCntDiff)<0}"></i>
              <span
                :class="{'redicon':Number(erchatsTable.newContactCntDiff)>=1,'greenicon':Number(erchatsTable.newContactCntDiff)<0}">{{erchatsTable.newContactCntDiff}}</span>
            </el-col>
            <el-col :span="6">比{{time}} <i
                :class="{'el-icon-top':Number(erchatsTable.newMemberCntDiff)>=1,'el-icon-bottom':Number(erchatsTable.newMemberCntDiff)<0,'redicon':Number(erchatsTable.newMemberCntDiff)>=1,'greenicon':Number(erchatsTable.newMemberCntDiff)<0}"></i>
              <span
                :class="{'redicon':Number(erchatsTable.newMemberCntDiff)>=1,'greenicon':Number(erchatsTable.newMemberCntDiff)<0}">{{erchatsTable.newMemberCntDiff?erchatsTable.newMemberCntDiff:0}}</span>
            </el-col>
            <el-col :span="6">比{{time}} <i
                :class="{'el-icon-top':Number(erchatsTable.negativeFeedbackCntDiff)>=1,'el-icon-bottom':Number(erchatsTable.negativeFeedbackCntDiff)<0,'redicon':Number(erchatsTable.negativeFeedbackCntDiff)>=1,'greenicon':Number(erchatsTable.negativeFeedbackCntDiff)<0}"></i>
              <span
                :class="{'redicon':Number(erchatsTable.negativeFeedbackCntDiff)>=1,'greenicon':Number(erchatsTable.negativeFeedbackCntDiff)<0}">{{erchatsTable.negativeFeedbackCntDiff}}</span>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="space-between" style="margin-top:20px">
            <div id="fatherbox" >
               <div id="main"  ref="views"></div>
            </div>
          </el-row>
        </div>
      </div>
      <div class="car dataall">
        <div>
          <el-row type="flex" class="row-bg" justify="space-between">
            <el-col :span="24">功能直通车 </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="space-between" style="margin-top:20px;text-align:center" >
            <el-col :span="4" v-for="(index,i) in car" :key="i" >
              <div class="circle" @click="carLink(index)" >
                <img :src="index.img" alt="">
              </div>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="space-between" style="margin-top:20px" >
            <el-col :span="4" v-for="(index,i) in car" :key="i" style="text-align:center" @click="carLink(index)">{{index.name}}</el-col>
          </el-row>
        </div>
      </div>
    </div>
    <div class="index_r whitebg">
      <div class="inedx_r_top">
        <div class="inedx_r_top_header btmboder">
          <div class="inedx_r_top_t">LinkWeChat 企业微信 SCRM</div>
          <div class="inedx_r_top_bottom">
            <span class="fontgay">版本信息 </span> <span class="fr">
              <el-button type="primary" plain round>测试版</el-button>
            </span>
          </div>
        </div>
        <div class="inedx_r_top_bottom">
          <span class="fontgay">可有日期: </span> <span class="fr ">永久</span>
        </div>
      </div>
      <div class="twolink">
        <div class="twolinkbox" style="color: orange"><i class="el-icon-warning-outline"></i> <span>帮助文档</span></div>
        <div class="twolinkbox" style="float:right;color:#199ed8"><i class="el-icon-s-management"></i><span>开发文档</span> </div>
      </div>
      <div class="listcard">
        <div class="inedx_r_top_bottom">
          <span>更新日志</span> <span class="fr" style="color:#199ed8">更多</span>
        </div>
        <ul>
          <li>即将上线，敬请期待</li>
          <li>即将上线，敬请期待</li>
          <li>即将上线，敬请期待</li>
        </ul>
      </div>
      <div class="listcard">
        <div class="inedx_r_top_bottom">
          <span>企业学院</span> <span class="fr" style="color:#199ed8">更多</span>
        </div>
        <ul>
          <li>即将上线，敬请期待</li>
          <li>即将上线，敬请期待</li>
          <li>即将上线，敬请期待</li>
        </ul>
      </div>
      <div class="listcard">
        <div class="inedx_r_top_bottom">
          <span>开发群</span>
        </div>
        <div class="listcard_img">
          <img :src="bossImg" alt="">
        </div>
      </div>
      <div class="listcard">
        <div class="inedx_r_top_bottom">
          <span>客户群</span>
        </div>
           <div class="listcard_img">
          <img :src="bossImg" alt="">
        </div>
      </div>
    </div>
  </div>
</template>
<script>
var elementResizeDetectorMaker = require("element-resize-detector")
 import {
    parseTime
  } from '@/utils/common.js'
  import { arrData } from "@/utils/common.js";
  import {
    content
  } from '@/api/content.js'
  import echarts from 'echarts'
  export default {
    name: 'Index',
    components: {},
    data() {
      return {
       nowTime:new Date(),
       bossImg:require('@/assets/index/boss.png'),
        car: [{
          name: '客户管理',
          url: '/customerManage/customer',
          img:require('@/assets/index/customer.png')
        }, {
          name: '内容存档',
          url: '/conversation/content',
          img:require('@/assets/index/customer.png')
        }, {
          name: '任务宝',
          url: '/appTool/task',
          img:require('@/assets/index/rwb.png')
        }, {
          name: '消息群发',
          url: '/groupMessage/add',
          img:require('@/assets/index/xzqf.png')
        }, {
          name: '员工活码',
          url: '/drainageCode/staff',
          img:require('@/assets/index/yghm.png')
        }, {
          name: '字典管理',
          url: '/system/dict',
          img:require('@/assets/index/zdlq.png')
        }],
        table: {},
        erchatsTable: {},
        xAxis:["1", "2", "3", "4", "5"],
        allData: {},
        time: '昨天',
        uptime: '',
        timeType: 'day',
        charts: '',
        opinionData:{
          opinionData1:[],
          opinionData2:[],
          opinionData3:[],
          opinionData4:[]
        },
        charts:null
      }
    },
    methods: {
      carLink(e){
        this.$router.push(e.url)
      },
      getTimeState() {
        let timeNow = new Date();
        let hours = timeNow.getHours();
        let state = ``;
        if (hours >= 0 && hours <= 10) {
          state = `早上好!`;
        } else if (hours > 10 && hours <= 14) {
          state = `中午好!`;
        } else if (hours > 14 && hours <= 18) {
          state = `下午好!`;
        } else if (hours > 18 && hours <= 24) {
          state = `晚上好!`;
        }
        return state;
      },
      canvansData(){
           let bkdata=(arrData(this.erchatsTable.dataList))
           this.opinionData.opinionData1=bkdata.arr1;
           this.opinionData.opinionData2=bkdata.arr2;
           this.opinionData.opinionData3=bkdata.arr3;
           this.opinionData.opinionData3=[];
           this.opinionData.opinionData4=bkdata.arr4;
           this.xAxis=bkdata.btm1;
           this.drawLine('main','')
      },
      timeTypeCheck() {
        if (this.timeType == 'day') {
          this.time='昨天'
          this.erchatsTable = this.allData.today
         this.canvansData()
        } else if (this.timeType == 'week') {
           this.time='上周'
          this.erchatsTable = this.allData.week
           this.canvansData()
        } else if (this.timeType == 'month') {
           this.time='上月'
           this.erchatsTable = this.allData.month
           this.canvansData()
        } else if (this.timeType == 'reset') {
           this.time='昨天'
          this.timeType = 'day'
          this.erchatInfo()
        }
      },
      tableInfo() {
        content.indexTable().then(res => {
          this.table = res.data
          
        })
      },
      erchatInfo() {
        content.indexEchart().then(res => {
           this.allData = res.data;
           this.uptime =res.data.updateTime;
           this.erchatsTable = this.allData.today
           this.canvansData()
        })
      },
      drawLine(id,width) {
        let obj=document.getElementById(id)
        obj.style.width=width?`${width}px`:'100%';
        obj.style.height='500px';
        this.charts = echarts.init(obj)
        this.charts.setOption({
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['发起申请数','新增客户数','群新增人数','流失客户数']
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
            data: this.xAxis
          },
          yAxis: {
            type: 'value'
          },
          series: 
          [{
            name: '发起申请数',
            type: 'line',
            stack: '总量',
         smooth: true,
            data: this.opinionData.opinionData1
          },
          {
            name: '新增客户数',
            type: 'line',
            stack: '总量',
            smooth: true,
            data: this.opinionData.opinionData2
          },
          {
            name: '群新增人数',
            type: 'line',
            stack: '总量',
            smooth: true,
            data: this.opinionData.opinionData3
          },
          {
            name: '流失客户数',
            type: 'line',
            stack: '总量',
            smooth: true,
            data: this.opinionData.opinionData4
          }]
        })
      }
    },
    //调用
    mounted() {
      this.erchatInfo()
      this.tableInfo()
      var erd = elementResizeDetectorMaker()
      let that=this
       erd.listenTo(document.getElementById("fatherbox"), function (element) {
      var width = element.offsetWidth
      var height = element.offsetHeight
        that.$nextTick(function () {
         this.drawLine('main',width)
      })
    })
    }
  }

</script>
<style lang="scss" scoped>
  .index {
    margin: 0;
    padding: 2% 1%;
    width: 100%;
    background: #f1f1f1;
    overflow: hidden;
    .fontgay {
      color: #999;
      font-size: 14px;
    }
    #fatherbox{ width: 100%;height: 500px; overflow-y: scroll;}
    #main{ width: 100%;height: 500px; 
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
      height: 150px;
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
      img{
        border-radius: 50%; 
      width: 80px;
      height: 80px;
      }
    }

    .index_r {
      width: 23%;
      margin-left: 2%;

      .listcard {
        width: 100%;
        background: #fff;
        overflow: hidden;
        border-radius: 5px;
        margin-top: 20px;
        padding: 0 15px;

        ul {
          padding: 0;
        }

        ul li {
          line-height: 40px;
          text-indent: 12px;
        }
       .listcard_img{
         width: 100%;
         height: 220px;
         text-align: center;
         img{
           max-width: 200px;
           max-height: 200px;
         }
       }
        .inedx_r_top_bottom {
          height: 60px;
          line-height: 60px;
          padding: 0 15px;
          font-weight: bold;
        }
      }

      .inedx_r_top {
        width: 100%;
        background: #fff;
        height: 220px;
        border-radius: 5px;

        .inedx_r_top_header {
          height: 160px;
          width: 100%;
          color: #999;
        }

        .inedx_r_top_t {
         height: 100px;
         width: 100%;
         line-height: 100px;
         padding: 0 15px;
         word-break: break-all;
         font-weight: bold;
         font-size: 16px;
         color: #199ed8;
        }

        .inedx_r_top_bottom {
          height: 60px;
          line-height: 60px;
          padding: 0 15px;
        }

        .btmboder {
          border-bottom: 1px solid #efefef;
        }
      }

      .twolink {
        width: 100%;
        height: 60px;
        line-height: 60px;
        margin-top: 20px;
        font-size: 16px;
        cursor: pointer;

        .twolinkbox {
          span{text-indent: 10px;}
          width: 48%;
          float: left;
          background: #fff;
          text-align: center;
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
      height: 800px;
      background: #fff;
      border-radius: 5px;
      padding: 20px;
      font-size: 16px;
    }

    .car {
      height: 200px;
      margin-top: 20px;
    }
  }

</style>
