<script>
  import { getList, add, remove, update } from '@/api/drainageCode/actual'
  import Customer from './customer'

  export default {
    components: { Customer },
    props: {
      // 实际群活码
      groupCodeId: {
        type: String,
        default: ''
      },
      // 检索状态
      status: {
        type: Number,
        default: -1
      }
    },
    data () {
      const checkScanTimes = (rule, value, callback) => {
        if (value < 1 || value > 190) {
          callback(new Error('扫码次数必须在1-190之间'))
        } else {
          callback()
        }
      }

      return {
        // 搜索数据
        query: {
          // 当前选择状态
          status: -1,
          // 当前页码
          pageNum: 1,
          // 每页数据量
          pageSize: 10
        },
        // 可选状态
        statusOptions: [
          { label: '全部状态', value: -1 },
          { label: '使用中', value: 0 },
          { label: '已使用', value: 1 }
        ],
        // 加载状态
        loading: false,
        // 多选实际群码
        multiRealCode: [],
        // 实际群码数据
        realCodes: [],
        // 新建实际群码dialog
        dialog: false,
        // 选择客户群dialog
        customerDialog: false,
        // 示例dialog
        exampleDialog: false,
        // 实际群码表单
        form: {
          groupName: '',
          actualGroupQrCode: '',
          effectTime: '',
          scanCodeTimesLimit: '',
          chatId: '',
          chatGroupName: ''
        },
        // 当前编辑的实际群码
        editedRealCodeId: null,
        // 客户群数据
        customerGroup: [],
        // 总数据量
        total: 0,
        // 表单验证
        rules: {
          groupName: [{ required: true, message: '请输入群名称', trigger: 'blur' }],
          actualGroupQrCode: [{ required: true, message: '请上传实际群码', trigger: 'blur' }],
          effectTime: [{ required: true, message: '请选择有效期', trigger: 'blur' }],
          scanCodeTimesLimit: [
            { required: true, message: '请输入扫码次数', trigger: 'blur' },
            {
              pattern: /^[1-9][0-9]*$/,
              message: '扫码次数必须为正整数',
              trigger: 'blur'
            },
            { validator: checkScanTimes, trigger: 'blur' }
          ]
        },
        // 日期选择器选项
        datePickerOptions: {
          disabledDate (time) {
            return time.getTime() <= Date.now() - 8.64e7
          }
        }
      }
    },
    watch: {
      dialog (val) {
        if (val === false) {
          this.$refs.form.resetFields()
          this.form.chatId = ''
          this.form.chatGroupName = ''
          this.editedRealCodeId = null
        }
      }
    },
    created () {
      this.query.status = this.status

      this.getRealCodes()
    },
    methods: {
      // 获取所有实际群码
      getRealCodes () {
        let params = {
          groupCodeId: this.groupCodeId
        }

        if (this.query.status !== -1) {
          params = Object.assign(params, this.query)
        } else {
          params = Object.assign(params, {
            pageNum: this.query.pageNum,
            pageSize: this.query.pageSize
          })
        }

        this.loading = true

        getList(params)
          .then((res) => {
            if (res.code === 200) {
              this.realCodes = res.rows
              this.total = parseInt(res.total)
            }

            this.loading = false
          })
          .catch(() => {
            this.loading = false
          })
      },
      // 新增实际群码
      add () {
        this.$refs.form.validate((valid) => {
          if (!valid) return

          const data = Object.assign({}, this.form, {
            groupCodeId: this.groupCodeId
          })

          add(data).then((res) => {
            this.dialog = false
            this.getRealCodes()
          })
        })
      },
      // 编辑
      edit (realCode) {
        this.editedRealCodeId = realCode.id

        this.form = {
          groupName: realCode.groupName,
          actualGroupQrCode: realCode.actualGroupQrCode,
          effectTime: realCode.effectTime,
          scanCodeTimesLimit: realCode.scanCodeTimesLimit,
          chatId: realCode.chatId,
          chatGroupName: realCode.chatGroupName
        }

        this.dialog = true
      },
      // 更新实际群码
      update () {
        this.$refs.form.validate((valid) => {
          if (!valid) return

          const data = Object.assign(
            {
              id: this.editedRealCodeId
            },
            this.form
          )

          update(data).then((res) => {
            this.dialog = false
            this.getRealCodes()
          })
        })
      },
      handleSubmit () {
        if (!this.editedRealCodeId) return this.add()

        this.update()
      },
      // 批量删除
      handleBulkRemove () {
        this.$confirm('确认删除当前实际群码?删除操作无法撤销，请谨慎操作。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            const ids = this.multiRealCode.map((code) => code.id)

            remove(ids + '').then((res) => {
              if (res.code === 200) {
                this.getRealCodes()
              } else {
              }
            })
          })
          .catch(() => { })
      },
      // 删除
      handleRemove (id) {
        this.$confirm('确认删除当前实际群码?删除操作无法撤销，请谨慎操作。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            remove(id).then((res) => {
              if (res.code === 200) {
                this.getRealCodes()
              } else {
              }
            })
          })
          .catch(() => { })
      },
      // checkbox选择变更
      handleSelectionChange (val) {
        this.multiRealCode = val
      },
      // 客户群dialog选中数据
      handleSelectCustomerGroup () {
        this.$refs.customer.submit()
      },
      // 获取选中客户群
      getSelectCustomerGroup (customerGroup) {
        this.form.chatId = customerGroup ? customerGroup.chatId : ''
        this.form.chatGroupName = customerGroup ? customerGroup.groupName : ''
        this.customerDialog = false
      }
    }
  }
</script>

