<script>
import { getList, update, add } from '@/api/appTool/selfApp'
// import clipboard from "clipboard";

export default {
  components: {},
  props: {},
  data() {
    return {
      query: {
        pageNum: 1,
        pageSize: 10,
        name: ''
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
        contactSecret: [{ required: true, message: '必填项', trigger: 'blur' }]
      }),
      status: ['正常', '停用'],
      dialogVisibleSelectMaterial: false
    }
  },
  watch: {},
  computed: {},
  created() {
    this.getList()

    this.$store.dispatch(
      'app/setBusininessDesc',
      `
        <div>用于添加自建应用，方便自建应用的配置与管理。</div>
      `
    )
  },
  mounted() {
    // new clipboard(".copy-btn");
  },
  methods: {
    getList(page) {
      if (this.dateRange) {
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
          ;(this.form.id ? update : add)(this.form)
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
        path: 'taskAev',
        query: { id }
      })
    },

    // 选择素材确认按钮
    submitSelectMaterial(text, image, file) {
      this.form.logoMediaid = image.id
      this.form.squareLogoUrl = image.materialUrl
      this.dialogVisibleSelectMaterial = false
    }
  }
}
</script>

<template>
  <div>
    <!-- <div class="top-search">
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
    </div> -->
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
      <li v-for="(item, index) of list" :key="index" class="list" @click="edit(item)">
        <el-image :src="item.squareLogoUrl" fit="fit"></el-image>
        <div>
          <div class="title">{{ item.agentName }}</div>
          <div class="title">{{ item.agentId }}</div>
          <div class="desc">{{ item.agentSecret }}</div>
        </div>
      </li>
      <li class="list aic" @click="edit()">
        <div class="el-image ac">
          <i class="el-icon-plus cc" style="color: #666"></i>
        </div>
        <div>添加应用</div>
      </li>
    </ul>

    <!-- <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="query.pageNum"
      :limit.sync="query.pageSize"
      @pagination="getList()"
    /> -->

    <el-dialog
      :title="form.id ? '查看' : '新增'"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
    >
      <el-form ref="form" label-position="right" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="10">
          <el-col :span="4">
            <div class="avatar-wrap ac" @click="dialogVisibleSelectMaterial = true">
              <img class="avatar" v-if="form.squareLogoUrl" :src="form.squareLogoUrl" />
              <i v-else class="el-icon-plus avatar-uploader-icon cc"></i>
            </div>
            <!-- <el-image
              style="width: 100px; height: 100px;border: 1px solid #eee; border-radius: 5px;"
              :src="form.squareLogoUrl"
              fit="fit"
            ></el-image> -->
          </el-col>
          <el-col :span="20">
            <el-form-item label="应用标题" prop="agentName">
              <el-input v-model="form.agentName" placeholder="请输入应用标题"></el-input>
            </el-form-item>
            <el-form-item label="应用描述" prop="description">
              <el-input v-model="form.description" placeholder="请输入应用描述"></el-input>
            </el-form-item>
            <el-form-item label="应用Id" prop="agentId">
              <el-input
                v-model="form.agentId"
                :disabled="!!form.id"
                placeholder="请输入应用Id"
              ></el-input>
            </el-form-item>
            <el-form-item label="应用Secret" prop="agentSecret">
              <el-input
                :disabled="!!form.id"
                v-model="form.agentSecret"
                placeholder="请输入应用Secret"
              ></el-input>
              <!-- <el-link class="fr" type="primary">如何获取？</el-link> -->
            </el-form-item>
            <el-form-item label="可见范围" v-if="form.id">
              <div class="flex">
                <div v-for="(item, index) of form.allowPartys.split(',')" :key="index">
                  <i class="el-icon-folder-opened" v-if="item"></i>{{ item }}
                </div>
                <div v-for="(item, index) of form.allowUserinfos.split(',')" :key="'1' + index">
                  <i class="el-icon-s-custom" v-if="item"></i>{{ item }}
                </div>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submit">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 选择素材弹窗 -->
    <!-- <SelectMaterial
      :visible.sync="dialogVisibleSelectMaterial"
      type="1"
      :showArr="[1]"
      @success="submitSelectMaterial"
    >
    </SelectMaterial> -->
  </div>
</template>

<style lang="scss" scoped>
.page {
  padding: 20px;
  border-radius: 5px;
}
.list-wrap {
  display: flex;
  flex-wrap: wrap;
  line-height: 20px;
  .list {
    width: 23%;
    padding: 20px;
    display: flex;
    border: 1px solid #eee;
    margin: 0 20px 20px 0;
    background: #fff;
    border-radius: 6px;
    .el-image {
      margin-right: 10px;
      width: 50px;
      height: 50px;
      flex: none;
      border: 1px solid #eee;
      border-radius: 5px;
      background: #eee;
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
.avatar-wrap {
  position: relative;
  width: 120px;
  height: 120px;
  border: 1px solid #eee;
  border-radius: 5px;
}
.avatar {
  width: 100%;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #ddd;
}
</style>
