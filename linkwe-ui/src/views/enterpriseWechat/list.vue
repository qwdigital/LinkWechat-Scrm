<script>
import * as api from '@/api/enterpriseId'
// import clipboard from "clipboard";

export default {
  components: {},
  props: {},
  data() {
    return {
      query: {
        pageNum: 1,
        pageSize: 10,
        companyName: ''
      },
      total: 0,
      form: {},
      list: [],
      dialogVisible: false,
      disabled: false,
      loading: false,
      rules: Object.freeze({
        companyName: [{ required: true, message: '必填项', trigger: 'blur' }],
        corpId: [{ required: true, message: '必填项', trigger: 'blur' }],
        corpSecret: [{ required: true, message: '必填项', trigger: 'blur' }],
        contactSecret: [{ required: true, message: '必填项', trigger: 'blur' }]
      }),
      status: ['正常', '停用']
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
    start(corpId) {
      api.start(corpId).then(({ rows, total }) => {
        this.msgSuccess('操作成功')
        this.getList()
      })
    }
  }
}
</script>

<template>
  <div>
    <div class="fxbw">
      <div class="top-search">
        <el-form inline label-position="right" :model="form" label-width="80px">
          <el-form-item label="企业名称">
            <el-input v-model="query.companyName" placeholder="请输入"></el-input>
          </el-form-item>
          <el-form-item label>
            <el-button v-hasPermi="['enterpriseWechat:query']" type="primary" @click="getList(1)">查询</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-button v-hasPermi="['enterpriseWechat:add']" type="primary" @click="edit()">添加</el-button>
    </div>
    <!-- <el-card shadow="never" :body-style="{padding: '20px 0 0'}">
    </el-card>-->

    <el-table v-loading="loading" :data="list">
      <!-- <el-table-column type="selection" width="50" align="center" /> -->
      <el-table-column label="企业名称" align="center" prop="companyName" :show-overflow-tooltip="true" />
      <el-table-column label="企业ID" align="center" prop="corpId" :show-overflow-tooltip="true" />
      <el-table-column label="应用秘钥" align="center" prop="corpSecret" :show-overflow-tooltip="true" />
      <el-table-column label="绑定时间" align="center" prop="createTime" width="160"></el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="160">
        <template slot-scope="scope">{{ status[scope.row.status] }}</template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-hasPermi="['enterpriseWechat:view']" size="mini" type="text" @click="edit(scope.row, 0)"
            >查看</el-button
          >
          <el-button v-hasPermi="['enterpriseWechat:edit']" size="mini" type="text" @click="edit(scope.row, 1)"
            >编辑</el-button
          >
          <el-button
            v-hasPermi="['enterpriseWechat:forbidden']"
            size="mini"
            type="text"
            @click="start(scope.row.corpId)"
            >启用</el-button
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

    <el-dialog title="配置企业微信号" :visible.sync="dialogVisible">
      <el-form
        ref="form"
        label-suffix=":"
        label-position="right"
        :model="form"
        :rules="rules"
        size="small"
        label-width="160px"
        :disabled="disabled"
      >
        <el-form-item label="企业ID" prop="corpId">
          <el-input :disabled="form.id" v-model="form.corpId" placeholder=""></el-input>
          <div class="tips">企业ID即CorpID，在企微后台->我的企业中获取</div>
        </el-form-item>
        <el-form-item label="企业名称" prop="companyName">
          <el-input v-model="form.companyName" disabled placeholder="企业名称"></el-input>
          <div class="tips">根据企业 ID 配置后回显，不可编辑</div>
        </el-form-item>

        <el-form-item label="服务商secret" prop="providerSecret">
          <el-input v-model="form.providerSecret"></el-input>
        </el-form-item>

        <el-form-item label="客户联系 Secret" prop="contactSecret">
          <el-input v-model="form.contactSecret"></el-input>
          <div class="tips">用于管理客户和联系客户，在企微后台->客户联系->客户 API中获取</div>
        </el-form-item>
        <!-- <el-form-item label="Token">
          <el-input disabled id="copy-input" v-model="form.companyName" placeholder="成员唯一标识，不支持更改，不支持中文"></el-input>
          <el-button type="primary" class="copy-btn" data-clipboard-target="#copy-input">复制</el-button>
        </el-form-item>
        <el-form-item label="EncodingAESKey">
          <el-input disabled id="copy-input1" v-model="form.companyName"></el-input>
          <el-button type="primary" class="copy-btn" data-clipboard-target="#copy-input1">复制</el-button>
        </el-form-item>-->
        <el-form-item label="通讯录管理secret" prop="corpSecret">
          <el-input v-model="form.corpSecret"></el-input>
          <div class="tips">用于同步企微通讯录，在企微后台->管理工具->通讯录同步中获取</div>
        </el-form-item>

        <!-- <el-form-item label="通讯录事件服务">
          <el-radio-group v-model="form.contactSecret">
            <el-radio label="label">开启</el-radio>
            <el-radio label="label">不开启</el-radio>
          </el-radio-group>
          <div>开启后，可以将成员、部门的增删改以及成员的标签变更实时的同步到仟微SCRM，无需手动更新同步。</div>
        </el-form-item>-->

        <!-- <el-form-item label="通讯录事件服务">
          <el-radio-group v-model="form.model">
            <el-radio label="label">开启</el-radio>
            <el-radio label="label">不开启</el-radio>
          </el-radio-group>
          <div>开启后，可以将企业客户的添加、编辑以及主动删除客户和被动被客户删除实时的同步到仟微SCRM，无需手动更新同步。</div>
        </el-form-item>-->
        <el-form-item label="企业微信扫码登陆回调地址" prop="wxQrLoginRedirectUri">
          <el-input v-model="form.wxQrLoginRedirectUri"></el-input>
        </el-form-item>
        <el-form-item label="应用回调token密钥" prop="token">
          <el-input v-model="form.token"></el-input>
        </el-form-item>

        <el-form-item label="应用回调消息体加密密钥" prop="encodingAesKey">
          <el-input v-model="form.encodingAesKey"></el-input>
        </el-form-item>
        <el-form-item label="微信公众号APPID" prop="appld">
          <el-input v-model="form.appld"></el-input>
        </el-form-item>
        <el-form-item label="微信公众号密钥" prop="secret">
          <el-input v-model="form.secret"></el-input>
        </el-form-item>
        <el-form-item label="任务宝H5访问地址" prop="fissionUrl">
          <el-input v-model="form.fissionUrl"></el-input>
        </el-form-item>
        <el-form-item label="群裂变H5访问地址" prop="fissionGroupUr">
          <el-input v-model="form.fissionGroupUr"></el-input>
        </el-form-item>
        <el-form-item label="H5域名" prop="h5DoMainName">
          <el-input v-model="form.h5DoMainName"></el-input>
        </el-form-item>

        <el-form-item label="消息提醒agentId" prop="agentId">
          <el-input v-model="form.agentId"></el-input>
          <div class="tips">用于接收应用消息，在企微后台->应用工具->自建应用中配置并获取</div>
        </el-form-item>
        <el-form-item label="消息应用Secret" prop="agentId">
          <el-input v-model="form.agentId"></el-input>
          <div class="tips">用于接收应用消息，在企微后台->应用工具->自建应用中配置并获取</div>
        </el-form-item>

        <el-form-item label="会话私钥" prop="financePrivateKey">
          <el-input v-model="form.financePrivateKey"></el-input>
          <div class="tips"></div>
        </el-form-item>
        <el-form-item label="会话存档secret" prop="chatSecret">
          <el-input v-model="form.chatSecret"></el-input>
          <div class="tips">用于同步企微会话，在企微后台->管理工具->会话存档中获取</div>
        </el-form-item>

        <div>接收事件服务器</div>
        <el-form-item label="URL" prop="fissionUrl">
          <el-input v-model="form.fissionUrl"></el-input>
        </el-form-item>
        <el-form-item label="Token" prop="fissionGroupUr">
          <el-input v-model="form.fissionGroupUr"></el-input>
        </el-form-item>
        <el-form-item label="EncodingAESKey" prop="h5DoMainName">
          <el-input v-model="form.h5DoMainName"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submit" v-show="!disabled">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.tips {
  color: #aaa;
  font-size: 12px;
}
</style>
