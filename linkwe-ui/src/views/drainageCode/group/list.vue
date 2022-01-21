<script>
import RealCode from './realCode'
import { getList, remove, downloadBatch, download } from '@/api/drainageCode/group'
import ClipboardJS from 'clipboard'

export default {
  components: { RealCode },
  data() {
    return {
      query: {
        pageNum: 1,
        pageSize: 10,
        activityName: '',
        createBy: '',
        beginTime: '',
        endTime: ''
      },
      loading: false,
      searchDate: '', // 查询日期
      multiGroupCode: [], // 多选数据
      groupCodes: [], // 群活码数据
      total: 0, // 总数据量
      realCodeDialog: false, // 实际群码总数dialog
      openGroupCodeId: null, // 打开实际群码关联的群活码ID
      openGroupCodeStatus: -1, // 打开实际群码的检索状态
      clipboard: null // 拷贝对象
    }
  },
  watch: {
    searchDate(dateRange) {
      if (!dateRange || dateRange.length !== 2) {
        this.query.beginTime = ''
        this.query.endTime = ''
      } else {
        ;[this.query.beginTime, this.query.endTime] = dateRange
      }
    },
    // 如果实际群码弹出框关闭,刷新数据
    realCodeDialog(val) {
      if (val === false) this.getGroupCodes()
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
  created() {
    this.getGroupCodes()

    this.$store.dispatch(
      'app/setBusininessDesc',
      `
        <div>群活码原理为把多个群二维码统一为固定的活码，群满自动切换新群，无需手动更新二维码，并未改变微信原有入群规则。</div>
      `
    )
  },
  destroyed() {
    this.clipboard.destroy()
  },
  methods: {
    // 获取活码数据
    getGroupCodes() {
      const params = Object.assign({}, this.query)

      this.loading = true

      getList(params)
        .then((res) => {
          if (res.code === 200) {
            this.groupCodes = res.rows
            this.total = parseInt(res.total)
          }
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    },
    // 查询
    handleSearch() {
      this.getGroupCodes()
    },
    // 搜索栏清空
    resetQuery() {
      this.searchDate = ''
      this.$refs['queryForm'].resetFields()

      this.$nextTick(() => {
        this.getGroupCodes()
      })
    },
    // 批量下载
    handleBulkDownload() {
      const ids = this.multiGroupCode.map((group) => group.id)

      this.$confirm('是否确认下载所有图片吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          return downloadBatch(ids + '')
        })
        .then((res) => {
          if (res != null) {
            let blob = new Blob([res], { type: 'application/zip' })
            let url = window.URL.createObjectURL(blob)
            const link = document.createElement('a') // 创建a标签
            link.href = url
            link.download = '批量群活码.zip' // 重命名文件
            link.click()
            URL.revokeObjectURL(url) // 释放内存
          }
        })
        .catch(function() {})
    },
    // 批量删除
    handleBulkRemove() {
      this.$confirm('确认删除当前群活码?删除操作无法撤销，请谨慎操作。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          const ids = this.multiGroupCode.map((group) => group.id)

          remove(ids + '').then((res) => {
            if (res.code === 200) {
              this.getGroupCodes()
            } else {
            }
          })
        })
        .catch(() => {})
    },
    // 下载
    handleDownload(codeId, activityName) {
      const name = activityName + '.png'

      download(codeId).then((res) => {
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
    },
    // 删除
    handleRemove(codeId) {
      this.$confirm('确认删除当前群活码?删除操作无法撤销，请谨慎操作。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          remove(codeId).then((res) => {
            if (res.code === 200) {
              this.getGroupCodes()
            } else {
            }
          })
        })
        .catch(() => {})
    },
    // 处理多选
    handleSelectionChange(val) {
      this.multiGroupCode = val
    },
    // 打开实际群码窗口
    handleRealCodeDialogOpen(groupCodeId, status) {
      this.openGroupCodeId = groupCodeId
      this.openGroupCodeStatus = status
      this.realCodeDialog = true
    }
  }
}
</script>

<template>
  <div class="page">
    <el-form ref="queryForm" :model="query" inline class="top-search" label-width="100px">
      <el-form-item label="活码名称" prop="activityName">
        <el-input v-model="query.activityName" placeholder="请输入活码名称"></el-input>
      </el-form-item>
      <el-form-item label="创建人" prop="createBy">
        <el-input v-model="query.createBy" placeholder="请输入创建人"></el-input>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="searchDate"
          format="yyyy-MM-dd"
          value-format="yyyyMMdd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label=" ">
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button type="success" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="mid-action">
      <div>
        <el-button type="primary" @click="$router.push('/drainageCode/groupAdd')"
          >新建群活码</el-button
        >
      </div>
      <div>
        <el-button :disabled="multiGroupCode.length === 0" @click="handleBulkDownload"
          >批量下载</el-button
        >
        <el-button :disabled="multiGroupCode.length === 0" @click="handleBulkRemove"
          >批量删除</el-button
        >
      </div>
    </div>

    <el-table :data="groupCodes" v-loading="loading" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column prop="activityName" label="活码名称" align="center"></el-table-column>
      <el-table-column prop="activityDesc" label="活码描述" align="center" width="160">
        <template #default="{ row }">
          <el-popover placement="bottom" width="200" trigger="hover" :content="row.activityDesc">
            <div slot="reference" class="table-desc overflow-ellipsis">
              {{ row.activityDesc }}
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column prop="codeUrl" label="活码样式" align="center" width="130">
        <template #default="{ row }">
          <el-popover placement="bottom" trigger="hover">
            <el-image slot="reference" :src="row.codeUrl" class="code-image--small"></el-image>
            <el-image :src="row.codeUrl" class="code-image"> </el-image>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="实际群码总数" align="center">
        <template #default="{ row }">
          <el-button type="text" @click="handleRealCodeDialogOpen(row.id, -1)">
            {{ (row.actualList && row.actualList.length) || 0 }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="availableCodes" label="可用实际群码数" align="center">
        <template #default="{ row }">
          <el-popover
            v-if="row.aboutToExpireCodes > 0"
            placement="bottom"
            width="200"
            trigger="hover"
            :content="'有' + row.aboutToExpireCodes + '个实际群码即将过期。'"
          >
            <i slot="reference" class="el-icon-warning expire-icon"></i>
          </el-popover>
          <el-button type="text" @click="handleRealCodeDialogOpen(row.id, 0)">
            {{ row.availableCodes }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="totalScanTimes" label="已扫码入群数" align="center"></el-table-column>
      <el-table-column prop="createBy" label="创建人" align="center"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" align="center"></el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="{ row }">
          <el-button
            type="text"
            size="mini"
            @click="
              $router.push({
                path: 'customerGroupDetail',
                query: { groupCodeId: row.id }
              })
            "
            >编辑</el-button
          >
          <el-button type="text" size="mini" @click="handleDownload(row.id, row.activityName)"
            >下载</el-button
          >
          <el-button type="text" size="mini" class="copy-btn" :data-clipboard-text="row.codeUrl"
            >复制</el-button
          >
          <el-button type="text" size="mini" @click="handleRemove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="query.pageNum"
      :limit.sync="query.pageSize"
      @pagination="getGroupCodes"
    />

    <el-dialog
      v-if="realCodeDialog"
      title="实际群码"
      :visible.sync="realCodeDialog"
      append-to-body
      width="70%"
    >
      <RealCode
        ref="realCode"
        :groupCodeId="openGroupCodeId"
        :status="openGroupCodeStatus"
      ></RealCode>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.overflow-ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.table-desc {
  max-width: 150px;
}
.code-image {
  width: 200px;
  height: 200px;
}
.code-image--small {
  width: 50px;
  height: 50px;
}
.expire-icon {
  color: red;
}
</style>
