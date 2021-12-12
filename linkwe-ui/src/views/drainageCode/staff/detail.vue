<template>
  <div>
    <el-row style="margin-top: 10px;" type="flex" :gutter="10">
      <el-col>
        <div class="g-card">
          <div class="card-title g-pad20">
            基本信息
          </div>
          <div class="my-divider"></div>
          <div class="g-pad20">
            <el-form label-position="right" label-width="100px">
              <el-form-item style="margin-bottom: 10px" label="活码名称">{{form.name}}</el-form-item>
              <el-form-item style="margin-bottom: 10px" label="活码分组">{{form.qrGroupName}}</el-form-item>
              <el-form-item style="margin-bottom: 10px" label="自动通过标签">
                <el-switch :value="form.autoAdd" :active-value=1 :inactive-value=0 disabled></el-switch>
              </el-form-item>
              <el-form-item style="margin-bottom: 10px" label="新客户标签">
                <el-tag v-for="(data, key) in form.qrTags" :key="key">{{data.tagName}}</el-tag>
              </el-form-item>
            </el-form>
          </div>
          <div class="card-title g-pad20">
            活码员工
          </div>
          <div class="my-divider"></div>
          <!-- <template v-if="form.ruleType === 1">
            <div class="g-pad20">
              <div class="content g-pad20">
                <el-form label-position="right" label-width="100px">
                  <el-form-item style="margin-bottom: 10px" label="排班员工">
                    <el-tag v-for="(data, key) in form.qrUserInfos" :key="key">{{data.businessName}}</el-tag>
                  </el-form-item>
                  <el-form-item style="margin-bottom: 10px" label="工作周期">
                    <el-checkbox-group v-model="checkList">
                      <el-checkbox label="周一" disabled></el-checkbox>
                      <el-checkbox label="周二" disabled></el-checkbox>
                      <el-checkbox label="周三" disabled></el-checkbox>
                      <el-checkbox label="周四" disabled></el-checkbox>
                      <el-checkbox label="周五" disabled></el-checkbox>
                      <el-checkbox label="周六" disabled></el-checkbox>
                      <el-checkbox label="周日" disabled></el-checkbox>
                    </el-checkbox-group>
                  </el-form-item>
                  <el-form-item style="margin-bottom: 10px" label="在线时间">
                    <el-time-select disabled value="00:00" placeholder="任意时间点">
                    </el-time-select>
                    <el-time-select arrow-control disabled value="23:59" placeholder="任意时间点">
                    </el-time-select>
                  </el-form-item>
                </el-form>
              </div>
            </div>
          </template>-->

          <template>
            <div class="g-pad20" v-for="(unit, index) in form.qrUserInfos" :key="index">
              <div class="content g-pad20">
                <el-form label-position="right" label-width="100px">
                  <el-form-item style="margin-bottom: 10px" label="排班员工">
                    <el-tag v-for="(data, key) in unit.weQrUserList" :key="key">{{data.userName}}</el-tag>
                  </el-form-item>
                  <el-form-item style="margin-bottom: 10px" label="工作周期">
                    <el-checkbox-group v-if="unit.type === 0" v-model="checkList">
                      <el-checkbox label="周一" disabled></el-checkbox>
                      <el-checkbox label="周二" disabled></el-checkbox>
                      <el-checkbox label="周三" disabled></el-checkbox>
                      <el-checkbox label="周四" disabled></el-checkbox>
                      <el-checkbox label="周五" disabled></el-checkbox>
                      <el-checkbox label="周六" disabled></el-checkbox>
                      <el-checkbox label="周日" disabled></el-checkbox>
                    </el-checkbox-group>
                    <el-checkbox-group v-else v-model="unit.weekday">
                      <el-checkbox label="1" disabled>周一</el-checkbox>
                      <el-checkbox label="2" disabled>周二</el-checkbox>
                      <el-checkbox label="3" disabled>周三</el-checkbox>
                      <el-checkbox label="4" disabled>周四</el-checkbox>
                      <el-checkbox label="5" disabled>周五</el-checkbox>
                      <el-checkbox label="6" disabled>周六</el-checkbox>
                      <el-checkbox label="7" disabled>周日</el-checkbox>
                    </el-checkbox-group>
                  </el-form-item>
                  <el-form-item v-if="unit.type === 0" v-model="checkList" style="margin-bottom: 10px" label="在线时间">
                    <el-time-select disabled value="00:00" placeholder="任意时间点">
                    </el-time-select>
                    <el-time-select disabled value="23:59" placeholder="任意时间点">
                    </el-time-select>
                  </el-form-item>
                  <el-form-item v-else style="margin-bottom: 10px" label="在线时间">
                    <el-time-select disabled :value="unit.beginTime" placeholder="任意时间点">
                    </el-time-select>
                    <el-time-select disabled :value="unit.endTime" placeholder="任意时间点">
                    </el-time-select>
                  </el-form-item>
                </el-form>
              </div>
            </div>
          </template>
          <div class="card-title g-pad20">
            活码统计
          </div>
          <div class="my-divider"></div>
          <div class="g-pad20">
            <div class="total">
              <div class="total-item g-pad20">
                <div class="name">
                  今日扫码次数
                </div>
                <div class="value">
                  {{currentTotal}}
                </div>
              </div>
              <div class="total-item g-pad20" style="margin-left: 20px;">
                <div class="name">
                  累计扫码次数
                </div>
                <div class="value">
                  {{total}}
                </div>
              </div>
            </div>
            <div class="operation">
              <el-button-group>
                <el-button size="small" type="primary" :plain="timeRange != 7" @click="setTime(7)">近7日</el-button>
                <el-button size="small" type="primary" :plain="timeRange != 30" @click="setTime(30)">近30日
                </el-button>
              </el-button-group>
              <el-date-picker v-model="dateRange" size="small" class="ml20" style="width: 260px" value-format="yyyy-MM-dd" type="daterange" :clearable="false" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" @change="getList"></el-date-picker>
            </div>
          </div>
          <div ref="chart" id="mychart" style="width:100%;height:400px;"></div>
        </div>
      </el-col>
      <el-col style="width: 350px;">
        <div style="height:100%;display: flex;flex-direction: column;">
          <div class="g-card">
            <div class="right-title g-pad20">
              <div class="right-title-name">
                员工活码
              </div>
              <div class="right-title-edit" @click="editFn">
                <!-- <img style="margin-right:5px;height: 12px;width: 12px;" :src="require('@/assets/drainageCode/code-add.png')" alt=""> -->
                编辑
              </div>
            </div>
            <div class="g-pad20">
              <div class="code-show">
                <el-image :src="form.qrCode" fit="fit" :preview-src-list="[form.qrCode]" style="width: 124px; height: 124px"></el-image>
                <div>
                  <el-button type="text" @click="download()">下载二维码</el-button>
                </div>
              </div>
            </div>
          </div>
          <div class="g-card g-pad20" style="flex: 1;">
            <preview-client :list="form"></preview-client>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>