<template>
  <div>
    <div class="mid-action">
      <div>
        <el-button type="primary" @click="dialog = true"> 新建实际群码 </el-button>
        <el-select v-model="query.status" class="ml10" @change="getRealCodes">
          <el-option v-for="option in statusOptions" :key="option.value" :label="option.label" :value="option.value"></el-option>
        </el-select>
      </div>
      <div>
        <el-button :disabled="multiRealCode.length === 0" @click="handleBulkRemove">
          批量删除
        </el-button>
      </div>
    </div>

    <el-table :data="realCodes" v-loading="loading" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column prop="groupName" label="群名称" align="center"></el-table-column>
      <el-table-column prop="actualGroupQrCode" label="实际群码" align="center" width="80">
        <template #default="{ row }">
          <el-popover placement="bottom" trigger="hover">
            <el-image slot="reference" :src="row.actualGroupQrCode" class="code-image--small"></el-image>
            <el-image :src="row.actualGroupQrCode" class="code-image"></el-image>
          </el-popover>
        </template>
      </el-table-column>
      <!-- <el-table-column prop="chatGroupName" label="客户群" align="center"></el-table-column> -->
      <el-table-column prop="effectTime" label="有效期" align="center"></el-table-column>
      <el-table-column prop="scanCodeTimesLimit" label="扫码次数限制" align="center"></el-table-column>
      <el-table-column prop="scanCodeTimes" label="已扫码入群数" align="center"></el-table-column>
      <el-table-column prop="status" label="使用状态" align="center">
        <template #default="{ row }">
          <div v-if="parseInt(row.status) === 0" class="green-text">使用中</div>
          <div v-else class="red-text">已使用</div>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="{ row }">
          <el-button type="text" size="mini" @click="edit(row)">编辑</el-button>
          <el-button type="text" size="mini" @click="handleRemove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="实际群码" :visible.sync="dialog" append-to-body>
      <el-form :model="form" ref="form" label-width="120px" :rules="rules">
        <el-form-item label="群名称" prop="groupName">
          <el-input v-model="form.groupName" placeholder="请输入群名称"></el-input>
        </el-form-item>
        <el-form-item label="实际群码" prop="actualGroupQrCode">
          <upload :fileUrl.sync="form.actualGroupQrCode" class="image-uploader">
            <div slot="tip">注: 只支持2M以内的jpg/png格式图片</div>
          </upload>
          <div>
            <el-button type="text" @click="exampleDialog = true"> 查看示例 </el-button>
          </div>
        </el-form-item>
        <el-form-item label="有效期" prop="effectTime">
          <el-date-picker v-model="form.effectTime" type="date" placeholder="请选择有效期" value-format="yyyy-MM-dd" :picker-options="datePickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="扫码次数限制" prop="scanCodeTimesLimit">
          <el-input v-model="form.scanCodeTimesLimit" placeholder="请输入次数" type="Number"></el-input>
          <div class="tip">注: 不超过190</div>
        </el-form-item>
        <!-- <el-form-item label="客户群">
          <template v-if="form.chatId">
            <el-button @click="customerDialog = true" type="primary">
              {{ form.chatGroupName }}
            </el-button>
          </template>
          <template v-else>
            <el-button icon="el-icon-plus" @click="customerDialog = true"> 选择客户群 </el-button>
          </template>
          <div class="tip">注: 主要用于老客标签建群</div>
        </el-form-item> -->
      </el-form>

      <el-dialog title="选择客户群" append-to-body :visible.sync="customerDialog">
        <Customer v-if="customerDialog" ref="customer" :customerGroupId="form.chatId" @callback="getSelectCustomerGroup"></Customer>

        <div slot="footer">
          <el-button @click="customerDialog = false"> 取消 </el-button>
          <el-button type="primary" @click="handleSelectCustomerGroup"> 确定 </el-button>
        </div>
      </el-dialog>

      <el-dialog :visible.sync="exampleDialog" append-to-body width="250px">
        <div class="example-code-box">
          <div class="example-text">如图所示为标准的微信群二维码:</div>
          <div class="code-box-title">
            <svg-icon icon-class="user" class="code-user"></svg-icon>
            实际群名称
          </div>
          <div class="code-content">
            <el-image :src="require('@/assets/example/groupCode.png')"> </el-image>
          </div>
        </div>
      </el-dialog>

      <div slot="footer">
        <el-button @click="dialog = false"> 取消 </el-button>
        <el-button type="primary" @click="handleSubmit"> 确定 </el-button>
      </div>
    </el-dialog>

    <pagination v-show="total > 0" :total="total" :page.sync="query.pageNum" :limit.sync="query.pageSize" @pagination="getRealCodes"></pagination>
  </div>
</template>

<style scoped lang="scss">
  .code-image {
    width: 200px;
    height: 200px;
  }
  .code-image--small {
    width: 50px;
    height: 50px;
  }
  ::v-deep .image-uploader {
    .uploader-icon {
      width: 80px;
      height: 80px;
      line-height: 80px;
    }
    .upload-img {
      width: 80px;
      height: 80px;
    }
  }
  .tip {
    color: #aaa;
    font-size: 12px;
  }
  .green-text {
    color: #19be6b;
  }
  .red-text {
    color: #ed4014;
  }
  .example-code-box {
    margin: 0 5px;
    background-color: white;
    padding: 10px;
    font-weight: bold;
    color: #666666;

    .example-text {
      font-size: 12px;
      padding: 10px 0;
    }
    .code-user {
      font-size: 30px;
      color: #4185f4;
    }
    .code-content {
      text-align: center;

      .el-image {
        margin-top: 20px;
      }
    }
  }
</style>
