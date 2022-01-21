<template>
  <div>
    <div>
      <el-form
        :model="query"
        label-position="left"
        ref="queryForm"
        :inline="true"
        label-width="70px"
        class="top-search"
      >
        <el-form-item label="电话" prop="phone" label-width="40px">
          <el-input
            v-model="query.phone"
            placeholder="请输入电话"
            clearable
            @keyup.enter.native="getList(1)"
          />
        </el-form-item>
        <el-form-item label="客户备注名" prop="customerName" label-width="90px">
          <el-input
            v-model="query.customerName"
            placeholder="请输入客户备注名"
            clearable
            @keyup.enter.native="getList(1)"
          />
        </el-form-item>
        <el-form-item label="员工名" prop="addUserName">
          <el-input
            v-model="query.addUserName"
            placeholder="请输入员工名"
            clearable
            @keyup.enter.native="getList(1)"
          />
        </el-form-item>
        <el-form-item label="添加状态" prop="addState">
          <el-select v-model="query.addState">
            <el-option label="待添加" :value="0"> </el-option>
            <el-option label="已添加" :value="1"> </el-option>
            <!-- <el-option label="待通过" :value="3">
						</el-option> -->
          </el-select>
        </el-form-item>
        <el-form-item label-width="0">
          <!-- v-hasPermi="['wecom:code:list']" -->
          <el-button type="primary" @click="getList(1)">查询 </el-button>
          <el-button @click="resetQuery">清空</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div style="margin-top: 20px; display: flex; justify-content: space-between">
      <el-button type="primary" style="margin-bottom: 20px" @click="importDialogVisible = true"
        >导入客户</el-button
      >
      <div>
        <el-button type="primary" plain size="mini" @click="removeMult()">批量删除 </el-button>
        <el-button type="primary" size="mini" plain @click="tipMulFn">批量提醒</el-button>
      </div>
    </div>
    <div>
      <el-table
        ref="myTable"
        v-loading="loading"
        :data="list"
        @selection-change="handleSelectionChange"
        max-height="600"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column
          label="电话号码"
          align="center"
          prop="phone"
          min-width="180px"
          show-overflow-tooltip
        />
        <el-table-column
          label="客户备注姓名"
          align="center"
          prop="customerName"
          show-overflow-tooltip
        />
        <el-table-column
          label="客户标签"
          align="center"
          prop="tagNames"
          min-width="180px"
          show-overflow-tooltip
        >
          <template slot-scope="{ row }">
            <div v-if="row.tagNames">
              <template v-for="(data, key) in row.tagNames.split(',')">
                <el-tag v-if="key <= 1" :key="key" size="mini">{{ data }}</el-tag>
              </template>
              <el-popover trigger="hover" width="200">
                <template v-for="(unit, index) in row.tagNames.split(',')">
                  <el-tag :key="index" v-if="index > 1" size="mini">
                    {{ unit }}
                  </el-tag>
                </template>
                <div style="display: inline" slot="reference">
                  <el-tag v-if="row.tagNames.split(',').length > 2" size="mini">...</el-tag>
                </div>
              </el-popover>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="分配员工" align="center" prop="addUserName" />
        <el-table-column label="当前状态" align="center" prop="addState">
          <template slot-scope="{ row }">
            <el-tag v-if="row.addState === 0" size="mini" type="warning"> 待添加</el-tag>
            <el-tag v-if="row.addState === 1" size="mini" type="warning">已添加</el-tag>
            <el-tag v-if="row.addState === 3" size="mini" type="warning">待通过</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="导入时间"
          align="center"
          prop="createTime"
          width="180"
        ></el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="{ row }">
            <el-button type="text" @click="tipFn(row.id)">提醒</el-button>
            <el-divider direction="vertical"></el-divider>
            <el-button type="text" @click="removeFn(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="bottom">
        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="query.pageNum"
          :limit.sync="query.pageSize"
          @pagination="getList()"
        />
      </div>
    </div>

    <div v-if="dialogVisible">
      <!-- 批量新建弹窗 -->
      <SelectUser
        :defaultValues="selectedUserList"
        :visible.sync="dialogVisible"
        title="选择员工"
        @success="selectedUserFn"
      ></SelectUser>
    </div>
    <div v-if="dialogVisibleSelectTag">
      <SelectTag
        :visible.sync="dialogVisibleSelectTag"
        :defaultValues="selectedTagList"
        @success="submitSelectTagFn"
      ></SelectTag>
    </div>
    <div v-if="importDialogVisible">
      <el-dialog title="导入客户" :visible.sync="importDialogVisible" width="40%">
        <el-form ref="importForm" :model="form" :rules="formRules" label-position="right">
          <el-form-item label="选择表格" prop="file" required>
            <div style="display: flex">
              <el-upload
                accept=".xls, .xlsx"
                :action="actionUrl"
                :limit="1"
                :headers="headers"
                ref="upload"
                :on-remove="handleRemove"
                :on-change="setFileData"
                :auto-upload="false"
              >
                <el-button size="mini" type="primary" plain>上传表格</el-button>
              </el-upload>
              <el-button style="margin-left: 10px" size="mini" type="text" plain @click="downloadFn"
                >下载模板</el-button
              >
            </div>
          </el-form-item>
          <el-form-item label="选择员工" prop="weCustomerStaffs" required>
            <el-tag v-for="(data, index) in form.weCustomerStaffs" :key="index">{{
              data.staffName
            }}</el-tag>
            <el-button type="primary" plain size="mini" @click="selectUserFn"
              >{{ form.weCustomerStaffs.length ? '编辑' : '选择' }}员工</el-button
            >
            <div class="sub-des">公海客户平均分配给选择的员工</div>
          </el-form-item>
          <el-form-item label="新客户标签" prop="customerSeasTags">
            <template v-for="(data, index) in form.customerSeasTags">
              <el-tag v-if="data.tagName" :key="index"> {{ data.tagName }}</el-tag>
            </template>
            <el-button type="primary" plain size="mini" @click="selectedFn">选择标签</el-button>
            <div class="sub-des">添加成功后，该客户系统添加此标签</div>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="importDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitImport">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { getList, downloadTemplate, remove, upload, detail, alertFn } from '@/api/drainageCode/seas'
