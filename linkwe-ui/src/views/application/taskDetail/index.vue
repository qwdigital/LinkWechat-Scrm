<script>
import * as api from "@/api/task";
import echarts from 'echarts'

export default {
    name: 'taskDetail',
    data () {
        return {
            query: {
                pageNum: 1,
                pageSize: 10,
                name: "", // "客户名称",
                beginTime: "", // "开始时间",
                endTime: "" // "结束时间"
            },
            dateRange: [],
            tableData: [],
            dateSelect: '近7日',
            dateRange: [],
            taskDetail: {}
        }
    },
    created () {
        this.getList(this.$route.query.id)
    },
    mounted () {
        this.initChart()
    },
    methods: {
        getList (id) {
            api.getTaskDetail(id)
                .then(res => {
                    this.taskDetail = res.data
                })
        },
        resetForm () {

        },
        exportCustomer () {

        },
        dateChange (event) {
            console.log(event)
        },
        initChart () {
            this.chartLine = echarts.init(document.getElementById('chartLineBox'));
            // 指定图表的配置项和数据
            let option = {
                tooltip: {              //设置tip提示
                    trigger: 'axis'
                },

                legend: {               //设置区分（哪条线属于什么）
                    data: ['平均成绩', '学生成绩']
                },
                color: ['#8AE09F', '#FA6F53'],       //设置区分（每条线是什么颜色，和 legend 一一对应）
                xAxis: {                //设置x轴
                    type: 'category',
                    boundaryGap: false,     //坐标轴两边不留白
                    data: ['2019-1-1', '2019-2-1', '2019-3-1', '2019-4-1', '2019-5-1', '2019-6-1', '2019-7-1',],
                    name: 'DATE',           //X轴 name
                    nameTextStyle: {        //坐标轴名称的文字样式
                        color: '#FA6F53',
                        fontSize: 16,
                        padding: [0, 0, 0, 20]
                    },
                    axisLine: {             //坐标轴轴线相关设置。
                        lineStyle: {
                            color: '#FA6F53',
                        }
                    }
                },
                yAxis: {
                    name: 'SALES VOLUME',
                    nameTextStyle: {
                        color: '#FA6F53',
                        fontSize: 16,
                        padding: [0, 0, 10, 0]
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#FA6F53',
                        }
                    },
                    type: 'value'
                },
                series: [
                    {
                        name: '平均成绩',
                        data: [220, 232, 201, 234, 290, 230, 220],
                        type: 'line',               // 类型为折线图
                        lineStyle: {                // 线条样式 => 必须使用normal属性
                            normal: {
                                color: '#8AE09F',
                            }
                        },
                    },
                    {
                        name: '学生成绩',
                        data: [120, 200, 150, 80, 70, 110, 130],
                        type: 'line',
                        lineStyle: {
                            normal: {
                                color: '#FA6F53',
                            }
                        },
                    }
                ]

            }
            this.chartLine.setOption(option)
        }
    }
}
</script>

<template>
    <div class="task-detail-container">
        <div class="task-detail-model task-detail-left">
            <h4 class="title">任务活动信息</h4>
            <div class="task-row">
                <div class="task-label" style="margin-bottom:30px">创建人:</div>
                <div class="task-value">{{taskDetail.createBy}}</div>
            </div>
            <div class="task-row">
                <div class="task-label">活动名称:</div>
                <div class="task-value">{{taskDetail.taskName}}</div>
            </div>
            <div class="task-row">
                <div class="task-label">裂变引导语:</div>
                <div class="task-value">{{taskDetail.fissInfo}}</div>
            </div>
            <div class="task-row">
                <div class="task-label">裂变客户数量:</div>
                <div class="task-value">{{taskDetail.fissNum}}人</div>
            </div>
            <div class="task-row" style="margin-bottom:30px">
                <div class="task-label">活动时间:</div>
                <div class="task-value">{{taskDetail.startTime}}至{{taskDetail.overTime}}</div>
            </div>
            <div class="task-row">
                <div class="task-label">活动发起成员:</div>
                <div class="task-value"></div>
            </div>
            <div class="task-row" style="margin-bottom:30px">
                <div class="task-label">客户标签:</div>
                <div class="task-value">{{taskDetail.customerTag}}</div>
            </div>
            <div class="task-row" >
                <div class="task-label">裂变海报:</div>
                <img style="width:30px" :src="taskDetail.postersUrl"/>
            </div>
            <div class="task-row" style="margin-bottom:30px">
                <div class="task-label">员工:</div>
                <div class="task-value">{{taskDetail.fissionTarget}}</div>
            </div>

            <div class="task-row">
                <div class="task-label">兑奖链接:</div>
                <div class="task-value">{{taskDetail.rewardUrl}}</div>
            </div>
            <div class="task-row" style="margin-bottom:30px">
                <div class="task-label">兑奖图片:</div>
                <img style="width:30px" :src="taskDetail.rewardImageUrl"/>
            </div>
              <div class="task-row">
                <div class="task-label">新客欢迎语:</div>
                <div class="task-value">{{taskDetail.welcomeMsg}}</div>
            </div>
        </div>
        <div class="task-detail-model task-detail-right">
            <h4 class="title">关键数据</h4>
            <div class="dateRang">
                <el-radio-group
                    @change='dateChange'
                    v-model="dateSelect"
                >
                    <el-radio-button label="近7日"></el-radio-button>
                    <el-radio-button label="近30日"></el-radio-button>
                </el-radio-group>
                <el-date-picker
                    v-model="dateRange"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    align="right"
                ></el-date-picker>
            </div>
            <div
                id="chartLineBox"
                style="width: 100%;height: 354px;"
            > </div>
        </div>
    </div>
</template>

<style lang="scss" scoped>
.task-detail-container {
    display: flex;
    justify-content: space-between;
    background: #efeff4;
    height: calc(100vh - 84px);
    box-sizing: border-box;
    padding: 20px 10px;
    .task-detail-model {
        width: 48%;
        height: 100%;
        box-sizing: border-box;
        background: #fff;
        padding: 10px 20px;
        .task-row {
            display: flex;
            color: #606266;
            font-size: 14px;
            margin-bottom: 10px;
        }
        .task-label {
            width: 100px;
            text-align: right;
            margin-right: 10px;
        }
        .title {
            color: #303133;
            text-align: left;
        }
        .dateRang {
            display: flex;
            justify-content: space-between;
            flex-wrap: wrap;
        }
        #chartLineBox {
            margin-top: 10px;
        }
    }
}
</style>
