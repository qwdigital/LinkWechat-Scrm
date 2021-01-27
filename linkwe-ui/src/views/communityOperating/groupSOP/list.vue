<script>
import * as api from '@/api/customer'

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
      pushType: {
        0: '发给客户',
        1: '发给客户群',
      },
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
      api
        .getList(this.query)
        .then(({ rows, total }) => {
          this.list = rows
          this.total = +total
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
          api[this.form.id ? 'update' : 'add'](this.form)
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
        path: '/communityOperating/groupSOPAev',
        query: { id },
      })
    },
  },
}
</script>

<template>
  <div>
    <div class="fxbw mb10 aic">
      <div class="total">
        群SOP 制定推送规则后，通知群主定时给指定客户群发送消息。
      </div>
      <div>
        <el-button type="primary" icon="el-icon-plus" @click="goRoute()"
          >创建规则</el-button
        >
        <el-input
          placeholder="请输入规则名称"
          prefix-icon="el-icon-search"
          v-model="query.welcomeMsg"
          style="width: 240px; margin-left: 10px;"
          @change="getList(0)"
        ></el-input>
      </div>
    </div>
    <!-- <el-card shadow="never" :body-style="{padding: '20px 0 0'}">
    </el-card>-->

    <el-table v-loading="loading" :data="list">
      <!-- <el-table-column type="selection" width="50" align="center" /> -->
      <el-table-column
        label="规则名称"
        align="center"
        prop="name"
        :show-overflow-tooltip="true"
      />
      <el-table-column prop="createTime" label="创建人" align="center">
        <template slot-scope="scope">{{
          Math.floor(Math.random() * 10000)
        }}</template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
      ></el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['enterpriseWechat:view']"
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="edit(scope.row, 0)"
            >查看</el-button
          >
          <el-button
            v-hasPermi="['enterpriseWechat:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="edit(scope.row, 1)"
            >编辑</el-button
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

    <el-dialog title="查看企业微信号" :visible.sync="dialogVisible">
      <el-form
        ref="form"
        label-position="right"
        :model="form"
        :rules="rules"
        label-width="160px"
        :disabled="disabled"
      >
        <el-form-item label="企业名称" prop="name">
          <el-input v-model="form.name" :disabled="form.id"></el-input>
        </el-form-item>
        <el-form-item label="企业ID（CorpID）" prop="corpId">
          <el-input
            :disabled="form.id"
            v-model="form.corpId"
            style="width: 80%"
            placeholder="可在新闻公告应用的生日祝福等场景使用"
          ></el-input>
          <el-link class="fr" type="primary">如何获取？</el-link>
        </el-form-item>
        <!-- <el-form-item label="Token">
          <el-input disabled id="copy-input" v-model="form.name" placeholder="成员唯一标识，不支持更改，不支持中文"></el-input>
          <el-button type="primary" class="copy-btn" data-clipboard-target="#copy-input">复制</el-button>
        </el-form-item>
        <el-form-item label="EncodingAESKey">
          <el-input disabled id="copy-input1" v-model="form.name"></el-input>
          <el-button type="primary" class="copy-btn" data-clipboard-target="#copy-input1">复制</el-button>
        </el-form-item>-->
        <el-form-item label="服务商secret" prop="providerSecret">
          <el-input v-model="form.providerSecret" style="width: 80%"></el-input>
          <el-link class="fr" type="primary">如何获取？</el-link>
        </el-form-item>
        <el-form-item label="通讯录管理secret" prop="corpSecret">
          <el-input v-model="form.corpSecret" style="width: 80%"></el-input>
          <el-link class="fr" type="primary">如何获取？</el-link>
        </el-form-item>
        <!-- <el-form-item label="通讯录事件服务">
          <el-radio-group v-model="form.contactSecret">
            <el-radio label="label">开启</el-radio>
            <el-radio label="label">不开启</el-radio>
          </el-radio-group>
          <div>开启后，可以将成员、部门的增删改以及成员的标签变更实时的同步到仟微SCRM，无需手动更新同步。</div>
        </el-form-item>-->
        <el-form-item label="外部联系人管理secret" prop="contactSecret">
          <el-input v-model="form.contactSecret"></el-input>
        </el-form-item>
        <!-- <el-form-item label="通讯录事件服务">
          <el-radio-group v-model="form.model">
            <el-radio label="label">开启</el-radio>
            <el-radio label="label">不开启</el-radio>
          </el-radio-group>
          <div>开启后，可以将企业客户的添加、编辑以及主动删除客户和被动被客户删除实时的同步到仟微SCRM，无需手动更新同步。</div>
        </el-form-item>-->
        <el-form-item
          label="企业微信扫码登陆回调地址"
          prop="wxQrLoginRedirectUri"
        >
          <el-input v-model="form.wxQrLoginRedirectUri"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submit" v-show="!disabled"
          >确 定</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>
