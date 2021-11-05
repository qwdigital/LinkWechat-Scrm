<template>
	<div>
		<div class="g-title">
			客户公海
			<img style="margin-left:10px;" :src="require('@/assets/drainageCode/icon-info.png')" alt="">
			<div class="desc">管理员在后台将客户导入后，系统根据分配规则自动分配给员工，员工可以一键复制客户手机号，快速添加客户，并支持添加效果统计</div>
		</div>
		<div class="my-divider"></div>
		<div style="margin-top: 0;background-color: #FFFFFF;">
			<el-tabs :value="currentActive">
				<el-tab-pane label="公海列表" name="sea">
					<div style="padding:20px;">
						<el-button type="primary" style="margin-bottom: 20px;" @click="importDialogVisible = true">导入客户
						</el-button>
						<el-form :model="query" label-position="left" ref="queryForm" :inline="true" label-width="70px" class="top-search">
							<el-form-item label="电话" prop="customerPhone" label-width="40px">
								<el-input v-model="query.customerPhone" placeholder="请输入电话" clearable @keyup.enter.native="getList(1)" />
							</el-form-item>
							<el-form-item label="客户备注名" prop="customerName" label-width="90px">
								<el-input v-model="query.customerName" placeholder="请输入客户备注名" clearable @keyup.enter.native="getList(1)" />
							</el-form-item>
							<el-form-item label="员工名" prop="staffName">
								<el-input v-model="query.staffName" placeholder="请输入员工名" clearable @keyup.enter.native="getList(1)" />
							</el-form-item>
							<el-form-item label="添加状态" prop="status">
								<el-select v-model="query.status">
									<el-option label="待添加" :value="0">
									</el-option>
									<el-option label="已添加" :value="1">
									</el-option>
									<el-option label="已通过" :value="2">
									</el-option>
								</el-select>
							</el-form-item>
							<el-form-item label-width="0">
								<!-- v-hasPermi="['wecom:code:list']" -->
								<el-button type="primary" @click="getList(1)">查询
								</el-button>
								<el-button @click="resetQuery">清空</el-button>
							</el-form-item>
						</el-form>
					</div>
					<div class="divider-content"></div>
					<div class="g-card g-m20">
						<el-table ref="myTable" v-loading="loading" :data="list" @selection-change="handleSelectionChange" max-height="600">
							<el-table-column type="selection" width="55" align="center" />
							<el-table-column label="电话号码" align="center" prop="customerPhone" show-overflow-tooltip />
							<el-table-column label="客户备注姓名" align="center" prop="customerName" show-overflow-tooltip />
							<el-table-column label="客户标签" align="center" prop="weCustomerSeasTag" min-width="180px" show-overflow-tooltip>
								<template slot-scope="{ row }">
									<div>
										<template v-for="(data, key) in row.weCustomerSeasTag">
											<el-tag v-if="key <= 1" :key="key" size="mini">{{data.tagName}}</el-tag>
										</template>
										<el-popover trigger="hover" width="200">
											<template v-for="(unit, index) in row.weCustomerSeasTag">
												<el-tag :key="index" v-if="index > 1" size="mini">
													{{unit.tagName}}
												</el-tag>
											</template>
											<div style="display:inline;" slot="reference">
												<el-tag v-if="row.weCustomerSeasTag.length > 1" size="mini">...</el-tag>
											</div>
										</el-popover>
									</div>
								</template>
							</el-table-column>
							<el-table-column label="分配员工" align="center" prop="staffName" />
							<el-table-column label="当前状态" align="center" prop="status">
								<template slot-scope="{ row }">
									<el-tag v-if="row.status === '0'" size="mini" type="warning">待添加</el-tag>
									<el-tag v-if="row.status === '1'" size="mini" type="warning">已添加</el-tag>
									<el-tag v-if="row.status === '2'" size="mini" type="warning">已通过</el-tag>
								</template>
							</el-table-column>
							<el-table-column label="导入时间" align="center" prop="createTime" width="180">
							</el-table-column>
							<el-table-column label="操作" align="center" class-name="small-padding fixed-width">
								<template slot-scope="{ row }">
									<el-button type="text" @click="tipFn">提醒</el-button>
									<el-divider direction="vertical"></el-divider>
									<el-button type="text" @click="removeFn(row.seasId)">删除</el-button>
								</template>
							</el-table-column>
						</el-table>
						<div class="bottom">
							<div style="align-self: flex-end;">
								<el-button type="primary" plain size="mini" @click="removeMult()">批量删除
								</el-button>
								<el-button type="primary" size="mini" plain @click="tipMulFn">批量提醒</el-button>
							</div>
							<pagination v-show="total > 0" :total="total" :page.sync="query.pageNum" :limit.sync="query.pageSize" @pagination="getList()" />
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="公海统计" name="seaTotal">
					<div class="divider-content"></div>
					<div class="g-card g-pad20" style="margin-top: 0;">
						<el-row :gutter="20">
							<el-col :span="4">
								<div class="total-item">
									<div class="name">
										导入客户总数
									</div>
									<div class="value">
										{{totalData.importNum || 0}}
									</div>
								</div>
							</el-col>
							<el-col :span="4">
								<div class="total-item">
									<div class="name">
										已添加客户数
									</div>
									<div class="value">
										{{totalData.finishAddClientNum || 0}}
									</div>
								</div>
							</el-col>
							<el-col :span="4">
								<div class="total-item">
									<div class="name">
										添加完成率
									</div>
									<div class="value">
										{{totalData.bfb}}%
									</div>
								</div>
							</el-col>
							<el-col :span="4">
								<div class="total-item">
									<div class="name">
										待添加客户数
									</div>
									<div class="value">
										{{totalData.waitAddClientNum || 0}}
									</div>
								</div>
							</el-col>
							<el-col :span="4">
								<div class="total-item">
									<div class="name">
										待通过客户数
									</div>
									<div class="value">
										{{totalData.waitPassClientNum || 0}}
									</div>
								</div>
							</el-col>
						</el-row>
					</div>
					<div class="divider-content"></div>
					<div class="g-card g-m20">
						<div class="table-header">
							导入记录
						</div>
						<el-table v-loading="importLoading" :data="importList" max-height="600">
							<el-table-column label="导入表格名称" align="center" prop="importName" show-overflow-tooltip />
							<el-table-column label="导入客户总数" align="center" prop="importNum" show-overflow-tooltip />
							<el-table-column label="导入时间" align="center" prop="importTime" show-overflow-tooltip>
							</el-table-column>
							<el-table-column label="分配员工" align="center" prop="allotStaffName" />
							<el-table-column label="待添加客户数" align="center" prop="waitAddClientNum">

							</el-table-column>
							<el-table-column label="待通过客户数" align="center" prop="waitPassClientNum" width="180">
							</el-table-column>
							<el-table-column label="已添加客户数" align="center" prop="finishAddClientNum" show-overflow-tooltip />
							<el-table-column label="添加完成率" align="center" prop="useUserName" show-overflow-tooltip>
								<template slot-scope="{ row }">
									<span>
										{{((Number(row.finishAddClientNum) / Number(row.importNum))*100).toFixed(2)}}%
									</span>
								</template>
							</el-table-column>
						</el-table>
						<pagination v-show="importTotal > 0" :total="importTotal" :page.sync="importPage.pageNum" :limit.sync="importPage.pageSize" @pagination="getImportListFn()" />
					</div>
					<div class="divider-content"></div>
					<div class="g-card g-m20">
						<div class="table-header">
							员工添加统计
						</div>
						<el-table v-loading="addLoading" :data="addList" max-height="600">
							<el-table-column label="员工名称" align="center" prop="staffName" show-overflow-tooltip />
							<el-table-column label="分配客户总数" align="center" prop="importNum" show-overflow-tooltip />
							<el-table-column label="待添加客户数" align="center" prop="waitAddClientNum"></el-table-column>
							<el-table-column label="待通过客户数" align="center" prop="waitPassClientNum" width="180">
							</el-table-column>
							<el-table-column label="已添加客户数" align="center" prop="finishAddClientNum" show-overflow-tooltip />
							<el-table-column label="添加完成率" align="center" show-overflow-tooltip>
								<template slot-scope="{ row }">
									<span>
										{{((Number(row.finishAddClientNum) / Number(row.importNum))*100).toFixed(2)}}%
									</span>
								</template>
							</el-table-column>
						</el-table>
						<pagination v-show="addTotal > 0" :total="addTotal" :page.sync="addPage.pageNum" :limit.sync="addPage.pageSize" @pagination="getAddList()" />
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>
		<div v-if="dialogVisible">
			<!-- 批量新建弹窗 -->
			<SelectUser :defaultValues="selectedUserList" :visible.sync="dialogVisible" title="选择员工" @success="selectedUserFn"></SelectUser>
		</div>
		<div v-if="dialogVisibleSelectTag">
			<SelectTag :visible.sync="dialogVisibleSelectTag" :defaultValues="selectedTagList" @success="submitSelectTagFn"></SelectTag>
		</div>
		<div v-if="importDialogVisible">
			<el-dialog title="导入客户" :visible.sync="importDialogVisible" width="40%">
				<el-form ref="importForm" :model="form" :rules="formRules" label-position="right">
					<el-form-item label="选择表格" prop="file" required>
						<div style="display: flex;">
							<el-upload accept=".xls, .xlsx" :action="actionUrl" :limit="1" :headers="headers" ref="upload" :on-remove="handleRemove" :on-change="setFileData" :auto-upload="false">
								<el-button size="mini" type="primary" plain>上传表格</el-button>
							</el-upload>
							<el-button style="margin-left: 10px;" size="mini" type="text" plain @click="downloadFn">下载模板</el-button>
						</div>
					</el-form-item>
					<el-form-item label="选择员工" prop="weCustomerStaffs" required>
						<el-tag v-for="(data, index) in form.weCustomerStaffs" :key="index">{{data.staffName}}</el-tag>
						<el-button type="primary" plain size="mini" @click="selectUserFn">{{form.weCustomerStaffs.length ? '编辑':'选择'}}员工</el-button>
						<div class="sub-des">公海客户平均分配给选择的员工</div>
					</el-form-item>
					<el-form-item label="新客户标签" prop="customerSeasTags">
						<template v-for="(data, index) in form.customerSeasTags">
							<el-tag v-if="data.tagName" :key="index">{{data.tagName}}</el-tag>
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
	import {
		getList,
		download,
		remove,
		upload,
		detail,
		getTotal,
		getImportList,
		getStaffList
	} from '@/api/drainageCode/seas'
	import SelectUser from '@/components/SelectUser'
	import SelectTag from '@/components/SelectTag'
	import ClipboardJS from 'clipboard'
	import { number } from 'echarts'
	export default {
		name: 'CodeStaff',
		components: {
			SelectUser,
			SelectTag
		},
		data () {
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
					customerrPhone: undefined,
					staffName: undefined,
					status: undefined
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
				importList: [],
				importLoading: false,
				importPage: {
					pageSize: 10,
					pageNum: 1
				},
				importTotal: 0,
				addList: [],
				addLoading: false,
				addPage: {
					pageSize: 10,
					pageNum: 1
				},
				addTotal: 0,
				totalData: {
					finishAddClientNum: 0,
					importNum: 0,
					waitAddClientNum: 0,
					waitPassClientNum: 0,
					bfb: 0
				},
				// actionUrl: seas_upload,
				selectedTagList: []
			}
		},
		created () {
			this.getList()
			this.getTotalFn()
			this.getImportListFn()
			this.getAddList()
			// this.$store.dispatch(
			// 	'app/setBusininessDesc',
			// 	`
			//      <div>支持单人、批量单人及多人方式新建员工活码，客户可以通过扫描员工活码添加员工为好友，并支持自动给客户打标签和发送欢迎语。</div>
			//    `
			// )
		},
		mounted () {
			var clipboard = new ClipboardJS('.copy-btn')
			clipboard.on('success', (e) => {
				this.$notify({
					title: '成功',
					message: '链接已复制到剪切板，可粘贴。',
					type: 'success'
				})
			})
			clipboard.on('error', (e) => {
				this.$message.error('链接复制失败')
			})
		},
		methods: {
			submitImport () {
				if (!this.form.file) {
					this.msgError('请先上传表格！')
					return false
				}
				if (!this.form.weCustomerStaffs.length) {
					this.msgError('请先选择员工！')
					return false
				}
				upload(this, this.toFormData(this.form))
					.then((res) => {
						if (res.code === 200) {
							this.msgSuccess('导入成功！')
							this.form = {
								file: {},
								customerSeasTags: [],
								weCustomerStaffs: []
							}
							this.selectedUserList = []
							this.selectedTagList = []
							this.getList()
							this.getTotalFn()
							this.getImportListFn()
							this.getAddList()
							this.importDialogVisible = false
						} else {
							this.msgError(res.msg)
						}
					})
					.catch(() => {
					})
			},
			toFormData (val) {
				let formData = new FormData();
				for (let i in val) {
					isArray(val[i], i);
				}
				function isArray (array, key) {
					if (array == undefined || typeof array == "function") {
						return false;
					}
					if (typeof array != "object") {
						formData.append(key, array);
					} else if (array instanceof Array) {
						if (array.length !== 0) {
							for (let i in array) {
								for (let j in array[i]) {
									isArray(array[i][j], `${key}[${i}].${j}`);
								}
							}
						}
					} else {
						let arr = Object.keys(array);
						if (arr.indexOf("uid") == -1) {
							for (let j in array) {
								isArray(array[j], `${key}.${j}`);
							}
						} else {
							formData.append(`${key}`, array);
						}
					}
				}
				return formData;
			},
			submitSelectTagFn (data) {
				this.form.customerSeasTags = data.map((d) => ({
					tagId: d.tagId,
					tagName: d.name,
				}))
			},
			selectedFn () {
				if (this.form.customerSeasTags) {
					this.selectedTagList = this.form.customerSeasTags.map((dd) => ({
						tagId: dd.tagId,
						name: dd.tagName
					}))
				}
				this.dialogVisibleSelectTag = true
			},
			getTotalFn () {
				getTotal(this)
					.then(
						data => {
							if (data) {
								this.totalData = data
								this.totalData.importNum = Number(this.totalData.waitAddClientNum) + Number(this.totalData.waitPassClientNum) + Number(this.totalData.finishAddClientNum)
								if (this.totalData.importNum) {
									this.totalData.bfb = ((Number(this.totalData.finishAddClientNum) / Number(this.totalData.importNum)) * 100).toFixed(2)
								} else {
									this.totalData.bfb = 0
								}
							}

						})
					.catch(() => {
					})
			},
			getImportListFn () {
				this.importLoading = true
				getImportList(this, this.importPage)
					.then(({
						rows,
						total
					}) => {
						this.importList = rows
						this.importTotal = +total
						this.importLoading = false
					})
					.catch(() => {
						this.importLoading = false
					})
			},
			getAddList () {
				this.addLoading = true
				getStaffList(this, this.addPage)
					.then(({
						rows,
						total
					}) => {
						this.addList = rows
						this.addList.forEach(dd => {
							dd.importNum = Number(dd.waitAddClientNum) + Number(dd.waitPassClientNum) + Number(dd.finishAddClientNum)
						})
						this.addTotal = +total
						this.addLoading = false
					})
					.catch(() => {
						this.addLoading = false
					})
			},
			selectUserFn () {
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
			selectedUserFn (users) {
				const selectedUserList = users.map((d) => {
					return {
						staffId: d.id || d.userId,
						staffName: d.name,
					}
				})
				this.form.weCustomerStaffs = selectedUserList
			},
			setFileData (f, fl) {
				this.form.file = f.raw
			},
			handleRemove () {
				this.form.file = ''
			},
			handleChange (file, fileList) {
				console.log(file, fileList)
			},
			downloadFn () {
				// download(this).then((res) => {
				// 	if (res != null) {
				// 		console.log(res)
				// 		// this.download(res.msg)
				// 		let blob = new Blob([res], {
				// 		// type: 'application/vnd.ms-excel;charset=utf-8'
				// 		type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
				// 		})
				// 		let url = window.URL.createObjectURL(blob)
				// 		const link = document.createElement('a') // 创建a标签
				// 		link.href = url
				// 		link.download = '客户数据'
				// 		link.click()
				// 		URL.revokeObjectURL(url) // 释放内存
				// 	}
				// })
				// window.open('http://storage.jd.com/lw-bucket/7ec39b7f-4f30-44dd-9bbb-1feb5310c4c1.xlsx')
				window.open('http://storage.jd.com/lw-bucket/5bd17e17-ef39-4fff-ac74-a405e899b749.xls')

			},
			getList (page) {
				page && (this.query.pageNum = page)
				this.loading = true
				getList(this, this.query)
					.then(({
						rows,
						total
					}) => {
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
			resetQuery () {
				this.dateRange = []
				this.$refs['queryForm'].resetFields()
				this.getList(1)
			},
			goRoute (path, id) {
				this.$router.push({
					path: path,
					query: {
						id
					}
				})
			},
			// 多选框选中数据
			handleSelectionChange (selection) {
				this.ids = selection.map((item) => item.seasId)
			},
			/** 删除按钮操作 */
			removeFn (id) {
				this.$confirm('是否确认删除?', '警告', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				})
					.then(() => {
						remove(this, id).then((res) => {
							if (res.code === 200) {
								this.getList()
								this.msgSuccess('删除成功')
							} else {
								this.msgError(res.msg)
							}
						})
					}, () => { })
			},
			removeMult () {
				if (this.ids.length) {
					this.$confirm('是否确认删除?', '警告', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					})
						.then(() => {
							remove(this, this.ids.join(',')).then((res) => {
								if (res.code === 200) {
									this.getList()
									this.msgSuccess('删除成功')
								} else {
									this.msgError(res.msg)
								}
							})
						}, () => { })
				} else {
					this.msgInfo('请先勾选要操作项！')
				}
			},
			tipFn () {
				this.$confirm('是否确定发送员工跟进客户提醒？确定后提醒将发送至对应员工', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				})
					.then(() => {
						this.msgSuccess('提醒成功！')
					}, () => { })
			},
			tipMulFn () {
				if (this.ids.length) {
					this.$confirm('是否确定发送员工跟进客户提醒？确定后提醒将发送至对应员工', '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					})
						.then(() => {
							this.msgSuccess('提醒成功！')
							this.ids = []
							this.$refs.myTable.clearSelection()
						}, () => { })
				} else {
					this.msgInfo('请先勾选要操作项！')
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	/deep/ .el-tabs__header {
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
