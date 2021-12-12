<template>
  <div>
    <div class="g-card">
      <el-row type="flex" justify="space-between">
        <el-col :span="5" class="left g-pad20">
          <div class="title">
            <div class="title-name">
              <img style="margin-right:2px;" :src="require('@/assets/drainageCode/up-arrow.png')" alt="">全部
            </div>
            <div class="title-btn" @click="addGroup">
              <img style="margin-right:5px;" :src="require('@/assets/drainageCode/plus-circle.png')" alt="">添加
            </div>
          </div>
          <div class="item-list">
            <div class="item" :class="{'active': groupIndex == key}" v-for="(group, key) in groupList" :key="group.id" @click="switchGroup(key, group)">
              <div class="name">{{ group.name }}</div>
              <el-dropdown v-if="groupIndex == key && group.flag === 0" class="dropdown" @command="onGroupCommand($event, group)">
                <span class="dot">
                  <!-- <img :src="require('@/assets/drainageCode/more.png')" alt=""> -->
                  <i class="el-icon-more"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="edit">修改分组</el-dropdown-item>
                  <el-dropdown-item command="remove">删除分组</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </div>
        </el-col>
        <el-col :span="19" class="g-pad20">
          <div>
            <el-form :inline="true" label-width="80px" label-position="left">
              <el-form-item label="活码名称">
                <el-input v-model="query.qrName" placeholder="请输入活码名称" clearable @keyup.enter.native="search()" />
              </el-form-item>
              <el-form-item label="选择员工" prop="qrUserName">
                <el-input :value="query.qrUserName" readonly @focus="dialogVisible = true" placeholder="请选择员工" />
              </el-form-item>
              <el-form-item label-width="0">
                <!-- <el-button v-hasPermi="['wecom:code:list']" type="cyan" @click="getList(1)">查询</el-button> -->
                <el-button type="primary" @click="search()">查询</el-button>
                <el-button @click="resetQuery" type="info" plain>清空</el-button>
              </el-form-item>
            </el-form>
            <div style="margin-top:20px;display:flex;justify-content:space-between;">
              <el-button type="primary" size="mini" @click="goRoute('staffAdd')">新建员工活码</el-button>
              <div>
                <el-button type="primary" plain size="mini" @click="removeFn('mult')">删除</el-button>
                <el-button type="primary" plain size="mini" @click="downloadBatch()">批量下载</el-button>
              </div>
            </div>
          </div>
          <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange" style="width: 100%">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="二维码" align="center" prop="qrCode" min-width="120">
              <template slot-scope="{ row }">
                <el-image :src="row.qrCode" fit="fit" :preview-src-list="[row.qrCode]" style="width: 100px; height: 100px"></el-image>
              </template>
            </el-table-column>
            <el-table-column label="活码名称" align="center" min-width="100" prop="name" show-overflow-tooltip />
            <el-table-column label="活码分组" align="center" min-width="100" prop="qrGroupName" show-overflow-tooltip />
            <el-table-column label="使用员工" align="center" min-width="140" prop="qrUserInfos" show-overflow-tooltip>
              <template slot-scope="{ row }">
                <!-- <template v-if="row.status === 1">
                  <template v-for="(data, key) in row.weEmpleCodeUseScops" >
                    <span :key="key">{{data.businessName}}</span>  <span :key="key" v-if="key >0">','</span>
                  </template>
                </template> -->
                <template>
                  <div v-for="(unit, key) in row.qrUserInfos" :key="key" style="display:inline;">
                    <template v-for="(item, index) in unit.weQrUserList">
                      <span :key="index">{{item.userName + " "}}</span>
                    </template>
                  </div>
                </template>
              </template>
            </el-table-column>
            <el-table-column label="标签" align="center" prop="qrTags" min-width='160px'>
              <template slot-scope="{ row }">
                <div>
                  <template v-for="(data, key) in row.qrTags">
                    <el-tag v-if="key < 2 && data.tagName" :key="key" size="mini">{{data.tagName}}</el-tag>
                  </template>
                  <el-popover trigger="hover" width="200">
                    <template v-for="(unit, index) in row.qrTags">
                      <el-tag :key="index" v-if="index > 1 && unit.tagName" size="mini">
                        {{unit.tagName}}</el-tag>
                    </template>
                    <div style="display:inline;" slot="reference">
                      <el-tag v-if="row.qrTags.length > 2" size="mini">...</el-tag>
                    </div>
                  </el-popover>
                </div>
              </template>
            </el-table-column>
            <!-- <el-table-column label="最近更新时间" align="center" prop="updateTime" width="180">
            </el-table-column> -->
            <el-table-column label="最近更新时间" align="center" prop="updateTime" width="180">
            </el-table-column>
            <el-table-column label="操作" align="center" fixed="right" width="180" class-name="small-padding fixed-width">
              <template slot-scope="{ row }">
                <!-- <el-button type="text" @click="download(row.id, row.useUserName, row.scenario)" v-hasPermi="['wecom:code:download']">下载</el-button>
                  <el-button type="text" class="copy-btn" :data-clipboard-text="row.qrCode" v-hasPermi="['monitor:operlog:query']">复制链接</el-button>
                  <el-button type="text" @click="goRoute('staffDetail', row.id)" v-hasPermi="['drainageCode:staff:detail']">查看详情</el-button>
                  <el-button type="text" @click="goRoute('staffAdd', row.id)" v-hasPermi="['wecom:code:edit']">编辑</el-button>
                  <el-button type="text" @click="remove(row.id)" v-hasPermi="['wecom:code:remove']">删除</el-button> -->
                <el-button type="text" @click="goRoute('staffDetail', row.id)">详情|统计</el-button>
                <el-divider direction="vertical"></el-divider>
                <el-button type="text" @click="goRoute('staffAdd', row.id)">编辑</el-button>
                <el-divider direction="vertical"></el-divider>
                <el-dropdown style="margin-left: 10px;">
                  <el-button type="text">
                    <i class="el-icon-more"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item>
                      <el-button type="text" @click="download(row.id, row.qrGroupName, row.name)">下载</el-button>
                    </el-dropdown-item>
                    <el-dropdown-item>
                      <el-button type="text" @click="removeFn('single',row.id)">删除</el-button>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>
          <!-- <div class="bottom"> -->

          <pagination :total="total" :page.sync="query.pageNum" :limit.sync="query.pageSize" @pagination="getList()" />
          <!-- </div> -->

        </el-col>
      </el-row>
    </div>

    <!-- 批量新建弹窗 -->
    <SelectUser :visible.sync="dialogVisible" title="组织架构" :defaultValues="userArray" @success="getSelectUser"></SelectUser>

    <el-dialog :title="`${groupForm.id ? '修改' : '新建'}分组`" :visible.sync="groupVisible" width="30%">
      <el-form :model="groupForm" :rules="rules" ref="groupForm">
        <el-form-item label="分组名称" prop="name" label-width="80px">
          <el-input v-model="groupForm.name" clearable autocomplete="off" maxlength="15" show-word-limit>
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="groupVisible = false">取 消</el-button>
        <el-button type="primary" @click="onAddOrUpdateGroup">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    getList,
    remove,
    downloadBatch,
    getCodeCategoryList,
    addCodeCategory,
    updateCodeCategory,
    removeCodeCategory
  } from '@/api/drainageCode/staff'
  import SelectUser from '@/components/SelectUser'
  // import ClipboardJS from 'clipboard'
  export default {
    name: 'CodeStaff',
    components: {
      SelectUser
    },
    data () {
      return {
        // 查询参数
        query: {
          pageNum: 1,
          pageSize: 10,
          groupId: '',
          qrName: '', // 活码名称
          qrUserName: undefined,
          orderByColumn: 'wqc.update_time',
          isAsc: 'desc'
        },
        userArray: [], // 选择人员
        userArrayStr: "",
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
          codeType: 3,
          qrcode: '',
          isJoinConfirmFriends: 0,
          weEmpleCodeTags: [],
          weEmpleCodeUseScops: []
        },
        groupList: [],
        groupVisible: false,
        groupForm: {
          mediaType: 6,
          name: ''
        },
        rules: {
          name: [{
            required: true,
            message: '请输入分组名称',
            trigger: 'blur'
          },
          {
            min: 1,
            max: 15,
            message: '长度在 1 到 15 个字符',
            trigger: 'blur'
          }
          ],
        },
        groupIndex: 0
      }
    },
    created () {
      this.getCodeCategoryListFn()
      this.$store.dispatch(
        'app/setBusininessDesc',
        `
        <div>支持单人、批量单人及多人方式新建员工活码，客户可以通过扫描员工活码添加员工为好友，并支持自动给客户打标签和发送欢迎语。</div>
      `
      )
    },
    mounted () {
      // var clipboard = new ClipboardJS('.copy-btn')
      // clipboard.on('success', (e) => {
      //   this.$notify({
      //     title: '成功',
      //     message: '链接已复制到剪切板，可粘贴。',
      //     type: 'success'
      //   })
      // })
      // clipboard.on('error', (e) => {
      //   this.$message.error('链接复制失败')
      // })
    },
    methods: {
      addGroup () {
        this.groupForm = {
          mediaType: 6,
          name: ''
        }
        this.groupVisible = true
      },
      getCodeCategoryListFn () {
        getCodeCategoryList({ mediaType: 6 }).then(res => {
          if (res.code == 200) {
            this.groupList = res.data
            this.query.groupId = this.groupList[0].id
            this.groupIndex = 0
            this.query.pageNum = 1
            this.getList()
          }
        })
      },
      switchGroup (index, data) {
        this.groupIndex = index
        this.query.groupId = data.id
        this.search()
      },
      getSelectUser (data) {
        this.userArray = data
        this.query.qrUserName = this.userArray.map(function (obj, index) {
          return obj.name
        }).join(",")
      },
      search () {
        this.query.pageNum = 1
        this.getList()
      },
      getList () {
        this.loading = true
        getList(this.query)
          .then(({ rows, total }) => {
            this.list = rows
            this.total = Number(total)
            this.loading = false
            this.ids = []
          })
          .catch(() => {
            this.loading = false
          })
      },
      /** 重置按钮操作 */
      resetQuery () {
        this.query.qrName = ''
        this.userArray = []
        this.query.qrUserName = ''
        // this.query.userArrayStr = ''
        this.search()
      },
      goRoute (path, id) {
        this.$router.push({ path: path, query: { id } })
      },
      // 多选框选中数据
      handleSelectionChange (selection) {
        this.ids = selection.map((item) => item.id)
      },
      /** 删除按钮操作 */
      removeFn (type, id) {
        if (type === 'mult' && !this.ids.length) {
          this.msgInfo('请选择要删除项！')
          return
        }
        const ids = id || this.ids.join(',')
        this.$confirm('是否确认删除?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            return remove(ids)
          })
          .then(() => {
            this.search()
            this.msgSuccess('删除成功')
          })
          .catch(function () { })
      },
      download (id, userName, qrName) {
        let name = userName + '-' + qrName + '.png'
        downloadBatch(id).then((res) => {
          if (res != null) {
            let blob = new Blob([res], {
              type: 'application/zip'
            })
            let url = window.URL.createObjectURL(blob)
            const link = document.createElement('a') // 创建a标签
            link.href = url
            link.download = name // 重命名文件
            link.click()
            URL.revokeObjectURL(url) // 释放内存
          }
        })
      },
      /** 下载 */
      downloadBatch (qrCode) {
        if (!this.ids.length) {
          this.msgInfo("请先勾选下载项！")
          return
        }
        this.$confirm('是否确认下载所有图片吗?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            return downloadBatch(this.ids.join(','))
          })
          .then((res) => {
            if (res != null) {
              let blob = new Blob([res], {
                type: 'application/zip'
              })
              let url = window.URL.createObjectURL(blob)
              const link = document.createElement('a') // 创建a标签
              link.href = url
              link.download = '批量员工活码.zip' // 重命名文件
              link.click()
              URL.revokeObjectURL(url) // 释放内存
            }
          })
          .catch(function () { })
      },
      // 新增分组
      onAddOrUpdateGroup () {
        this.$refs.groupForm.validate(validate => {
          console.log(this.groupForm)
          if (this.groupForm.id) {
            let obj = {
              id: this.groupForm.id,
              name: this.groupForm.name,
              mediaType: 6
            }
            this.groupForm = obj
          }
          if (validate) {
            (this.groupForm.id ? updateCodeCategory : addCodeCategory)(this.groupForm).then(res => {
              this.groupVisible = false
              this.groupForm = {
                name: '',
                mediaType: 6
              }
              this.$refs.groupForm.clearValidate()
              this.getCodeCategoryListFn()
            })
          }
        })
      },
      onGroupCommand (e, group) {
        if (group.id === "1") {
          this.msgInfo('默认分组不可操作！')
          return
        }
        if (e == 'edit') {
          this.editGroup(group)
        } else if (e == 'remove') {
          this.removeGroup(group.id)
        }
      },
      editGroup (group) {
        this.groupForm = JSON.parse(JSON.stringify(group))
        this.groupVisible = true
      },
      removeGroup (id) {
        this.$confirm('确认删除当前分组? 删除后其下活码将移至默认分组！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            removeCodeCategory(id).then(res => {
              this.getCodeCategoryListFn()
            })
          })
          .catch(() => { })

      }
    }
  }