</template>

<script>
  import echarts from 'echarts'
  import PreviewClient from '@/components/previewInMobileClient.vue'
  import {
    getDetail,
    getTotal,
    downloadBatch
  } from '@/api/drainageCode/staff'

  import ClipboardJS from 'clipboard'

  export default {
    name: 'CodeDetail',
    components: {
      PreviewClient
    },
    data () {
      return {
        form: {},
        checkList: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
        // 遮罩层
        loading: false,
        // 选中数组
        ids: [],
        // 总条数
        total: 0,
        currentTotal: 0,
        // 表格数据
        list: [],
        // 日期范围
        dateRange: [],
        // 表单参数
        form: {},
        // 查询参数
        query: {
          qrId: '',
          beginTime: undefined,
          endTime: undefined
        },
        type: {
          1: '单人',
          2: '多人',
          3: '批量单人'
        },
        timeRange: 7
      }
    },
    created () {
      let id = this.$route.query.id
      this.query.qrId = id
      id && this.getDetail(id)
      // this.getList()
    },
    mounted () {
      var clipboard = new ClipboardJS('.copy-btn')
      clipboard.on('success', (e) => {
        this.$notify({
          title: '成功',
          message: '链接已复制到剪切板，可粘贴。',
          type: 'success'
        })
      })
      clipboard.on('error', (e) => {
        this.$message.error('链接复制失败')
      })
    },
    methods: {
      editFn () {
        this.$router.push({
          path: '/drainageCode/' + 'staffAdd',
          query: {
            id: this.form.id
          }
        })
      },
      /** 获取详情 */
      getDetail (id) {
        this.loading = true
        getDetail(id).then(res => {
          this.form = res.data
          if (this.form.qrAttachments && this.form.qrAttachments.length) {
            this.form.welcomeMsg = this.form.qrAttachments[0].content
            this.form.materialMsgList = this.form.qrAttachments.slice(1)
          }
          this.form.qrUserInfos.forEach(dd => {
            if (dd.workCycle) {
              dd.weekday = dd.workCycle.split(',')
            } else {
              dd.weekday = []
            }
          })
          this.loading = false
          this.setTime(7)
        })
      },
      getList () {
        this.query.beginTime = this.dateRange[0]
        this.query.endTime = this.dateRange[1]
        getTotal(this.query).then(({
          data
        }) => {
          this.total = data.total
          this.currentTotal = data.today
          let option = {
            xAxis: {
              type: 'category',
              data: data.xaxis
            },
            yAxis: {
              type: 'value'
            },
            series: [{
              data: data.yaxis,
              type: 'line'
            }]
          }
          var myChart = echarts.init(document.getElementById('mychart'))
          myChart.setOption(option)
          this.$nextTick(() => {
            myChart.resize()
          })
        })
      },
      setTime (days) {
        this.timeRange = days
        let date = new Date()
        date.setDate(date.getDate() - days)
        this.dateRange = [this.getTime(date), this.getTime()]
        this.getList()
      },
      getHandledValue (num) {
        return num < 10 ? '0' + num : num
      },
      getTime (datePar) {
        const d = datePar ? new Date(datePar) : new Date()
        const year = d.getFullYear()
        const month = this.getHandledValue(d.getMonth() + 1)
        const date = this.getHandledValue(d.getDate())
        return year + '-' + month + '-' + date
      },
      downloadByBlob (url, name) {
        let image = new Image()
        image.setAttribute('crossOrigin', 'anonymous')
        image.src = url
        image.onload = () => {
          let canvas = document.createElement('canvas')
          canvas.width = image.width
          canvas.height = image.height
          let ctx = canvas.getContext('2d')
          ctx.drawImage(image, 0, 0, image.width, image.height)
          canvas.toBlob((blob) => {
            let url = URL.createObjectURL(blob)
            this.downloadFn(url, name)
            // 用完释放URL对象
            URL.revokeObjectURL(url)
          })
        }
      },
      downloadFn (href, name) {
        let eleLink = document.createElement('a')
        eleLink.download = name
        eleLink.href = href
        eleLink.click()
        eleLink.remove()
      },
      download () {
        let name = this.form.name + '.png'
        this.downloadByBlob(this.form.qrCode, name)
      }
    }
  }
