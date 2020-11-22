<script>
import { getDetail } from '@/api/drainageCode/staff'
import { download, downloadBatch } from '@/api/common'
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
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        operName: undefined,
        businessType: undefined,
        status: undefined,
      },
      type: { 1: '单人', 2: '多人', 3: '批量单人' },
    }
  },
  created() {
    let id = this.$route.query.id
    id && this.getDetail(id)
    // this.getList()
  },
  mounted() {
    var clipboard = new ClipboardJS('.copy-btn')
    clipboard.on('success', (e) => {
      this.$notify({
        title: '成功',
        message: '链接已复制到剪切板，可粘贴。',
        type: 'success',
      })
    })
    clipboard.on('error', (e) => {
      this.$message.error('链接复制失败')
    })
    var option = {
      xAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
      },
      yAxis: {
        type: 'value',
      },
      series: [
        {
          data: [820, 932, 901, 934, 1290, 1330, 1320],
          type: 'line',
        },
      ],
    }
    var myChart = echarts.init(this.$refs.chart)
    myChart.setOption(option)
  },
  methods: {
    /** 获取详情 */
    getDetail(id) {
      this.loading = true
      getDetail(id).then(({ data }) => {
        this.form = data
        this.loading = false
      })
    },
    /**  */
    getList() {
      this.loading = false
      list(this.addDateRange(this.queryParams, this.dateRange)).then(
        (response) => {
          this.list = response.rows
          this.total = response.total
          this.loading = false
        }
      )
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const operIds = row.operId || this.ids
      this.$confirm(
        '是否确认删除日志编号为"' + operIds + '"的数据项?',
        '警告',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
      )
        .then(function() {
          return delOperlog(operIds)
        })
        .then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        })
        .catch(function() {})
    },
  },
}
</script>

<template>
  <div v-loading="loading">
    <div>员工活码信息</div>
    <div class="flex top">
      <div>
        <el-image
          :src="form.qrCode"
          fit="fit"
          style="width: 100px; height: 100px"
        ></el-image>
        <div>
          <el-button type="text" @click="downloadBatch(row.id)"
            >下载二维码</el-button
          >
          <el-button
            type="text"
            class="copy-btn"
            :data-clipboard-text="form.qrCode"
            >复制链接</el-button
          >
        </div>
      </div>
      <el-form ref="form" label-width="100px">
        <el-form-item label="使用成员：">
          <el-tag
            size="medium"
            v-for="(item, index) in form.weEmpleCodeUseScops"
            :key="index"
            >{{ item.userUserName }}</el-tag
          >
        </el-form-item>
        <el-form-item label="活动场景：">{{ form.activityScene }}</el-form-item>
        <el-form-item label="类型：">{{ type[form.codeType] }}</el-form-item>
        <el-form-item label="设置：">{{
          `客户添加时${
            form.isJoinConfirmFriends === 1 ? '无' : ''
          }需经过确认自动成为好友`
        }}</el-form-item>
        <el-form-item label="创建人：">{{ form.createBy }}</el-form-item>
        <el-form-item label="创建时间：">{{ form.createTime }}</el-form-item>
      </el-form>

      <el-form ref="form" label-width="100px">
        <el-form-item label="扫码标签："
          ><el-tag
            size="medium"
            v-for="(item, index) in form.weEmpleCodeTags"
            :key="index"
            >{{ item.tagName }}</el-tag
          ></el-form-item
        >
        <el-form-item label="欢迎语：">{{ form.welcomeMsg }}</el-form-item>
      </el-form>
    </div>
    <el-divider></el-divider>
    <div>扫码人数</div>
    <div>累计总人数：0</div>
    <div>
      <el-button-group>
        <el-button type="primary">近7日</el-button>
        <el-button type="primary">近30日</el-button>
      </el-button-group>

      <el-date-picker
        v-model="dateRange"
        size="small"
        style="width: 240px"
        value-format="yyyy-MM-dd"
        type="daterange"
        range-separator="-"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
      ></el-date-picker>
    </div>
    <div class="chart" ref="chart" style="width: 600px;height:400px;"></div>
  </div>
</template>

<style lang="scss" scoped>
.top {
  align-items: flex-start;
}
</style>
