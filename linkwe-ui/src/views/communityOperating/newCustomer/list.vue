<script>
  import { getList, remove, download, downloadBatch } from '@/api/communityOperating/newCustomer'

  export default {
    components: {},
    props: {},
    data () {
      return {
        query: {
          pageNum: 1,
          pageSize: 10,
          emplCodeName: '', // 活码名称
          createBy: '', // 创建人
          beginTime: '', // 开始日期
          endTime: '' // 结束日期
        },
        dateRange: [], // 添加日期
        total: 0, // 数据总量
        form: {},
        list: [],
        dialogVisible: false,
        disabled: false,
        loading: false,
        status: ['正常', '停用'],
        pushType: {
          0: '发给客户',
          1: '发给客户群'
        },
        queryUser: [], // 搜索框选择的添加人
        ids: []
      }
    },
    computed: {},
    watch: {
      // 日期选择器数据同步至查询参数
      dateRange (dateRange) {
        if (!dateRange || dateRange.length !== 2) {
          this.query.beginTime = ''
          this.query.endTime = ''
        } else {
          ;[this.query.beginTime, this.query.endTime] = dateRange
        }
      }
    },
    created () {
      this.getList()
      this.$store.dispatch(
        'app/setBusininessDesc',
        `
        <div>指在客户通过员工活码加为好友后，员工自动推送入群引导语和群活码，客户可通过群活码扫码入群。</div>
      `
      )
    },
    mounted () {
      // new clipboard(".copy-btn");
    },
    methods: {
      // 获取新客数据
      getList (page) {
        page && (this.query.pageNum = page)
        this.loading = true

        getList(this.query)
          .then(({ rows, total }) => {
            this.list = rows
            this.total = +total
            this.loading = false
          })
          .catch(() => {
            this.loading = false
          })
      },
      // 新建/编辑新客数据
      goRoute (id) {
        this.$router.push({
          path: 'newCustomerAev',
          query: { id }
        })
      },
      // 多选框选中数据
      handleSelectionChange (selection) {
        this.ids = selection.map((item) => item.id)
      },
      /** 删除按钮操作 */
      remove (id) {
        const ids = id || this.ids
        this.$confirm('是否确认删除?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(function () {
            return remove(ids)
          })
          .then(() => {
            this.getList()
            this.msgSuccess('删除成功')
          })
          .catch(function () { })
      },
      // 下载
      download (data) {
        let name = data.codeName + '.png'
        download(data.id).then((res) => {
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
      // 批量下载
      downloadBatch () {
        this.$confirm('是否确认下载所有活码图片吗?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            return downloadBatch(this.ids + '')
            // window.open(downloadBatch(this.ids))
          })
          .then((res) => {
            if (res != null) {
              let blob = new Blob([res], { type: 'application/zip' })
              let url = window.URL.createObjectURL(blob)
              const link = document.createElement('a') // 创建a标签
              link.href = url
              link.download = '批量员工活码.zip' // 重命名文件
              link.click()
              URL.revokeObjectURL(url) // 释放内存
            }
          })
          .catch(function () { })
      },
      // 重置查询参数
      resetQuery () {
        this.dateRange = []
        this.$refs['queryForm'].resetFields()

        this.$nextTick(() => {
          this.getList(1)
        })
      }
    }
  }
</script>

<template>
  <div>
    <el-form ref="queryForm" :inline="true" :model="query" label-width="100px" class="top-search" size="small">
      <el-form-item label="活码名称" prop="emplCodeName">
        <el-input v-model="query.emplCodeName" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="创建人" prop="createBy">
        <el-input v-model="query.createBy" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker v-model="dateRange" value-format="yyyy-MM-dd" type="daterange" :picker-options="pickerOptions" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" align="right"></el-date-picker>
      </el-form-item>
      <el-form-item label=" ">
        <el-button v-hasPermi="['customerManage:customer:query']" type="primary" @click="getList(1)">查询</el-button>
        <el-button v-hasPermi="['customerManage:customer:query']" type="success" @click="resetQuery()">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="mid-action">
      <div>
        <el-button type="primary" @click="goRoute()">新建活码</el-button>
      </div>
      <div>
        <el-button type="primary" :disabled="!ids.length" @click="downloadBatch()">批量下载</el-button>
        <el-button v-hasPermi="['customerManage:customer:export']" :disabled="!ids.length" type="cyan" @click="remove()">批量删除</el-button>
      </div>
    </div>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center"></el-table-column>
      <el-table-column prop="codeName" label="活码名称" align="center"></el-table-column>
      <el-table-column prop="emplList" label="活码" align="center" width="130">
        <template #default="{ row }">
          <el-popover placement="bottom" trigger="hover">
            <el-image slot="reference" :src="row.emplCodeUrl" class="code-image--small"></el-image>
            <el-image :src="row.emplCodeUrl" class="code-image"></el-image>
          </el-popover>
        </template>
        <!-- <template slot-scope="{ row }">
          <el-image v-if="row.emplCodeUrl" :src="row.emplCodeUrl" class="code-image">
          </el-image>
        </template> -->
      </el-table-column>
      <el-table-column prop="emplList" label="使用员工" align="center">
        <template #default="{ row }">
          <el-popover v-if="row.emplList" placement="bottom" trigger="hover" :content="row.emplList.map((d) => d.businessName).join()">
            <div slot="reference" class="table-desc toe">
              <!-- {{ getDisplayRealGroups(row) }} -->
              {{ row.emplList.map((d) => d.businessName).join() }}
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="客户标签" align="center">
        <div v-if="row.tagList" slot-scope="{ row }">
          <el-popover placement="bottom" trigger="hover" :disabled="row.tagList.length < 3">
            <div>
              <el-tag v-for="(unit, unique) in row.tagList" :key="unique">
                {{ unit.tagName }}
              </el-tag>
            </div>
            <div slot="reference">
              <el-tag v-for="(unit, unique) in row.tagList.slice(0, 2)" :key="unique">
                {{ unit.tagName }}
              </el-tag>
              <el-tag key="a" v-if="row.tagList.length > 2">...</el-tag>
            </div>
          </el-popover>
        </div>
        <span v-else>无标签</span>
      </el-table-column>
      <el-table-column label="实际群聊" align="center">
        <div v-if="row.actualGroupName" slot-scope="{ row }">
          <el-popover placement="bottom" trigger="hover" :disabled="row.tagList.length < 3">
            <div>
              <el-tag type="primary" v-for="(unit, unique) in row.actualGroupName.split(',')" :key="unique">
                {{ unit }}
              </el-tag>
            </div>
            <div slot="reference">
              <el-tag type="primary" v-for="(unit, unique) in row.actualGroupName.split(',').slice(0, 2)" :key="unique">
                {{ unit }}
              </el-tag>
              <el-tag type="primary" key="a" v-if="row.tagList.length > 2">...</el-tag>
            </div>
          </el-popover>
        </div>
        <span v-else>无群聊</span>
      </el-table-column>
      <el-table-column label="添加好友数" align="center" prop="cusNumber" width="100"></el-table-column>
      <el-table-column label="创建人" align="center" prop="createBy"></el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160"></el-table-column>
      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
        <template slot-scope="{ row }">
          <el-button v-hasPermi="['enterpriseWechat:edit']" type="text" @click="goRoute(row.id)">编辑</el-button>
          <el-button v-hasPermi="['enterpriseWechat:view']" type="text" @click="download(row)">下载</el-button>
          <el-button v-hasPermi="['enterpriseWechat:edit']" type="text" @click="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="query.pageNum" :limit.sync="query.pageSize" @pagination="getList()" />
  </div>
</template>

<style scoped lang="scss">
  .code-image {
    width: 200px;
    height: 200px;
  }

  .code-image--small {
    width: 50px;
    height: 50px;
  }
</style>
