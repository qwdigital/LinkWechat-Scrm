<script>
import {
  getTree,
  getList,
  getTreeDetail,
  updateTree,
  addTree,
  removeTree,
  getDetail,
  add,
  update,
  remove,
  moveGroup
} from '@/api/material'

// import {
//   getList
// } from '@/api/material/poster'

export default {
  name: 'MaPage',
  components: {},
  props: {
    // "0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file) 4 文本 5 海报 6 海报字体",
    type: {
      type: String,
      default: '0'
    },
    // 选中的素材ids
    selected: {
      type: [Array, String],
      default: ''
    }
  },
  data() {
    return {
      loading: true, // 遮罩层
      // 查询条件
      query: {
        pageNum: 1,
        pageSize: 10,
        categoryId: '',
        search: '',
        mediaType: '0'
      },
      list: [], // 列表
      total: 0, // 总条数
      treeData: [{}], // 树
      // 树props
      treeProps: {
        children: 'children',
        label: 'name'
      },
      treeForm: {}, // 树表格
      treeDialogVisible: false, // 树表格对话框显隐

      group: '', // 选择的分组
      groupDialogVisible: false, // 移动分组对话框
      // 分组props
      groupProps: {
        // expandTrigger: 'hover',
        checkStrictly: true,
        children: 'children',
        label: 'name',
        value: 'id',
        emitPath: false
      },

      typeTitle: ['图片', '语音', '视频', '文件', '文本', '海报', '海报字体'],
      form: {}, // 素材表单
      dialogVisible: false, // 素材表格对话框显隐
      // 表单校验
      rules: Object.freeze({
        categoryId: [{ required: true, message: '不能为空', trigger: 'change' }],
        content: [{ required: true, message: '不能为空', trigger: 'blur' }],
        materialUrl: [{ required: true, message: '不能为空', trigger: 'change' }],
        materialName: [{ required: true, message: '不能为空', trigger: 'blur' }],
        digest: [{ required: true, message: '不能为空', trigger: 'blur' }],
        coverUrl: [{ required: true, message: '不能为空', trigger: 'blur' }]
      })
    }
  },
  watch: {},
  computed: {},
  created() {
    this.query.mediaType = this.type
    this.getTree()
    this.getList()
  },
  mounted() {},
  methods: {
    // 获取类目树
    getTree() {
      getTree(this.type).then(({ data }) => {
        this.treeData = [
          {
            id: '',
            name: '全部',
            parentId: '0',
            hasParent: false,
            hasChildren: true,
            children: data || []
          }
        ]
      })
    },
    // 获取素材列表
    getList(page) {
      page && (this.query.pageNum = page)
      this.loading = true
      getList(this.query)
        .then(({ rows, total }) => {
          this.list = rows
          this.total = +total
          this.loading = false
          this.$emit('listChange', this.list)
        })
        .catch(() => {
          this.loading = false
        })
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.query.categoryId = data.id
      this.getList(1)
    },
    // 类目树节点添加/编辑 0: 添加， 1：编辑
    treeEdit(data, type) {
      this.treeForm = Object.assign({}, type ? data : { parentId: data.id || '0', mediaType: this.type })
      this.treeDialogVisible = true
    },
    // 类目树节点提交
    treeSubmit() {
      ;(this.treeForm.id ? updateTree : addTree)(this.treeForm)
        .then(() => {
          this.msgSuccess('操作成功')
          this.treeDialogVisible = false
          this.getTree()
        })
        .catch(() => {
          this.treeDialogVisible = false
        })
    },
    // 类目树节点删除
    treeRemove(id) {
      this.$confirm('是否确认删除吗?', '警告', {
        type: 'warning'
      })
        .then(function() {
          return removeTree(id)
        })
        .then(() => {
          this.getTree()
          this.msgSuccess('删除成功')
        })
    },
    // 素材添加/编辑
    edit(data, type) {
      switch (type) {
        case 5:
          // 清除编辑的数据
          this.$parent.posterSubassemblyList = []
          this.$parent.dialog.edit = true
          this.$parent.initPoster()
          break
        default:
          this.form = Object.assign({}, data || { categoryId: this.query.categoryId })
          this.dialogVisible = true
          this.$nextTick(() => {
            this.$refs['form'].clearValidate()
          })
          break
      }
      // type || !data ? (this.disabled = false) : (this.disabled = true)
    },
    // 素材提交
    submit() {
      // this.$refs['form'].validateField
      this.$refs['form'].validate((valid) => {
        if (valid) {
          let form = JSON.parse(JSON.stringify(this.form))
          ;(form.id ? update : add)(form)
            .then(() => {
              this.msgSuccess('操作成功')
              this.dialogVisible = false
              this.getList()
              this.$refs['form'].resetFields()
            })
            .catch(() => {
              this.dialogVisible = false
            })
        }
      })
    },
    // 素材删除
    remove(id) {
      const Ids = id || this.selected + ''
      this.$confirm('是否确认删除吗?', '警告', {
        type: 'warning'
      })
        .then(function() {
          return remove(Ids)
        })
        .then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        })
    },
    // 移动分组
    moveGroup() {
      moveGroup(this.group, this.selected + '').then(() => {
        this.getList()
        this.msgSuccess('操作成功')
        this.groupDialogVisible = false
      })
    },
    // 下载
    download(data) {
      window.open(data.materialUrl)
    }
  }
}
</script>

