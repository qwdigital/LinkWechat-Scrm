<script>
import {
  getTree,
  getList,
  getTreeDetail,
  updateTree,
  addTree,
  removeTree,
} from '@/api/material'
export default {
  name: 'MaPage',
  components: {},
  props: {
    // "0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file) 4 文本",
    type: {
      type: String,
      default: '0',
    },
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
      },
      list: [], // 列表
      total: 0, // 总条数
      treeData: [], // 树
      // 树props
      treeProps: {
        children: 'children',
        label: 'label',
      },
      treeForm: {}, // 树表格
      treeDialogVisible: false, // 树表格对话框显隐
      group: [], // 选择的分组
      groupDialogVisible: false, // 移动分组对话框
      // 分组props
      groupProps: {
        expandTrigger: 'hover',
        checkStrictly: true,
        children: 'children',
        label: 'label',
        emitPath: false,
      },

      typeTitle: ['图片', '语音', '视频', '普通文件', '文本'],
      form: {}, // 素材表单
      dialogVisible: false, // 素材表格对话框显隐
      // 表单校验
      rules: {
        content: [{ required: true, message: '不能为空', trigger: 'blur' }],
        materialUrl: [{ required: true, message: '不能为空', trigger: 'blur' }],
        materialName: [
          { required: true, message: '不能为空', trigger: 'blur' },
        ],
        digest: [{ required: true, message: '不能为空', trigger: 'blur' }],
        coverUrl: [{ required: true, message: '不能为空', trigger: 'blur' }],
      },
    }
  },
  watch: {},
  computed: {},
  created() {
    this.getTree()
    this.getList()
  },
  mounted() {},
  methods: {
    // 获取类目树
    getTree() {
      getTree(this.type).then(({ data }) => {
        this.treeData = data
      })
    },
    // 获取素材列表
    getList(page) {
      page && (this.query.pageNum = page)
      this.loading = true
      getList(this.query)
        .then(({ rows, total }) => {
          this.userList = rows
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
      this.treeForm = Object.assign(
        {},
        type ? data : { parentId: data.id || '0' }
      )
      this.treeDialogVisible = true
    },
    // 类目树节点提交
    treeSubmit() {
      this.treeForm.id
        ? 'updateTree'
        : 'addTree'(this.treeForm)
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
        type: 'warning',
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
      this.form = Object.assign({}, data || { _new: true })
      this.dialogVisible = true
      // type || !data ? (this.disabled = false) : (this.disabled = true)
    },
    // 素材提交
    submit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          let form = JSON.parse(JSON.stringify(this.form))
          form.department += ''
          form.isLeaderInDept += ''
          api[form._new ? 'addUser' : 'updateUser'](form)
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
    // 素材删除
    remove(id) {
      // const operIds = id || this.ids + "";
      this.$confirm('是否确认删除吗?', '警告', {
        type: 'warning',
      })
        .then(function() {
          return remove(id)
        })
        .then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        })
    },
  },
}
</script>

