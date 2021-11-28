<script>
import { getList, update, getMaterial } from '@/api/appTool/chatBar'
import SelectMaterialMult from '@/components/SelectMaterialMult/list'

export default {
  components: { SelectMaterialMult },
  props: {},
  data() {
    return {
      dialogVisible: false,
      loading: false,
      list: [],
      mediaType: Object.freeze({
        0: '图片',
        1: '语音',
        2: '视频',
        3: '普通文件',
        4: '文本',
        5: '海报'
      }),

      metarialParams: {
        sideId: '',
        materialIds: [], // '素材id列表',
        mediaType: '', //  '素材类型 0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file) 4 文本 5 海报',
        checkAll: '1' // '是否全选 0 全选 1 非全选',
      },
      selectedMaterial: []
    }
  },
  watch: {},
  computed: {},
  created() {
    this.getList()
    this.$store.dispatch(
      'app/setBusininessDesc',
      `
        <div><span>抓取快捷回复素材，</span>素材抓取后，即可在聊天工具栏使用</div>
      `
    )
  },
  mounted() {},
  methods: {
    getList() {
      this.loading = true
      getList()
        .then(({ rows, total }) => {
          this.list = rows
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    },
    openDialog(data) {
      this.dialogVisible = true
      this.metarialParams.sideId = data.sideId
      this.metarialParams.mediaType = data.mediaType
    },
    // 抓取素材
    getMaterial() {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      this.metarialParams.materialIds = this.selectedMaterial.map((d) => d.id)
      if (this.metarialParams.checkAll == 0) {
        this.metarialParams.materialIds = []
      }
      getMaterial(this.metarialParams)
        .then(({ rows, total }) => {
          // this.list = rows
          loading.close()
          this.msgSuccess('操作成功')
          this.dialogVisible = false
          this.getList()
        })
        .catch(() => {
          loading.close()
        })
    },
    update(data) {
      // this.loading = true
      update(data)
        .then(() => {
          this.msgSuccess('操作成功')
          this.$set(data, 'isEdit', false)
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    }
  }
}
</script>
<template>
  <div>
    <div>
      配置聊天工具栏，方便成员在外部会话中查看和使用，提高服务效率。
      <el-button type="text" @click="$router.push('explain')">图文详解</el-button>
      <!-- <el-button type="text" @click="$router.push({ path: 'config' })"
        >查看已配置信息</el-button
      > -->
    </div>
    <el-table v-loading="loading" :data="list">
      <el-table-column label="素材类型" align="center" prop="mediaType">
        <template slot-scope="scope">
          {{ mediaType[scope.row.mediaType] }}
        </template>
      </el-table-column>
      <el-table-column label="聊天工具栏名称" align="center" prop="sideName">
        <template slot-scope="{ row }">
          <el-input
            class="bfc-d"
            style="width: 100px;"
            v-if="row.isEdit"
            v-model="row.sideName"
            placeholder="请输入"
          ></el-input>
          <span v-else>
            {{ row.sideName }}
          </span>

          <i
            v-if="!row.isEdit"
            class="row-icon el-icon-edit"
            @click="$set(row, 'isEdit', true)"
          ></i>
          <i v-else class="row-icon el-icon-circle-check" @click="update(row)"></i>
        </template>
      </el-table-column>
      <el-table-column label="已抓取素材数量" align="center" prop="total" />
      <el-table-column label="是否启用" align="center" prop="using" width="180">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.using"
            :active-value="0"
            :inactive-value="1"
            inactive-color="#ddd"
            @change="update(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" prop="operId">
        <template slot-scope="scope">
          <el-button type="text" @click="openDialog(scope.row)">抓取素材</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- <el-card class="mt20" shadow="never" header="红包工具栏">
      <el-row :gutter="10">
        <el-col :span="10">
          <div>
            <div>
              发红包权限人
              <el-button type="text">修改</el-button>
            </div>
            <el-tag type="info" effect="plain">
              <i class="el-icon-user-solid"></i> 深肤色
            </el-tag>
            <el-tag type="info" effect="plain">
              <i class="el-icon-user-solid"></i>深肤色
            </el-tag>
            <el-tag type="info" effect="plain">
              <i class="el-icon-user-solid"></i>深肤色
            </el-tag>
          </div>
          <el-form ref="form" :model="form" label-width="100px">
            <el-form-item label="企业logo">
              <el-upload
                action
                :show-file-list="false"
                :on-success="d"
                :before-upload="d"
              >
                <img v-if="imageUrl" :src="imageUrl" />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
              <div>上传企业logo，建议112*112</div>
            </el-form-item>
            <el-form-item label="企业名称">
              <el-input v-model="model"></el-input>
            </el-form-item>
            <el-form-item label="红包应用ID">
              <el-input v-model="model"></el-input>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="10">
          <div
            class="chat-preview"
            style='background-image: url("https://wscos-1253767630.cos.ap-nanjing.myqcloud.com/wework/assets/red-packet.png");'
          >
            <div class="el-image logo" style>
              <div class="el-image__error">加载失败</div>
            </div>
            <el-image class="logo" :src="url" fit="fit"></el-image>
            <div class="company-name">脑白金</div>
          </div>
        </el-col>
      </el-row>
    </el-card> -->

    <el-dialog
      :title="`抓取${mediaType[metarialParams.mediaType]}类型素材库`"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
    >
      <SelectMaterialMult
        isCheck
        :selected.sync="selectedMaterial"
        :type="metarialParams.mediaType"
        :key="metarialParams.mediaType"
      ></SelectMaterialMult>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="getMaterial">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.chat-preview {
  border-radius: 4px;
  position: relative;
  margin-left: 40px;
  display: inline-block;
  width: 150px;
  min-height: 263px;
  background-repeat: no-repeat;
  background-size: cover;
  top: 15px;
  .logo {
    width: 40px;
    height: 40px;
    position: absolute;
    top: 18px;
    left: 50%;
    transform: translateX(-50%);
    border-radius: 50%;
  }
  .company-name {
    font-size: 12px;
    position: absolute;
    top: 70px;
    text-align: center;
    color: #ffe7be;
    width: 100%;
  }
}
.el-table__row {
  .el-icon-edit {
    visibility: hidden;
  }
  &:hover {
    .el-icon-edit {
      visibility: visible;
    }
  }
}
.row-icon {
  font-size: 14px;
  // vertical-align: middle;
  margin-left: 5px;
}
.el-icon-circle-check {
  margin-top: 10px;
}
</style>