</script>

<style>
  .el-dialog {
    display: flex;
    flex-direction: column;
    margin: 0 !important;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    /*height:600px;*/
    max-height: calc(100% - 30px);
    max-width: calc(100% - 30px);
  }

  .el-dialog .el-dialog__body {
    flex: 1;
    overflow: auto;
  }
</style>
<style lang="scss" scoped>
  .bottom {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .my-divider {
    display: block;
    height: 1px;
    width: 100%;
    background-color: #dcdfe6;
  }
  .hover-item {
    font-size: 12px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #3c88f0;
    padding: 6px 15px;
    cursor: pointer;

    &:hover {
      background-color: #f5f8fe;
    }
  }

  .left {
    border-right: 1px solid #f1f1f1;
    .title {
      color: #3c88f0;
      display: flex;
      justify-content: space-between;
      align-items: center;
      // padding-right: 20px;

      .title-name {
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #333333;
        display: flex;
        align-items: center;
      }

      .title-btn {
        cursor: pointer;
        display: flex;
        align-items: center;
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #3c88f0;

        &:hover {
          opacity: 0.8;
        }
      }
    }

    .item-list {
      max-height: 800px;
      padding-top: 15px;
      display: flex;
      flex-direction: column;
      height: 100%;
      overflow-x: hidden;
      overflow-y: auto;

      .item {
        cursor: pointer;
        display: flex;
        justify-content: space-between;
        align-items: center;
        font-size: 12px;
        color: #333333;
        height: 40px;
        line-height: 40px;
        width: 100%;
        padding-left: 20px;

        .name {
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
        }

        .dropdown {
          // display: none;
          .dot {
            cursor: pointer;
            width: 14px;
            height: 14px;
            line-height: 14px;
            font-size: 14px;
            font-family: JMT-Font, JMT;
            font-weight: normal;
            color: #3c88f0;
            margin-right: 20px;
            margin-left: 5px;
          }
        }

        &:hover {
          color: #3c88f0;
          background: #f5f8fe;
          opacity: 0.8;

          .dropdown {
            // display: block;
          }
        }
      }

      .active {
        // border-left: 2px solid #3c88f0;
        color: #3c88f0;
        background: #f5f8fe;
      }
    }
  }
</style>