import { download } from '@/utils/common'

import SelectUser from '@/components/SelectUser'
import SelectTag from '@/components/SelectTag'
export default {
  name: 'CodeStaff',
  components: {
    SelectUser,
    SelectTag
  },
  data() {
    return {
      headers: global.CONFIG.headers,
      currentActive: 'sea',
      importDialogVisible: false,
      dialogVisibleSelectTag: false,
      // 查询参数
      query: {
        pageNum: 1,
        pageSize: 10,
        customerName: undefined,
        phone: undefined,
        addUserName: undefined,
        addState: undefined
      },
      // 日期范围
      dateRange: [],
      dialogVisible: false,
      // 遮罩层
      loading: false,
      // 选中数组
      ids: [],
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 表单参数
      form: {
        customerSeasTags: [],
        weCustomerStaffs: []
      },
      formRules: {},
      selectedUserList: [],
      actionUrl: '...',
      selectedTagList: []
    }
  },
  created() {
    this.getList()
    this.$store.dispatch(
      'app/setBusininessDesc',
      `
			     <div>支持单人、批量单人及多人方式新建员工活码，客户可以通过扫描员工活码添加员工为好友，并支持自动给客户打标签和发送欢迎语。</div>
			   `
    )
  },
  mounted() {},
  methods: {
    submitImport() {
      if (!this.form.file) {
        this.msgError('请先上传表格！')
        return false
      }
      if (!this.form.weCustomerStaffs.length) {
        this.msgError('请先选择员工！')
        return false
      }
      let obj = {
        file: this.form.file,
        addUserId: this.form.weCustomerStaffs
          .map((dd) => {
            return dd.staffId
          })
          .join(','),
        addUserName: this.form.weCustomerStaffs
          .map((dd) => {
            return dd.staffName
          })
          .join(','),
        tagIds: this.form.customerSeasTags.length
          ? this.form.customerSeasTags
              .map((dd) => {
                return dd.tagId
              })
              .join(',')
          : '',
        tagNames: this.form.customerSeasTags.length
          ? this.form.customerSeasTags
              .map((dd) => {
                return dd.tagName
              })
              .join(',')
          : ''
      }
      upload(this.toFormData(obj))
        .then((res) => {
          if (res.code === 200) {
            this.msgSuccess(res.msg)
            this.form = {
              file: {},
              customerSeasTags: [],
              weCustomerStaffs: []
            }
            this.selectedUserList = []
            this.selectedTagList = []
            this.getList()
            this.importDialogVisible = false
          } else {
            this.msgError(res.msg)
          }
        })
        .catch(() => {})
    },
    toFormData(val) {
      let formData = new FormData()
      for (let i in val) {
        isArray(val[i], i)
      }

      function isArray(array, key) {
        if (array == undefined || typeof array == 'function') {
          return false
        }
        if (typeof array != 'object') {
          formData.append(key, array)
        } else if (array instanceof Array) {
          if (array.length !== 0) {
            for (let i in array) {
              for (let j in array[i]) {
                isArray(array[i][j], `${key}[${i}].${j}`)
              }
            }
          }
        } else {
          let arr = Object.keys(array)
          if (arr.indexOf('uid') == -1) {
            for (let j in array) {
              isArray(array[j], `${key}.${j}`)
            }
          } else {
            formData.append(`${key}`, array)
          }
        }
      }
      console.log(formData)
      return formData
    },
    submitSelectTagFn(data) {
      this.form.customerSeasTags = data.map((d) => ({
        tagId: d.tagId,
        tagName: d.name
      }))
    },
    selectedFn() {
      if (this.form.customerSeasTags) {
        this.selectedTagList = this.form.customerSeasTags.map((dd) => ({
          tagId: dd.tagId,
          name: dd.tagName
        }))
      }
      this.dialogVisibleSelectTag = true
    },
    selectUserFn() {
      this.selectedUserList = []
      let arr = []
      arr = this.form.weCustomerStaffs.map((dd) => {
        return {
          userId: dd.staffId,
          name: dd.staffName
        }
      })
      this.selectedUserList = arr
      this.dialogVisible = true
    },
    selectedUserFn(users) {
      const selectedUserList = users.map((d) => {
        return {
          staffId: d.id || d.userId,
          staffName: d.name
        }
      })
      this.form.weCustomerStaffs = selectedUserList
    },
    setFileData(f, fl) {
      this.form.file = f.raw
    },
    handleRemove() {
      this.form.file = ''
    },
    handleChange(file, fileList) {
      console.log(file, fileList)
    },
    downloadFn() {
      downloadTemplate().then((res) => {
        if (res != null) {
          download(res.msg)
        }
      })
    },
    getList(page) {
      page && (this.query.pageNum = page)
      this.loading = true
      getList(this.query)
        .then(({ rows, total }) => {
          this.list = rows
          this.total = +total
          this.loading = false
          this.ids = []
        })
        .catch(() => {
          this.loading = false
        })
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.$refs['queryForm'].resetFields()
      this.getList(1)
    },
    goRoute(path, id) {
      this.$router.push({
        path: path,
        query: {
          id
        }
      })
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id)
    },
    /** 删除按钮操作 */
    removeFn(id) {
      this.$confirm('是否确认删除?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(
        () => {
          remove(id).then((res) => {
            if (res.code === 200) {
              this.getList()
              this.msgSuccess('删除成功')
            } else {
              this.msgError(res.msg)
            }
          })
        },
        () => {}
      )
    },
    removeMult() {
      if (this.ids.length) {
        this.$confirm('是否确认删除?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(
          () => {
            remove(this.ids.join(',')).then((res) => {
              if (res.code === 200) {
                this.getList()
                this.msgSuccess('删除成功')
              } else {
                this.msgError(res.msg)
              }
            })
          },
          () => {}
        )
      } else {
        this.msgInfo('请先勾选要操作项！')
      }
    },
    tipFn(id) {
      this.$confirm('是否确定发送员工跟进客户提醒？确定后提醒将发送至对应员工', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(
        () => {
          alertFn(id).then((res) => {
            if (res.code === 200) {
              this.msgSuccess('提醒成功！')
            } else {
              this.msgError(res.msg)
            }
          })
        },
        () => {}
      )
    },
    tipMulFn() {
      if (this.ids.length) {
        this.$confirm('是否确定发送员工跟进客户提醒？确定后提醒将发送至对应员工', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(
          () => {
            alertFn(this.ids.join(',')).then((res) => {
              if (res.code === 200) {
                this.msgSuccess('提醒成功！')
                this.ids = []
                this.$refs.myTable.clearSelection()
              } else {
                this.msgError(res.msg)
              }
            })
          },
          () => {}
        )
      } else {
        this.msgInfo('请先勾选要操作项！')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-tabs__header {
  margin-bottom: 0;
}

.divider-content {
  width: 100%;
  height: 10px;
  background-color: #f5f7fb;
}

.my-divider {
  display: block;
  height: 1px;
  width: 100%;
  background-color: #dcdfe6;
}

.bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.sub-des {
  font-size: 12px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #999999;
}

.total-item {
  // width: 200px;
  padding: 20px;
  background: linear-gradient(90deg, #3c89f0 0%, #1364f4 100%);
  border-radius: 4px;
  .name {
    font-size: 14px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #ffffff;
  }
  .value {
    margin-top: 5px;
    font-size: 18px;
    font-family: JDZhengHT-EN-Regular, JDZhengHT-EN;
    font-weight: 400;
    color: #ffffff;
  }
}

.table-header {
  font-size: 16px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #333333;
  margin-bottom: 20px;
}
</style>
