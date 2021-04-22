<script>
import { getList, update } from '@/api/appTool/selfApp'
// import clipboard from "clipboard";

export default {
  components: {},
  props: {},
  data() {
    return {
      query: {
        pageNum: 1,
        pageSize: 10,
        name: '',
      },
      dateRange: [], // 添加日期
      total: 0,
      form: {},
      list: [],
      dialogVisible: false,
      disabled: false,
      loading: false,
      rules: Object.freeze({
        name: [{ required: true, message: '必填项', trigger: 'blur' }],
        corpId: [{ required: true, message: '必填项', trigger: 'blur' }],
        corpSecret: [{ required: true, message: '必填项', trigger: 'blur' }],
        contactSecret: [{ required: true, message: '必填项', trigger: 'blur' }],
      }),
      status: ['正常', '停用'],
    }
  },
  watch: {},
  computed: {},
  created() {
    this.getList()
  },
  mounted() {
    // new clipboard(".copy-btn");
  },
  methods: {
    getList(page) {
      if (this.dateRange[0]) {
        this.query.beginTime = this.dateRange[0]
        this.query.endTime = this.dateRange[1]
      } else {
        this.query.beginTime = ''
        this.query.endTime = ''
      }
      page && (this.query.pageNum = page)
      this.loading = true
      getList()
        .then(({ data, total }) => {
          // debugger
          this.list = data
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    },
    edit(data, type) {
      this.form = Object.assign({}, data || {})
      this.dialogVisible = true
      type || !data ? (this.disabled = false) : (this.disabled = true)
    },
    submit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          update(this.form)
            .then(() => {
              this.msgSuccess('操作成功')
              this.dialogVisible = false
              this.getList(!this.form.id && 1)
            })
            .catch(() => {
              this.dialogVisible = false
            })
        }
      })
    },
    goRoute(id, path) {
      this.$router.push({
        path: '/appTool/taskAev',
        query: { id },
      })
    },
  },
}
</script>

<template>
  <div>
    <div class="top-search">
      <el-tooltip
        effect="light"
        content="用于添加自建应用，方便自建应用的配置与管理。"
        placement="top-start"
      >
        <i
          class="el-icon-question"
          style="font-size: 26px;vertical-align: middle; margin-left: 10px;"
        ></i>
      </el-tooltip>
    </div>
    <!-- <div class="ar mb10">
      <el-button
        v-hasPermi="['enterpriseWechat:add']"
        type="primary"
        icon="el-icon-plus"
        @click="goRoute()"
        >新建任务</el-button
      >
    </div> -->

    <ul v-loading="loading" class="list-wrap">
      <li
        v-for="(item, index) of list"
        :key="index"
        class="list"
        @click="edit(item)"
      >
        <el-image :src="item.url" fit="fit"></el-image>
        <div>
          <div class="title">{{ item.agentName }}</div>
          <div class="title">{{ item.agentId }}</div>
          <div class="desc">{{ item.agentSecret }}</div>
        </div>
      </li>
    </ul>

    <!-- <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="query.pageNum"
      :limit.sync="query.pageSize"
      @pagination="getList()"
    /> -->

    <el-dialog title="查看企业微信号" :visible.sync="dialogVisible">
      <el-form
        ref="form"
        label-position="right"
        :model="form"
        :rules="rules"
        label-width="160px"
      >
        <el-form-item label="应用标题" prop="agentName">
          <el-input v-model="form.agentName"></el-input>
        </el-form-item>
        <el-form-item label="应用描述" prop="description">
          <el-input v-model="form.description"></el-input>
        </el-form-item>
        <el-form-item label="应用Id" prop="agentId">
          <el-input v-model="form.agentId" :disabled="!!form.id"></el-input>
        </el-form-item>
        <el-form-item label="应用Secret" prop="agentSecret">
          <el-input
            :disabled="!!form.id"
            v-model="form.agentSecret"
            placeholder="应用Secret"
          ></el-input>
          <!-- <el-link class="fr" type="primary">如何获取？</el-link> -->
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.list-wrap {
  display: flex;
  flex-wrap: wrap;
  .list {
    width: 23%;
    padding: 20px;
    display: flex;
    border: 1px solid #eee;
    margin: 0 20px 20px 0;
    .el-image {
      margin-right: 10px;
      width: 50px;
      height: 50px;
      flex: none;
    }
    .title {
      font-size: 16px;
    }
    .desc {
      color: #ddd;
      word-break: break-all;
    }
  }
}
</style>