<template>
  <div class="page">
    <el-row :gutter="20">
      <el-col :span="6" :xs="24">
        <div class="head-container">
          <el-button slot="reference" type="primary" @click="treeEdit({}, 0)"
            >添加分类</el-button
          >
        </div>
        <div class="head-container">
          <!-- :filter-node-method="filterNode" -->
          <el-tree
            :data="treeData"
            :props="treeProps"
            :expand-on-click-node="false"
            ref="tree"
            default-expand-all
            @node-click="handleNodeClick"
          />
          <div class="custom-tree-node" slot-scope="{ node, data }">
            <span>{{ node.label }}</span>
            <span class="fr">
              <i
                class="el-icon-edit"
                title="编辑"
                v-hasPermi="['contacts:organization:editDep']"
                v-if="node.level !== 1"
                @click.stop="treeEdit(data, 1)"
              ></i>
              <i
                class="el-icon-plus"
                title="添加子分类"
                v-hasPermi="['contacts:organization:addDep']"
                @click.stop="treeEdit(data, 0)"
              ></i>
              <i
                class="el-icon-minus"
                title="删除"
                v-hasPermi="['contacts:organization:removeDep']"
                v-if="node.level !== 1"
                @click.stop="treeRemove(data.id)"
              ></i>
            </span>
          </div>
        </div>
      </el-col>

      <el-col :span="18" :xs="24">
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
          <el-button class="ml10" @click="getList(1)">搜索</el-button>
          <el-button @click="remove">删除</el-button>
          <el-popover placement="top" width="260" v-model="groupDialogVisible">
            <div>选择分组</div>
            <div style="position: relative; margin: 10px 0;">
              <el-cascader
                v-model="group"
                :options="treeData"
                :props="groupProps"
              ></el-cascader>
            </div>
            <div style="text-align: right;">
              <el-button size="mini" @click="groupDialogVisible = false"
                >取消</el-button
              >
              <el-button
                type="primary"
                size="mini"
                @click="groupDialogVisible = false"
                >确定</el-button
              >
            </div>
            <el-button slot="reference" class="ml10">移动分组</el-button>
          </el-popover>

          <div class="fr">
            <el-button type="primary" @click="edit(1)"
              >添加{{ typeTitle[type] }}</el-button
            >
          </div>
        </div>

        <div v-loading="loading">
          <slot></slot>
        </div>

        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="query.pageNum"
          :limit.sync="query.pageSize"
          @pagination="getList(1)"
        />
      </el-col>
    </el-row>

    <!-- 分类树添加/编辑弹窗 -->
    <el-dialog
      :title="(treeForm.id ? '修改' : '添加') + '分类'"
      :visible.sync="treeDialogVisible"
      width="400px"
    >
      <el-form ref="treeForm" :model="treeForm">
        <el-form-item>
          <el-input
            v-model="treeForm.name"
            maxlength="20"
            show-word-limit
            placeholder="分类名称"
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
        <el-form-item :label="typeTitle[type] + '分类'">
          <el-cascader
            v-model="form.categoryId"
            :options="treeData"
            :props="groupProps"
          ></el-cascader>
        </el-form-item>
        <el-form-item label="文本内容" prop="content" v-if="type === '4'">
          <el-input
            v-model="form.content"
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 50 }"
            placeholder="请输入内容"
          ></el-input>
        </el-form-item>

        <el-form-item label="图片" prop="materialUrl" v-else-if="type === '0'">
          <el-upload
            action
            :show-file-list="false"
            :on-success="d"
            :before-upload="d"
          >
            <img v-if="imageUrl" :src="imageUrl" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
          <div>支持JPG,PNG格式，图片大小不超过2M，建议上传宽高1:1的图片</div>
        </el-form-item>

        <el-form-item label="语音" prop="materialUrl" v-else-if="type === '1'">
          <el-upload
            action=""
            :on-remove="d"
            :before-remove="d"
            multiple
            :limit="3"
            :on-exceed="d"
            :file-list="[{ name: 'name', url: 'url' }]"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">
              只能上传amr格式的语音文件。单个文件大小不超过2M，时长不超过1分钟
            </div>
          </el-upload>
        </el-form-item>

        <template v-else-if="type === '2'">
          <el-form-item label="视频" prop="materialUrl">
            <el-upload
              action
              :show-file-list="false"
              :on-success="d"
              :before-upload="d"
            >
              <img v-if="imageUrl" :src="imageUrl" />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <div>不超过10M, 文件格式: mp4</div>
          </el-form-item>
          <el-form-item label="封面">
            <el-upload
              action
              :show-file-list="false"
              :on-success="d"
              :before-upload="d"
            >
              <img v-if="imageUrl" :src="imageUrl" />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <div>建议尺寸：1068*455</div>
          </el-form-item>
          <el-form-item label="摘要">
            <el-input
              v-model="form.remark"
              type="textarea"
              placeholder="非必填,限120字,如不填会自动抓取正文前54个字"
            ></el-input>
          </el-form-item>
        </template>

        <el-form-item label="文件" prop="materialUrl" v-else-if="type === '3'">
          <el-upload
            action=""
            :on-remove="d"
            :before-remove="d"
            multiple
            :limit="3"
            :on-exceed="d"
            :file-list="[{ name: 'name', url: 'url' }]"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">单个文件大小不超过20M</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submit">确 定</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped></style>