</script>


<style lang="scss" scoped>
  .top {
    align-items: flex-start;
  }

  .title {
    font-size: 16px;
    font-weight: 600;
    margin: 0 0 20px;
  }

  .crumb {
    font-size: 12px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #666666;
    display: flex;
  }

  .crumb- {
    // 一级 页面标题
    &title {
      display: flex;
      flex-direction: column;
      justify-content: center;
      height: 90px; // line-height: 90px;
      font-size: 18px;
      font-weight: 500;
      color: #333;
      padding: 0 20px;
      background: #fff;
      border-top-left-radius: 4px;
      border-top-right-radius: 4px;
    }
  }

  .card-title {
    font-size: 16px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #333333;
  }

  .my-divider {
    display: block;
    height: 1px;
    width: 100%;
    background-color: #dcdfe6;
  }

  .content {
    margin-top: 20px;
    border-radius: 4px;
    border: 1px solid #f1f1f1;
    &:first-child {
      margin-top: 0;
    }
  }

  .total {
    margin-bottom: 20px;
    display: flex;
    .total-item {
      width: 200px;
      height: 90px;
      background: linear-gradient(90deg, #f5f8fe 0%, #ebf3fd 100%);
      border-radius: 4px;
      font-weight: 400;
      color: #333333;
      .value {
        margin-top: 5px;
        font-size: 18px;
        font-family: JDZhengHT-EN-Regular, JDZhengHT-EN;
      }
      .name {
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
      }
    }
  }

  .operation {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .right-title {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .right-title-name {
      font-size: 16px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #333333;
    }
    .right-title-edit {
      font-size: 12px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #3c88f0;
      display: flex;
      align-items: center;
      cursor: pointer;
      &:hover {
        opacity: 0.8;
      }
    }
  }

  .code-show {
    text-align: center; // .download-btn {
    // 	font-size: 12px;
    // 	font-family: PingFangSC-Regular, PingFang SC;
    // 	font-weight: 400;
    // 	color: #3C88F0;
    // }
  }
</style>
