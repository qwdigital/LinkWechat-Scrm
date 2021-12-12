<script>
import * as api from '@/api/customer/tag'
import AddTag from '@/components/AddTag'

export default {
  name: 'GroupTag',
  components: { AddTag },
  props: {
    // "标签分组类型(1:客户标签;2:群标签)"
    type: {
      type: String,
      default: '1'
    }
  },
  data() {
    return {
      query: {
        pageNum: 1,
        pageSize: 10,
        groupTagType: this.type
      },
      // 遮罩层
      loading: false,
      dialogVisible: false,
      // 表单参数
      form: {
        gourpName: '',
        weTags: []
      },
      // 添加标签输入框
      newInput: '',
      // 添加标签显隐
      newInputVisible: false,
      // 表单验证规则
      rules: Object.freeze({
        gourpName: [{ required: true, message: '必填项', trigger: 'blur' }]
      }),
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 表格数据
      list: []
    }
  },
  created() {
    // this.query.groupTagType = this.type
    this.getList()
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
      this.form = JSON.parse(
        JSON.stringify(Object.assign({ groupTagType: this.type, weTags: [] }, data || {}))
      )
      this.dialogVisible = true
    },
    syncTag() {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      api.syncTag().then(() => {
        loading.close()
        this.msgSuccess('操作成功')
        this.getList()
      })
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.groupId)
      this.multiple = !selection.length
    },
    /** 删除按钮操作 */
    remove(id) {
      const operIds = id || this.ids + ''
      this.$confirm('是否确认删除吗?', '警告', {
        type: 'warning'
      })
        .then(function() {
          return api.remove(operIds)
        })
        .then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        })
    }
  }
}
</script>
<template>
  <div class="">
    <div class="mid-action">
      <div>
        <el-button v-hasPermi="['customerManage:tag:add']" type="primary" @click="edit()"
          >新建标签组</el-button
        >
        <el-button
          v-if="type == 1"
          v-hasPermi="['customerManage:tag:sync']"
          type="primary"
          plain
          @click="syncTag"
          >同步企微标签</el-button
        >
      </div>
      <div>
        <el-button
          v-hasPermi="['customerManage:tag:remove']"
          :disabled="!ids.length"
          type="danger"
          @click="remove()"
          >批量删除</el-button
        >
      </div>
    </div>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="标签组" align="center" prop="gourpName" />
      <el-table-column label="标签" align="center" prop="weTags">
        <template slot-scope="scope">
          <template v-for="(item, index) in scope.row.weTags">
            <el-tag v-if="index < 3" type="primary" :key="index">{{ item.name }}</el-tag>
          </template>
          <el-popover trigger="hover" width="200">
            <template v-for="(unit, index) in scope.row.weTags">
              <el-tag :key="index" v-if="index >= 3" size="mini">{{ unit.name }}</el-tag>
            </template>
            <div style="display:inline;" slot="reference">
              <el-tag v-if="scope.row.weTags.length > 3" size="mini">...</el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <!-- <el-table-column label="创建人" align="center" prop="createBy" /> -->
      <!-- <el-table-column label="创建时间" align="center" prop="operTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.operTime) }}</span>
        </template>
      </el-table-column>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['customerManage:tag:edit']"
            type="text"
            @click="edit(scope.row, scope.index)"
            >编辑</el-button
          >
          <el-button
            v-hasPermi="['customerManage:tag:remove']"
            @click="remove(scope.row.groupId)"
            type="text"
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
    <!-- 弹窗 -->
    <AddTag :visible.sync="dialogVisible" :form="form" @success="getList(!form.groupId && 1)" />
  </div>
</template>
<style lang="scss" scoped></style>
