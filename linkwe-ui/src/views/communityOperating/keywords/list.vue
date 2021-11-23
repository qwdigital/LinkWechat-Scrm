<script>
import { getList, remove } from '@/api/communityOperating/keywords'
import ClipboardJS from 'clipboard'
import SelectUser from '@/components/SelectUser'

export default {
  components: { SelectUser },
  props: {},
  data() {
    return {
      h5Link: 'https://platform.wshoto.com/H5/?corpId=ww8e09372aff8d9190',
      query: {
        pageNum: 1,
        pageSize: 10,
        taskName: '', // 活码名称
        createBy: '', // 创建人
        keywords: '', // 关键词
        beginTime: '', // 创建开始时间
        endTime: '' // 创建结束时间
      },
      queryCreateByName: '',
      dateRange: [], // 添加日期
      total: 0, // 关键词拉群数据总量
      list: [], // 关键词拉群数据
      multiSelect: [], // 多选数据
      dialogVisible: false,
      dialogHowToConfig: false,
      disabled: false,
      loading: false,
      clipboard: null
    }
  },
  watch: {
    // 日期选择器数据同步至查询参数
    dateRange(dateRange) {
      if (!dateRange || dateRange.length !== 2) {
        this.query.beginTime = ''
        this.query.endTime = ''
      } else {
        ;[this.query.beginTime, this.query.endTime] = dateRange
      }
    }
  },
  created() {
    this.getList(1)
    this.$store.dispatch(
      'app/setBusininessDesc',
      `
        <div>当企业开通聊天工具栏后，用户可点击聊天工具栏中的【关键字群发】，搜索或选择某个关键词下的引导语及群活码，进行一键发送，客户手动扫码进群。</div>
      `
    )
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
    // 获取关键词拉群数据
    getList(page) {
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
    // 新增/编辑关键词拉群
    goRoute(id) {
      this.$router.push({
        path: 'keywordsAev',
        query: { id: id }
      })
    },
    // 重置查询参数
    resetQuery() {
      this.dateRange = []
      this.$refs['queryForm'].resetFields()

      this.$nextTick(() => {
        this.getList(1)
      })
    },
    // 批量删除
    handleBulkRemove() {
      this.$confirm('确认删除当前数据?删除操作无法撤销，请谨慎操作。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          const ids = this.multiSelect.map((t) => t.taskId)

          remove(ids + '').then((res) => {
            if (res.code === 200) {
              this.getList()
            } else {
            }
          })
        })
        .catch(() => {})
    },
    // 删除
    handleRemove(id) {
      this.$confirm('确认删除当前数据?删除操作无法撤销，请谨慎操作。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          remove(id + '').then((res) => {
            if (res.code === 200) {
              this.getList()
            } else {
            }
          })
        })
        .catch(() => {})
    },
    openHelpDialog() {
      this.dialogHowToConfig = true
    },
    // 获取显示用keyword字符串
    getDisplayKeywords(row) {
      const keywordList = row.keywordList || []
      const keywords = keywordList.map((k) => k.keyword)

      return keywords.join(' ')
    },
    // 获取显示用实际群码字符串
    getDisplayRealGroups(row) {
      if (!row || !row.groupNameList) return ''
      return row.groupNameList.join(' ')
    },
    // 处理多选
    handleSelectionChange(val) {
      this.multiSelect = val
    }
  }
}
</script>

<template>
  <div>
    <el-form ref="queryForm" :inline="true" :model="query" label-width="100px" class="top-search" size="small">
      <el-form-item label="活码名称" prop="taskName">
        <el-input v-model="query.taskName" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="创建人" prop="createBy">
        <el-input v-model="query.createBy" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="关键词" prop="keywords">
        <el-input v-model="query.keywords" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          value-format="yyyy-MM-dd"
          type="daterange"
          :picker-options="pickerOptions"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          align="right"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label=" ">
        <el-button v-hasPermi="['customerManage:customer:query']" type="primary" @click="getList(1)">查询</el-button>
        <el-button v-hasPermi="['customerManage:customer:query']" type="success" @click="resetQuery()">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="mid-action">
      <div>
        <el-button type="primary" @click="goRoute()">新建关键词</el-button>
      </div>
      <div>
        <!-- <el-button type="primary" @click="handleBulkDownload">批量下载</el-button> -->
        <el-button
          v-hasPermi="['customerManage:customer:export']"
          :disabled="multiSelect.length === 0"
          @click="handleBulkRemove"
          type="cyan"
          >批量删除</el-button
        >
      </div>
    </div>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="活码名称" align="center" prop="taskName" :show-overflow-tooltip="true" />
      <el-table-column label="群活码" align="center" width="130">
        <template #default="{ row }">
          <el-popover placement="bottom" trigger="hover">
            <el-image
              slot="reference"
              :src="(row.groupCodeInfo && row.groupCodeInfo.codeUrl) || ''"
              class="code-image--small"
            ></el-image>
            <el-image :src="(row.groupCodeInfo && row.groupCodeInfo.codeUrl) || ''" class="code-image"></el-image>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="关键词" align="center" width="120">
        <template #default="{ row }">
          <el-popover placement="bottom" width="200" trigger="hover" :content="row.keywords">
            <div slot="reference" class="table-desc overflow-ellipsis">
              <!-- {{ getDisplayKeywords(row) }} -->
              {{ row.keywords }}
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="实际群聊" align="center" width="120">
        <template #default="{ row }">
          <el-popover placement="bottom" width="200" trigger="hover" :content="row.groupNameList">
            <div slot="reference" class="table-desc overflow-ellipsis">
              <!-- {{ getDisplayRealGroups(row) }} -->
              {{ row.groupNameList }}
            </div>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column label="创建人" align="center" prop="createBy"></el-table-column>

      <el-table-column label="创建时间" align="center" prop="createTime"></el-table-column>

      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-hasPermi="['enterpriseWechat:edit']" size="mini" type="text" @click="goRoute(scope.row.taskId)"
            >编辑</el-button
          >
          <!-- <el-button
            v-hasPermi="['enterpriseWechat:view']"
            size="mini"
            type="text"
            icon="el-icon-view"
            >下载</el-button
          > -->
          <el-button
            v-hasPermi="['enterpriseWechat:edit']"
            size="mini"
            type="text"
            @click="handleRemove(scope.row.taskId)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="query.pageNum"
      :limit.sync="query.pageSize"
      @pagination="getList()"
    />

    <el-dialog title="配置方法" :visible.sync="dialogHowToConfig" width="500px" style="margin-bottom: 7vh;">
      <div class="help">
        <div class="step">
          <p>
            1、登录企业微信官方后台，进入应用管理，点击 LinkWeChat，再点击【配置到聊天工具栏】。
          </p>
          <el-image :src="require('@/assets/example/keywordHelp1.png')"></el-image>
        </div>
        <div class="step">
          <p>2、点击【配置】后，进入配置页面。</p>
          <el-image :src="require('@/assets/example/keywordHelp2.png')"></el-image>
        </div>
        <div class="step">
          <p>
            3、点击【配置页面】后，在弹窗中，输入页面名称及链接，并确定即可。进入配置页面。
          </p>
          <el-image :src="require('@/assets/example/keywordHelp3.png')"></el-image>
        </div>
      </div>
      <div slot="footer">
        <el-button type="primary" @click="dialogHowToConfig = false">
          我知道了
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.help {
  .step {
    margin-bottom: 20px;
  }
}
.code-image {
  width: 200px;
  height: 200px;
}
.code-image--small {
  width: 50px;
  height: 50px;
}
.overflow-ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.table-desc {
  max-width: 120px;
}
</style>