<template>
  <div class="page">
    <el-row type="flex" justify="space-between">
      <el-col :span="6">
        <div>
          <el-button v-hasPermi="['wechat:category:add']" slot="reference" type="primary" @click="treeEdit({}, 0)"
            >添加分类</el-button
          >
        </div>
        <div class="mt20">
          <!-- :filter-node-method="filterNode" -->
          <el-tree
            ref="tree"
            class="left-tree"
            :data="treeData"
            :props="treeProps"
            :expand-on-click-node="false"
            highlight-current
            default-expand-all
            @node-click="handleNodeClick"
          >
            <div class="custom-tree-node" slot-scope="{ node, data }">
              <span>{{ node.label }}</span>
              <span class="fr" v-if="data.id">
                <i
                  v-hasPermi="['wechat:category:edit']"
                  class="el-icon-edit-outline"
                  title="编辑"
                  @click.stop="treeEdit(data, 1)"
                ></i>
                <i
                  v-hasPermi="['wechat:category:edit']"
                  class="el-icon-circle-plus-outline"
                  title="添加子分类"
                  @click.stop="treeEdit(data, 0)"
                ></i>
                <i
                  v-hasPermi="['wechat:category:remove']"
                  class="el-icon-delete"
                  title="删除"
                  @click.stop="treeRemove(data.id)"
                ></i>
              </span>
            </div>
          </el-tree>
        </div>
      </el-col>

      <el-col :span="17">
        <!-- <el-checkbox
          :indeterminate="isIndeterminate"
          v-model="checkAll"
          @change="handleCheckAllChange"
          >全选</el-checkbox
        > -->
        <div>
          <el-input
            v-model="query.search"
            placeholder="请输入素材"
            clearable
            prefix-icon="el-icon-search"
            style="width: 300px;"
          />
          <el-button class="ml10" @click="getList(1)" v-hasPermi="['wecom:material:list']">搜索</el-button>
          <el-button v-hasPermi="['wechat:material:remove']" @click="remove()" :disabled="selected.length === 0"
            >删除</el-button
          >
          <el-popover placement="top" width="260" v-model="groupDialogVisible">
            <div>选择分组</div>
            <div style="position: relative; margin: 10px 0;">
              <el-cascader v-model="group" :options="treeData[0].children" :props="groupProps"></el-cascader>
            </div>
            <div style="text-align: right;">
              <el-button size="mini" @click="groupDialogVisible = false">取消</el-button>
              <el-button type="primary" size="mini" @click="moveGroup">确定</el-button>
            </div>
            <el-button
              v-hasPermi="['wechat:material:resetCategory']"
              slot="reference"
              class="ml10"
              :disabled="selected.length === 0"
              >移动分组</el-button
            >
          </el-popover>

          <div class="fr">
            <el-button v-hasPermi="['wechat:material:add']" type="primary" @click="edit(1, ~~type)"
              >添加{{ typeTitle[type] }}</el-button
            >
          </div>
        </div>

        <div v-loading="loading" class="mt20">
          <slot v-bind:list="list"></slot>
        </div>

        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="query.pageNum"
          :limit.sync="query.pageSize"
          @pagination="getList()"
        />
      </el-col>
    </el-row>

    <!-- 分类树添加/编辑弹窗 -->
    <el-dialog :title="(treeForm.id ? '修改' : '添加') + '分类'" :visible.sync="treeDialogVisible" width="400px">
      <el-form ref="treeForm" :model="treeForm">
        <el-form-item>
          <el-input
            v-model="treeForm.name"
            maxlength="20"
            show-word-limit
            placeholder="分类名称"
            @keyup.enter.native="treeSubmit"
          ></el-input>
        </el-form-item>
        <!-- <el-form-item label="所属分类" v-if="treeForm.pName">
          <el-input disabled v-model="treeForm.pName"></el-input>
        </el-form-item> -->
      </el-form>
      <div slot="footer">
        <el-button @click="treeDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="treeSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改素材对话框 -->
    <el-dialog
      :title="(form.id ? '修改' : '添加') + typeTitle[type]"
      :visible.sync="dialogVisible"
      width="600px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="分类" prop="categoryId">
          <el-cascader v-model="form.categoryId" :options="treeData[0].children" :props="groupProps"></el-cascader>
        </el-form-item>

        <el-form-item label="文本内容" prop="content" v-if="type === '4'">
          <el-input
            v-model="form.content"
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 50 }"
            placeholder="请输入内容"
          ></el-input>
        </el-form-item>

        <template v-else-if="type === '0'">
          <el-form-item label="文案" prop="content">
            <el-input
              v-model="form.content"
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 50 }"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
          <el-form-item label="图片" prop="materialUrl">
            <upload :fileUrl.sync="form.materialUrl" :fileName.sync="form.materialName" :type="type">
              <div slot="tip">
                支持JPG,PNG格式，图片大小不超过2M，建议上传宽高1:1的图片
              </div>
            </upload>
          </el-form-item>
          <el-form-item label="名称" prop="materialName">
            <el-input v-model="form.materialName" placeholder="请输入"></el-input>
          </el-form-item>
        </template>

        <template v-else-if="type === '1'">
          <el-form-item label="语音" prop="materialUrl">
            <upload :fileUrl.sync="form.materialUrl" :fileName.sync="form.materialName" :type="type">
              <div slot="tip">
                只能上传amr格式的语音文件。单个文件大小不超过2M，时长不超过1分钟
              </div>
            </upload>
          </el-form-item>
          <el-form-item label="名称" prop="materialName">
            <el-input v-model="form.materialName" placeholder="请输入"></el-input>
          </el-form-item>
        </template>

        <template v-else-if="type === '2'">
          <el-form-item label="视频" prop="materialUrl">
            <upload :fileUrl.sync="form.materialUrl" :fileName.sync="form.materialName" :type="type">
              <div slot="tip">
                不超过10M, 文件格式: mp4
              </div>
            </upload>
          </el-form-item>
          <el-form-item label="名称" prop="materialName">
            <el-input v-model="form.materialName" placeholder="请输入"></el-input>
          </el-form-item>
          <el-form-item label="封面">
            <upload :fileUrl.sync="form.coverUrl" type="0">
              <div slot="tip">
                建议尺寸：1068*455
              </div>
            </upload>
          </el-form-item>
          <el-form-item label="摘要">
            <el-input
              v-model="form.digest"
              type="textarea"
              placeholder="非必填,限120字,如不填会自动抓取正文前54个字"
            ></el-input>
          </el-form-item>
        </template>

        <template v-else-if="type === '3'">
          <el-form-item label="文件" prop="materialUrl">
            <upload :fileUrl.sync="form.materialUrl" :fileName.sync="form.materialName" :type="type">
              <div slot="tip">
                单个文件大小不超过20M
              </div>
            </upload>
          </el-form-item>
          <el-form-item label="名称" prop="materialName">
            <el-input v-model="form.materialName" placeholder="请输入"></el-input>
          </el-form-item>
        </template>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submit">确 定</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
</style>
