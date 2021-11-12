<script>
import { getDetail, getUserAddCustomerStat, download } from '@/api/drainageCode/staff'
import ClipboardJS from 'clipboard'
import echarts from 'echarts'
export default {
  name: 'CodeDetail',
  data() {
    return {
      // 遮罩层
      loading: false,
      // 选中数组
      ids: [],
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 日期范围
      dateRange: [],
      // 表单参数
      form: {},
      // 查询参数
      query: {
        codeId: undefined,
        // addWay: undefined,
        beginTime: undefined,
        endTime: undefined
      },
      type: { 1: '单人', 2: '多人', 3: '批量单人' },
      timeRange: 7
    }
  },
  created() {
    let id = (this.query.codeId = this.$route.query.id)
    if (id) {
      this.getDetail(id)
      this.setTime(7)
      this.getList()
    }
  },
  mounted() {
    this.clipboard = new ClipboardJS('.copy-btn')
    this.clipboard.on('success', (e) => {
      this.$notify({
        title: '成功',
        message: '链接已复制到剪切板，可粘贴。',
        type: 'success'
      })
    })
    this.clipboard.on('error', (e) => {
      this.$message.error('链接复制失败')
    })
  },
  destroyed() {
    this.clipboard.destroy()
  },
  methods: {
    /** 获取详情 */
    getDetail(id) {
      this.loading = true
      getDetail(id).then(({ data }) => {
        this.form = data
        this.loading = false
        // this.query.userId = data.weEmpleCodeUseScops[0].businessId
        // this.query.addWay = 1
      })
    },
    /**  */
    getList() {
      this.query.beginTime = this.dateRange[0]
      this.query.endTime = this.dateRange[1]
      getUserAddCustomerStat(this.query).then(({ data }) => {
        this.total = data.total
        this.loading = false

        let option = {
          xAxis: {
            type: 'category',
            data: data.dateList
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              data: data.statList,
              type: 'line'
            }
          ]
        }
        var myChart = echarts.init(this.$refs.chart)
        myChart.setOption(option)
        this.$nextTick(() => {})
      })
    },
    setTime(days) {
      this.timeRange = days
      let date = new Date()
      date.setDate(date.getDate() - days)
      this.dateRange = [this.getTime(date), this.getTime()]
      this.getList()
    },
    getHandledValue(num) {
      return num < 10 ? '0' + num : num
    },
    getTime(datePar) {
      const d = datePar ? new Date(datePar) : new Date()
      const year = d.getFullYear()
      const month = this.getHandledValue(d.getMonth() + 1)
      const date = this.getHandledValue(d.getDate())
      return year + '-' + month + '-' + date
    },
    download() {
      let userName = this.form.weEmpleCodeUseScops
        .map((item) => {
          return item.businessName
        })
        .join(',')
      let name = userName + '-' + this.form.activityScene + '.png'
      download(this.form.id).then((res) => {
        if (res != null) {
          let blob = new Blob([res], { type: 'application/zip' })
          let url = window.URL.createObjectURL(blob)
          const link = document.createElement('a') // 创建a标签
          link.href = url
          link.download = name // 重命名文件
          link.click()
          URL.revokeObjectURL(url) // 释放内存
        }
      })
    }
  }
}
</script>

<template>
  <div v-loading="loading">
    <div class="title">员工活码信息</div>
    <div class="flex top">
      <div class="ac">
        <el-image
          :src="form.qrCode"
          fit="fit"
          :preview-src-list="[form.qrCode]"
          style="width: 150px; height: 150px"
        ></el-image>
        <div>
          <el-button type="text" @click="download()">下载二维码</el-button>
          <el-button type="text" class="copy-btn" :data-clipboard-text="form.qrCode">复制链接</el-button>
        </div>
      </div>
      <el-form ref="form" label-width="100px">
        <el-form-item label="使用成员：">
          <el-tag size="small" v-for="(item, index) in form.weEmpleCodeUseScops" :key="index">{{
            item.businessName
          }}</el-tag>
        </el-form-item>
        <el-form-item label="活动场景：">{{ form.activityScene }}</el-form-item>
        <el-form-item label="类型：">{{ type[form.codeType] }}</el-form-item>
        <el-form-item label="设置：">{{
          `客户添加时${form.isJoinConfirmFriends === 1 ? '无' : ''}需经过确认自动成为好友`
        }}</el-form-item>
        <el-form-item label="创建人：">{{ form.createBy }}</el-form-item>
        <el-form-item label="创建时间：">{{ form.createTime }}</el-form-item>
      </el-form>

      <el-form ref="form" label-width="100px">
        <el-form-item label="扫码标签："
          ><el-tag size="small" v-for="(item, index) in form.weEmpleCodeTags" :key="index">{{
            item.tagName
          }}</el-tag></el-form-item
        >
        <el-form-item label="欢迎语：">{{ form.welcomeMsg }}</el-form-item>
      </el-form>
    </div>
    <el-divider></el-divider>
    <div class="title">扫码人数</div>
    <div class="mb15">累计总人数：{{ total }}</div>
    <div>
      <el-button-group>
        <el-button size="small" type="primary" :plain="timeRange != 7" @click="setTime(7)">近7日</el-button>
        <el-button size="small" type="primary" :plain="timeRange != 30" @click="setTime(30)">近30日</el-button>
      </el-button-group>

      <el-date-picker
        v-model="dateRange"
        size="small"
        class="ml20"
        style="width: 260px"
        value-format="yyyy-MM-dd"
        type="daterange"
        :clearable="false"
        range-separator="-"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        @change="getList"
      ></el-date-picker>
    </div>
    <div class="chart" ref="chart" style="width: 90%;height:400px;"></div>
  </div>
</template>

<style lang="scss" scoped>
.top {
  align-items: flex-start;
}
.title {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 20px;
}
</style>
